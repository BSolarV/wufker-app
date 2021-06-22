package com.messirvedevs.wufker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ProfileEditDetailsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "USER_DATA_WUFKER";
    public static final String EMAIL = "EMAIL";
    public static final String LASTNAME = "LASTNAME";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_details);

        this.saveButton = findViewById(R.id.registerButton);

        setup();
    }

    private void setup() {

        EditText editTextFirstName = findViewById(R.id.editTextFirstName);
        EditText editTextLastName = findViewById(R.id.editTextLastName);
        EditText editTextBirthday = findViewById(R.id.editTextBirthday);
        Switch switchVet = findViewById(R.id.switchVet);

        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextFirstName.getText().toString().isEmpty() && !editTextLastName.getText().toString().isEmpty() && !editTextBirthday.getText().toString().isEmpty()) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(LASTNAME, editTextLastName.getText().toString());
                    editor.apply();

                    db.collection("users").add(new HashMap<String, String>() {{
                        put("email", sharedPreferences.getString(EMAIL, ""));
                        put("lastname", editTextLastName.getText().toString());
                        put("firstname", editTextFirstName.getText().toString());
                        put("birthdate", editTextBirthday.getText().toString());
                        put("isVet", String.valueOf(switchVet.isActivated()));
                    }});
                    showHome();
                } else {
                    Toast.makeText(v.getContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_LONG).show();
                }
            }
        } );
    }

    private void showHome() {
        Intent homeIntent = new Intent( this, ForoActivity.class );
        startActivity(homeIntent);
    }

}