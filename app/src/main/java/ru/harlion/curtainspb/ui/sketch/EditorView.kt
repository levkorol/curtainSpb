package ru.harlion.curtainspb.ui.sketch

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.withMatrix
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
    private val bigCircleRadius = 15 * resources.displayMetrics.density

    val topView: ImageView = ImageView(context).also {
        it.scaleType = ImageView.ScaleType.FIT_XY
        it.setPadding((circleRadius + CLICK_DISTANCE_LIMIT).toInt())
        it.setOnTouchListener(this)
    }
    val bottomView: ImageView = ImageView(context)
    private val waterMarkView: ImageView = ImageView(context)

    var showWatermark: Boolean
        get() = waterMarkView.visibility == View.VISIBLE
        set(value) {
            waterMarkView.visibility = if (value) View.VISIBLE else View.GONE
        }

    private val startTouchPoint: PointF = PointF()
    private val startTopSize: Point = Point()
    private val targetTopSize: Point = Point()
    private var editType: EditType? = null

    var rotateCx: Int = 0
    var rotateCy: Int = 0


    private val initialSize = Point((230 * resources.displayMetrics.density).toInt(), (180 * resources.displayMetrics.density).toInt())
    init {
        addView(bottomView)
        addView(topView, LayoutParams(initialSize.x, initialSize.y))
        addView(waterMarkView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        waterMarkView.setImageResource(R.drawable.ic_group_8)
        setWillNotDraw(false)
    }

    fun remove() {
        topView.apply {
            setImageDrawable(null)
            translationX = 0f
            translationY = 0f
            rotation = 0f
            layoutParams = layoutParams.apply {
                width = initialSize.x
                height = initialSize.y
            }
        }
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
                            val cx = topView.x + topView.width / 2
                            val cy = topView.y + topView.height / 2
                            topView.rotation += Math.toDegrees(
                                angleBetweenLines(
                                    cx, cy, startTouchPoint.x, startTouchPoint.y,
                                    cx, cy, event.x, event.y,
                                )
                            ).toFloat()
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
                            topView.translationX += dx
                            topView.translationY += dy
//                            topView.translationX = MathUtils.clamp(topView.translationX + dx, 0f, (width - topView.width).toFloat())
//                            topView.translationY = MathUtils.clamp(topView.translationY + dy, 0f, (height - topView.height).toFloat())
                        }

                        else -> {}
                    }

                    when (editType) {
                        EditType.LEFT_TOP_CORNER, EditType.LEFT_BOTTOM_CORNER,
                        EditType.RIGHT_TOP_CORNER, EditType.RIGHT_BOTTOM_CORNER -> {
                            val minSize = CLICK_DISTANCE_LIMIT + topView.paddingLeft +
                                    circleRadius + bigCircleRadius + circleRadius +
                                    topView.paddingRight + CLICK_DISTANCE_LIMIT

                            if (targetTopSize.x > minSize) {
                                topView.layoutParams.width = targetTopSize.x
                                when (editType) {
                                    EditType.LEFT_TOP_CORNER, EditType.LEFT_BOTTOM_CORNER ->
                                        topView.translationX += startTopSize.x - targetTopSize.x

                                    else -> {}
                                }
                            }
                            if (targetTopSize.y > minSize) {
                                topView.layoutParams.height = targetTopSize.y
                                when (editType) {
                                    EditType.LEFT_TOP_CORNER, EditType.RIGHT_TOP_CORNER ->
                                        topView.translationY += startTopSize.y - targetTopSize.y

                                    else -> {}
                                }
                            }
                            topView.layoutParams = topView.layoutParams
                        }

                        else -> {}
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
        p.color = ContextCompat.getColor(context, R.color.white_transparent_20)
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
        } else if (event.x.toInt() in offset..(topView.width - offset) && event.y.toInt() in offset..(topView.height - offset)) {
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
        val resultBitmap =
            Bitmap.createBitmap(bottomView.width, bottomView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        bottomView.draw(canvas)

        canvas.withMatrix(topView.matrix, topView::draw)

        waterMarkView.draw(canvas)

        return resultBitmap
    }

    private fun distSqr(x0: Int, y0: Int, x1: Int, y1: Int) =
        (x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0)

    private fun angleBetweenLines(
        fX: Float, fY: Float, sX: Float, sY: Float, nfX: Float, nfY: Float, nsX: Float, nsY: Float
    ): Double {
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