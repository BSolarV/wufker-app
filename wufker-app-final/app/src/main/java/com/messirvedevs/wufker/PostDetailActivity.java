package com.messirvedevs.wufker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
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
import com.google.type.DateTime;
import com.messirvedevs.wufker.databinding.ActivityPostDetailBinding;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPostDetailBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Answer> answers;
    private TextView inForoTitle,InForoUser,InForoQuestion;

    private List<String> ans_list = new ArrayList();

    private ArrayAdapter adapter;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set user an question
        inForoTitle = findViewById(R.id.inForoTitle);
        InForoQuestion = findViewById(R.id.InForoQuestion);
        InForoUser = findViewById(R.id.InForoUser);

        setSupportActionBar(binding.appBarMainPostDetail.toolbar);
        binding.appBarMainPostDetail.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent ii = new Intent(view.getContext(), ResponderPostActivity.class);
                //ii.putExtra("category",  );
                ii.putExtra("id_post", id);
                ii.putExtra("postTitle", inForoTitle.getText().toString());
                ii.putExtra("postContent", InForoQuestion.getText().toString());


                startActivity(ii);
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

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("postId");

        adapter = new ArrayAdapter(this, R.layout.list_item, ans_list );
        ListView InForoAnswers = findViewById(R.id.InForoAnswers);

        InForoAnswers.setAdapter(adapter);
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



        // Get post and answers from database

        Task<DocumentSnapshot> data = db.collection("posts").document(id).get();
        data.addOnSuccessListener(command -> {
            inForoTitle.setText(command.get("title").toString());
            InForoQuestion.setText(command.get("content").toString());
            InForoUser.setText(command.get("authorEmail").toString());
            Task<QuerySnapshot> answersQuery = db.collection("answers").whereEqualTo("postId", id).get();
            answersQuery.addOnSuccessListener(content -> {
                List<DocumentSnapshot> docList = content.getDocuments();
                Collections.sort(docList, new Comparator<DocumentSnapshot>() {
                    @Override public int compare(DocumentSnapshot u1, DocumentSnapshot u2) {
                        return Timestamp.valueOf( u1.get("datetime").toString() ).compareTo(Timestamp.valueOf( u2.get("datetime").toString() ));
                    } });
                if (answersQuery.isComplete()) {
                    int i = 0;
                    while (i < docList.size()) {
                        DocumentSnapshot doc = docList.get(i);
                        Answer ans = new Answer( doc.get("username").toString(),  doc.get("content").toString(), Timestamp.valueOf( doc.get("datetime").toString() ),  Integer.valueOf(doc.get("votes").toString()));
                        ans_list.add(ans.getContent() + "\n" + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(ans.getDatetime()) + " - "  + ans.getUsername());
                        i++;
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }
}