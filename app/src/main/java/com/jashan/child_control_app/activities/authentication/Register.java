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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.activities.StartUp;
import com.jashan.child_control_app.model.Child;
import com.jashan.child_control_app.model.Parent;
import com.jashan.child_control_app.model.User;
import com.jashan.child_control_app.activities.parent.ParentProfile;
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
    private SharedPreferences pref;

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
        pref = getSharedPreferences("com.jashan.users",MODE_PRIVATE);

    }

    private void validateListeners() {
        addEmailListener(formElements.get("EMAIL"));
        addEmailListener(formElements.get("PARENT_EMAIL"));
        addValidateListener(formElements.get("PASSWORD"), 5, "Password");
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


        SharedPreferences.Editor editor = pref.edit();

        editor.putString("userName",inputData.getUserName());
        editor.putString("password",inputData.getPassword());
        editor.putString("userEmail",inputData.getEmail());

        if (inputData.isParent()) {
            registerUserAndRedirect(inputData.getEmail(), inputData.getPassword(), inputData.getUserName());
            editor.putString("type","parent");
        } else {
            registerChild(inputData.getEmail(), inputData.getPassword(), inputData.getUserName(), inputData.getParentEmail());
            editor.putString("type","child");
        }
        editor.putBoolean("loggedin",true);
        editor.apply();
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
                            writeNewUser(user.getUid(), new Parent(userName, email, user.getUid()));
                            Intent intent = new Intent(Register.this, ParentProfile.class);
                            intent.putExtra("UID", user.getUid());
                            startActivity(intent);
                        } else {
                            Toast.makeText(Register.this, "failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    public void writeNewUser(String userId, User user) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userId).setValue(user);
    }

    private void registerChild(String childEmail, String childPassword, String childName, String parentEmail) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(childEmail, childPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            User child = new Child(childName, childEmail, parentEmail);
                            writeNewUser(user.getUid(), child);
                            addChildToParent((Child) child);
                        } else {
                            Toast.makeText(Register.this, "failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    private void addChildToParent(Child child) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase
                .orderByChild("userEmail")
                .equalTo(child.getParentEmail().toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Parent parent = snapshot.getChildren().iterator().next().getValue(Parent.class);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(child);
                    Map<String, Object> update = new HashMap<>();
                    update.put("/users/" + parent.getUserId(), parent);
                    FirebaseDatabase.getInstance().getReference().updateChildren(update);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("testing", "failed");
            }
        });
    }

    private void setFormElements() {
        formElements = new HashMap<>();
        TextInputLayout emailTextLayout = (TextInputLayout) findViewById(R.id.email);
        TextInputLayout passwordTextLayout = (TextInputLayout) findViewById(R.id.parent_register_password);
        TextInputLayout confirmPasswordTextLayout = (TextInputLayout) findViewById(R.id.parent_register_confirmpassword);
        TextInputLayout userNameTextLayout = (TextInputLayout) findViewById(R.id.username);
        TextInputLayout parentEmail = findViewById(R.id.parent_email);

        formElements.put("EMAIL", emailTextLayout);
        formElements.put("PASSWORD", passwordTextLayout);
        formElements.put("CONFIRM_PASSWORD", confirmPasswordTextLayout);
        formElements.put("USER_NAME", userNameTextLayout);
        formElements.put("PARENT_EMAIL", parentEmail);
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
        private boolean isParent;
        private TextInputLayout emailTextLayout;
        private TextInputLayout passwordTextLayout;
        private TextInputLayout confirmPasswordTextLayout;
        private TextInputLayout userNameTextLayout;
        private TextInputLayout parentEmailTextLayout;

        public InputData(boolean isParent) {
            this.isParent = isParent;
            this.emailTextLayout = (TextInputLayout) findViewById(R.id.email);
            this.passwordTextLayout = (TextInputLayout) findViewById(R.id.parent_register_password);
            this.confirmPasswordTextLayout = (TextInputLayout) findViewById(R.id.parent_register_confirmpassword);
            this.userNameTextLayout = (TextInputLayout) findViewById(R.id.username);
            this.parentEmailTextLayout = (TextInputLayout) findViewById(R.id.parent_email);

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
            return userNameTextLayout.getEditText().getText().toString();
        }

        public String getEmail() {
            return emailTextLayout.getEditText().getText().toString();
        }

        public String getPassword() {
            return passwordTextLayout.getEditText().getText().toString();

        }

        public String getConfirmPassword() {
            return confirmPasswordTextLayout.getEditText().getText().toString();

        }

        public String getParentEmail() {
            return isChild() ? parentEmailTextLayout.getEditText().getText().toString() : null;
        }


    }
}