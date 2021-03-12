package com.jashan.child_control_app.activities.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.activities.StartUp;
import com.jashan.child_control_app.activities.child.ChildHomepage;
import com.jashan.child_control_app.activities.parent.ParentHomepage;
import com.jashan.child_control_app.model.Child;
import com.jashan.child_control_app.model.Parent;
import com.jashan.child_control_app.model.User;
import com.jashan.child_control_app.repository.AfterCompletion;
import com.jashan.child_control_app.repository.WebService;
import com.jashan.child_control_app.utils.ActivityTransition;
import com.jashan.child_control_app.utils.Configuration;
import com.jashan.child_control_app.utils.ValidateInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Register extends AppCompatActivity {
    private List<TextInputLayout> parentFormElements;
    private List<TextInputLayout> childFormElements;
    private InputData inputData;
    private RadioButton radioParent;
    private RadioButton radiochild;
    private RadioGroup radioGroup;
    public DatabaseReference mDatabase;
    private SharedPreferences pref;
    private WebService webService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setFormElements();
        setParentFormElements();
        setChildFormElements();

        radioParent = findViewById(R.id.radioparent);
        radiochild = findViewById(R.id.radiochild);
        radioGroup = findViewById(R.id.radioparentchild);

        validateListeners();

        pref = getSharedPreferences(Configuration.getSharedPreferenceFileLocation(), MODE_PRIVATE);
        webService = Configuration.getWebservice();
    }

    private void validateListeners() {
        ValidateInput.addEmailListener(inputData.getEmailTextLayout());
        ValidateInput.addEmailListener(inputData.getParentEmailTextLayout());
        ValidateInput.addValidateListener(inputData.getUserNameTextLayout(),3,"Username");
        ValidateInput.addValidateListener(inputData.getPasswordTextLayout(), 5, "Password");
        ValidateInput.addMatchPasswordListener(inputData.getConfirmPasswordTextLayout(), inputData.getPasswordTextLayout());
    }


    public void registerUser(View view) {

        if (!isSelected(radiochild) && !isSelected(radioParent)) {
            radiochild.setError("Select an Item");
            return;
        }

        InputData inputData = isSelected(radioParent) ? new InputData(true)
                                                      : new InputData(false);
        if (!inputData.verifyInputData()) {
            Log.v("VERIFIED", "Data not verified");
            return;
        }

        SharedPreferences.Editor editor = pref.edit();
        editor.clear();

        editor.putString("userName", inputData.getUserName());
        editor.putString("password", inputData.getPassword());
        editor.putString("userEmail", inputData.getEmail());

        if (inputData.isParent()) {
            registerParentAndRedirect(new Parent(inputData.getUserName(), inputData.getEmail()), inputData.getPassword());
            editor.putString("type", "parent");
        } else {
            registerChildAndRedirect(new Child(inputData.getUserName(),inputData.getEmail(),inputData.getParentEmail()), inputData.getPassword());
            editor.putString("type", "child");
        }

        editor.apply();
    }

    private void registerParentAndRedirect(Parent parent, String password) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        webService.createUser(parent, password)
                .addAfterCompletion(new AfterCompletion<User>() {
                    @Override
                    public void onSuccess(User user) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(Register.this, ParentHomepage.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("Exception", e.toString());
                        Toast.makeText(Register.this, "Unable to register user", Toast.LENGTH_LONG).show();
                    }
                });


    }


    private void registerChildAndRedirect(Child child, String childPassword) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        webService.createUser(child, childPassword)
                .addAfterCompletion(new AfterCompletion<User>() {
                    @Override
                    public void onSuccess(User user) {
                        addChildToParent((Child) user);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.v("Exception",e.toString());
                        Toast.makeText(Register.this,"User is not registered",Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void addChildToParent(Child child) {

        webService.queryByKeyValue("userEmail",child.getParentEmail()) ;

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase
                .orderByChild("userEmail")
                .equalTo(child.getParentEmail().toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.v("obj",snapshot.getChildren().iterator().next().toString());

                Parent parent = snapshot.getChildren().iterator().next().getValue(Parent.class);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(child);
                    Map<String, Object> update = new HashMap<>();
                    update.put("/users/" + parent.getUserId(), parent);
                    FirebaseDatabase.getInstance().getReference().updateChildren(update);

                    Intent intent = new Intent(Register.this, ChildHomepage.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("testing", "failed");
            }
        });
    }

    private void setFormElements() {
        inputData = new InputData();
    }

    // Form elements that will appear when a parent fills the form
    private void setParentFormElements() {
        parentFormElements = new ArrayList<>();

    }

    // Form element that will appear when form is filled on child's phone
    private void setChildFormElements() {
        childFormElements = new ArrayList<>();
        TextInputLayout parentEmail = findViewById(R.id.parent_email);
        childFormElements.add(parentEmail);
    }

    public void goBackToStartUp(View view) {
        ActivityTransition.goBack(this, StartUp.class);
    }

    public void goToLogin(View view) {
        ActivityTransition.goToActivity(this, Login.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.enter_back, R.transition.back);
    }

    public void renderForm(View view) {
        radiochild.setError(null);
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

    private class InputData {
        private Boolean isParent;
        private final TextInputLayout emailTextLayout;
        private final TextInputLayout passwordTextLayout;
        private final  TextInputLayout confirmPasswordTextLayout;
        private final TextInputLayout userNameTextLayout;
        private final TextInputLayout parentEmailTextLayout;

        public InputData(Boolean isParent) {
            this.isParent = isParent;
            this.emailTextLayout = findViewById(R.id.email);
            this.passwordTextLayout = findViewById(R.id.parent_register_password);
            this.confirmPasswordTextLayout = findViewById(R.id.parent_register_confirmpassword);
            this.userNameTextLayout =  findViewById(R.id.username);
            this.parentEmailTextLayout = findViewById(R.id.parent_email);

        }
        public InputData() {
            this(null);
        }

        public TextInputLayout getEmailTextLayout() {
            return emailTextLayout;
        }

        public TextInputLayout getPasswordTextLayout() {
            return passwordTextLayout;
        }

        public TextInputLayout getConfirmPasswordTextLayout() {
            return confirmPasswordTextLayout;
        }

        public TextInputLayout getUserNameTextLayout() {
            return userNameTextLayout;
        }

        public TextInputLayout getParentEmailTextLayout() {
            return parentEmailTextLayout;
        }

        public void setParent(Boolean parent) {
            isParent = parent;
        }

        public boolean verifyInputData() {
            boolean isVerified = true;

            if (emailTextLayout.isErrorEnabled()
                    || (passwordTextLayout.isErrorEnabled())
                    || (confirmPasswordTextLayout.isErrorEnabled())
                    || userNameTextLayout.isErrorEnabled()
                    || (isChild() && parentEmailTextLayout.isErrorEnabled()
            )
            ) {
                isVerified = false;
            }

            return isVerified;
        }


        boolean isChild() {
            return !isParent;
        }

        boolean isParent() {
            return this.isParent;
        }

        public String getUserName() {
            return Objects.requireNonNull(userNameTextLayout.getEditText()).getText().toString();
        }

        public String getEmail() {
            return Objects.requireNonNull(emailTextLayout.getEditText()).getText().toString();
        }

        public String getPassword() {
            return Objects.requireNonNull(passwordTextLayout.getEditText()).getText().toString();

        }

        public String getParentEmail() {
            return isChild() ? Objects.requireNonNull(parentEmailTextLayout.getEditText()).getText().toString() : "";
        }


    }
}