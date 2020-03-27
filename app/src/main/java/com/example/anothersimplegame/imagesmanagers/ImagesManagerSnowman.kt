package com.example.anothersimplegame.imagesmanagers

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView

class ImagesManagerSnowman {

    var snowmanTimer = 30000L

    fun setSnowmanVisibility(snowman: ImageView?, isVisible: Boolean) {

        if (isVisible) {
            snowman!!.visibility = View.VISIBLE
        } else {
            snowman!!.visibility = View.INVISIBLE
        }
    }

    fun moveSnowman(image: ImageView?, layoutWidth: Int) {

        val adjustedWidth = layoutWidth * .7

        val tx = ValueAnimator.ofFloat(0f, adjustedWidth.toFloat())
        val mDuration = 40000 //in millis
        tx.duration = mDuration.toLong()
        tx.addUpdateListener { animation ->
            image!!.translationX = animation.animatedValue as Float
        }
        tx.start()
    }

    fun fade(image: ImageView?, fadeDuration: Long) {
        val fade = ObjectAnimator.ofFloat(image, View.ALPHA, 0.1f, 5.1f)
        fade.duration = fadeDuration
        fade.start()
    }

    fun fadeAway(image: ImageView?, fadeDuration: Long) {
        val fade = ObjectAnimator.ofFloat(image, View.ALPHA, 0.1f, 5.1f)
        fade.duration = fadeDuration
        fade.reverse()
    }

    fun doSnowman(snowmanImage: ImageView?) {
        object : CountDownTimer(snowmanTimer, 3000) {

            override fun onFinish() {
                fadeAway(snowmanImage, 8000)
            }

            override fun onTick(p0: Long) {

            }
        }.start()
    }

}