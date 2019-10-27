package com.example.anothersimplegame

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


class MainActivity : AppCompatActivity() {

    var imageList = ArrayList<ImageView>()
    var displayedImageList = ArrayList<ImageView>()
    var randomIndexesList = ArrayList<Int>()

    var score: Int = 0
    var imageTimer = 3000L
    var handler: Handler = Handler()
    var runnable: Runnable = Runnable {}
    var displayedIdx = 0
    var clickedId = 0
    var randomIdx = 0
    var gameActive = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)

        gameActive = true;
        playAgainButton.visibility = View.INVISIBLE
        score = 0

        initImagesList()
        initRandomIndexes()
        hideImages()

        object : CountDownTimer(30000, 3000) {
            override fun onFinish() {
                timerTextView.text = ""
                imageView5.setImageDrawable(getResources().getDrawable(R.drawable.piggysnake_smiley_old))
                // imageView5.setWidth(imageView5.maxWidth * 2)
                imageView5.visibility = View.VISIBLE
                playAgainButton.visibility = View.VISIBLE
                gameActive = false
                handler.removeCallbacks(runnable)
            }

            override fun onTick(p0: Long) {
                timerTextView.text = "Time: " + p0 / 3000
            }
        }.start()
    }

    override fun onRestart() {
        super.onRestart();
        // setContentView(R.layout.add_entry);
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
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    fun hideImages() {

        runnable = object : Runnable {
            override fun run() {

                if (clickedId != 0) {
                    imageTimer = 3000L
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


                imageList[randomIdx].visibility = View.VISIBLE
                handler.postDelayed(runnable, imageTimer)

                clickedId = 0
            }
        }

        handler.post(runnable)
    }

    fun increaseScore(view: View) {
        if (gameActive) {
            score++
            scoreTextView.text = "Score: " + score
            imageTimer = 1L
            clickedId = view.id
            displayedImageList.add(imageList[imageList.indexOf(view)])
        }
    }

    fun doRestart(view: View) {

        finish();
        startActivity(getIntent());
    }

    fun initImagesList() {

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
