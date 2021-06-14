package com.example.wufker_app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val bundle:Bundle? = intent.extras
        val email:String? = bundle?.getString("email");
        val provider:String? = bundle?.getString("provider");

        setup(email?:"", provider?:"");
    }

    private fun setup(email:String, provider:String) {
        title = "inicio";

        val emailText: TextView = findViewById(R.id.emailText)
        emailText.text = email;
        val providerText: TextView = findViewById(R.id.providerText)
        providerText.text = email;

        val logout: Button = findViewById(R.id.logout)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            onBackPressed();
        }
    }
}