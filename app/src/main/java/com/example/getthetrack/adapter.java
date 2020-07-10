package com.example.getthetrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class adapter extends BaseAdapter {
    Context context;
    String tion[];
    String ans[];
    LayoutInflater inflter;

    public adapter(Context applicationContext, String[] tion, String[] ans) {
        this.context = context;
        this.tion = tion;
        this.ans = ans;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return tion.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item, null);
        TextView tiontext = view.findViewById(R.id.tion);
        TextView anstext = view.findViewById(R.id.ans);
        tiontext.setText(tion[i]);
        anstext.setText(ans[i]);
        return view;
    }
}
