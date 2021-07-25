package com.messirvedevs.wufker.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.messirvedevs.wufker.Answer;

import java.util.List;

public class AnswerListAdapter extends ArrayAdapter<Answer> {
    public AnswerListAdapter(Context context, List<Answer> answerList){
        super(context, 0, answerList);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
