package com.messirvedevs.wufker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class InitActivity : AppCompatActivity() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "integracion completa")
        mFirebaseAnalytics!!.logEvent("InitScreen", bundle)

        setup();
    }

    private fun setup() {
        title = "Autenticación";
        val singup: Button = findViewById(R.id.registerButton)
        val editTextTextEmailAddress: EditText = findViewById(R.id.editTextEmail)
        val editTextTextPassword: EditText = findViewById(R.id.editTextPassword)

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

        val singin: Button = findViewById(R.id.singinButton)
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
        val homeIntent:Intent = Intent( this, ForoActivity::class.java ).apply {
            putExtra("email", email)
            putExtra( "provider", provider )
        }
        startActivity(homeIntent);
    }
}