package azka.tech.mvvmtest.Views

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import azka.tech.mvvmtest.R
import azka.tech.mvvmtest.ViewModels.MainActivityViewModel
import kotlin.math.round

/* UI PAGE: EVERYTHING RELATED TO LISTENERS ON VIEWS/WIDGETS OR MANIPULATION OF THEM WILL BE PLACED HERE */

class MainActivity : AppCompatActivity() {
    private lateinit var minutesLnrLyt: LinearLayout
    private lateinit var minutesBtnsLnrLyt: LinearLayout
    private lateinit var toolbar : Toolbar

    // text views
    private lateinit var timeTxtV : TextView
    private lateinit var scoreTxtV : TextView

    // edit texts
    private lateinit var parEdtTxt : EditText
    private lateinit var inputEdtTxt : EditText

    // buttons
    private lateinit var submitBtn : Button
    private lateinit var minute1Btn : Button
    private lateinit var minute2Btn : Button
    private lateinit var minute5Btn : Button
    private lateinit var minuteCustomBtn : Button

    private lateinit var minutesPckr: Spinner

    // others
    private val mainActivityViewModel = MainActivityViewModel()
    private lateinit var wordsList: MutableList<String>
    private lateinit var wordsListReserve: MutableList<String>
    private var milliSeconds: Long = 1000
    private var currentScore = 0
    private var totalWords = 0
    private var isTimerRunning = false
    private lateinit var timer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        /* initialize the activity */

        minutesLnrLyt = findViewById(R.id.minutesLnrLyt)
        minutesBtnsLnrLyt = findViewById(R.id.minutesBtnsLnrLyt)

        toolbar = findViewById(R.id.toolbar)

        timeTxtV = findViewById(R.id.timeTxtV)
        scoreTxtV = findViewById(R.id.scoreTxtV)

        parEdtTxt = findViewById(R.id.parEdtTxt)
        inputEdtTxt = findViewById(R.id.inputEdtTxt)

        submitBtn = findViewById(R.id.submitBtn)
        minute1Btn = findViewById(R.id.minute1Btn)
        minute2Btn = findViewById(R.id.minute2Btn)
        minute5Btn = findViewById(R.id.minute5Btn)
        minuteCustomBtn = findViewById(R.id.minuteCustomBtn)
        minutesPckr = findViewById(R.id.minutesPckr)

        setSupportActionBar(toolbar)

