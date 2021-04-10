package com.jashan.child_control_app.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jashan.child_control_app.R;
import com.jashan.child_control_app.activities.authentication.Login;
import com.jashan.child_control_app.activities.child.ChildHomepage;
import com.jashan.child_control_app.activities.parent.ParentHomepage;
import com.jashan.child_control_app.model.Child;
import com.jashan.child_control_app.model.Parent;
import com.jashan.child_control_app.model.User;
import com.jashan.child_control_app.repository.AfterCompletion;
import com.jashan.child_control_app.repository.FirebaseWebService;
import com.jashan.child_control_app.repository.WebService;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ActivityTransition {
    private static WebService webService;

    public static void goToActivity(Activity context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
        context.overridePendingTransition(R.transition.enter, R.transition.exit);
    }

    public static void goBack(Activity context, Class activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
        context.overridePendingTransition(R.transition.enter_back, R.transition.back);
    }

    public static void fetchUserDetailsAndGoToHomePage(Activity context, ProgressBar progressBar, SharedPreferences pref) {
        webService = new FirebaseWebService();
        progressBar.setVisibility(View.VISIBLE);
        webService.getCurrentUserAndDo(new AfterCompletion<User>() {
            @Override
            public void onSuccess(User user) {
                goToUserHomepage(context,user,pref,progressBar);
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(context, "No User found!", Toast.LENGTH_LONG);
            }
        });


    }
    public static void goToUserHomepage(Activity context, User user, SharedPreferences pref, ProgressBar progressBar) {
        setSharedPreferences(user, pref);
        goToHomepage(context, user);
        progressBar.setVisibility(View.GONE);
    }
    private static void setSharedPreferences(User user, SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userName", user.getUserName());
        editor.putString("userEmail", user.getUserEmail());
        editor.putString("type", user.getType());
        if (user.getType().equals("Parent")) {
            List<Child> children = ((Parent) user).getChildren();
            String childrens = getChildrenString(children);

            editor.putString("children",childrens );
        }
        editor.apply();
    }
    private static String getChildrenString(List<Child> children) {
        StringBuffer strBuffer = new StringBuffer("");
        if (children != null) {
            for (Child child : children) {
                strBuffer.append(child.toString());
                strBuffer.append(", ");
            }
            strBuffer.deleteCharAt(strBuffer.length()-2);
        }
        return strBuffer.toString();
    }
    private static void goToHomepage(Activity activity, User user) {
        Class goTo = null;
        if (user.getType().equals("Parent")) {
            goTo = ParentHomepage.class;
        } else {
            goTo = ChildHomepage.class;
        }
        Intent intent = new Intent(activity, goTo);
        activity.startActivity(intent);
        activity.finish();

    }
}
