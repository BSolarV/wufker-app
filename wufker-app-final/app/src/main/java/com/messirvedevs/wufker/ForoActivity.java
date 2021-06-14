package com.messirvedevs.wufker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.messirvedevs.wufker.databinding.ActivityForoBinding;

import java.util.List;

public class ForoActivity extends AppCompatActivity implements View.OnClickListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityForoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForoBinding.inflate(getLayoutInflater());
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

        //Foro Selector
        TextView enf =  findViewById(R.id.CategoriasEnfemedades);
        TextView cons =  findViewById(R.id.CategoriasConsejos);
        TextView comp =  findViewById(R.id.CategoriasComportamiento);
        TextView  ali =  findViewById(R.id.CategoriasAlimentacion);
        TextView misc =  findViewById(R.id.CategoriasMiscelaneos);

        enf.setOnClickListener(this);
        cons.setOnClickListener(this);
        comp.setOnClickListener(this);
        ali.setOnClickListener(this);
        misc.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {

        int i = v.getId();
        TextView ans = findViewById(i);
        String selected = ans.getText().toString();
        Toast.makeText(this, "Selected: " + selected, Toast.LENGTH_LONG).show();

        /*Intent foro = new Intent(this, ForoDetailActivity.class);
        foro.putExtra("category", selected);
        startActivity(foro)*/;

    }
}