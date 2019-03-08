package com.example.vysusno4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.Window;

import java.util.Random;

public class HomePage extends AppCompatActivity  {

    private TextView mTextMessage;
    FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            profilePageFragment ppFragmentObject = new profilePageFragment();

            switch (item.getItemId()) {
                case R.id.navigation_jobs:

                    // Begin the transaction
                    //FragmentTransaction jobTransaction = getSupportFragmentManager().beginTransaction();
                    if (ppFragmentObject.isVisible()) {
                        //ft.detach(ppFragmentObject);
                    }
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment, new SignUpFragment());

                case R.id.navigation_profilePage:
                    // Begin the transaction
                    // Replace the contents of the container with the new fragment
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment, ppFragmentObject);
                    // or ft.add(R.id.your_placeholder, new FooFragment());
                    // Complete the changes added above
                    //ft.commit();
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!this.isInterrupted()) {
                        Thread.sleep(10);
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                Random rand = new Random();

                                int r = rand.nextInt();
                                int g = rand.nextInt();
                                int b = rand.nextInt();

                                int hey;
                                hey = Color.rgb(r,g,b);

                                findViewById(R.id.navigation).setBackgroundColor(hey);

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();

        // Begin the transaction
        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, new profilePageFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void showSomething(View view){
        Log.d("Button Click", "button was succesfully clicked");
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setCancelable(false);
        ad.setTitle("Error");
        ad.setMessage("The turtle is superior; you shan't change it");
        ad.setButton("I accept the superiority of the turtle", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();

    }



}
