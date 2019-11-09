package com.example.anothersimplegame

import android.content.Context
import java.util.*

class PropertiesReader() {

    private val properties: Properties

    init {
        properties = Properties()
    }

    fun getProperties(FileName: String, context: Context): String {

        val properties = Properties()
        val assetManager = context.getAssets()
        val inputStream = assetManager.open("PiggySnake.properties")
        properties.load(inputStream)
        return properties.getProperty("backgroundimage")
    }
}

