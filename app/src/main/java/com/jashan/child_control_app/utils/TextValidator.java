package com.jashan.child_control_app.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public abstract class TextValidator implements TextWatcher {
    private final TextView textView;
    private final TextInputLayout textInputLayout;
    public TextValidator(TextView textView, TextInputLayout textInputLayout) {
        this.textView = textView;
        this.textInputLayout = textInputLayout;
    }

    public abstract void validate(TextInputLayout textInputLayout , String text );

    @Override
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        validate(textInputLayout, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}