package ru.harlion.curtainspb.ui.sketch

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.math.MathUtils
import ru.harlion.curtainspb.ui.sketch.EditorView.EditType.*

private const val CLICK_DISTANCE_LIMIT = 60
private const val CLICK_DISTANCE_LIMIT_SQR = CLICK_DISTANCE_LIMIT * CLICK_DISTANCE_LIMIT

class EditorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val topView: ImageView = ImageView(context)
    val bottomView: ImageView = ImageView(context)

    private var startTouchPoint: PointF? = null
    private var startTopPoint: PointF? = null
    private var startTopSizes: Point? = null
    private var editType: EditType? = null

    var isEditMode: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    init {
        addView(bottomView)
        //    bottomView.layoutParams = LayoutParams() //todo
        addView(topView, LayoutParams(500, 500, Gravity.CENTER))
        // topView.setImageResource(R.drawable.test_pic_big)
        topView.setBackgroundColor(Color.MAGENTA)

        setWillNotDraw(false)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTouchPoint = PointF(event.x, event.y)
                startTopPoint = PointF(topView.translationX, topView.translationY)
                startTopSizes = Point(topView.width, topView.height)
                editType = getEditType(event)
            }
            MotionEvent.ACTION_MOVE -> {
                if (editType != null) {
                    val dx = event.x - startTouchPoint!!.x
                    val dy = event.y - startTouchPoint!!.y

                    when (editType) {
                        LEFT_TOP_CORNER -> {
                            if (startTopSizes!!.x - dx.toInt() > 200 && startTopSizes!!.y - dy.toInt() > 200) {
                                topView.translationX = startTopPoint!!.x + dx
                                topView.translationY = startTopPoint!!.y + dy
                                topView.layoutParams = LayoutParams(
                                    startTopSizes!!.x - dx.toInt(),
                                    startTopSizes!!.y - dy.toInt()
                                )
                            }
                        }

                        LEFT_BOTTOM_CORNER -> {
                            if (startTopSizes!!.x - dx.toInt() > 200 && startTopSizes!!.y - dy.toInt() > 200) {
                                topView.translationX = startTopPoint!!.x + dx
                                topView.layoutParams = LayoutParams(
                                    startTopSizes!!.x - dx.toInt(),
                                    startTopSizes!!.y + dy.toInt()

                                )
                            }
                        }

                        RIGHT_TOP_CORNER -> {
                            if (startTopSizes!!.x - dx.toInt() > 200 && startTopSizes!!.y - dy.toInt() > 200) {
                                topView.translationY = startTopPoint!!.y + dy
                                topView.layoutParams = LayoutParams(
                                    startTopSizes!!.x + dx.toInt(),
                                    startTopSizes!!.y - dy.toInt()

                                )
                            }
                        }

                        RIGHT_BOTTOM_CORNER -> {
                            if (startTopSizes!!.x - dx.toInt() > 200 && startTopSizes!!.y - dy.toInt() > 200) {
                                topView.layoutParams = LayoutParams(
                                    startTopSizes!!.x + dx.toInt(),
                                    startTopSizes!!.y + dy.toInt()

                                )
                            }

                        }

                        ALL -> {
                            val hSpace = (width - topView.width) / 2f
                            topView.translationX = MathUtils.clamp(startTopPoint!!.x + dx, -hSpace, hSpace)
                            val vSpace = (height - topView.height) / 2f
                            topView.translationY = MathUtils.clamp(startTopPoint!!.y + dy, -vSpace, vSpace)
                        }
                    }
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> editType = null
        }
        return editType != null
    }

    val p = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

//        if (isEditMode) {
        p.color = Color.RED
        val fifty = 50 * resources.displayMetrics.density
        canvas.drawCircle(topView.x, topView.y, fifty, p)
        canvas.drawCircle(topView.x + topView.width, topView.y, fifty, p)
        canvas.drawCircle(topView.x + topView.width, topView.y + topView.height, fifty, p)
        canvas.drawCircle(topView.x, topView.y + topView.height, fifty, p)
//        }
    }

    fun getEditType(event: MotionEvent): EditType? {
        val tx = topView.translationX.toInt()
        val ty = topView.translationY.toInt()
        if (distSqr(
                topView.left + tx,
                topView.top + ty,
                event.x.toInt(),
                event.y.toInt()
            ) < CLICK_DISTANCE_LIMIT_SQR
        ) {
            return LEFT_TOP_CORNER

        } else if (event.x.toInt() in (topView.left + tx)..(topView.right + tx) && event.y.toInt() in (topView.top + ty)..(topView.bottom + ty)) {
            return ALL
        } else if (distSqr(
                topView.right + tx,
                topView.top + ty,
                event.x.toInt(),
                event.y.toInt()
            ) < CLICK_DISTANCE_LIMIT_SQR
        ) {
            return RIGHT_TOP_CORNER

        } else if (distSqr(
                topView.left + tx,
                topView.bottom + ty,
                event.x.toInt(),
                event.y.toInt()
            ) < CLICK_DISTANCE_LIMIT_SQR
        ) {
            return LEFT_BOTTOM_CORNER

        } else if (distSqr(
                topView.right + tx,
                topView.bottom + ty,
                event.x.toInt(),
                event.y.toInt()
            ) < CLICK_DISTANCE_LIMIT_SQR
        ) {
            return RIGHT_BOTTOM_CORNER
        }
        return null
    }

    fun toBitmap(): Bitmap {
        val resultBitmap = viewToBitmap(bottomView)
        val canvas = Canvas(resultBitmap)
        val topBitmap = viewToBitmap(topView)
        val matrix = Matrix()
        // TODO проверить потом итоговую картинку
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

    enum class EditType {
        LEFT_TOP_CORNER,
        RIGHT_TOP_CORNER,
        LEFT_BOTTOM_CORNER,
        RIGHT_BOTTOM_CORNER,
        ALL
    }
}