package com.example.anothersimplegame

import android.content.Context
import android.os.*
import android.os.VibrationEffect.createOneShot
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MotionEventCompat
import com.example.anothersimplegame.R.drawable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Runnable
import androidx.constraintlayout.widget.ConstraintLayout as ConstraintLayout1

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    var imageList = ArrayList<ImageView>()
    var displayedImageList = ArrayList<ImageView>()
    var fruitsList = ArrayList<ImageView?>()
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
    private var showBar = false

    private var fallingleafone: ImageView? = null
    private var fallingleaftwo: ImageView? = null
    private var fallingleafthree: ImageView? = null
    private var fallingleaffour: ImageView? = null
    private var theSnowman: ImageView? = null
    private var prefs: PiggySnakePreferencesReader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);

        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()
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

        prepareSnowman()

        initImagesList(bgImage)
        initRandomIndexes()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        layoutWidth = displayMetrics.widthPixels

        manageImages()

        when (prefs!!.currentSeason) {
            Seasons.Spring -> {
                //fruitsList.clear()
            }
            Seasons.Summer -> {
                prepareSummerFruits()
            }
            Seasons.Autumn -> {
                //fruitsList.clear()
                prepareAutumnLeaves()
                descendingLeaves()
            }
            Seasons.Winter -> {
                // fruitsList.clear()
                imagesManager.doSnowman(theSnowman)
                imagesManager.setSnowmanVisibility(theSnowman, true)
                imagesManager.fade(theSnowman, 10000)
                imagesManager.moveSnowman(theSnowman, layoutWidth)
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
                    fruitsList.clear()
                    randomIndexesList.clear()
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

    private fun prepareSnowman() {
        theSnowman = findViewById(R.id.snowmanImageView)
        imagesManager.setSnowmanVisibility(theSnowman, false)
    }

    override fun onStart() {
        super.onStart()
    }

    //setting menu in action bar
    //  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    //      menuInflater.inflate(R.menu.piggy_snake_menu, menu)
    //      return super.onCreateOptionsMenu(menu)
    //  }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setBackground(
        context: Context, bgImage: String?
    ) {

        val rl: ConstraintLayout1
        rl = findViewById(R.id.mainConstraint)
        prefs!!.determineSeason(bgImage)
        rl.setBackgroundResource(prefs!!.setBackground())
    }

    fun manageImages() {

        runnable = object : Runnable {
            override fun run() {

                if (nbrImagesDisplayed >= 9) {
                    isDone = true
                }

                // if the grid's spot is was selected/caught, have the smiley face image display.
                for (displayedImage in displayedImageList) {
                    displayedIdx = imageList.indexOf(displayedImage)
                    if (imageList.indexOf(displayedImage) >= 0) {
                        imageList[displayedIdx].setImageDrawable(resources.getDrawable(drawable.piggysnake_smiley_trans))
                        imageList[displayedIdx].maxWidth = 75
                        // set image that overlaps with piggy snake image to be invisible whan at the selected spot.
                        if (displayedIdx == 1) {
                            orangeTwo.visibility = View.INVISIBLE
                        }
                    }
                }

                // if the grid's spot has not yet been selected/caught, do not diplay anything.
                for (image in imageList) {
                    // if not selected yet, hide it.
                    if (displayedImageList.indexOf(image) == -1) {
                        image.visibility = View.INVISIBLE
                    }
                }

                // select a random index to work with and then remove that entry from the list that holds the randomly ordered index values.
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

    private fun prepareAutumnLeaves() {

        fallingleafone = findViewById<ImageView>(R.id.fallingleaf_one)
        fallingleaftwo = findViewById<ImageView>(R.id.fallingleaf_two)
        fallingleafthree = findViewById<ImageView>(R.id.fallingleaf_three)
        fallingleaffour = findViewById<ImageView>(R.id.fallingleaf_four)
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
                        imagesManager.rotate(fallingleafone, 15000)
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
            vibrate()
            score++
            scoreValueView.text = score.toString()
            displayedImageList.add(imageList[imageList.indexOf(view)])
            view.setEnabled(false)
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

    fun doWinter(view: View) {
        changeSeasons("Winter")
    }

    fun doSpring(view: View) {
        changeSeasons("Spring")
    }

    fun doSummer(view: View) {
        changeSeasons("Summer")
    }

    fun doAutumn(view: View) {
        changeSeasons("Autumn")
    }

    fun changeSeasons(season: String) {

        prefs!!.determineSeason(season)
        prefs!!.writeBackgroundToUse()
        restartTheGame()
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

    fun prepareSummerFruits() {

        fruitsList = arrayListOf(
            findViewById<ImageView>(R.id.orangeOne),
            findViewById<ImageView>(R.id.orangeTwo),
            findViewById<ImageView>(R.id.orangeThree),
            findViewById<ImageView>(R.id.orangeFour),
            findViewById<ImageView>(R.id.orangeFive),
            findViewById<ImageView>(R.id.orangeSix),
            findViewById<ImageView>(R.id.orangeSeven)
        )

        imagesManager.doTheFruit(
            fruitsList,
            true
        )
    }

    fun initRandomIndexes() {
        // load and then randomize the list to determine the order of the images display.
        randomIndexesList = arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        randomIndexesList.shuffle()
    }

    fun Context.vibrate(milliseconds: Long = 100) {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Check whether device/hardware has a vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()

        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    createOneShot(
                        milliseconds,
                        // The default vibration strength of the device.
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(milliseconds)
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        val action: Int = MotionEventCompat.getActionMasked(event)

        return when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (supportActionBar!!.isShowing) {
                    supportActionBar?.hide()
                    //    Toast.makeText(this, "Chewy is good  <80 ", Toast.LENGTH_SHORT).show()
                } else {
                    supportActionBar?.show()
                    //    Toast.makeText(this, "Chey is rad =:0", Toast.LENGTH_SHORT).show()
                }
                true
            }
            MotionEvent.ACTION_MOVE -> {
                false
            }
            MotionEvent.ACTION_UP -> {
                false
            }
            else -> false
        }
    }

}