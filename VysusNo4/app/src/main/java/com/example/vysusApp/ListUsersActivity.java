package com.example.vysusApp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vysusApp.Adapter.ListUserAdapter;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

/*
    ListUsersActivity -    contains functions essential to displaying chats that can be created, thus also finding possible chat partners

    Authors -               Maximilian Lietz

    The messaging service relies on the Quickblox framework
    Quickblox license

            Copyright (c) 2015, QuickBlox
            All rights reserved.

            Redistribution and use in source and binary forms, with or without
            modification, are permitted provided that the following conditions are met:

            * Redistributions of source code must retain the above copyright notice, this
              list of conditions and the following disclaimer.

            * Redistributions in binary form must reproduce the above copyright notice,
              this list of conditions and the following disclaimer in the documentation
              and/or other materials provided with the distribution.

            THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
            AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
            IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
            DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
            FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
            DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
            SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
            CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
            OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
            OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

public class ListUsersActivity extends AppCompatActivity {

    ListView lstUsers;
    Button btnCreateChat;
    int btnClickIndex;
    ImageButton improvisedBackButton;
    TextView topText;
    //QBUser user = MainActivity.user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        topText = (TextView)findViewById(R.id.topText);
        topText.setText("New Chat With");

        lstUsers = (ListView)findViewById(R.id.lstUsers);
        lstUsers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lstUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                btnClickIndex = i;
                //Toast.makeText(getApplicationContext(), "HI " + lstUsers.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
            }
        });

        retrieveAllUsers();

        btnCreateChat = (Button)findViewById(R.id.btn_create_chat);
        btnCreateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countChoice = lstUsers.getCount();

                if (lstUsers.getCheckedItemPositions().size() == 1)
                    createPrivateChat(lstUsers.getCheckedItemPositions());
                else {
                    Activity activity = (Activity) getBaseContext();
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Wait, that's illegal").setTitle("kill me please");

                    // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                    AlertDialog dialog = builder.create();
                }


            }
        });

        improvisedBackButton = (ImageButton)findViewById(R.id.improvisedBackButton);
        improvisedBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListUsersActivity.this, ChatDialogActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createPrivateChat(SparseBooleanArray checkedItemPositions) {
        int countChoice = lstUsers.getCount();
        ArrayList<Integer> occupantIdsList = new ArrayList<>();

        QBUser user = (QBUser)lstUsers.getItemAtPosition(btnClickIndex);
        occupantIdsList.add(user.getId());

        QBChatDialog dialog = new QBChatDialog();
        // should build in a proper stringbuilder here - imported from common package
        dialog.setName("How about now");
        dialog.setType(QBDialogType.PRIVATE);
        dialog.setOccupantsIds(occupantIdsList);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {
                Toast.makeText(getBaseContext(), "Dialog Successfully created", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(QBResponseException responseException) {
                Log.d("ListUserActivity Error", "what a surprise");
            }
        });
    }

    private void retrieveAllUsers() {

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                ArrayList<QBUser> qbUsersWithoutCurrent = new ArrayList<QBUser>();
                for (QBUser user : qbUsers)
                if(!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin())){
                    qbUsersWithoutCurrent.add(user);
                }

                ListUserAdapter adapter = new ListUserAdapter(getBaseContext(), qbUsersWithoutCurrent);
                lstUsers.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("Failure", "At List Users Activity");
            }
        });

    }
}
