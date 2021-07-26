package com.messirvedevs.wufker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.messirvedevs.wufker.objects.Answer;
import com.messirvedevs.wufker.objects.User;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnswerPublicationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerPublicationFragment extends Fragment {

    public static final String SHARED_PREFS = "USER_DATA_WUFKER";
    public static final String EMAIL = "EMAIL";

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
    private User user;

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
        category = getArguments().getString("category");
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

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            String email = sharedPreferences.getString(EMAIL, "");

            String id = db.collection("Answers").document().getId();
            ArrayList<String> badges = new ArrayList<String>();
            db.collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            user = document.toObject(User.class);
                        } else {
                            backToLogin();
                        }
                    } else {
                        backToLogin();
                    }
                }
            });
            if( user.getVet() ) badges.add("veterinario");
            Answer answerObj = new Answer(email, answer, idPost, date, 0, new HashMap<String, Boolean>(), badges);
            answerObj.setId(id);
            db.collection("answers").document(id).set(answerObj);

            Navigation.findNavController(view).popBackStack();

        } else {
            Toast.makeText(getContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelar(View view) {
        Navigation.findNavController(view).popBackStack();
    }

    public void backToLogin(){
        Intent login = new Intent(getContext(), InitActivity.class);
        startActivity(login);
        getActivity().finish();
    }
}