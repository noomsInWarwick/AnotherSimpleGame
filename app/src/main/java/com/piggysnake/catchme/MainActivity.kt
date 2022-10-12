package com.piggysnake.catchme

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        startGameButton.setOnClickListener {
            val gameIntent = Intent(this@MainActivity, GameBoardActivity::class.java)
            startActivity(gameIntent)
        }

        iv_exit_game.setOnClickListener {
            finishAndRemoveTask()
        }

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

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            startGameButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
        }, 2000)
    }
}