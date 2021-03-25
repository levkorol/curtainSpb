package ru.harlion.curtainspb.ui.sketch

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
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

    var isEditMode: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    init {
        addView(bottomView)
        addView(topView)
        topView.setBackgroundColor(Color.RED)
        topView.layoutParams = LayoutParams(500, 500)
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
                                // TODO ещё translationY зафиксить
                                topView.layoutParams = LayoutParams(
                                    startTopSizes!!.x - dx.toInt(),
                                    startTopSizes!!.y
                                )
                            }

                        }

                        RIGHT_TOP_CORNER -> {

                        }

                        ALL -> {
                            topView.translationX = startTopPoint!!.x + dx
                            topView.translationY = startTopPoint!!.y + dy
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> editType = null
        }
        return editType != null
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas) // отрисовывается всё содержимое вьюшки (то есть две картинки)
        // TODO добавление рисования кружочков (только если isEditMode)
    }

    fun getEditType(event: MotionEvent): EditType? {
        // TODO добавить else if для трёх остальных углов
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
        }
        return null
    }

    fun toBitmap(): Bitmap {
        val resultBitmap = viewToBitmap(bottomView)
        val canvas = Canvas(resultBitmap)
        val topBitmap = viewToBitmap(topView)
        val matrix = Matrix()
        // TODO проверить потом итоговую картинку
        matrix.postTranslate(topView.translationX, topView.translationY)
        canvas.drawBitmap(topBitmap, matrix, Paint())
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