package com.example.anothersimplegame

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView

class ImagesManager {

    var imageList = ArrayList<ImageView>()
    var displayedImageList = ArrayList<ImageView>()
    var randomIndexesList = ArrayList<Int>()

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

    public fun rotate(image: ImageView?) {

        val rotate = ObjectAnimator.ofFloat(image, View.ROTATION, -180f, 0f)
        rotate.setDuration(9000)

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

        fade(image, 3000)
        //val adjustedWidth = layoutWidth * .8

        val tx = ValueAnimator.ofFloat(0f, layoutWidth.toFloat())
        val mDuration = 20000 //in millis
        tx.duration = mDuration.toLong()
        tx.addUpdateListener { animation -> image!!.setTranslationX(animation.animatedValue as Float) }
        tx.start()
        // delay and fade

    }

    public fun fade(image: ImageView?, fadeDuration: Long) {
        val fade = ObjectAnimator.ofFloat(image, View.ALPHA, 0.2f, 1.0f)
        fade.setDuration(fadeDuration)
        fade.start()
    }
}