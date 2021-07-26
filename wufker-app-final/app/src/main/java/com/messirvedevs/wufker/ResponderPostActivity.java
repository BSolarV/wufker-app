package com.messirvedevs.wufker;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.messirvedevs.wufker.databinding.ActivityResponderPostBinding;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class ResponderPostActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActivityResponderPostBinding binding;

    private String category, postTitle, postContent ,idPost;
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResponderPostBinding.inflate(getLayoutInflater());
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
        //category = bundle.getString("category");
        postTitle = bundle.getString("postTitle");
        postContent = bundle.getString("postContent");
        idPost = bundle.getString("id_post");

        TextView cat_txtview = findViewById(R.id.category);
        TextView title_txtview = findViewById(R.id.postTitle);
        TextView content_txtview = findViewById(R.id.postContent);
        cat_txtview.setText(category);
        title_txtview.setText(postTitle);
        content_txtview.setText(postContent);
    }

    public void responder(View view) {
        TextInputEditText editAnswer = findViewById(R.id.answer);
        answer = editAnswer.getText().toString().trim();

        if (answer.length() > 0) {
            // Agregar la respuesta a firebase
            Date date= new Date();
            long time = date.getTime();
            Timestamp ts = new Timestamp(time);

            db.collection("answers").add(new HashMap<String, String>() {{
                put("postId", idPost);
                put("content", answer);
                put("datetime", String.valueOf(ts));
                put("votes", "0");
                put("username", "overcloveer@gmail.com");
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
