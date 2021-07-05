package com.piggysnake.catchme

import android.content.Intent
import android.os.*
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.piggysnake.catchme.R.*
import com.piggysnake.catchme.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var startGameBinding: ActivityMainBinding
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startGameBinding = ActivityMainBinding.inflate(layoutInflater)
        val mainView = startGameBinding.root
        setContentView(mainView)
        // To hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        startGameButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))

        // set for an exit after 5 minutes of inactivity.
        countDownTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                Thread.sleep(2000)
                finishAndRemoveTask()
            }
        }.start()
    }

    fun startGame(view: View) {
        val gameIntent = Intent(this@MainActivity, GameBoardActivity::class.java)
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