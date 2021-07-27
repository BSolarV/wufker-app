package com.messirvedevs.wufker.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.messirvedevs.wufker.objects.Answer;
import com.messirvedevs.wufker.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class AnswerListAdapter extends ArrayAdapter<Answer> {

    public static final String SHARED_PREFS = "USER_DATA_WUFKER";
    public static final String EMAIL = "EMAIL";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AnswerListAdapter(Context context, List<Answer> answerList){
        super(context, 0, answerList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Answer answer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.answer_list_item, parent, false);
        }
        TextView answerDate = convertView.findViewById(R.id.AnswerDate);
        answerDate.setText( new SimpleDateFormat("MM/dd/yyyy HH:mm").format(answer.getDatetime()) );
        TextView answerContent = convertView.findViewById(R.id.AnswerContent);
        answerContent.setText( answer.getContent() );
        TextView answerEmail = convertView.findViewById(R.id.AnswerEmail);
        answerEmail.setText( answer.getUsername() );
        TextView answerScore = convertView.findViewById(R.id.AnswerScore);
        answerScore.setText( String.valueOf( answer.getVotes() ) );

        // Votaciones

        TextView answerUpVote = convertView.findViewById(R.id.AnswerUpVote);
        TextView answerDownVote = convertView.findViewById(R.id.AnswerDownVote);

        SharedPreferences sharedPreferences = convertView.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString(EMAIL, "");
        if( answer.getVoters().containsKey( userEmail ) ){
            if( answer.getVoters().get( userEmail ) ){
                answerUpVote.setBackgroundTintList( ColorStateList.valueOf(Color.CYAN) );
                answerUpVote.setClickable(false);
                answerDownVote.setBackgroundTintList( ColorStateList.valueOf( 0xFF8d8d8d ) );
                answerDownVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        answer.setVotes( answer.getVotes() - 2 );
                        answer.getVoters().put( userEmail , false );
                        db.collection("answers").document(answer.getId()).set(answer);
                        notifyDataSetChanged();
                    }
                });
            }else {
                answerUpVote.setBackgroundTintList( ColorStateList.valueOf( 0xFF8d8d8d ) );
                answerUpVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        answer.setVotes( answer.getVotes() + 2 );
                        answer.getVoters().put( userEmail , true );
                        db.collection("answers").document(answer.getId()).set(answer);
                        notifyDataSetChanged();
                    }
                });
                answerDownVote.setBackgroundTintList( ColorStateList.valueOf(Color.CYAN) );
                answerDownVote.setClickable(false);
            }
        }else {
            answerUpVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answer.setVotes( answer.getVotes() + 1 );
                    answer.getVoters().put( userEmail , true );
                    db.collection("answers").document(answer.getId()).set(answer);
                    notifyDataSetChanged();
                }
            });
            answerDownVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    answer.setVotes( answer.getVotes() - 1 );
                    answer.getVoters().put( userEmail , false );
                    db.collection("answers").document(answer.getId()).set(answer);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

}