package ru.harlion.curtainspb.ui.main_menu.saved_projects

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.SystemClock
import androidx.core.graphics.withTranslation
import com.sdsmdg.harjot.vectormaster.utilities.parser.PathParser
import kotlin.math.min

class ShimmerImageDrawable() : Drawable() {
    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(colorFilter: ColorFilter?) {}
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    companion object {
        val PATH: Path =
            PathParser.doPath("M18,13v7L4,20L4,6h5.02c0.05,-0.71 0.22,-1.38 0.48,-2L4,4c-1.1,0 -2,0.9 -2,2v14c0,1.1 0.9,2 2,2h14c1.1,0 2,-0.9 2,-2v-5l-2,-2zM16.5,18h-11l2.75,-3.53 1.96,2.36 2.75,-3.54zM19.3,8.89c0.44,-0.7 0.7,-1.51 0.7,-2.39C20,4.01 17.99,2 15.5,2S11,4.01 11,6.5s2.01,4.5 4.49,4.5c0.88,0 1.7,-0.26 2.39,-0.7L21,13.42 22.42,12 19.3,8.89zM15.5,9C14.12,9 13,7.88 13,6.5S14.12,4 15.5,4 18,5.12 18,6.5 16.88,9 15.5,9z")
    }
    val path = Path()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val offsets = PointF()
    val matrix = Matrix()
    var tx = 0f
    var pathWidth = 0f
    var lastFrame = SystemClock.uptimeMillis()
    override fun onBoundsChange(bounds: Rect?) {
        path.rewind()
        paint.shader = null
    }
    override fun draw(canvas: Canvas) {
        val width = bounds.width()
        val height = bounds.height()
        if (path.isEmpty) {
            val scale = min(width / 24f, height / 24f) * .8f
            PATH.transform(matrix.apply { setScale(scale, scale) }, path)
            pathWidth = 24 * scale
            val half = pathWidth / 2f
            offsets.set(bounds.left + width/2f - half, bounds.top + height/2f - half)
        }
        if (paint.shader == null) {
            paint.shader = LinearGradient(
                0f, 0f, pathWidth / 2f, 0f, intArrayOf(Color.GRAY, Color.LTGRAY),
                floatArrayOf(.6f, 1f), Shader.TileMode.MIRROR
            )
        }

        val now = SystemClock.uptimeMillis()
        val dt = now - lastFrame
        lastFrame = now

        val dx = dt * pathWidth / 1000
        val oldTx = tx
        tx = (tx + dx) % pathWidth
        path.transform(matrix.apply { setTranslate(tx - oldTx, 0f) })

        canvas.withTranslation(offsets.x - tx, offsets.y) {
            canvas.drawPath(path, paint)
        }

        invalidateSelf()
    }
}