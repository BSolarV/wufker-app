package com.messirvedevs.wufker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    //TextView veterinario = findViewById(R.id.ProfileSince); //este será el switch de veterinario

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String SHARED_PREFS = "USER_DATA_WUFKER";
    public static final String EMAIL = "EMAIL";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfile newInstance(String param1, String param2) {
        UserProfile fragment = new UserProfile();
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
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        Task<QuerySnapshot> data = db.collection("users").whereEqualTo("email","cajara@sansano.usm.cl").get();
        data.addOnSuccessListener(result -> {
            List<DocumentSnapshot> users = result.getDocuments();
            /*if(users.size()== 0 ){
                Toast.makeText(this.getContext(), sharedPreferences.getString(EMAIL, ""), Toast.LENGTH_LONG).show();

            }*/

//            users =  result.getDocuments();
            DocumentSnapshot user =  users.get(0);
            TextView nombre =  (TextView) getView().findViewById(R.id.ProfileName);
            TextView correo = (TextView) getView().findViewById(R.id.ProfileSince);
            TextView fecha = (TextView) getView().findViewById(R.id.fechaNacimiento);


            nombre.setText(user.get("firstname").toString() + " " + user.get("lastname").toString());
            correo.setText(sharedPreferences.getString(EMAIL, ""));
            fecha.setText(user.get("birthdate").toString());
            Switch isvet = (Switch) getView().findViewById(R.id.vetSwitch);
            if (user.get("isVet").toString()=="true"){
                isvet.setChecked(true);
            }

        });
    }
}