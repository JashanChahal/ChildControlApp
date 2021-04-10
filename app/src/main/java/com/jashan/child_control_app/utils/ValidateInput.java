package com.jashan.child_control_app.utils;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateInput {
    public static void addEmailListener(TextInputLayout emailTextLayout) {
        String emailRegex = "^(.+)@(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        emailTextLayout.getEditText().addTextChangedListener(new TextValidator(emailTextLayout.getEditText(), emailTextLayout) {
            @Override
            public void validate(TextInputLayout layout, String text) {
                Matcher matcher = emailPattern.matcher(text);
                if (matcher.matches()) {
                    layout.setErrorEnabled(false);
                } else {
                    layout.setError("Type a valid email");
                }
            }
        });
    }

    public static void addValidateListener(TextInputLayout userNameTextLayout, int minimumCharacter, String nameOfLayout) {
        userNameTextLayout.getEditText().addTextChangedListener(new TextValidator(userNameTextLayout.getEditText(), userNameTextLayout) {
            @Override
            public void validate(TextInputLayout layout, String text) {
                if (text == null || text.length() < minimumCharacter) {
                    layout.setError(nameOfLayout + " should be atleast " + minimumCharacter + " character long");
                } else {
                    layout.setErrorEnabled(false);
                }
            }
        });
    }


    public static void addMatchPasswordListener(TextInputLayout confirmPasswordTextLayout, TextInputLayout password) {
        confirmPasswordTextLayout.getEditText().addTextChangedListener(new TextValidator(confirmPasswordTextLayout.getEditText(), confirmPasswordTextLayout) {
            @Override
            public void validate(TextInputLayout layout, String text) {
                String match = password.getEditText().getText().toString();
                if (text == null || !text.equals(match)) {
                    layout.setError("Password does not match");
                } else {
                    layout.setErrorEnabled(false);
                }
            }
        });
    }


}
