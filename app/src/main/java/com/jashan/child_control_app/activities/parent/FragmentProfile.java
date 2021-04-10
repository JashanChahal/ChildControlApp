package com.jashan.child_control_app.activities.parent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jashan.child_control_app.R;


import static android.content.Context.MODE_PRIVATE;

public class FragmentProfile extends Fragment {
    private SharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        pref = getActivity().getSharedPreferences("com.jashan.users",MODE_PRIVATE);
        setProfileOfUser();
    }

    private void setProfileOfUser() {
        TextView userNameTitle = getView().findViewById(R.id.username_title);
        TextView userName = getView().findViewById(R.id.profile_username);
        TextView email = getView().findViewById(R.id.profile_email);
        TextView children = getView().findViewById(R.id.registered_children);

        userNameTitle.setText(pref.getString("userName","userName"));

        userName.setText(pref.getString("userName","userName"));
        // Change Color
        userName.setTextColor(getActivity().getResources().getColor(R.color.black,getActivity().getTheme()));

        email.setText(pref.getString("userEmail","userName"));
        // Change Color
        email.setTextColor(getActivity().getResources().getColor(R.color.black,getActivity().getTheme()));;

        children.setText(pref.getString("children","None"));
        // Change Color
        children.setTextColor(getActivity().getResources().getColor(R.color.black,getActivity().getTheme()));;

    }


}
