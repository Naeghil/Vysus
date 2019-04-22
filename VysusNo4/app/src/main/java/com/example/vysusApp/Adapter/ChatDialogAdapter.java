package com.example.vysusApp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vysusApp.R;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class ChatDialogAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBChatDialog> qbChatDialogs;

    public ChatDialogAdapter(Context context, ArrayList<QBChatDialog> result) {
        this.context = context;
        this.qbChatDialogs = result;
    }

    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }

    @Override
    public Object getItem(int i) {
        return qbChatDialogs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewToBeReturned = view;
        if (viewToBeReturned == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewToBeReturned = inflater.inflate(R.layout.list_chat_dialog, null);

            TextView txtTitle, txtMessage;
            ImageView imageView;

            txtMessage = (TextView)viewToBeReturned.findViewById(R.id.list_chat_dialog_message);

            txtTitle = (TextView)viewToBeReturned.findViewById(R.id.list_chat_dialog_title);
            imageView = (ImageView)viewToBeReturned.findViewById(R.id.image_chatDialog);

            String actualText = qbChatDialogs.get(i).getLastMessage();

            txtTitle.setText(qbChatDialogs.get(i).getName());

            //Log.d("txtMessage Height: ", String.valueOf(txtMessage.getHeight()));
            Log.d("txtMessage Height ", String.valueOf(txtMessage.getText().length()));
            if (actualText != null) {
                if (actualText.length() > 100 ) {
                    actualText = actualText.substring(0,97);
                    actualText = actualText.concat("...");

                    txtMessage.setText(actualText);
                } else {
                    Log.d("Play Adele", "Because this is making me cry");
                    txtMessage.setText(qbChatDialogs.get(i).getLastMessage());
                }
            } else {
                txtMessage.setText(qbChatDialogs.get(i).getLastMessage());
            }

            //Drawable icon = imageView.getResources().getDrawable(R.mipmap.duck_foreground);
            //imageView.setImageIcon(icon);
        }
        return viewToBeReturned;
    }
}
