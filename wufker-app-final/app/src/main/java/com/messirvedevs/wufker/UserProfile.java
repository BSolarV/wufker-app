package com.messirvedevs.wufker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
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
import com.messirvedevs.wufker.objects.User;

import java.security.AccessController;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

         try {
             Button logoutButton = getView().findViewById(R.id.logoutButton);
             logoutButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                     SharedPreferences.Editor editor = sharedPreferences.edit();
                     editor.remove(EMAIL);
                     editor.apply();
                     backToLogin();
                 }
             });
         }catch (Exception e){
             Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
         }

        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            String email = sharedPreferences.getString(EMAIL, "");
            Task<DocumentSnapshot> data = db.collection("users").document(email).get();
            data.addOnSuccessListener(result -> {
                User user = result.toObject(User.class);

                Toast.makeText(getContext(), "getData", Toast.LENGTH_SHORT).show();

                TextView nombre =  (TextView) getView().findViewById(R.id.ProfileName);
                TextView correo = (TextView) getView().findViewById(R.id.ProfileSince);
                TextView fecha = (TextView) getView().findViewById(R.id.fechaNacimiento);
                TextView profileVeterinario = (TextView) getView().findViewById(R.id.profileVeterinario);


                nombre.setText(user.getFirstname() + " " + user.getLastname());
                correo.setText(email);
                fecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(user.getBirthdate()));
                Switch isVet = getView().findViewById(R.id.vetSwitch);
                if (user.getVet()){
                    profileVeterinario.setText("Veterinario");
                    isVet.setChecked(true);
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void backToLogin(){
        Intent login = new Intent(getContext(), InitActivity.class);
        startActivity(login);
        getActivity().finish();
    }
}