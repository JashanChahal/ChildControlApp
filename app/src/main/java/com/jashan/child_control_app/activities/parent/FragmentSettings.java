package com.jashan.child_control_app.activities.parent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jashan.child_control_app.R;
import com.jashan.child_control_app.repository.NotificationService;
import com.jashan.child_control_app.utils.Configuration;

public class FragmentSettings extends Fragment {

    private Button notificationButton;
    private NotificationService notificationService;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings,container,false);

        notificationButton = view.findViewById(R.id.notification);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendnoti(view);
            }
        });

        notificationService = Configuration.getNotificationService();

        return view;
    }

    public void sendnoti(View view) {

        Log.d("first func","inside sendNoti");

        notificationService.send("SS",getActivity().getApplicationContext());
    }
}
