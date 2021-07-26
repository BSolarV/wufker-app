package com.messirvedevs.wufker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.messirvedevs.wufker.databinding.ActivityForoBinding;
import com.messirvedevs.wufker.objects.ForoPost;

import java.util.ArrayList;
import java.util.List;

public class ForoDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityForoBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<String> post_list = new ArrayList();
    private List<String> id_list = new ArrayList();
    private String category;

    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*binding = ActivityForoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ////
        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("category");*/

        TextView title = findViewById(R.id.Foro_listCategory);
        title.setText(category);
        ////
        /*setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Snackbar.make(view, "Replace with your own button", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*


                Intent ii = new Intent(view.getContext(), CrearPostActivity.class);
                ii.putExtra("category", category );
                startActivity(ii);

            }
        });
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
        NavigationUI.setupWithNavController(navigationView, navController);*/


        adapter = new ArrayAdapter(this, R.layout.list_item, post_list );
        ListView lv = findViewById(R.id.Foro_listPosts);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);


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
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

        Intent intent = new Intent(this, PostDetailActivity.class);
        intent.putExtra("postId",  id_list.get(pos));
        startActivity(intent);
    }

    @Override
    protected void onResume() {

        super.onResume();

        post_list.clear();
        id_list.clear();

        Toast.makeText(this, "Obteniendo DB", Toast.LENGTH_LONG).show();

        Task<QuerySnapshot> data = db.collection("posts").whereEqualTo("category", category).get();

        Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show();

        data.addOnSuccessListener(command -> {
            String text = "Selected: "+category+"\n"+command.size()+"\n";
            if ( data.isComplete() ){
                int i = 0;
                for (ForoPost post:
                        command.toObjects(ForoPost.class)) {
                    text = text + post.getTitle() + "\n";
                    post_list.add(post.getTitle());
                    id_list.add(command.getDocuments().get(i).getId());
                    i++;
                }
                //Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
            }
        });
        data.addOnFailureListener(command -> {
            Toast.makeText(this, "Error:" + command.toString(), Toast.LENGTH_LONG).show();
        });
    }

}