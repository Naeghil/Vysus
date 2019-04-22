package com.example.vysusApp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/*
    profilePageFragment - the lacking connection to the backend has essentially left this page useless and makes
    it only load the fragment, although it is possible to upload a picture (it's not being saved though)

    Authors -       Maximilian Lietz
*/

public class profilePageFragment extends Fragment {

    View view;
    Activity homeActivity = getActivity();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        return inflater.inflate(R.layout.profile_page_layout,container, false);
        //Button buttonPicChange = (Button) view.findViewById(R.id.button5);
        // buttonPicChange.setOnClickListener(this);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

    public void changeImage(Bitmap bitmap) {
        ImageView imageView = (ImageView) getView().findViewById(R.id.imageView3);
        imageView.setImageBitmap(bitmap);
    }


}
