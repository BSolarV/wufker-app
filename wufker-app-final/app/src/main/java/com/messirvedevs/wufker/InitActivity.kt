package com.messirvedevs.wufker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.messirvedevs.wufker.objects.User


class InitActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private val db = FirebaseFirestore.getInstance()

    val SHARED_PREFS = "USER_DATA_WUFKER"
    val EMAIL = "EMAIL"
    val FULLNAME = "FULLNAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "integracion completa")
        mFirebaseAnalytics!!.logEvent("InitScreen", bundle)

        setup()
    }

    private fun setup() {

        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        if (sharedPreferences.getString(
                EMAIL,
                ""
            ) != ""
        ) showHome(sharedPreferences.getString(EMAIL, "") ?: "", "GOOGLE")

        title = "Autenticación"
        val singup: Button = findViewById(R.id.registerButton)
        val editTextTextEmailAddress: EditText = findViewById(R.id.editTextFirstName)
        val editTextTextPassword: EditText = findViewById(R.id.editTextLastName)

        singup.setOnClickListener {
            showSingUp();
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

                            val editor = sharedPreferences.edit()
                            val email = editTextTextEmailAddress.text.toString()
                            db.collection("users").document(email).get()
                                .addOnCompleteListener(OnCompleteListener<DocumentSnapshot?> { task ->
                                    if (task.isSuccessful) {
                                        val document = task.result
                                        if (document!!.exists()) {
                                            val user = document!!.toObject(User::class.java)
                                            editor.putString(EMAIL, email)
                                            editor.putString(FULLNAME, user!!.firstname + " " + user.lastname)
                                            editor.apply()
                                            showHome(it.result?.user?.email ?: "", "BASIC")
                                        }
                                    }
                                })

                        } else {
                            showAlert(it.toString())
                        }
                    }
            }
        }
        /*val googleButton : Button = findViewById(R.id.GoogleSingIn)
        googleButton.setOnClickListener {
            val googleConf =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN )
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)

            googleClient.signOut()

            startActivity(googleClient.signInIntent)

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }*/
    }

    private fun showAlert(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(msg)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: String) {
        val homeIntent: Intent = Intent(this, ForoActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider)
        }
        startActivity(homeIntent)
        finish()
    }

    private fun showSingUp() {
        val singUpIntent: Intent = Intent(this, SingUpActivity::class.java)
        startActivity(singUpIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                showAlert("account")

                if (account != null) {

                    showAlert("entre")
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            showAlert("firebase")
                            if (it.isSuccessful) {
                                showHome(account.email ?: "", "GOOGLE")
                            } else {
                                showAlert(it.toString())
                            }
                        }
                }
            } catch (e: ApiException) {
                showHome("overcloveer@gmail.com", "GOOGLE")
            }
        }
    }
}