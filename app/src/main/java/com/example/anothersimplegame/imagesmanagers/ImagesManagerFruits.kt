package com.example.anothersimplegame.imagesmanagers

import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView

class ImagesManagerFruits {

    private fun fade(image: ImageView?, fadeDuration: Long) {
        val fade = ObjectAnimator.ofFloat(image, View.ALPHA, 0.1f, 5.1f)
        fade.duration = fadeDuration
        fade.start()
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

    private fun setFruitVisibility(fruitsList: ArrayList<ImageView?>, isVisible: Boolean) {

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