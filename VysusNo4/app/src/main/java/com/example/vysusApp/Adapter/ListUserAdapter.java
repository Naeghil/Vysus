package com.example.vysusApp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ListUserAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBUser> qbUserArrayList;

    public ListUserAdapter(Context baseContext, ArrayList<QBUser> qbUsersWithoutCurrent) {
        this.context = baseContext;
        this.qbUserArrayList = qbUsersWithoutCurrent;
    }

    @Override
    public int getCount() {
        return qbUserArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return qbUserArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewToBeReturned = view;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewToBeReturned = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, null);

            TextView textView = (TextView)viewToBeReturned.findViewById(android.R.id.text1);
            textView.setText(qbUserArrayList.get(i).getFullName());
            //Drawable icon = imageView.getResources().getDrawable(R.mipmap.duck_foreground);
            //imageView.setImageIcon(icon);
        }
        return viewToBeReturned;
    }
}
