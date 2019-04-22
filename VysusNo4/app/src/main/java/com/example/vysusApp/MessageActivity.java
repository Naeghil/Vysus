package com.example.vysusApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vysusApp.Adapter.ChatMessageAdapter;
import com.example.vysusApp.Common.Common;
import com.example.vysusApp.Holder.QBChatMessagesHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;

/*
    MessageActivity -  Loads messages from Quickblox and then calls layouts which are fed with the gathered data and finally loaded
    into the list view via the adapter


    Authors -       Maximilian Lietz

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

public class MessageActivity extends AppCompatActivity {

    QBChatDialog qbChatDialog;
    ListView lstChatMessages;
    ImageButton submitButton;
    EditText editContent;
    TextView personName;
    ImageButton improvisedBackButton;

    ChatMessageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initViews();

        initChatDialogs();

        retrieveMessage();



        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        personName = (TextView)findViewById(R.id.personName);
        personName.setText(qbChatDialog.getName());

        //myToolbar.inflateMenu(R.menu.drawer_view);

        improvisedBackButton = (ImageButton)findViewById(R.id.improvisedBackButton);
        improvisedBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, ChatDialogActivity.class);
                startActivity(intent);
            }
        });

        //personName.setText(QBChatService.getInstance().getUser().getFullName());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody(editContent.getText().toString());
                chatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
                chatMessage.setSaveToHistory(true);

                try {
                    qbChatDialog.sendMessage(chatMessage);
                } catch (SmackException.NotConnectedException e) {

                    e.printStackTrace();
                }


                QBChatMessagesHolder.getInstance().putMessage(qbChatDialog.getDialogId(), chatMessage);
                ArrayList<QBChatMessage> messages = QBChatMessagesHolder.getInstance().getChatMessageByDialogId(qbChatDialog.getDialogId());
                adapter = new ChatMessageAdapter(getBaseContext(), messages);
                lstChatMessages.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                editContent.setText("");
                editContent.setFocusable(true);
            }
        });
    }

    private void retrieveMessage() {
        QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setLimit(500);

        if (qbChatDialog != null) {
            QBRestChatService.getDialogMessages(qbChatDialog,messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    QBChatMessagesHolder.getInstance().putMessages(qbChatDialog.getDialogId(), qbChatMessages);
                    ChatMessageAdapter adapter = new ChatMessageAdapter(getBaseContext(), qbChatMessages);
                    lstChatMessages.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        }
    }

    private void initChatDialogs() {
        qbChatDialog = (QBChatDialog)getIntent().getSerializableExtra(Common.DIALOG_EXTRA);
        qbChatDialog.initForChat(QBChatService.getInstance());

        // Register listener Incoming Message
        QBIncomingMessagesManager incomingMessage = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessage.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                
            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                Toast.makeText(MessageActivity.this, "Error at Message Activity", Toast.LENGTH_SHORT).show();
            }
        });

        qbChatDialog.addMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                QBChatMessagesHolder.getInstance().putMessage(qbChatMessage.getDialogId(), qbChatMessage);
                ArrayList<QBChatMessage> messages = QBChatMessagesHolder.getInstance().getChatMessageByDialogId(qbChatMessage.getDialogId());
                adapter = new ChatMessageAdapter(getBaseContext(), messages);
                lstChatMessages.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                Log.d("ERROR MESSAGE ACTIVITY", "Because " + e.getMessage());
            }
        });
    }

    private void initViews() {

        lstChatMessages = (ListView)findViewById(R.id.messages_list);
        submitButton = (ImageButton)findViewById(R.id.send_button);
        editContent = (EditText)findViewById(R.id.edit_content);

    }
}
