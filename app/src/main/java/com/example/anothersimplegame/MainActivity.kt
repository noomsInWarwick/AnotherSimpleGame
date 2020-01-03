package com.example.anothersimplegame

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.anothersimplegame.R.drawable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Runnable
import androidx.constraintlayout.widget.ConstraintLayout as ConstraintLayout1

class MainActivity : AppCompatActivity() {

    var imageList = ArrayList<ImageView>()
    var descendingImageList = ArrayList<ImageView?>()
    var displayedImageList = ArrayList<ImageView>()
    var randomIndexesList = ArrayList<Int>()

    var score: Int = 0
    var nbrImagesDisplayed = 0
    var imageTimer = 3000L
    var handler: Handler = Handler()
    var runnable: Runnable = Runnable {}
    var imageRunnable: Runnable = Runnable {}
    var displayedIdx = 0
    var randomIdx = 0
    var gameActive = true
    var threadCounter = 0
    var countDownTimer = 30000L
    var isDone = false
    var imagesManager: ImagesManager = ImagesManager()
    var leafNumber = 1
    var layoutWidth = 0
    // val bitmap: Bitmap = Bitmap.createBitmap(700, 1000, Bitmap.Config.ARGB_8888)

    private var fallingleafone: ImageView? = null
    private var fallingleaftwo: ImageView? = null
    private var fallingleafthree: ImageView? = null
    private var fallingleaffour: ImageView? = null
    private var theSnowman: ImageView? = null

    private var orangeOne: ImageView? = null
    private var orangeTwo: ImageView? = null
    private var orangeThree: ImageView? = null
    private var orangeFour: ImageView? = null
    private var orangeFive: ImageView? = null
    private var orangeSix: ImageView? = null

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

        fallingleafone = findViewById<ImageView>(R.id.fallingleaf_one)
        fallingleaftwo = findViewById<ImageView>(R.id.fallingleaf_two)
        fallingleafthree = findViewById<ImageView>(R.id.fallingleaf_three)
        fallingleaffour = findViewById<ImageView>(R.id.fallingleaf_four)

        theSnowman = findViewById<ImageView>(R.id.snowmanImageView)

        orangeOne = findViewById<ImageView>(R.id.orangeOne)
        orangeTwo = findViewById<ImageView>(R.id.orangeTwo)
        orangeThree = findViewById<ImageView>(R.id.orangeThree)
        orangeFour = findViewById<ImageView>(R.id.orangeFour)
        orangeFive = findViewById<ImageView>(R.id.orangeFive)
        orangeSix = findViewById<ImageView>(R.id.orangeSix)

        imagesManager.setSnowmanVisibility(theSnowman, false)
        imagesManager.setLeavesVisibility(
            fallingleafone,
            fallingleaftwo,
            fallingleafthree,
            fallingleaffour,
            false
        )
        imagesManager.setFruitVisibility(
            orangeOne,
            orangeTwo,
            orangeThree,
            orangeFour,
            orangeFive,
            orangeSix,
            false
        )

        initImagesList(bgImage)
        initRandomIndexes()

        // get device dimensions
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        layoutWidth = displayMetrics.widthPixels
    }

    override fun onStart() {
        super.onStart()

        hideImages()

        when (prefs!!.currentSeason) {
            Seasons.Spring -> {
            }
            Seasons.Summer -> {
                imagesManager.doTheFruit(
                    orangeOne,
                    orangeTwo,
                    orangeThree,
                    orangeFour,
                    orangeFive,
                    orangeSix,
                    true
                )
            }
            Seasons.Autumn -> {
                descendingLeaves()
            }
            Seasons.Winter -> {
                imagesManager.doSnowman(theSnowman)
                imagesManager.setSnowmanVisibility(theSnowman, true)
                imagesManager.fade(theSnowman, 10000)
                imagesManager.moveSnowman(theSnowman, layoutWidth)
            }
            else -> {
            }
        }

        object : CountDownTimer(countDownTimer, 3000) {

            override fun onFinish() {
                if (gameActive) {
                    timerTextView.text = ""
                    imageView5.setImageDrawable(resources.getDrawable(drawable.piggysnake_smiley_old))
                    imageView5.visibility = View.VISIBLE
                    imagesManager.fade(imageView5, 3000)
                    playAgainButton.visibility = View.VISIBLE
                    gameActive = false
                }

                handler.removeCallbacks(runnable)
                handler.removeCallbacks(imageRunnable)
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
            if (item.title == "Winter" || item.title == "Spring"
                || item.title == "Summer" || item.title == "Autumn"
            ) {
                prefs!!.determineSeason(item.title.toString())
                prefs!!.writeBackgroundToUse()
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
        prefs!!.determineSeason(bgImage)
        rl.setBackgroundResource(prefs!!.setBackground())
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
                        imageList[displayedIdx].setImageDrawable(resources.getDrawable(drawable.piggysnake_smiley))
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

    fun descendingLeaves() {

        imagesManager.setLeavesVisibility(
            fallingleafone,
            fallingleaftwo,
            fallingleafthree,
            fallingleaffour,
            true
        )

        imageRunnable = object : Runnable {
            override fun run() {

                when (leafNumber) {
                    1 -> {
                        imagesManager.rotate(fallingleafone, 9000)
                        imagesManager.rotate(fallingleafthree, 12000)
                        imagesManager.rotate(fallingleaftwo, 7000)
                        imagesManager.rotate(fallingleaffour, 10000)
                        leafNumber = 2
                    }
                    2 -> {
                        imagesManager.rotate(fallingleaftwo, 9000)
                        imagesManager.rotate(fallingleaffour, 7000)
                        leafNumber = 3
                    }
                    3 -> {
                        imagesManager.rotate(fallingleafthree, 8000)
                        imagesManager.rotate(fallingleaftwo, 10000)
                        leafNumber = 4
                    }
                    4 -> {
                        imagesManager.rotate(fallingleaffour, 9000)
                        imagesManager.rotate(fallingleaftwo, 7000)
                        leafNumber = 1
                    }
                    else -> {
                        imagesManager.rotate(fallingleafone, 9000)
                        leafNumber = 1
                    }
                }

                handler.postDelayed(imageRunnable, 9000L)
            }
        }

        handler.post(imageRunnable)
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
        finish()
        startActivity(intent)
    }

    fun initImagesList(bgImage: String?) {

        prefs!!.determineSeason(bgImage)
        var dr = prefs!!.setPiggySnakeImage()

        imageView1.setImageDrawable(resources.getDrawable(dr))
        imageView2.setImageDrawable(resources.getDrawable(dr))
        imageView3.setImageDrawable(resources.getDrawable(dr))
        imageView4.setImageDrawable(resources.getDrawable(dr))
        imageView5.setImageDrawable(resources.getDrawable(dr))
        imageView6.setImageDrawable(resources.getDrawable(dr))
        imageView7.setImageDrawable(resources.getDrawable(dr))
        imageView8.setImageDrawable(resources.getDrawable(dr))
        imageView9.setImageDrawable(resources.getDrawable(dr))

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