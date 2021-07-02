package com.piggysnake.catchme

import android.content.Intent
import android.os.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.piggysnake.catchme.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var startGameBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startGameBinding = ActivityMainBinding.inflate(layoutInflater)
        val mainView = startGameBinding.root
        setContentView(mainView)
        // To hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // startGameButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
    }

    fun startGame(view: View) {
        val gameIntent = Intent(this@MainActivity, GameBoardActivity::class.java)
        //val gameIntent = Intent(this@MainActivity, PiggySnakeSplash::class.java)
        startActivity(gameIntent)
    }

    fun exitGame(view: View) {
        finishAndRemoveTask()
    }

    fun resetSelected(view: View) {
        finish()
        startActivity(getIntent());
    }
}