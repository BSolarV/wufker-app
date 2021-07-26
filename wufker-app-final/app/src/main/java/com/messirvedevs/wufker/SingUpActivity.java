package com.messirvedevs.wufker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SingUpActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "USER_DATA_WUFKER";
    public static final String EMAIL = "EMAIL";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        setup();
    }

    private void setup() {
        Button singup = findViewById(R.id.registerButton);
        EditText editTextTextEmailAddress = findViewById(R.id.editTextFirstName);
        EditText editTextTextPassword = findViewById(R.id.editTextLastName);
        EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextTextEmailAddress.getText().toString().isEmpty() && !editTextTextPassword.getText().toString().isEmpty() && !editTextConfirmPassword.getText().toString().isEmpty()) {
                    if( editTextTextPassword.getText().toString().equals( editTextConfirmPassword.getText().toString() ) ){
                        FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(editTextTextEmailAddress.getText().toString(),
                                        editTextTextPassword.getText().toString()).addOnCompleteListener(it -> {
                            if (it.isSuccessful()) {
                                showProfileDetails(it.getResult().getUser().getEmail());
                            } else {
                                showAlert(it.getException().toString());
                            }
                        });
                    }

                }
            }
        } );
    }

    private void showProfileDetails(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email);
        editor.apply();
        Intent profileDetailsEditIntent = new Intent( this, ProfileEditDetailsActivity.class );
        startActivity(profileDetailsEditIntent);
    }

    private void showAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}