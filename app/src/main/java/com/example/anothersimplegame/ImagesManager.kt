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

    public fun setLeavesVisibility(
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

    public fun rotate(image: ImageView?, fallDuration: Long) {

        fade(image, 3000)

        val rotate = ObjectAnimator.ofFloat(image, View.ROTATION, -180f, 0f)
        rotate.setDuration(fallDuration)

        rotate.setFloatValues(-600f, 400f)
        rotate.addUpdateListener { animation -> image!!.setTranslationY(animation.animatedValue as Float) }

        rotate.interpolator = AccelerateInterpolator()
        rotate.start()
    }

    public fun setSnowmanVisibility(snowman: ImageView?, isVisible: Boolean) {

        if (isVisible) {
            snowman!!.visibility = View.VISIBLE
        } else {
            snowman!!.visibility = View.INVISIBLE
        }
    }

    public fun moveSnowman(image: ImageView?, layoutWidth: Int) {

        val adjustedWidth = layoutWidth * .7

        val tx = ValueAnimator.ofFloat(0f, adjustedWidth.toFloat())
        val mDuration = 40000 //in millis
        tx.duration = mDuration.toLong()
        tx.addUpdateListener { animation -> image!!.setTranslationX(animation.animatedValue as Float) }
        tx.start()
    }

    public fun fade(image: ImageView?, fadeDuration: Long) {
        val fade = ObjectAnimator.ofFloat(image, View.ALPHA, 0.1f, 5.1f)
        fade.setDuration(fadeDuration)
        fade.start()
    }

    public fun fadeAway(image: ImageView?, fadeDuration: Long) {
        val fade = ObjectAnimator.ofFloat(image, View.ALPHA, 0.1f, 5.1f)
        fade.setDuration(fadeDuration)
        fade.reverse()
    }

    public fun doSnowman(snowmanImage: ImageView?) {
        object : CountDownTimer(snowmanTimer, 3000) {

            override fun onFinish() {
                fadeAway(snowmanImage, 8000)
            }

            override fun onTick(p0: Long) {

            }
        }.start()
    }
}