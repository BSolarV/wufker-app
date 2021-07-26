package com.messirvedevs.wufker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
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

public class ForoActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityForoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMain.toolbar);
        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.foroSelector)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Foro Selector
        /*TextView enf =  findViewById(R.id.CategoriasEnfemedades);
        TextView cons =  findViewById(R.id.CategoriasConsejos);
        TextView comp =  findViewById(R.id.CategoriasComportamiento);
        TextView  ali =  findViewById(R.id.CategoriasAlimentacion);
        TextView misc =  findViewById(R.id.CategoriasMiscelaneos);
        Button mapButton =  findViewById(R.id.mapButton);

        enf.setOnClickListener(this);
        cons.setOnClickListener(this);
        comp.setOnClickListener(this);
        ali.setOnClickListener(this);
        misc.setOnClickListener(this);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapa = new Intent(view.getContext(), MapActivity.class);
                Navigation.findNavController(view).navigate(R.id.googleMap);
                //startActivity(mapa);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*@Override
    public void onClick(View v) {

        int i = v.getId();
        TextView ans = findViewById(i);
        String selected = ans.getText().toString();

        *//*Intent foro = new Intent(this, ForoDetailActivity.class);
        foro.putExtra("category", selected);*//*

        Bundle bundle = new Bundle();
        bundle.putString("selected" ,selected);


        Navigation.findNavController(v).navigate(R.id.publicationSelector, bundle);
        //startActivity(foro);
    }*/
}