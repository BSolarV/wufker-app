package com.messirvedevs.wufker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.messirvedevs.wufker.databinding.ActivityCrearPostBinding;

import java.util.HashMap;

public class CrearPostActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "USER_DATA_WUFKER";
    public static final String EMAIL = "EMAIL";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityCrearPostBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String category, postTitle, postContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCrearPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.foroSelector, R.id.publicationSelector, R.id.publicationDetail)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");
    }

    public void publicar(View view) {
        TextInputEditText editTitle = findViewById(R.id.post_title);
        TextInputEditText editContent = findViewById(R.id.post_content);

        postTitle = editTitle.getText().toString().trim();
        postContent = editContent.getText().toString().trim();

        if (postTitle.length() > 0 && postContent.length() > 0) {

            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

            // Agregar los nuevos post a firebase

            db.collection("posts").add(new HashMap<String, String>() {{
                put("category", category);
                put("title", postTitle);
                put("content", postContent);
                put("authorEmail", sharedPreferences.getString(EMAIL, ""));
            }});
            finish();
        } else {
            Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_LONG).show();
        }

    }

    public void cancelar(View view) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
