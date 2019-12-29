package com.example.anothersimplegame

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

class TheSun(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val thumbPaint = Paint()

    private val defaultBarWidth = resources.getDimensionPixelSize(R.dimen.volume_bar_default_width)
    private val defaultBarHeight =
        resources.getDimensionPixelSize(R.dimen.volume_bar_default_height)

    init {
        thumbPaint.color = Color.RED
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            View.MeasureSpec.EXACTLY -> widthSize
            View.MeasureSpec.AT_MOST -> defaultBarWidth
            View.MeasureSpec.UNSPECIFIED -> defaultBarWidth
            else -> defaultBarWidth
        }

        val height = when (heightMode) {
            View.MeasureSpec.EXACTLY -> heightSize
            View.MeasureSpec.AT_MOST -> defaultBarHeight
            View.MeasureSpec.UNSPECIFIED -> defaultBarHeight
            else -> defaultBarHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        // drawBar(canvas)
        drawThumb(canvas)
    }

    private fun drawThumb(canvas: Canvas) {
        val thumbX = calculateThumbX()
        //val thumbY = calculateThumbY()
        val thumbY = height.toFloat() / 2.3F
        val radius = height.toFloat() / 2.3F

        canvas.drawCircle(thumbX, thumbY, radius, thumbPaint)
    }

    private fun calculateThumbX(): Float {
//        val volumeLevelsCount = this.volumeLevelsCount
//        val currentVolumeLevel = this.currentVolumeLevel

//        return if (volumeLevelsCount != null && currentVolumeLevel != null) {
//            ((width - height) / volumeLevelsCount * currentVolumeLevel).toFloat() + height / 20.0F
//        } else {
//            50.0F
//        }

        return 50.0F
    }

    private fun calculateThumbY(): Float {

        return 25.0F
    }

    public fun fade(image: ImageView?, fadeDuration: Long) {
        val fade = ObjectAnimator.ofFloat(image, View.ALPHA, 0.1f, 5.1f)
        fade.setDuration(fadeDuration)
        fade.start()
    }

}