        val words = mainActivityViewModel.generateWords()
        parEdtTxt.setText(words)
        wordsList = words.split(" ") as MutableList<String>
        wordsListReserve = words.split(" ") as MutableList<String>

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, arrayOf("3", "4", "6", "7", "8")
        )
        minutesPckr.adapter = adapter

        listeners()
    }

    private fun listeners() {
        /* function containing listeners for all views */

        parEdtTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // make sure not to do the same opeartion again and again if the buttons are already enabled
                if (parEdtTxt.text.trim().isNotEmpty()) {
                    if (!minutesBtnsLnrLyt.getChildAt(0).isEnabled && !isTimerRunning) {
                        setButtonsEnabled()
                        submitBtn.isEnabled = true
                    }
                } else {
                    setButtonsEnabled(false)
                    submitBtn.isEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (parEdtTxt.text.toString().trim().isEmpty())
                    minute1Btn.callOnClick()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        minute1Btn.setOnClickListener(View.OnClickListener {
            minuteBtnsEvent(it)
            milliSeconds = 60_000
        })

        minute2Btn.setOnClickListener(View.OnClickListener {
            minuteBtnsEvent(it)
            milliSeconds = 120_000
        })

        minute5Btn.setOnClickListener(View.OnClickListener {
            minuteBtnsEvent(it)
            milliSeconds = 300_000
        })

        minuteCustomBtn.setOnClickListener(View.OnClickListener {
            minuteBtnsEvent(it, true)
        })

        minutesPckr.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                milliSeconds = minutesPckr.selectedItem.toString().toLong() * 60 * 1000
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        })

        submitBtn.setOnClickListener(View.OnClickListener {

            if (parEdtTxt.text.isEmpty() || parEdtTxt.text.trim()
                    .split(" ").size < 10
            ) { // make sure to enter at least 10 words
                Toast.makeText(
                    applicationContext,
                    "Please enter at least 10 words.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val currentPar = parEdtTxt.text.toString().trim().split(" ") as MutableList<String>
                wordsList = currentPar
                wordsListReserve = currentPar

                totalWords = parEdtTxt.text.split(" ").size

                parEdtTxt.isEnabled = false
                submitBtn.isEnabled = false
                inputEdtTxt.isEnabled = true

                setButtonsEnabled(false)

                minutesLnrLyt.visibility = View.GONE
                submitBtn.visibility = View.GONE

                timer = object : CountDownTimer(milliSeconds, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val remainedSecs = millisUntilFinished / 1000
                        timeTxtV.setText("" + remainedSecs / 60 + ":" + remainedSecs % 60)
                    }

                    override fun onFinish() {
                        done()
                    }
                }
                timer.start()

                isTimerRunning = true
            }
        })

        inputEdtTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.endsWith(" ") == true) { // when user enters white space validate the word
                    val inputText =
                        inputEdtTxt.text.trim().toString() // contains the words entered as list

                    // only validate word if input is not empty string/white space
                    if (inputText.isNotEmpty()) {
                        if (inputText == wordsListReserve[0]) { // compare the word entered with the first word in the paragraph generated
                            currentScore++
                            scoreTxtV.setText("$currentScore/${totalWords}")
                        }

                        inputEdtTxt.setText("")
                        wordsListReserve.removeAt(0)
                        parEdtTxt.setText(wordsListReserve.joinToString(separator = " "))

                        // check if there are no remaining words
                        if (wordsListReserve.isNullOrEmpty()) {
                            timer.onFinish()
                            timer.cancel()
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // set by default the 1 min button
        minute1Btn.callOnClick()
    }

    private fun done() {
        /* when the user has finished or the timer has finished execute this function */
        Toast.makeText(
            applicationContext,
            "Accuracy: ${round((currentScore / (totalWords * 1.0)) * 100)}% Speed: $currentScore/$totalWords",
            Toast.LENGTH_LONG
        ).show()
        resetPage()
    }

    private fun resetPage() {
        /* reset everything when timer/user has finished */
        timeTxtV.setText("0:00")

        val words = mainActivityViewModel.generateWords()
        parEdtTxt.setText(words)
        parEdtTxt.isEnabled = true
        wordsList = words.split(" ") as MutableList<String>
        wordsListReserve = words.split(" ") as MutableList<String>

        minutesLnrLyt.visibility = View.VISIBLE
        submitBtn.visibility = View.VISIBLE
        submitBtn.isEnabled = true

        resetButtonsColors()
        setButtonsEnabled()
        // set by default the 1 min button
        minute1Btn.callOnClick()

        // clear text
        inputEdtTxt.setText("")
        inputEdtTxt.isEnabled = false

        scoreTxtV.setText("0")
        currentScore = 0
        isTimerRunning = false;
    }

    private fun minuteBtnsEvent(it: View?, showPicker: Boolean = false) {
        /* function that will be executed whenever one of the minutes buttons is clicked
        *  @param it: button clicked
        *  @param showPicker: boolean to know if the numbers picker should be hidden or not
        * */
        resetButtonsColors()
        it?.setBackgroundColor(resources.getColor(R.color.teal_700))

        minutesPckr.visibility = if(showPicker) View.VISIBLE else View.GONE
    }

    private fun resetButtonsColors() {
        /*  reset the background color of the minutes buttons */
        for(v: View in minutesBtnsLnrLyt.children) {
            v.setBackgroundColor(resources.getColor(R.color.purple_500))
        }
    }

    private fun setButtonsEnabled(enabled: Boolean = true) {
        /* enable/disable the buttons
        *  @param enabled: boolean used to enable or disable the buttons
        * */
        for(v: View in minutesBtnsLnrLyt.children) {
            v.isEnabled = enabled
        }

        if(!enabled) {
            resetButtonsColors()
            submitBtn.isEnabled = false
        }
    }
}