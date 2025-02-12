package com.maran.androidalfa

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val textField = view.findViewById<TextInputLayout>(R.id.text_field)
        val editText = view.findViewById<EditText>(R.id.edit_text)
        val button = view.findViewById<Button>(R.id.button_enter)
        val output = view.findViewById<TextView>(R.id.output)
        var isCorrect = false
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (editText.text.isEmpty()) {
                    isCorrect = false
                    textField.error = getString(R.string.empty)
                } else if (editText.text[0].isLowerCase()) {
                    isCorrect = false
                    textField.error = getString(R.string.not_capital)
                } else if (!Regex("^[a-zA-Zа-яА-Я]+$").matches(editText.text)) {
                    isCorrect = false
                    textField.error = getString(R.string.not_letters)
                } else {
                    isCorrect = true
                    textField.error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        button.setOnClickListener {
            if (isCorrect) {
                output.apply {
                    alpha = 0f
                    visibility = View.VISIBLE
                    text = getString(R.string.hello) + " " + editText.text

                    animate()
                        .alpha(1f)
                        .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
                        .setListener(null)
                }
            }
        }

        return view
    }
}