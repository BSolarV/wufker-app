package com.messirvedevs.wufker;

import android.content.Context;
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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPublication#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPublication extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String id,category,titulo,question,user;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //windu
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String postTitle, postContent;
    public static final String SHARED_PREFS = "USER_DATA_WUFKER";
    public static final String EMAIL = "EMAIL";


    public EditPublication() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPublication.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPublication newInstance(String param1, String param2) {
        EditPublication fragment = new EditPublication();
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
        return inflater.inflate(R.layout.fragment_edit_publication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        id = getArguments().getString("id_post");
        category = getArguments().getString("category");
        titulo = getArguments().getString("titulo");
        question = getArguments().getString("question");
        user = getArguments().getString("user");

        TextInputEditText editTitle = (TextInputEditText) getView().findViewById(R.id.post_title);
        TextInputEditText editContent = (TextInputEditText) getView().findViewById(R.id.post_content);

        editTitle.setText(titulo);
        editContent.setText(question);


        Button publish_btn = (Button) getView().findViewById(R.id.publicar);
        Button cancel_btn = (Button) getView().findViewById(R.id.cancelar);

        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicar(view);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelar(view);
            }
        });



    }
    public void publicar(View view) {
        TextInputEditText editTitle = (TextInputEditText) getView().findViewById(R.id.post_title);
        TextInputEditText editContent = (TextInputEditText) getView().findViewById(R.id.post_content);

        postTitle = editTitle.getText().toString().trim();
        postContent = editContent.getText().toString().trim();

        if (postTitle.length() > 0 && postContent.length() > 0) {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

            // Agregar los nuevos post a firebase
            //Toast.makeText(getContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
            DocumentReference publication_post = db.collection("posts").document(id);
            publication_post.update(
                            "authorEmail",user,
                    "category", category,
                            "title", postTitle,
                            "content", postContent
                    );



            Navigation.findNavController(view).popBackStack();
        } else {
            Toast.makeText(getContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_LONG).show();
        }

    }
    public void cancelar(View view) {
        Navigation.findNavController(view).popBackStack();
    }
}