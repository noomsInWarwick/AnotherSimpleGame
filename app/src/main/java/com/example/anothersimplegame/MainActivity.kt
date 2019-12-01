package com.example.anothersimplegame

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Runnable
import androidx.constraintlayout.widget.ConstraintLayout as ConstraintLayout1

class MainActivity : AppCompatActivity() {

    var imageList = ArrayList<ImageView>()
    var displayedImageList = ArrayList<ImageView>()
    var randomIndexesList = ArrayList<Int>()

    var score: Int = 0
    var nbrImagesDisplayed = 0
    var imageTimer = 3000L
    var handler: Handler = Handler()
    var runnable: Runnable = Runnable {}
    var displayedIdx = 0
    var randomIdx = 0
    var gameActive = true
    var threadCounter = 0
    var countDownTimer = 30000L
    var isDone = false

    var prefs: PiggySnakePreferencesReader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)

        val context: Context = this@MainActivity.baseContext
        prefs = PiggySnakePreferencesReader(this)
        val bgImage = prefs!!.bgImage

        setBackground(context, bgImage)

        gameActive = true
        threadCounter = 0
        isDone = false
        playAgainButton.visibility = View.INVISIBLE
        score = 0
        nbrImagesDisplayed = 0

        initImagesList(bgImage)
        initRandomIndexes()
    }

    override fun onStart() {
        super.onStart();

        hideImages()

        object : CountDownTimer(countDownTimer, 3000) {
            override fun onFinish() {
                timerTextView.text = ""
                imageView5.setImageDrawable(getResources().getDrawable(R.drawable.piggysnake_smiley_old))
                imageView5.visibility = View.VISIBLE
                playAgainButton.visibility = View.VISIBLE
                gameActive = false
                handler.removeCallbacks(runnable)
            }

            override fun onTick(p0: Long) {
                if (isDone) {
                    onFinish()
                } else {
                    timerTextView.text = "Time: " + p0 / 3000
                }
            }
        }.start()
    }

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.piggy_snake_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.settings -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            if (item.getTitle() == "Winter" || item.getTitle() == "Spring"
                || item.getTitle() == "Summer" || item.getTitle() == "Autumn"
            ) {
                prefs!!.writeBackgroundToUse(item.getTitle().toString())
                restartTheGame()
            }

            super.onOptionsItemSelected(item)
        }
    }

    fun setBackground(
        context: Context, bgImage: String?
    ) {

        val rl: ConstraintLayout1
        rl = findViewById(R.id.mainConstraint)
        rl.setBackgroundResource(prefs!!.setBackground(bgImage))
    }

    fun hideImages() {

        runnable = object : Runnable {
            override fun run() {

                if (nbrImagesDisplayed >= 9) {
                    isDone = true
                }

                for (displayedImage in displayedImageList) {
                    displayedIdx = imageList.indexOf(displayedImage)
                    if (imageList.indexOf(displayedImage) >= 0) {
                        imageList[displayedIdx].setImageDrawable(getResources().getDrawable(R.drawable.piggysnake_smiley));
                        imageList[displayedIdx].maxWidth = 75
                    }
                }

                for (image in imageList) {
                    // if not selected yet, hide it.
                    if (displayedImageList.indexOf(image) == -1) {
                        image.visibility = View.INVISIBLE
                    }
                }

                if (randomIndexesList.size > 0) {
                    randomIdx = randomIndexesList.get(0)
                    randomIndexesList.removeAt(0)
                }

                nbrImagesDisplayed++
                imageList[randomIdx].visibility = View.VISIBLE
                handler.postDelayed(runnable, imageTimer)
            }
        }

        handler.post(runnable)
    }

    fun increaseScore(view: View) {

        if (gameActive) {
            score++
            scoreValueView.text = score.toString()
            displayedImageList.add(imageList[imageList.indexOf(view)])
            handler.removeCallbacks(runnable)
            handler.post(runnable)
        }
    }

    fun doRestart(view: View) {
        restartTheGame()
    }

    fun restartTheGame() {
        finish();
        startActivity(getIntent());
    }

    fun initImagesList(bgImage: String?) {

        var dr = prefs!!.setPiggySnakeImage(bgImage)

        imageView1.setImageDrawable(getResources().getDrawable(dr));
        imageView2.setImageDrawable(getResources().getDrawable(dr));
        imageView3.setImageDrawable(getResources().getDrawable(dr));
        imageView4.setImageDrawable(getResources().getDrawable(dr));
        imageView5.setImageDrawable(getResources().getDrawable(dr));
        imageView6.setImageDrawable(getResources().getDrawable(dr));
        imageView7.setImageDrawable(getResources().getDrawable(dr));
        imageView8.setImageDrawable(getResources().getDrawable(dr));
        imageView9.setImageDrawable(getResources().getDrawable(dr));

        imageList = arrayListOf(
            imageView1,
            imageView2,
            imageView3,
            imageView4,
            imageView5,
            imageView6,
            imageView7,
            imageView8,
            imageView9
        )
    }

    fun initRandomIndexes() {
        // load and then randomize the list to determine the order of the images display.
        randomIndexesList = arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        randomIndexesList.shuffle()
    }
}