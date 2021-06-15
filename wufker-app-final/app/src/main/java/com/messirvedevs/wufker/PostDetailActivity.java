package com.messirvedevs.wufker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.messirvedevs.wufker.databinding.ActivityPostDetailBinding;
import com.messirvedevs.wufker.ui.Answer;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPostDetailBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Answer> answers;

    private List<String> ans_list = new ArrayList();

    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainPostDetail.toolbar);
        binding.appBarMainPostDetail.fab.setOnClickListener(new View.OnClickListener() {
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
        Toast.makeText(this, "navController", Toast.LENGTH_SHORT).show();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();

        adapter = new ArrayAdapter(this, R.layout.list_item, ans_list );
        ListView lv = findViewById(R.id.Foro_listPosts);
        lv.setAdapter(adapter);
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
    protected void onResume() {

        super.onResume();

        ans_list.clear();

        // set user an question
        TextView inTitle = findViewById(R.id.inForoTitle);
        TextView InForoQuestion = findViewById(R.id.InForoQuestion);
        TextView InForoUser = findViewById(R.id.InForoUser);

        // Get post and answers from database
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("postId");
        Task<DocumentSnapshot> data = db.collection("posts").document(id).get();
        data.addOnSuccessListener(command -> {
            inTitle.setText(command.get("title").toString());
            InForoQuestion.setText(command.get("content").toString());
            InForoUser.setText(command.get("authorEmail").toString());
            Task<QuerySnapshot> answersQuery = db.collection("anwers").whereEqualTo("post_id", command.getId()).get();
            answersQuery.addOnSuccessListener(content -> {
                if (answersQuery.isComplete()) {
                    for (Answer ans : content.toObjects(Answer.class)) {
                        ans_list.add(ans.getContent() + " \n - " + ans.getUsername());
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }
}