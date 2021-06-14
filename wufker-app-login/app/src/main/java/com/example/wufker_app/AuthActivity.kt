package com.example.wufker_app

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptIntrinsicYuvToRGB
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthProvider
import com.google.firebase.inject.Provider

class AuthActivity : AppCompatActivity() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_auth);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "integracion completa")
        mFirebaseAnalytics!!.logEvent("InitScreen", bundle)

        setup();
    }

    private fun setup() {
        title = "Autenticación";
        val singup: Button = findViewById(R.id.singup)
        val editTextTextEmailAddress: EditText = findViewById(R.id.editTextTextEmailAddress)
        val editTextTextPassword: EditText = findViewById(R.id.editTextTextPassword)
        singup.setOnClickListener {
            if( editTextTextEmailAddress.text.isNotEmpty() && editTextTextPassword.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(editTextTextEmailAddress.text.toString(),
                                editTextTextPassword.text.toString()).addOnCompleteListener {
                                    if( it.isSuccessful ) {
                                        showHome( it.result?.user?.email ?:"", "BASIC" )
                                    }else {
                                        showAlert(it.exception.toString())
                                    }
                        }
            }
        }

        val singin: Button = findViewById(R.id.singin)
        singin.setOnClickListener {
            if (editTextTextEmailAddress.text.isNotEmpty() && editTextTextPassword.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        editTextTextEmailAddress.text.toString(),
                        editTextTextPassword.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showHome(it.result?.user?.email ?: "", "BASIC")
                        } else {
                            showAlert(it.toString())
                        }
                    }
            }
        }
    }

    private fun showAlert(msg:String) {
         val builder = AlertDialog.Builder(this);
        builder.setTitle("Error")
        builder.setMessage(msg)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: String) {
        val homeIntent:Intent = Intent( this, HomeActivity::class.java ).apply {
            putExtra("email", email)
            putExtra( "provider", provider )
        }
        startActivity(homeIntent);
    }
}