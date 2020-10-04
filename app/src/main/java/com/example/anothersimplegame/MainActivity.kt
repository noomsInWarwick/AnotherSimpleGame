package com.example.anothersimplegame

import android.app.FragmentTransaction
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.*
import android.os.VibrationEffect.createOneShot
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat
import com.example.anothersimplegame.R.drawable
import com.example.anothersimplegame.imagesmanagers.ImagesManagerAutumn
import com.example.anothersimplegame.imagesmanagers.ImagesManagerFruits
import com.example.anothersimplegame.imagesmanagers.ImagesManagerSnowman
import com.example.anothersimplegame.imagesmanagers.MessagesManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_snowman.*
import kotlinx.android.synthetic.main.fragment_timer_n_messages.*
import kotlinx.coroutines.Runnable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import androidx.constraintlayout.widget.ConstraintLayout as ConstraintLayout1

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    companion object {
        const val WINTERLABEL = "Winter"
        const val SPRINGLABEL = "Spring"
        const val SUMMERLABEL = "Summer"
        const val AUTUMNLABEL = "Autumn"
    }

    var imageList = ArrayList<ImageView>()
    var displayedImageList = ArrayList<ImageView>()
    var fruitsList = ArrayList<ImageView?>()
    var randomIndexesList = ArrayList<Int>()
    var timer: Timer? = null
    var messageBackgroundColor = Color.BLUE

    private var score: Int = 0
    private var nbrImagesDisplayed = 0
    var imageTimer = 3000L
    var handler: Handler = Handler()
    var runnable: Runnable = Runnable {}
    var displayedIdx = 0
    var randomIdx = 0
    var gameActive = true
    var countDownTimer = 30000L
    var isDone = false
    var autumnImagesManager: ImagesManagerAutumn = ImagesManagerAutumn
    var snowmanImagesManager: ImagesManagerSnowman = ImagesManagerSnowman
    var fruitsImagesManager: ImagesManagerFruits = ImagesManagerFruits
    var messagesManager: MessagesManager = MessagesManager
    var layoutWidth = 0
    var layoutHeight = 0

    private var fallingleafone: ImageView? = null
    private var fallingleaftwo: ImageView? = null
    private var fallingleafthree: ImageView? = null
    private var fallingleaffour: ImageView? = null
    private var theSnowman: ImageView? = null

    private var endOfGameImage: Drawable? = null

    private var prefs: PiggySnakePreferencesReader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        layoutWidth = displayMetrics.widthPixels
        layoutHeight = displayMetrics.heightPixels
        val ratioValueGridWidth = .319
        val ratioValueGridHeight = .3125
        val ratioValueHeight = .64

        var TIMELABEL = resources.getString(R.string.time)
        messagesManager.loadMessages(resources)
        playAgainButton.visibility = View.INVISIBLE
        score = 0
        nbrImagesDisplayed = 0
        gameActive = true
        isDone = false

        val context: Context = this@MainActivity.baseContext
        prefs = PiggySnakePreferencesReader(this)
        val bgImage = prefs!!.bgImage

        setBackground(context, bgImage)
        horizontalGuideline_bottom.setGuidelineBegin(500)

        prepareSnowman()
        initImagesList(bgImage)

        manageImages()

        baseLinearLayout.getLayoutParams().height = (ratioValueHeight * layoutHeight).toInt()

        val gridImageWidth = (ratioValueGridWidth * layoutWidth).toInt()
        val gridImageHeight =
            (ratioValueGridHeight * baseLinearLayout.getLayoutParams().height).toInt()

        imageView1.getLayoutParams().width = gridImageWidth
        imageView1.getLayoutParams().height = gridImageHeight
        imageView2.getLayoutParams().width = gridImageWidth
        imageView2.getLayoutParams().height = gridImageHeight
        imageView3.getLayoutParams().width = gridImageWidth
        imageView3.getLayoutParams().height = gridImageHeight
        imageView4.getLayoutParams().width = gridImageWidth
        imageView4.getLayoutParams().height = gridImageHeight
        imageView5.getLayoutParams().width = gridImageWidth
        imageView5.getLayoutParams().height = gridImageHeight
        imageView6.getLayoutParams().width = gridImageWidth
        imageView6.getLayoutParams().height = gridImageHeight
        imageView7.getLayoutParams().width = gridImageWidth
        imageView7.getLayoutParams().height = gridImageHeight
        imageView8.getLayoutParams().width = gridImageWidth
        imageView8.getLayoutParams().height = gridImageHeight
        imageView9.getLayoutParams().width = gridImageWidth
        imageView9.getLayoutParams().height = gridImageHeight
        when (prefs!!.currentSeason) {
            Seasons.Spring -> {
                messageBackgroundColor = Color.GREEN
            }
            Seasons.Summer -> {
                messageBackgroundColor = Color.YELLOW
                prepareSummerFruits()
            }
            Seasons.Autumn -> {
                messageBackgroundColor = Color.parseColor("#FF9800")
                prepareAutumnLeaves()
                setLeavesVisibility()
                descendingLeaves()
            }
            Seasons.Winter -> {
                messageBackgroundColor = Color.CYAN
                snowmanImagesManager.doSnowman(theSnowman)
                snowmanImagesManager.setSnowmanVisibility(theSnowman, true)
                snowmanImagesManager.fade(theSnowman, 10000)
                snowmanImagesManager.moveSnowman(theSnowman, layoutWidth)
            }
        }

        object : CountDownTimer(countDownTimer, 3000) {

            override fun onFinish() {
                if (gameActive) {
                    timerTextView.visibility = View.INVISIBLE
                    niceMessageTextView.visibility = View.VISIBLE
                    niceMessageTextView.text = messagesManager.getMessage()
                    imageView5.setImageDrawable(resources.getDrawable(prefs!!.getEndOfGameImage()))
                    imageView5.visibility = View.VISIBLE
                    autumnImagesManager.fade(imageView5, 3000)
                    playAgainButton.visibility = View.VISIBLE
                    gameActive = false
                    fruitsList.clear()
                    randomIndexesList.clear()
                    imageList.clear()
                    displayedImageList.clear()
                    fragment_message_toolbar.setBackgroundColor(messageBackgroundColor)
                }

                handler.removeCallbacks(runnable)
                if (prefs!!.currentSeason == Seasons.Autumn) {
                    autumnImagesManager.cleanUp()
                }
            }

            override fun onTick(p0: Long) {
                if (isDone) {
                    onFinish()
                } else {
                    timerTextView.text = TIMELABEL + " " + p0 / 3000
                }
            }
        }.start()
    }

    override fun onStart() {
        super.onStart()
        initRandomIndexes()
    }

    override fun onPause() {
        super.onPause()
        Timer().schedule(300000) {
            finishAndRemoveTask()
        }
    }

    private fun prepareSnowman() {
        theSnowman = findViewById(R.id.snowmanImageView)
        snowmanImagesManager.setSnowmanVisibility(theSnowman, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setBackground(
        context: Context, bgImage: String?
    ) {
        val rl: ConstraintLayout1
        rl = findViewById(R.id.mainConstraint)
        prefs!!.determineSeason(bgImage)
        rl.setBackgroundResource(prefs!!.setBackground())
    }

    private fun manageImages() {

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

        fallingleafone = findViewById(R.id.fallingleaf_one)
        fallingleaftwo = findViewById(R.id.fallingleaf_two)
        fallingleafthree = findViewById(R.id.fallingleaf_three)
        fallingleaffour = findViewById(R.id.fallingleaf_four)
    }

    private fun setLeavesVisibility() {

        autumnImagesManager.setLeavesVisibility(
            fallingleafone,
            fallingleaftwo,
            fallingleafthree,
            fallingleaffour,
            true
        )
    }

    private fun descendingLeaves() {

        autumnImagesManager.descendingLeavesDriver(
            fallingleafone,
            fallingleaftwo,
            fallingleafthree,
            fallingleaffour
        )
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
        finishAndRemoveTask()
        startActivity(intent)
    }

    fun doBackArrow(view: View) {
        finishAndRemoveTask()
    }

    fun doWinter(view: View) {
        changeSeasons(WINTERLABEL)
    }

    fun doSpring(view: View) {
        changeSeasons(SPRINGLABEL)
    }

    fun doSummer(view: View) {
        changeSeasons(SUMMERLABEL)
    }

    fun doAutumn(view: View) {
        changeSeasons(AUTUMNLABEL)
    }

    private fun changeSeasons(season: String) {

        if (!prefs!!.currentSeason.equals(season)) {
            prefs!!.determineSeason(season)
            prefs!!.writeBackgroundToUse()
        }
        restartTheGame()
    }

    private fun initImagesList(bgImage: String?) {

        prefs!!.determineSeason(bgImage)

        // determine the piggy snake image to display - is based on season.
        var dr = prefs!!.setPiggySnakeImage()

        // load the grid's views with the piggy snake image.
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

    private fun prepareSummerFruits() {

        fruitsList = arrayListOf(
            findViewById(R.id.orangeOne),
            findViewById(R.id.orangeTwo),
            findViewById(R.id.orangeThree),
            findViewById(R.id.orangeFour),
            findViewById(R.id.orangeFive),
            findViewById(R.id.orangeSix),
            findViewById(R.id.orangeSeven)
        )

        fruitsImagesManager.doTheFruit(
            fruitsList,
            true
        )
    }

    private fun initRandomIndexes() {
        // load and then randomize the list to determine the order of the images display.
        randomIndexesList = arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        randomIndexesList.shuffle()
    }

    private fun Context.vibrate(milliseconds: Long = 100) {
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

        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        val action: Int = MotionEventCompat.getActionMasked(event)
        return false
    }

}