package com.example.anothersimplegame

import android.content.Context
import android.content.SharedPreferences
import com.example.anothersimplegame.Seasons.*

class PiggySnakePreferencesReader(context: Context) {

    val PREFS_FILENAME = "com.example.anothersimplegame.PiggySnake.properties"
    val BACKGROUND_IMAGE = "backgroundimage"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var currentSeason = Summer

    var bgImage: String?
        get() = prefs.getString(BACKGROUND_IMAGE, "piggy_backdrop_spring")
        set(value) = prefs.edit().putString(BACKGROUND_IMAGE, value).apply()

    fun determineSeason(seasonTitle: String?) {

        when (seasonTitle) {
            "Spring" -> currentSeason = Spring
            "Summer" -> currentSeason = Summer
            "Autumn" -> currentSeason = Autumn
            "Winter" -> currentSeason = Winter
            "piggy_backdrop_spring" -> currentSeason = Spring
            "piggy_backdrop_summer" -> currentSeason = Summer
            "piggy_backdrop_autumn" -> currentSeason = Autumn
            "piggy_backdrop_winter" -> currentSeason = Winter
            else -> currentSeason = Summer
        }
    }

    fun writeBackgroundToUse() {

        when (currentSeason) {
            Spring -> bgImage = "piggy_backdrop_spring"
            Summer -> bgImage = "piggy_backdrop_summer"
            Autumn -> bgImage = "piggy_backdrop_autumn"
            Winter -> bgImage = "piggy_backdrop_winter"
            else -> bgImage = "piggy_backdrop"
        }
    }

    fun setBackground(): Int {

        var dr: Int = R.drawable.piggy_backdrop
        when (currentSeason) {
            Spring -> dr = R.drawable.piggy_backdrop_spring
            Summer -> dr = R.drawable.piggy_backdrop
            Autumn -> dr = R.drawable.piggy_backdrop_autumn
            Winter -> dr = R.drawable.piggy_backdrop_winter
            else -> R.drawable.piggy_backdrop
        }
        return dr
    }

    fun setPiggySnakeImage(): Int {

        var dr: Int = R.drawable.piggysnake
        when (currentSeason) {
            Spring -> dr = R.drawable.piggysnake
            Summer -> dr = R.drawable.piggysnakedoessummer
            Autumn -> dr = R.drawable.piggysnake
            Winter -> dr = R.drawable.piggysnakedoeswinter
            else -> dr = R.drawable.piggysnake
        }
        return dr
    }

    fun getEndOfGameImage(): Int {

        var dr = R.drawable.endofgame_splat_trans
        when (currentSeason) {
            Spring -> dr = R.drawable.endofgame_star_trans
            Summer -> dr = R.drawable.endofgame_icecream_trans
            Autumn -> dr = R.drawable.endofgame_splat_trans
            Winter -> dr = R.drawable.endofgame_snowman_trans
            else -> R.drawable.endofgame_splat_trans
        }
        return dr
    }
}