package com.example.anothersimplegame

import android.content.Context
import android.content.SharedPreferences

class PiggySnakePreferencesReader(context: Context) {

    val PREFS_FILENAME = "com.example.anothersimplegame.PiggySnake.properties"
    val BACKGROUND_IMAGE = "backgroundimage"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var bgImage: String?
        get() = prefs.getString(BACKGROUND_IMAGE, "piggy_backdrop_spring")
        set(value) = prefs.edit().putString(BACKGROUND_IMAGE, value).apply()

    fun writeBackgroundToUse(seasonTitle: String) {
        when (seasonTitle) {
            "Spring" -> bgImage = "piggy_backdrop_spring"
            "Summer" -> bgImage = "piggy_backdrop_summer"
            "Autumn" -> bgImage = "piggy_backdrop_autumn"
            "Winter" -> bgImage = "piggy_backdrop_winter"
            else -> bgImage = "piggy_backdrop"
        }
    }

    fun setBackground(bgImage: String?): Int {

        var dr: Int = R.drawable.piggy_backdrop

        when (bgImage) {
            "piggy_backdrop_spring" -> dr = R.drawable.piggy_backdrop_spring
            "piggy_backdrop_summer" -> dr = R.drawable.piggy_backdrop
            "piggy_backdrop_autumn" -> dr = R.drawable.piggy_backdrop_autumn
            "piggy_backdrop_winter" -> dr = R.drawable.piggy_backdrop_winter
            else -> R.drawable.piggy_backdrop
        }

        return dr
    }

    fun setPiggySnakeImage(bgImage: String?): Int {

        var dr: Int = R.drawable.piggysnake

        when (bgImage) {
            "piggy_backdrop_spring" -> dr = R.drawable.piggysnake
            "piggy_backdrop_summer" -> dr = R.drawable.piggysnakedoessummer
            "piggy_backdrop_autumn" -> dr = R.drawable.piggysnake
            "piggy_backdrop_winter" -> dr = R.drawable.piggysnakedoeswinter
            else -> dr = R.drawable.piggysnake
        }

        return dr;
    }

}

