package com.messirvedevs.wufker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.DialogFragmentNavigator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.messirvedevs.wufker.objects.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ProfileEditDetailsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "USER_DATA_WUFKER";
    public static final String EMAIL = "EMAIL";
    public static final String LASTNAME = "LASTNAME";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button saveButton;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_details);

        this.saveButton = findViewById(R.id.registerButton);
        this.dateButton = findViewById(R.id.buttonBirthday);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = makeDateSrting(day, month, year);
        dateButton.setText(date);

        initDatePicker();
        setup();
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateSrting(dayOfMonth, month, year);
                dateButton.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    private String makeDateSrting(int day, int month, int year){

        String sDay = String.valueOf(day);
        if( day < 10 ) sDay = "0"+sDay;

        String sMonth = String.valueOf(month);
        if( month < 10 ) sMonth = "0"+sMonth;

        return sDay + "/" + sMonth + "/" + String.valueOf(year);
    }

    private void setup() {

        EditText editTextFirstName = findViewById(R.id.editTextFirstName);
        EditText editTextLastName = findViewById(R.id.editTextLastName);
        Switch switchVet = findViewById(R.id.switchVet);

        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextFirstName.getText().toString().isEmpty() && !editTextLastName.getText().toString().isEmpty() && !dateButton.getText().toString().isEmpty()) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(LASTNAME, editTextLastName.getText().toString());
                    editor.apply();

                    String email = sharedPreferences.getString(EMAIL, "");
                    Date birthdate;
                    try{
                        birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(dateButton.getText().toString());
                    }catch (Exception e){
                        birthdate = new Date();
                    }
                    User user = new User(email, editTextFirstName.getText().toString(), editTextLastName.getText().toString(), switchVet.isActivated(), birthdate);

                    db.collection("users").document(sharedPreferences.getString(EMAIL, "")).set(user);
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

    private void showDatePickerDialog(){
        datePickerDialog.show();
    }

}