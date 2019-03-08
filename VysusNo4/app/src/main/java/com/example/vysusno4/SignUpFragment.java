package com.example.vysusno4;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SignUpFragment extends Fragment implements View.OnClickListener {
    @Override
    public void onClick(View view) {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        return inflater.inflate(R.layout.sign_up_fragment,container, false);
        //Button buttonPicChange = (Button) view.findViewById(R.id.button5);
        // buttonPicChange.setOnClickListener(this);
    }

    public void signupDetails() {

    }
}
