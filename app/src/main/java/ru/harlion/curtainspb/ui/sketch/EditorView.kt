package ru.harlion.curtainspb.ui.sketch

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.withSave
import androidx.core.math.MathUtils
import androidx.core.view.setPadding
import ru.harlion.curtainspb.R
import kotlin.math.atan2

private const val CLICK_DISTANCE_LIMIT = 60
private const val CLICK_DISTANCE_LIMIT_SQR = CLICK_DISTANCE_LIMIT * CLICK_DISTANCE_LIMIT

class EditorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnTouchListener {

    private val circleRadius = 10 * resources.displayMetrics.density
    private val bigCircleRadius = 15 * resources.displayMetrics.density

    val topView: ImageView = ImageView(context).also {
        it.scaleType = ImageView.ScaleType.FIT_XY
        it.setPadding((circleRadius + CLICK_DISTANCE_LIMIT).toInt())
        it.setOnTouchListener(this)
    }
    val bottomView: ImageView = ImageView(context)

    private val startTouchPoint: PointF = PointF()
    private val startTopSize: Point = Point()
    private val targetTopSize: Point = Point()
    private var editType: EditType? = null

    var rotateCx: Int = 0
    var rotateCy: Int = 0

    var isVisibleEditorView: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    init {
        addView(bottomView)
        addView(topView, LayoutParams(700, 500))

        setWillNotDraw(false)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTouchPoint.set(event.x, event.y)
                startTopSize.set(topView.width, topView.height)
                targetTopSize.set(startTopSize.x, startTopSize.y)
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
                            targetTopSize.offset(-dx.toInt(), -dy.toInt())
                        }
                        EditType.LEFT_BOTTOM_CORNER -> {
                            targetTopSize.offset(-dx.toInt(), dy.toInt())
                        }
                        EditType.RIGHT_TOP_CORNER -> {
                            targetTopSize.offset(dx.toInt(), -dy.toInt())
                        }
                        EditType.RIGHT_BOTTOM_CORNER -> {
                            targetTopSize.offset(dx.toInt(), dy.toInt())
                        }

                        EditType.ALL -> {
                            //если вью по центру
//                            val hSpace = (width - topView.width) / 2f
//                            topView.translationX = MathUtils.clamp(startTopPoint!!.x + dx, -hSpace, hSpace)
//                            val vSpace = (height - topView.height) / 2f
//                            topView.translationY = MathUtils.clamp(startTopPoint!!.y + dy, -vSpace, vSpace)
                            topView.translationX = MathUtils.clamp(topView.translationX + dx, 0f, (width - topView.width).toFloat())
                            topView.translationY = MathUtils.clamp(topView.translationY + dy, 0f, (height - topView.height).toFloat())
                        }
                    }

                    when (editType) {
                        EditType.LEFT_TOP_CORNER, EditType.LEFT_BOTTOM_CORNER,
                        EditType.RIGHT_TOP_CORNER, EditType.RIGHT_BOTTOM_CORNER -> {
                            val minSize = CLICK_DISTANCE_LIMIT + topView.paddingLeft +
                                    circleRadius + bigCircleRadius + circleRadius +
                                    topView.paddingRight + CLICK_DISTANCE_LIMIT

                            if (targetTopSize.x > minSize)
                                topView.layoutParams.width = targetTopSize.x
                            if (targetTopSize.y > minSize) topView.layoutParams.height = targetTopSize.y
                            topView.layoutParams = topView.layoutParams

                            when (editType) {
                                EditType.LEFT_TOP_CORNER, EditType.LEFT_BOTTOM_CORNER ->
                                    topView.translationX += startTopSize.x - targetTopSize.x
                            }
                            when (editType) {
                                EditType.LEFT_TOP_CORNER, EditType.RIGHT_TOP_CORNER ->
                                    topView.translationY += startTopSize.y - targetTopSize.y
                            }
                        }
                    }
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> editType = null
        }
        startTouchPoint.set(event.x, event.y)
        startTopSize.set(topView.layoutParams.width, topView.layoutParams.height)
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

        if (topView.drawable != null) {
            drawDots(canvas)
        }
    }
    private fun drawDots(canvas: Canvas) {
        p.color = Color.WHITE
        canvas.withSave {
            translate(topView.x, topView.y)
            rotate(topView.rotation, topView.pivotX, topView.pivotY)

            val offset = CLICK_DISTANCE_LIMIT + circleRadius
            drawCircle(offset, offset, circleRadius, p)
            drawCircle(topView.width - offset, offset, circleRadius, p)
            drawCircle(topView.width - offset, topView.height - offset, circleRadius, p)
            drawCircle(offset, topView.height - offset, circleRadius, p)

            rotateCx = topView.width / 2
            rotateCy = (topView.height - offset).toInt()
            drawCircle(rotateCx.toFloat(), rotateCy.toFloat(), bigCircleRadius, p)
            val half = (bigCircleRadius / 1.2).toInt()
            icon.setBounds(rotateCx - half, rotateCy - half, rotateCx + half, rotateCy + half)
            icon.draw(this)
        }
    }

    override fun onDescendantInvalidated(child: View, target: View) {
        super.onDescendantInvalidated(child, target)
        if (child === topView) invalidate()
    }
    override fun invalidateChildInParent(location: IntArray?, dirty: Rect?): ViewParent? {
        if (topView.isDirty) invalidate()
        return super.invalidateChildInParent(location, dirty)
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