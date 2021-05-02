package com.jashan.child_control_app.activities.parent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jashan.child_control_app.R;
import com.jashan.child_control_app.model.Child;
import com.jashan.child_control_app.model.Parent;
import com.jashan.child_control_app.model.User;
import com.jashan.child_control_app.repository.AfterCompletion;
import com.jashan.child_control_app.repository.WebService;
import com.jashan.child_control_app.utils.Configuration;

import java.util.List;


public class FragmentDashboard extends Fragment {
    private LinearLayout linearLayout;
    private WebService webService;
    private ProgressBar progressBar;
    private ScrollView statsView;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initialiseInstanceVariable(view);

        renderChildrenCards(false);

        setOnRefresh();

        return view;
    }

    private void initialiseInstanceVariable(View view) {
        linearLayout = view.findViewById(R.id.layout_list);

        webService = Configuration.getWebservice();

        statsView = view.findViewById(R.id.stats_view);

        refreshLayout = view.findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeColors(Color.CYAN);

        progressBar = view.findViewById(R.id.dashboard_progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }


    private void renderChildrenCards(boolean isRefresh) {
        webService.getCurrentUserAndDo(new AfterCompletion<User>() {
            @Override
            public void onSuccess(User user) {

                addCardsToLinearLayout(((Parent) user).getChildren());

                // Change visibility on success
                progressBar.setVisibility(View.GONE);
                statsView.setVisibility(View.VISIBLE);
                if (isRefresh) {
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FragmentDashboard", "No user found error");
                renderChildrenCards(true);
            }
        });
    }

    private void setOnRefresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                renderChildrenCards(true);
            }
        });
    }

    private void addCardsToLinearLayout(List<Child> children) {
        linearLayout.removeAllViews();
        for (Child child : children) {
            View card = getLayoutInflater().inflate(R.layout.card_child, null, false);
            TextView cardTitle = card.findViewById(R.id.card_title);
            TextView cardEmail = card.findViewById(R.id.card_email);

            AppCompatButton callLogs = card.findViewById(R.id.call_logs_btn);

            callLogs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),CallLogsActivity.class);
                    intent.putExtra("ChildInformation",child.getChildInformation());
                    startActivity(intent);
                }
            });
            cardEmail.setText(child.getUserEmail());
            cardTitle.setText(child.getUserName());
            linearLayout.addView(card);
        }
    }
}
