package com.jashan.child_control_app.common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.StartUp;
import com.jashan.child_control_app.utils.ActivityTransition;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    List<TextInputLayout> parentFormElements;
    List<TextInputLayout> childFormElements;
    RadioButton radioParent;
    RadioButton radiochild;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setParentFormElements();
        setChildFormElements();

        radioParent = (RadioButton) findViewById(R.id.radioparent);
        radiochild = (RadioButton) findViewById(R.id.radiochild);
        radioGroup = (RadioGroup) findViewById(R.id.radioparentchild);

    }

    // Form elements that will appear when a parent fills the form
    private void setParentFormElements() {
        parentFormElements = new ArrayList<>();
        TextInputLayout password = findViewById(R.id.parent_register_password);
        TextInputLayout confirmPassword = findViewById(R.id.parent_register_confirmpassword);

        parentFormElements.add(password);
        parentFormElements.add(confirmPassword);
    }

    // Form element that will appear when form is filled on child's phone
    private void setChildFormElements() {
        childFormElements = new ArrayList<>();
        TextInputLayout parentUsername = findViewById(R.id.parent_username);

        childFormElements.add(parentUsername);
    }

    public void goToStartUp(View view) {
        ActivityTransition.goToActivity(this, StartUp.class);
    }

    public void goToLogin(View view) {
        ActivityTransition.goToActivity(this, Login.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.enter, R.transition.exit);
    }

    public void renderForm(View view) {
        if (isSelected(radioParent)) {
            setFormElementsVisibility(parentFormElements, View.VISIBLE);
            setFormElementsVisibility(childFormElements, View.GONE);

        } else if (isSelected(radiochild)) {
            setFormElementsVisibility(parentFormElements, View.GONE);
            setFormElementsVisibility(childFormElements, View.VISIBLE);

        }
    }

    private boolean isSelected(RadioButton radioButton) {
        RadioButton selectedRadioButton = getSelectedRadioButton(radioGroup);
        return selectedRadioButton == radioButton;
    }

    private void setFormElementsVisibility(List<TextInputLayout> formElements, int visibility) {
        for (TextInputLayout formElement : formElements) {
            formElement.setVisibility(visibility);
        }
    }


    private RadioButton getSelectedRadioButton(RadioGroup radioGroup) {
        return (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
    }
}