package com.piggysnake.catchme.imagesmanagers

import android.animation.ObjectAnimator
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import kotlinx.coroutines.Runnable

object ImagesManagerAutumn {

    var imageRunnable: Runnable = Runnable {}
    var handler: Handler = Handler()

    var leafNumber = 1

    fun descendingLeavesDriver(
        leafOne: ImageView?, leafTwo: ImageView?,
        leafThree: ImageView?,
        leafFour: ImageView?
    ) {

        imageRunnable = object : Runnable {
            override fun run() {
                leafNumber = descendingLeaves(
                    leafOne, leafTwo,
                    leafThree, leafFour, leafNumber
                )
                handler.postDelayed(imageRunnable, 9000L)
            }
        }

        handler.post(imageRunnable)
    }

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

    fun descendingLeaves(
        leafOne: ImageView?, leafTwo: ImageView?,
        leafThree: ImageView?,
        leafFour: ImageView?,
        leafNumber: Int
    ): Int {

        var nextLeaf = 0

        when (leafNumber) {
            1 -> {
                rotate(leafOne, 9000)
                rotate(leafThree, 12000)
                rotate(leafTwo, 7000)
                rotate(leafFour, 10000)
                nextLeaf = 2
            }
            2 -> {
                rotate(leafTwo, 9000)
                rotate(leafFour, 7000)
                nextLeaf = 3
            }
            3 -> {
                rotate(leafOne, 15000)
                rotate(leafThree, 8000)
                rotate(leafTwo, 10000)
                nextLeaf = 4
            }
            4 -> {
                rotate(leafOne, 9000)
                rotate(leafFour, 7000)
                nextLeaf = 1
            }
            else -> {
                rotate(leafOne, 9000)
                nextLeaf = 1
            }
        }
        return nextLeaf
    }

    private fun rotate(image: ImageView?, fallDuration: Long) {

        //fade(image, 3000)
        fade(image, 8000)

        val rotate = ObjectAnimator.ofFloat(image, View.ROTATION, -180f, 0f)
        rotate.duration = fallDuration

        rotate.setFloatValues(-600f, 400f)
        rotate.addUpdateListener { animation ->
            image!!.translationY = animation.animatedValue as Float
        }

        rotate.interpolator = AccelerateInterpolator()
        rotate.start()
    }

    fun fade(image: ImageView?, fadeDuration: Long) {
        val fade = ObjectAnimator.ofFloat(image, View.ALPHA, 0.1f, 5.1f)
        fade.duration = fadeDuration
        fade.start()
    }

    fun cleanUp() {
        handler.removeCallbacks(imageRunnable)
    }
}