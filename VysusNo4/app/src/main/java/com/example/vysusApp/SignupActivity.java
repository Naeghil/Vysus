package com.example.vysusApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class SignupActivity extends AppCompatActivity {

    EditText txtUsername;
    EditText txtPassword;
    EditText txtPasswordCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
    }

    private void registerSession() {
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("Auth", "Session not created");
            }
        });
    }

    public void signUp(View view) {
        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtPasswordCon = (EditText)findViewById(R.id.txtPasswordCon);
        EditText firstName = (EditText)findViewById(R.id.txtFirstname);
        EditText surName = (EditText)findViewById(R.id.txtSurname);

        if (!txtPassword.getText().toString().equals(txtPasswordCon.getText().toString())) {
            Toast.makeText(getBaseContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
        } else if(txtPassword.getText().toString().length() < 8) {
            Toast.makeText(getBaseContext(), "Quickblox requires passwords to be 8 char long", Toast.LENGTH_SHORT).show();
        } else {
            String login = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();

            final QBUser user = new QBUser(login, password);
            user.setFullName(firstName.getText().toString() + " " + surName.getText().toString());

            QBUsers.signUp(user).performAsync( new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser user, Bundle args) {
                    finish();
                }

                @Override
                public void onError(QBResponseException error) {
                    Log.d("Failure", error.getMessage());
                }
            });

            startActivity(new Intent(SignupActivity.this, MainActivity.class));
        }


        //
    }
}
