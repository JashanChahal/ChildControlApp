package com.jashan.child_control_app.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.StartUp;
import com.jashan.child_control_app.modal.Child;
import com.jashan.child_control_app.modal.Parent;
import com.jashan.child_control_app.modal.User;
import com.jashan.child_control_app.parent.ParentHomepage;
import com.jashan.child_control_app.utils.ActivityTransition;
import com.jashan.child_control_app.utils.TextValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private List<TextInputLayout> parentFormElements;
    private List<TextInputLayout> childFormElements;
    private Map<String, TextInputLayout> formElements;
    private RadioButton radioParent;
    private RadioButton radiochild;
    private RadioGroup radioGroup;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setFormElements();
        setParentFormElements();
        setChildFormElements();

        radioParent = (RadioButton) findViewById(R.id.radioparent);
        radiochild = (RadioButton) findViewById(R.id.radiochild);
        radioGroup = (RadioGroup) findViewById(R.id.radioparentchild);

        validateListeners();

        mAuth = FirebaseAuth.getInstance();

    }

    private void validateListeners() {
        addEmailListener(formElements.get("EMAIL"));
        addEmailListener(formElements.get("PARENT_EMAIL"));
        addValidateListener(formElements.get("PASSWORD"), 5, "Password");
        addValidateListener(formElements.get("PARENT_PASSWORD"), 5, "Password");
        addMatchPasswordListener(formElements.get("CONFIRM_PASSWORD"));
    }

    public void addEmailListener(TextInputLayout emailTextLayout) {
        String emailRegex = "^(.+)@(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        emailTextLayout.getEditText().addTextChangedListener(new TextValidator(emailTextLayout.getEditText(), emailTextLayout) {
            @Override
            public void validate(TextInputLayout layout, String text) {
                Matcher matcher = emailPattern.matcher(text);
                if (matcher.matches()) {
                    layout.setError(null);
                } else {
                    layout.setErrorEnabled(false);
                }
            }
        });
    }

    public void addValidateListener(TextInputLayout userNameTextLayout, int minimumCharacter, String nameOfLayout) {
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

    public void addMatchPasswordListener(TextInputLayout confirmPasswordTextLayout) {
        confirmPasswordTextLayout.getEditText().addTextChangedListener(new TextValidator(confirmPasswordTextLayout.getEditText(), confirmPasswordTextLayout) {
            @Override
            public void validate(TextInputLayout layout, String text) {
                String match = formElements.get("PASSWORD").getEditText().getText().toString();
                if (text == null || !text.equals(match)) {
                    layout.setError("Password donot match");
                } else {
                    layout.setErrorEnabled(false);
                }
            }
        });
    }

    public void registerUser(View view) {

        if (!isSelected(radiochild) && !isSelected(radioParent)) {
            radiochild.setError("Select an Item");
            return;
        }

        InputData inputData;

        if (isSelected(radioParent)) {
            inputData = new InputData(true);

        } else {
            inputData = new InputData(false);

        }

        if (!inputData.verifyInputData()) {
            Log.v("VERIFIED", "Data not verified");
            return;
        }

        if (inputData.isParent()) {
            registerUserAndRedirect(inputData.getEmail(), inputData.getPassword(), inputData.getUserName());
        } else {
            registerChild(inputData.getParentEmail(), inputData.getParentPassword(), inputData.getUserName());
        }

    }

    private void registerUserAndRedirect(String email, String password, String userName) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUser(user.getUid(), userName, email);
                            Intent intent = new Intent(Register.this, ParentHomepage.class);
                            intent.putExtra("USER_ID", user.getUid());
                            startActivity(intent);
                        } else {
                            Toast.makeText(Register.this, "failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    public void writeNewUser(String userId, String name, String email) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        User user = new Parent(name, email);
        mDatabase.child("users").child(userId).setValue(user);
    }

    private void registerChild(String parentEmail, String parentPassword, String childName) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(parentEmail, parentPassword).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            addChildToParent(user.getUid(), childName, parentEmail);
                        } else {
                            Toast.makeText(Register.this, "Email or Password is wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void addChildToParent(String uid, String childName, String parentEmail) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> update = new HashMap<>();
        update.put("/users/" + uid + "/children/" + childName + "/parent", uid);
        mDatabase.updateChildren(update);

    }

    private void setFormElements() {
        formElements = new HashMap<>();
        TextInputLayout emailTextLayout = (TextInputLayout) findViewById(R.id.email);
        TextInputLayout passwordTextLayout = (TextInputLayout) findViewById(R.id.parent_register_password);
        TextInputLayout confirmPasswordTextLayout = (TextInputLayout) findViewById(R.id.parent_register_confirmpassword);
        TextInputLayout userNameTextLayout = (TextInputLayout) findViewById(R.id.username);
        TextInputLayout parentEmail = findViewById(R.id.parent_email);
        TextInputLayout parentPassword = findViewById(R.id.child_register_password);

        formElements.put("EMAIL", emailTextLayout);
        formElements.put("PASSWORD", passwordTextLayout);
        formElements.put("CONFIRM_PASSWORD", confirmPasswordTextLayout);
        formElements.put("USER_NAME", userNameTextLayout);
        formElements.put("PARENT_EMAIL", parentEmail);
        formElements.put("PARENT_PASSWORD", parentPassword);
    }

    // Form elements that will appear when a parent fills the form
    private void setParentFormElements() {
        parentFormElements = new ArrayList<>();
        TextInputLayout password = findViewById(R.id.parent_register_password);
        TextInputLayout confirmPassword = findViewById(R.id.parent_register_confirmpassword);
        TextInputLayout email = findViewById(R.id.email);
        parentFormElements.add(password);
        parentFormElements.add(confirmPassword);
        parentFormElements.add(email);
    }

    // Form element that will appear when form is filled on child's phone
    private void setChildFormElements() {
        childFormElements = new ArrayList<>();
        TextInputLayout parentEmail = findViewById(R.id.parent_email);
        TextInputLayout parentPassword = findViewById(R.id.child_register_password);

        childFormElements.add(parentEmail);
        childFormElements.add(parentPassword);
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
        private boolean isParent;
        private TextInputLayout emailTextLayout;
        private TextInputLayout passwordTextLayout;
        private TextInputLayout confirmPasswordTextLayout;
        private TextInputLayout userNameTextLayout;
        private TextInputLayout parentEmailTextLayout;
        private TextInputLayout parentPasswordTextLayout;

        public InputData(boolean isParent) {
            this.isParent = isParent;
            this.emailTextLayout = (TextInputLayout) findViewById(R.id.email);
            this.passwordTextLayout = (TextInputLayout) findViewById(R.id.parent_register_password);
            this.confirmPasswordTextLayout = (TextInputLayout) findViewById(R.id.parent_register_confirmpassword);
            this.userNameTextLayout = (TextInputLayout) findViewById(R.id.username);
            this.parentEmailTextLayout = (TextInputLayout) findViewById(R.id.parent_email);
            this.parentPasswordTextLayout = findViewById(R.id.child_register_password);

        }

        public boolean verifyInputData() {
            boolean isVerified = true;

            if (emailTextLayout.isErrorEnabled()
                    || (isParent() && passwordTextLayout.isErrorEnabled())
                    || (isParent() && confirmPasswordTextLayout.isErrorEnabled())
                    || userNameTextLayout.isErrorEnabled()
                    || (isChild() && parentEmailTextLayout.isErrorEnabled()
                    || (isChild() && parentPasswordTextLayout.isErrorEnabled())
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
            return userNameTextLayout.getEditText().getText().toString();
        }

        public String getEmail() {
            return emailTextLayout.getEditText().getText().toString();
        }

        public String getPassword() {
            return isParent() ? passwordTextLayout.getEditText().getText().toString() : null;

        }

        public String getConfirmPassword() {
            return isChild() ? confirmPasswordTextLayout.getEditText().getText().toString() : null;

        }

        public String getParentEmail() {
            return isChild() ? parentEmailTextLayout.getEditText().getText().toString() : null;
        }

        public String getParentPassword() {
            return isChild() ? parentPasswordTextLayout.getEditText().getText().toString() : null;
        }

    }
}