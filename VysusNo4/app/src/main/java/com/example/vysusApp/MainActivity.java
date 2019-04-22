package com.example.vysusApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.StoringMechanism;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

/*
    MainActivity -  entry point for the app and controls the login for the app but more importantly for quickblox as well.
                    Quickblox session is created here and the credentials are used throughout the app

    Authors -       Maximilian Lietz
 */


public class MainActivity extends AppCompatActivity {

    // credentials to initialize the Quickblox framework
    static final String APP_ID = "76466";
    static final String AUTH_KEY = "Dz4AnLRQFnqkfJt";
    static final String AUTH_SECRET = "NfBLZ92kq6OX2a6";
    static final String ACCOUNT_KEY = "ykbSyCy3bNjhHBfepPss";

    //static String login = "login";
    //static String password = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFramework();
    }

    public void login(View v) {

        EditText loginField = (EditText)findViewById(R.id.emailText);
        EditText password = (EditText)findViewById(R.id.password);

        final QBUser user = new QBUser(loginField.getText().toString(), password.getText().toString());

        // Quickblox requires a double login - one for the service itself and one for the chat function hence the two
        // onSuccess methods
        QBAuth.createSession(user).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

                user.setId(qbSession.getUserId());
                user.setPassword(password.getText().toString());

                Log.d("user id", Integer.toString(qbSession.getUserId()));

                // INIT CHAT SERVICE
                QBChatService  chatService = QBChatService.getInstance();

                chatService.login(user, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        Log.d("Login", "Success");

                        startActivity(new Intent(MainActivity.this, HomePage.class));


                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d("Login", "Failure");
                        Log.d("Login", e.getMessage());
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("Failure", "couldn't create Session");
            }
        });




    }

    public void changeProfilePicture() {

    }

    private void initializeFramework() {
        Log.d("QBSettings", "should be working in theory");

        QBSettings.getInstance().setStoringMehanism(StoringMechanism.UNSECURED); //call before init method for QBSettings
        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

    }

    public void signUp(View v) {
        startActivity(new Intent(MainActivity.this, SignupActivity.class));
    }
}
