package com.example.wufkerapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Thread.sleep(2000); // optional Splash

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // SetUp
        setUp();
    }

    private fun setUp() {
        title = "Authentication";


    }
}