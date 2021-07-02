package com.piggysnake.catchme

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class PiggySnakeSplash : AppCompatActivity() {

    private var callGame: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_piggy_snake_splash)

        // To hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        Handler().postDelayed({
            doStartGame()
        }, 2000)
    }

    fun doStartGame(view: View) {
        doStartGame()
    }

    fun doStartGame() {
        if (callGame) {
            callGame = false
            val gameIntent = Intent(this@PiggySnakeSplash, MainActivity::class.java)
            startActivity(gameIntent)
            finish()
        }
    }
}