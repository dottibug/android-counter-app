package com.example.counter

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var numberText : TextView;
    private lateinit var numberEdit : EditText;
    private lateinit var interval : EditText;
    private lateinit var summary : TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Button references
        val buttonSubmit : Button = findViewById(R.id.mainButtonSubmit);
        val buttonRandomNumber : Button = findViewById(R.id.mainButtonRandomNumber);
        val buttonIncrease : Button = findViewById(R.id.mainButtonInc);
        val buttonDecrease: Button = findViewById(R.id.mainButtonDec);

        // Define EditText and TextView references
        numberText = findViewById(R.id.mainTextViewNumber);
        numberEdit = findViewById(R.id.mainEditTextNumberInput);
        interval = findViewById(R.id.mainEditTextInterval);
        summary = findViewById(R.id.mainTextViewSummary);

        // Get and display number inputted by user
        buttonSubmit.setOnClickListener {
            val number = getNumberInput();
            resetViews();
            hideKeyboard();
            displayNumber(number);
        }

        // Get and display a random number, then clear numberEdit
        buttonRandomNumber.setOnClickListener {
            val number = getRandomNumber();
            resetViews();
            displayNumber(number);
        }

        // Increase the current number by an interval
        buttonIncrease.setOnClickListener {
            val increment = getIntervalNumber();
            updateNumber("+", increment);
            hideKeyboard();
        }

        // Decrease the current number by an interval
        buttonDecrease.setOnClickListener {
            val decrement = getIntervalNumber();
            updateNumber("-", decrement);
            hideKeyboard();
        }

        // Check for savedInstanceState
        if (savedInstanceState != null) {
            numberText.text = savedInstanceState.getString("numberText")
            numberEdit.setText(savedInstanceState.getString("numberEdit"))
            interval.setText(savedInstanceState.getString("interval"))
        }
    }

    // Override onSaveInstanceState to save current data
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("numberText", numberText.text.toString())
        outState.putString("numberEdit", numberEdit.text.toString())
        outState.putString("interval", interval.text.toString())
    }

    // Add onSaveInstanceState override to persist data on screen rotations

    // Get number from input
    private fun getNumberInput() : String {
        if (numberEdit.text.toString().isEmpty()) {
            // Show a toast message if the number input is empty
            Toast.makeText(baseContext, "Enter a number", Toast.LENGTH_SHORT).show();
            // Return the current value of numberText
            return numberText.text.toString()
        } else {
            // Return the inputted number
            return numberEdit.text.toString();
        }
    }

    private fun resetViews() {
        clearNumberEdit();
        clearInterval();
        summary.text = resources.getString(R.string.placeholderSummary);
    }

    // Hide keyboard
    private fun hideKeyboard() {
        // Get service from context and cast as InputMethodManager type
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
        inputManager.hideSoftInputFromWindow(numberText.windowToken, 0);
    }

    // Display number in the numberText view
    private fun displayNumber(number : String) {
        numberText.text = number;
    }

    // Clear numberEdit
    private fun clearNumberEdit() {
        numberEdit.setText("");
    }

    // Clear interval EditText view
    private fun clearInterval() {
        interval.setText("");
    }

    // Get random number from -100 to 100 as a string
    private fun getRandomNumber() : String {
        return Random.nextInt(-100, 100).toString();
    }

    // Get the interval number
    private fun getIntervalNumber() : Int {
        return if (interval.text.toString().isEmpty()) {
            0
        } else {
            interval.text.toString().toInt();
        }
    }

    // Update number (calculates new number plus or minus the set interval)
    private fun updateNumber(type : String, intervalAmount : Int) {
        val currentNumber : Int = getCurrentNumber();
        val newNumber : Int = if (type == "+") currentNumber + intervalAmount else currentNumber - intervalAmount;
        displayNumber(newNumber.toString());
        updateSummary(currentNumber, type, intervalAmount, newNumber);
    }

    // Get current number
    private fun getCurrentNumber() : Int {
        return numberText.text.toString().toInt();
    }

    // Update summary
    private fun updateSummary(currentNumber: Int, type: String, intervalAmount: Int, newNumber: Int) {
        val curNum = currentNumber.toString();
        val intAmt = intervalAmount.toString();
        val newNum = newNumber.toString();
        summary.text = String.format(resources.getString(R.string.summary), curNum, type, intAmt, newNum);
    }
}