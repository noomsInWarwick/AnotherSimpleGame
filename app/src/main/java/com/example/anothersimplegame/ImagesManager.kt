package com.example.anothersimplegame

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView

class ImagesManager {

    var imageList = ArrayList<ImageView>()
    var displayedImageList = ArrayList<ImageView>()
    var randomIndexesList = ArrayList<Int>()
    var snowmanTimer = 30000L

    private var orangeOne: ImageView? = null

    fun setLeavesVisibility(
        leafOne: ImageView?, leafTwo: ImageView?,
        leafThree: ImageView?,
        leafFour: ImageView?,
        isVisible: Boolean
    ) {

        if (isVisible) {
            leafOne!!.visibility = View.VISIBLE
            leafTwo!!.visibility = View.VISIBLE
            leafThree!!.visibility = View.VISIBLE
            leafFour!!.visibility = View.VISIBLE
        } else {
            leafOne!!.visibility = View.INVISIBLE
            leafTwo!!.visibility = View.INVISIBLE
            leafThree!!.visibility = View.INVISIBLE
            leafFour!!.visibility = View.INVISIBLE
        }
    }

    fun rotate(image: ImageView?, fallDuration: Long) {

        fade(image, 3000)

        val rotate = ObjectAnimator.ofFloat(image, View.ROTATION, -180f, 0f)
        rotate.duration = fallDuration

        rotate.setFloatValues(-600f, 400f)
        rotate.addUpdateListener { animation ->
            image!!.translationY = animation.animatedValue as Float
        }

        rotate.interpolator = AccelerateInterpolator()
        rotate.start()
    }

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

    fun doTheFruit(
        fruitsList: ArrayList<ImageView?>,
        isVisible: Boolean
    ) {

        setFruitVisibility(
            fruitsList,
            isVisible
        )
    }

    fun setFruitVisibility(fruitsList: ArrayList<ImageView?>, isVisible: Boolean) {

        var idx = 0

        for (fruitImage in fruitsList) {
            if (isVisible) {
                fruitImage?.visibility = View.VISIBLE
                when (idx) {
                    0 -> {
                        fade(fruitImage, 60000)
                    }
                    1 -> {
                        fade(fruitImage, 35000)
                    }
                    2 -> {
                        fade(fruitImage, 50000)
                    }
                    3 -> {
                        fade(fruitImage, 18000)
                    }
                    4 -> {
                        fade(fruitImage, 30000)
                    }
                    5 -> {
                        fade(fruitImage, 45000)
                    }
                    else -> {
                        fade(fruitImage, 75000)
                    }
                }
                idx++
            } else {
                fruitImage?.visibility = View.INVISIBLE
            }
        }
    }

}