package com.example.anothersimplegame.imagesmanagers

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView

object ImagesManagerSeasonCharacter {

    var seasonCharacterTimer = 30000L

    fun setSeasonCharacterVisibility(seasonCharacter: ImageView?, isVisible: Boolean) {

        if (isVisible) {
            seasonCharacter!!.visibility = View.VISIBLE
        } else {
            seasonCharacter!!.visibility = View.INVISIBLE
        }
    }

    fun moveSeasonCharacter(image: ImageView?, layoutWidth: Int) {

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

    fun doSeasonCharacter(seasonCharacterImage: ImageView?) {
        object : CountDownTimer(seasonCharacterTimer, 3000) {

            override fun onFinish() {
                fadeAway(seasonCharacterImage, 8000)
            }

            override fun onTick(p0: Long) {
            }
        }.start()
    }

}