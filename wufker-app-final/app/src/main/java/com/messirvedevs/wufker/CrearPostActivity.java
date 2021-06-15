package com.messirvedevs.wufker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.messirvedevs.wufker.ForoDetailActivity;
import com.messirvedevs.wufker.R;
import com.messirvedevs.wufker.databinding.ActivityCrearPostBinding;
import com.messirvedevs.wufker.databinding.ActivityForoDetailBinding;

public class CrearPostActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityCrearPostBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCrearPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void publicar(View view) {
        TextInputEditText editTitle = findViewById(R.id.post_title);
        TextInputEditText editContent = findViewById(R.id.post_content);

        String postTitle = editTitle.getText().toString();
        String postContent = editContent.getText().toString();

        if (postTitle.length() > 0 && postContent.length() > 0) {
            // Agregar los nuevos post a firebase
            Toast.makeText(this, "Titulo: " + postTitle +
                    "\nContenido: " + postContent, Toast.LENGTH_LONG).show();
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
