package ru.harlion.curtainspb.ui.sketch

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.withSave
import androidx.core.view.setPadding
import ru.harlion.curtainspb.R
import kotlin.math.atan2

private const val CLICK_DISTANCE_LIMIT = 60
private const val CLICK_DISTANCE_LIMIT_SQR = CLICK_DISTANCE_LIMIT * CLICK_DISTANCE_LIMIT

class EditorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnTouchListener {

    private val circleRadius = 10 * resources.displayMetrics.density
    val topView: ImageView = ImageView(context).also {
        it.scaleType = ImageView.ScaleType.FIT_XY
        it.setPadding((circleRadius + CLICK_DISTANCE_LIMIT).toInt())
        it.setOnTouchListener(this)
    }
    val bottomView: ImageView = ImageView(context)
    val waterView: ImageView = ImageView(context)

    private val startTouchPoint: PointF = PointF()
    private val startTopSizes: Point = Point()
    private var editType: EditType? = null

    var rotateCx: Int = 0
    var rotateCy: Int = 0

//    var isEditMode: Boolean = false
//        set(value) {
//            field = value
//            invalidate()
//        }

    init {
        addView(bottomView)
//        addView(waterView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
//        waterView.setImageResource(R.drawable.pic_water_down)
        // addView(topView, LayoutParams(500, 500, Gravity.CENTER))
        addView(topView, LayoutParams(700, 500))
        // topView.setImageResource(R.drawable.unnamed)

        setWillNotDraw(false)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTouchPoint.set(event.x, event.y)
                startTopSizes.set(topView.width, topView.height)
                return super.dispatchTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                if (editType != null) {
                    val dx = event.x - startTouchPoint.x
                    val dy = event.y - startTouchPoint.y

                    when (editType) {

                        EditType.ROTATE -> {
                            val cx = topView.x + topView.width/2
                            val cy = topView.y + topView.height/2
                            topView.rotation += Math.toDegrees(angleBetweenLines(
                                cx, cy, startTouchPoint.x, startTouchPoint.y,
                                cx, cy, event.x, event.y,
                            )).toFloat()
                        }

                        EditType.LEFT_TOP_CORNER -> {
                            if (startTopSizes.x - dx.toInt() > 200 && startTopSizes.y - dy.toInt() > 200) {
                                topView.translationX += dx
                                topView.translationY += dy
                                topView.layoutParams.width -= dx.toInt()
                                topView.layoutParams.height -= dy.toInt()
                                topView.layoutParams = topView.layoutParams
                            }
                        }

                        EditType.LEFT_BOTTOM_CORNER -> {
                            if (startTopSizes.x - dx.toInt() > 200 && startTopSizes.y - dy.toInt() > 200) {
                                topView.translationX += dx
                                topView.layoutParams = LayoutParams(
                                    startTopSizes.x - dx.toInt(),
                                    startTopSizes.y + dy.toInt()

                                )
                            }
                        }

                        EditType.RIGHT_TOP_CORNER -> {
                            if (startTopSizes.x - dx.toInt() > 200 && startTopSizes.y - dy.toInt() > 200) {
                                topView.translationY += dy
                                topView.layoutParams = LayoutParams(
                                    startTopSizes.x + dx.toInt(),
                                    startTopSizes.y - dy.toInt()

                                )
                            }
                        }

                        EditType.RIGHT_BOTTOM_CORNER -> {
                            if (startTopSizes.x - dx.toInt() > 200 && startTopSizes.y - dy.toInt() > 200) {
                                topView.layoutParams = LayoutParams(
                                    startTopSizes.x + dx.toInt(),
                                    startTopSizes.y + dy.toInt()

                                )
                            }

                        }

                        EditType.ALL -> {
                            //если вью по центру
//                            val hSpace = (width - topView.width) / 2f
//                            topView.translationX = MathUtils.clamp(startTopPoint!!.x + dx, -hSpace, hSpace)
//                            val vSpace = (height - topView.height) / 2f
//                            topView.translationY = MathUtils.clamp(startTopPoint!!.y + dy, -vSpace, vSpace)
                            topView.translationX += dx
                            topView.translationY += dy
                        }
                    }
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> editType = null
        }
        startTouchPoint.set(event.x, event.y)
        startTopSizes.set(topView.layoutParams.width, topView.layoutParams.height)
        return editType != null
    }
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                editType = getEditType(event)
                return true
            }
        }
        return false
    }

    private val icon = ContextCompat.getDrawable(context, R.drawable.ic_cicrle_right)!!
    private val p = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        p.color = Color.WHITE
        val fifty50 = 15 * resources.displayMetrics.density
        canvas.withSave {
            canvas.translate(topView.x, topView.y)
            canvas.rotate(topView.rotation, topView.pivotX, topView.pivotY)

            val offset = CLICK_DISTANCE_LIMIT + circleRadius
            canvas.drawCircle(offset, offset, circleRadius, p)
            canvas.drawCircle(topView.width - offset, offset, circleRadius, p)
            canvas.drawCircle(topView.width - offset, topView.height - offset, circleRadius, p)
            canvas.drawCircle(offset, topView.height - offset, circleRadius, p)

            rotateCx = topView.width / 2
            rotateCy = (topView.height - offset).toInt()
            canvas.drawCircle(rotateCx.toFloat(), rotateCy.toFloat(), fifty50, p)
            val half = (fifty50 / 1.2).toInt()
            icon.setBounds(rotateCx - half, rotateCy - half, rotateCx + half, rotateCy + half)
            icon.draw(canvas)
        }
    }

    fun getEditType(event: MotionEvent): EditType? {
        val offset = (CLICK_DISTANCE_LIMIT + circleRadius).toInt()
        if (distSqr(
                offset,
                offset,
                event.x.toInt(),
                event.y.toInt()
            ) < CLICK_DISTANCE_LIMIT_SQR
        ) {
            return EditType.LEFT_TOP_CORNER

        } else if (distSqr(
                rotateCx,
                rotateCy,
                event.x.toInt(),
                event.y.toInt()
            ) < CLICK_DISTANCE_LIMIT_SQR + 15 * resources.displayMetrics.density
        ) {
            return EditType.ROTATE
        } else if (event.x.toInt() in offset..(topView.width-offset) && event.y.toInt() in offset..(topView.height-offset)) {
            return EditType.ALL
        } else if (distSqr(
                (topView.width - offset).toInt(),
                offset,
                event.x.toInt(),
                event.y.toInt()
            ) < CLICK_DISTANCE_LIMIT_SQR
        ) {
            return EditType.RIGHT_TOP_CORNER

        } else if (distSqr(
                offset,
                (topView.height - offset).toInt(),
                event.x.toInt(),
                event.y.toInt()
            ) < CLICK_DISTANCE_LIMIT_SQR
        ) {
            return EditType.LEFT_BOTTOM_CORNER

        } else if (distSqr(
                (topView.width - offset).toInt(),
                (topView.height - offset).toInt(),
                event.x.toInt(),
                event.y.toInt()
            ) < CLICK_DISTANCE_LIMIT_SQR
        ) {
            return EditType.RIGHT_BOTTOM_CORNER
        }
        return null
    }

    fun toBitmap(): Bitmap {
        val resultBitmap = viewToBitmap(bottomView)
        val canvas = Canvas(resultBitmap)
        val topBitmap = viewToBitmap(topView)
        val matrix = Matrix()
        matrix.postTranslate(topView.x, topView.y)
        canvas.drawBitmap(topBitmap, matrix, null)
        return resultBitmap
    }

    private fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun distSqr(x0: Int, y0: Int, x1: Int, y1: Int) =
        (x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0)
    private fun angleBetweenLines(
        fX: Float, fY: Float, sX: Float, sY: Float, nfX: Float, nfY: Float, nsX: Float, nsY: Float): Double {
        val angle1 = atan2((fY - sY), (fX - sX)).toDouble()
        val angle2 = atan2((nfY - nsY), (nfX - nsX)).toDouble()
        return when {
            angle1 >= 0 && angle2 >= 0 || angle1 < 0 && angle2 < 0 -> angle2 - angle1
            angle2 < 0 -> angle1 + angle2
            else -> angle2 + angle1
        }
    }

    enum class EditType {
        LEFT_TOP_CORNER,
        RIGHT_TOP_CORNER,
        LEFT_BOTTOM_CORNER,
        RIGHT_BOTTOM_CORNER,
        ROTATE,
        ALL
    }
}