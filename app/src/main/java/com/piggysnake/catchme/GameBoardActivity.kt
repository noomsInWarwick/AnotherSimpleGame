package com.piggysnake.catchme

import android.content.Context
import android.graphics.Color
import android.os.*
import android.os.VibrationEffect.createOneShot
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.piggysnake.catchme.R.drawable
import com.piggysnake.catchme.imagesmanagers.ImagesManagerAutumn
import com.piggysnake.catchme.imagesmanagers.ImagesManagerFruits
import com.piggysnake.catchme.imagesmanagers.ImagesManagerSeasonCharacter
import com.piggysnake.catchme.imagesmanagers.MessagesManager
import kotlinx.android.synthetic.main.activity_game_board.*
import kotlinx.android.synthetic.main.fragment_start_again.*
import kotlinx.android.synthetic.main.fragment_timer_n_messages.*
import kotlinx.android.synthetic.main.fragment_tool_bar_constrained.*
import kotlinx.coroutines.Runnable
import java.util.*
import kotlin.concurrent.schedule
import android.widget.LinearLayout as LinearLayout1

@Suppress("DEPRECATION")
class
GameBoardActivity : AppCompatActivity(), OnInitListener {

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

    private var textToSpeech: TextToSpeech? = null
    private var score: Int = 0
    private var nbrImagesDisplayed = 0
    private var imageTimer = 3000L
    private var handler: Handler = Handler()
    private var runnable: Runnable = Runnable {}
    private var displayedIdx = 0
    private var randomIdx = 0
    private var gameActive = true
    private var okToTalk = true
    private var countDownTimer = 30000L
    private var inactivityTimer: CountDownTimer? = null

    // var spValue = 40F
    var isDone = false
    var autumnImagesManager: ImagesManagerAutumn = ImagesManagerAutumn
    private var seasonCharacterImagesManager: ImagesManagerSeasonCharacter =
        ImagesManagerSeasonCharacter
    private var fruitsImagesManager: ImagesManagerFruits = ImagesManagerFruits
    var messagesManager: MessagesManager = MessagesManager
    private var layoutWidth = 0
    private var layoutHeight = 0
    private var seasonFriendName = "snowman"
    private var endOfGameFriendName = "snowman"

    private var fallingleafone: ImageView? = null
    private var fallingleaftwo: ImageView? = null
    private var fallingleafthree: ImageView? = null
    private var fallingleaffour: ImageView? = null
    private lateinit var theSeasonCharacter: ImageView
    // private var endOfGameImage: Drawable? = null

    private var prefs: PiggySnakePreferencesReader? = null
    private var messagesQueue: Queue<String> = LinkedList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_board)
        // To hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        layoutWidth = displayMetrics.widthPixels
        layoutHeight = displayMetrics.heightPixels

        val TIMELABEL = resources.getString(R.string.time)
        messagesManager.loadMessages(resources)
        playAgainButton.visibility = View.INVISIBLE
        score = 0
        nbrImagesDisplayed = 0
        gameActive = true
        isDone = false

        val context: Context = this@GameBoardActivity.baseContext
        prefs = PiggySnakePreferencesReader(this)
        val bgImage = prefs!!.bgImage

        setBackground(context, bgImage)

        prepareSeasonCharacter(layoutWidth)
        initImagesList(bgImage)
        manageImages()

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
            }
        }

        object : CountDownTimer(countDownTimer, 3000) {

            override fun onFinish() {
                if (gameActive) {
                    timerTextView.visibility = View.INVISIBLE
                    if (niceMessageTextView != null) {
                        niceMessageTextView.visibility = View.VISIBLE
                        val endMessage = messagesManager.getMessage()
                        niceMessageTextView.text = endMessage
                        doTalk(endMessage)
                    }
                    imageView5.setImageDrawable(resources.getDrawable(prefs!!.getEndOfGameImage()))
                    imageView5.visibility = View.VISIBLE
                    imageView5.isEnabled = true
                    autumnImagesManager.fade(imageView5, 3000)
                    playAgainButton.visibility = View.VISIBLE
                    gameActive = false
                    fruitsList.clear()
                    randomIndexesList.clear()
                    imageList.clear()
                    displayedImageList.clear()
                    if (fragment_message_toolbar != null) {
                        fragment_message_toolbar.setBackgroundColor(messageBackgroundColor)
                    }
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

        inactivityTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                finishAndRemoveTask()
            }
        }.start()

        textToSpeech = TextToSpeech(this, this)

        // ### listeners
        backarrow_image.setOnClickListener {
            finishAndRemoveTask()
        }
        spring_image.setOnClickListener {
            changeSeasons(SPRINGLABEL)
        }
        summer_image.setOnClickListener {
            changeSeasons(SUMMERLABEL)
        }
        autumn_image.setOnClickListener {
            changeSeasons(AUTUMNLABEL)
        }
        winter_image.setOnClickListener {
            changeSeasons(WINTERLABEL)
        }
        playAgainButton.setOnClickListener {
            finishAndRemoveTask()
            startActivity(intent)
        }

        imageView1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                increaseScore(view)
            }
        })
        imageView2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                increaseScore(view)
            }
        })
        imageView3.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                increaseScore(view)
            }
        })
        imageView4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                increaseScore(view)
            }
        })
        imageView5.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (gameActive) {
                    increaseScore(view)
                } else {
                    sayEndOfGameFriendName(view)
                    vibrate()
                }
            }
        })
        imageView6.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                increaseScore(view)
            }
        })
        imageView7.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                increaseScore(view)
            }
        })
        imageView8.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                increaseScore(view)
            }
        })
        imageView9.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                increaseScore(view)
            }
        })
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

    override fun onUserInteraction() {
        inactivityTimer!!.cancel()
        inactivityTimer!!.start()
    }


    private fun prepareSeasonCharacter(layoutWidth: Int) {

        theSeasonCharacter = findViewById(R.id.snowmanImageView)

        seasonCharacterImagesManager.doSeasonCharacter(theSeasonCharacter)
        seasonCharacterImagesManager.setSeasonCharacterVisibility(theSeasonCharacter, true)
        seasonCharacterImagesManager.fade(theSeasonCharacter, 10000)
        seasonCharacterImagesManager.moveSeasonCharacter(theSeasonCharacter, layoutWidth)

        when (prefs!!.currentSeason) {
            Seasons.Spring -> {
                theSeasonCharacter.setImageDrawable(getResources().getDrawable(R.drawable.ladybug_transparent))
                seasonFriendName = "red bug"
                endOfGameFriendName = resources.getString(R.string.star)
            }
            Seasons.Summer -> {
                theSeasonCharacter.setImageDrawable(getResources().getDrawable(R.drawable.bumble_transparent))
                seasonFriendName = "bumble bee"
                endOfGameFriendName = resources.getString(R.string.icecream)
            }
            Seasons.Autumn -> {
                theSeasonCharacter.setImageDrawable(getResources().getDrawable(R.drawable.pumpkin_transparent))
                seasonFriendName = "pumpkin"
                endOfGameFriendName = resources.getString(R.string.splat)
            }
            Seasons.Winter -> {
                theSeasonCharacter.setImageDrawable(getResources().getDrawable(R.drawable.snowman_trans))
                seasonFriendName = "snowman"
                endOfGameFriendName = resources.getString(R.string.snowman)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setBackground(
        context: Context, bgImage: String?
    ) {
        val rl: LinearLayout1
        rl = findViewById(R.id.mainConstraint)
        prefs!!.determineSeason(bgImage)
        rl.setBackgroundResource(prefs!!.setBackground())
    }

    private fun manageImages() {

        runnable = Runnable {
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
                        //  orangeTwo.visibility = View.INVISIBLE
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
            scoreTextView.text = resources.getString(R.string.score) + " " + score.toString()
            displayedImageList.add(imageList[imageList.indexOf(view)])
            view.setEnabled(false)
            handler.removeCallbacks(runnable)
            handler.post(runnable)
        }
    }

    fun doRestart(view: View) {
        restartTheGame()
    }

    private fun restartTheGame() {
        finishAndRemoveTask()
        startActivity(intent)
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
        val dr = prefs!!.setPiggySnakeImage()

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
        return false
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech!!.setLanguage(Locale.getDefault())
            textToSpeech!!.setPitch(1.75F)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The specified language is not supported.")
            }
        } else {
            Log.e("TTS", "Unable to initialize the TextToSpeech functionality.")
        }
    }

    fun sayFriendName(view: View) {
        doTalk(seasonFriendName)
    }

    fun sayEndOfGameFriendName(view: View) {
        doTalk(endOfGameFriendName)
    }

    private fun doTalk(text: String) {
        if (textToSpeech != null) {
            if (!textToSpeech!!.isSpeaking) {
                textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }
    }
//    private fun doTalk(text: String) {
//        messagesQueue.add(text)
//        var currentMessage: String
//        if (textToSpeech != null) {
//            for (message in messagesQueue) {
//                if (!textToSpeech!!.isSpeaking) {
//                    currentMessage = messagesQueue.poll()
//                    textToSpeech!!.speak(currentMessage, TextToSpeech.QUEUE_FLUSH, null, "")
//                }
//            }
//        }
//    }

//
}