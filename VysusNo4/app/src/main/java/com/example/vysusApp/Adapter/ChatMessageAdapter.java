package com.example.vysusApp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vysusApp.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ChatMessageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBChatMessage> qbChatMessages;

    public ChatMessageAdapter(Context context, ArrayList<QBChatMessage> qbChatMessages) {
        this.context = context;
        this.qbChatMessages = qbChatMessages;
    }


    @Override
    public int getCount() {
        return qbChatMessages.size();
    }

    @Override
    public Object getItem(int i) {
        return qbChatMessages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String[] senderID = {""};

        View viewToBeReturned = view;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Log.d("Catch the Error", "Place No 1");
            if (qbChatMessages.get(i).getSenderId().equals(QBChatService.getInstance().getUser().getId())) {
                Log.d("Catch the Error", "Place No 2");
                viewToBeReturned = inflater.inflate(R.layout.send_message_layout, null);
                TextView textView = (TextView)viewToBeReturned.findViewById(R.id.message_content);
                Log.d("Catch the Error", "Place No 3");
                textView.setText(qbChatMessages.get(i).getBody());
                Log.d("Catch the Error", "Place No 4");
            } else {
                Log.d("Catch the Error", "Place No 5");
                viewToBeReturned = inflater.inflate(R.layout.receive_message_layout,null);
                TextView textView = (TextView)viewToBeReturned.findViewById(R.id.message_content);
                textView.setText(qbChatMessages.get(i).getBody());
                TextView sender = (TextView)viewToBeReturned.findViewById(R.id.message_user);
                Log.d("Catch the Error", "Place No 6");
                QBUsers.getUser(qbChatMessages.get(i).getSenderId()).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        senderID[0] = qbUser.getFullName();
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
                Log.d("Catch the Error", "Place No 7");
                sender.setText(senderID[0]);

            }
        }

        return viewToBeReturned;
    }
}
