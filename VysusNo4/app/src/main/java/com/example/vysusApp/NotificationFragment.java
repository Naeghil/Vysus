package com.example.vysusApp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
    NotificationFragment - the lacking connection to the backend has essentially left this page useless and makes
    it only load the fragment and that's it

    Authors -       Maximilian Lietz
*/

public class NotificationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.notification_fragment_layout,container, false);
    }

}
