package com.messirvedevs.wufker;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnswerPublicationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerPublicationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //windu
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String category, postTitle, postContent ,idPost;
    private String answer;

    public AnswerPublicationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnswerPublicationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnswerPublicationFragment newInstance(String param1, String param2) {
        AnswerPublicationFragment fragment = new AnswerPublicationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer_publication, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        postTitle = getArguments().getString("postTitle");
        postContent = getArguments().getString("postContent");
        idPost = getArguments().getString("id_post");

        TextView cat_txtview = (TextView) getView().findViewById(R.id.category);
        TextView title_txtview = (TextView) getView().findViewById(R.id.postTitle);
        TextView content_txtview = (TextView) getView().findViewById(R.id.postContent);
        cat_txtview.setText(category);
        title_txtview.setText(postTitle);
        content_txtview.setText(postContent);

        Button responder_btn = (Button) getView().findViewById(R.id.responder);
        Button cancelar_btn = (Button) getView().findViewById(R.id.cancelar2);

        responder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responder(view);
            }
        });
        cancelar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelar(view);
            }
        });

    }
    public void responder(View view) {
        TextInputEditText editAnswer = (TextInputEditText) getView().findViewById(R.id.answer);
        answer = editAnswer.getText().toString().trim();

        if (answer.length() > 0) {
            // Agregar la respuesta a firebase
            Date date= new Date();
            long time = date.getTime();
            Timestamp ts = new Timestamp(time);

            db.collection("answers").add(new HashMap<String, String>() {{
                put("postId", idPost);
                put("content", answer);
                put("datetime", String.valueOf(ts));
                put("votes", "0");
                put("username", "overcloveer@gmail.com");
            }});
            Navigation.findNavController(view).popBackStack();
        } else {
            Toast.makeText(getContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelar(View view) {
        Navigation.findNavController(view).popBackStack();
    }
}