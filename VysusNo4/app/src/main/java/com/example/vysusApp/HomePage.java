package com.example.vysusApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

/*
    HomePage -      controls the job fragment, notification fragment, and profile fragment, as well as implementing methods
                    used in those fragments

    Authors -       Maximilian Lietz
 */

public class HomePage extends AppCompatActivity  {

    private TextView mTextMessage;
    FragmentTransaction ft;
    profilePageFragment ppFragmentObject = new profilePageFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    JobFragment jobFragment = new JobFragment();
    boolean jobFragmentWasOpened = false;
    String loadedFragment = "";
    public static final int PICK_IMAGE = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_jobs:

                    loadFragment(jobFragment);
                    loadedFragment = jobFragment.toString();
                    return true;

                case R.id.navigation_profilePage:

                    loadFragment(ppFragmentObject);
                    loadedFragment = ppFragmentObject.toString();
                    return true;

                case R.id.navigation_notifications:
                    loadFragment(notificationFragment);
                    loadedFragment = notificationFragment.toString();
                    return true;
                case R.id.navigation_message:
                    Intent intent = new Intent(HomePage.this, ChatDialogActivity.class);
                    startActivity(intent);;
                    return true;
            }
            return false;
        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hamburger_menu:
                startActivity(new Intent(HomePage.this, SettingsActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(ppFragmentObject);
        loadedFragment = "Profile Page";

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(loadedFragment);
        }
        myToolbar.inflateMenu(R.menu.drawer_view);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    // enables users to replace their profile picture with a picture from their phone - specifically this method calls
    // the local gallery on the phone
    public void showSomething(View view){

        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                if (bitmap.equals(null)) {
                    Log.d("Huh","something");
                } else {
                    Log.d("Nope", "different issue");
                }
                ppFragmentObject.changeImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
