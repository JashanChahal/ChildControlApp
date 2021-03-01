package com.jashan.child_control_app.activities.parent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.jashan.child_control_app.R;
import com.jashan.child_control_app.activities.StartUp;
import com.jashan.child_control_app.model.Parent;
import com.jashan.child_control_app.model.User;
import com.jashan.child_control_app.repository.AfterSuccess;
import com.jashan.child_control_app.repository.FirebaseWebService;
import com.jashan.child_control_app.repository.WebService;

public class FragmentProfile extends Fragment {
    WebService webService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        setListenerOnLogoutButton(view);

        return view;
    }

    private void setListenerOnLogoutButton(View view) {
        AppCompatButton button = view.findViewById(R.id.logout_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), StartUp.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        webService = new FirebaseWebService();
        setProfileOfUser(webService);
    }

    private void setProfileOfUser(WebService webService) {
        TextView userName = getView().findViewById(R.id.profile_username);
        TextView email = getView().findViewById(R.id.profile_email);
        TextView children = getView().findViewById(R.id.profile_children);

        ProgressBar progressBar = getView().findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);

        webService.getCurrentUserAndDo(new AfterSuccess<User>() {
            @Override
            public void doThis(User user) {
                progressBar.setVisibility(View.GONE);

                if (user != null) {
                    userName.setText(user.getUserName());
                    email.setText(user.getUserEmail());
                    if (user.getType().equals("Parent"))
                        children.setText(((Parent) user).getChildren().toString());
                    else {
                        children.setText("No");
                    }
                } else {
                    Log.e("FragmentProfile", "User is null");
                }
            }
        });
    }


}
