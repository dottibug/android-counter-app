package com.example.counter

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.counter.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // View binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get and display number inputted by user
        binding.mainButtonSubmit.setOnClickListener {
            val number = getNumberInput();
            resetViews();
            hideKeyboard();
            displayNumber(number);
        }

        // Get and display a random number, then clear numberEdit
        binding.mainButtonRandomNumber.setOnClickListener {
            val number = getRandomNumber();
            resetViews();
            displayNumber(number);
        }

        // Increase the current number by an interval
        binding.mainButtonInc.setOnClickListener {
            val increment = getIntervalNumber();
            updateNumber("+", increment);
            hideKeyboard();
        }

        // Decrease the current number by an interval
        binding.mainButtonDec.setOnClickListener {
            val decrement = getIntervalNumber();
            updateNumber("-", decrement);
            hideKeyboard();
        }

        // Check for savedInstanceState
        if (savedInstanceState != null) {
            binding.mainTextViewNumber.text = savedInstanceState.getString("numberText")
            binding.mainEditTextNumberInput.setText(savedInstanceState.getString("numberEdit"))
            binding.mainEditTextInterval.setText(savedInstanceState.getString("interval"))
        }
    }

    // Override onSaveInstanceState to save current data
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("numberText", binding.mainTextViewNumber.text.toString())
        outState.putString("numberEdit", binding.mainEditTextNumberInput.text.toString())
        outState.putString("interval", binding.mainEditTextInterval.text.toString())
    }

    // Get number from input
    private fun getNumberInput() : String {
        if (binding.mainEditTextNumberInput.text.toString().isEmpty()) {
            // Show a toast message if the number input is empty
            Toast.makeText(baseContext, "Enter a number", Toast.LENGTH_SHORT).show();
            // Return the current value of numberText
            return binding.mainTextViewNumber.text.toString()
        } else {
            // Return the inputted number
            return binding.mainEditTextNumberInput.text.toString();
        }
    }

    private fun resetViews() {
        clearNumberEdit();
        clearInterval();
        binding.mainTextViewSummary.text = resources.getString(R.string.placeholderSummary);
    }

    // Hide keyboard
    private fun hideKeyboard() {
        // Get service from context and cast as InputMethodManager type
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
        inputManager.hideSoftInputFromWindow(binding.mainTextViewNumber.windowToken, 0);
    }

    // Display number in the numberText view
    private fun displayNumber(number : String) {
        binding.mainTextViewNumber.text = number;
    }

    // Clear numberEdit
    private fun clearNumberEdit() {
        binding.mainEditTextNumberInput.setText("");
    }

    // Clear interval EditText view
    private fun clearInterval() {
        binding.mainEditTextInterval.setText("");
    }

    // Get random number from -100 to 100 as a string
    private fun getRandomNumber() : String {
        return Random.nextInt(-100, 100).toString();
    }

    // Get the interval number
    private fun getIntervalNumber() : Int {
        return if (binding.mainEditTextInterval.text.toString().isEmpty()) {
            0
        } else {
            binding.mainEditTextInterval.text.toString().toInt();
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
        return binding.mainTextViewNumber.text.toString().toInt();
    }

    // Update summary
    private fun updateSummary(currentNumber: Int, type: String, intervalAmount: Int, newNumber: Int) {
        val curNum = currentNumber.toString();
        val intAmt = intervalAmount.toString();
        val newNum = newNumber.toString();
        binding.mainTextViewSummary.text = String.format(resources.getString(R.string.summary), curNum, type, intAmt, newNum);
    }
}