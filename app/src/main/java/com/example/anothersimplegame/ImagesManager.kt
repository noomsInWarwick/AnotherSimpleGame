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
        fruitOne: ImageView?, fruitTwo: ImageView?,
        fruitThree: ImageView?,
        fruitFour: ImageView?,
        fruitFive: ImageView?,
        fruitSix: ImageView?,
        isVisible: Boolean
    ) {

        setFruitVisibility(
            fruitOne,
            fruitTwo,
            fruitThree,
            fruitFour,
            fruitFive,
            fruitSix,
            isVisible
        )
        fade(fruitOne, 60000)
        fade(fruitTwo, 35000)
        fade(fruitThree, 50000)
        fade(fruitFour, 18000)
        fade(fruitFive, 30000)
        fade(fruitSix, 45000)
    }

    fun setFruitVisibility(
        fruitOne: ImageView?, fruitTwo: ImageView?,
        fruitThree: ImageView?,
        fruitFour: ImageView?,
        fruitFive: ImageView?,
        fruitSix: ImageView?,
        isVisible: Boolean
    ) {

        if (isVisible) {
            fruitOne!!.visibility = View.VISIBLE
            fruitTwo!!.visibility = View.VISIBLE
            fruitThree!!.visibility = View.VISIBLE
            fruitFour!!.visibility = View.VISIBLE
            fruitFive!!.visibility = View.VISIBLE
            fruitSix!!.visibility = View.VISIBLE
        } else {
            fruitOne!!.visibility = View.INVISIBLE
            fruitTwo!!.visibility = View.INVISIBLE
            fruitThree!!.visibility = View.INVISIBLE
            fruitFour!!.visibility = View.INVISIBLE
            fruitFive!!.visibility = View.INVISIBLE
            fruitSix!!.visibility = View.INVISIBLE
        }
    }
}