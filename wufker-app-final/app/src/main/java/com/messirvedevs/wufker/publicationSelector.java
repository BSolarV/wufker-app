package com.messirvedevs.wufker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link publicationSelector#newInstance} factory method to
 * create an instance of this fragment.
 */
public class publicationSelector extends Fragment implements AdapterView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Windu


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<String> post_list = new ArrayList();
    private List<String> id_list = new ArrayList();
    private String category;

    private ArrayAdapter adapter;

    public publicationSelector() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment publicationSelector.
     */
    // TODO: Rename and change types and number of parameters
    public static publicationSelector newInstance(String param1, String param2) {
        publicationSelector fragment = new publicationSelector();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publication_selector, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        category = getArguments().getString("selected");

        FloatingActionButton btn =  getView().findViewById(R.id.fab3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "To addPostFragment", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Bundle bundle = new Bundle();
                bundle.putString("category", category);

                Navigation.findNavController(view).navigate(R.id.addPostFragment, bundle);
            }
        });

        TextView title = (TextView) getView().findViewById(R.id.Foro_listCategory);
        title.setText(category);

        adapter = new ArrayAdapter(getContext(), R.layout.list_item, post_list );
        ListView lv = (ListView) getView().findViewById(R.id.Foro_listPosts);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        /*Intent intent = new Intent(getContext(), PostDetailActivity.class);
        intent.putExtra("postId",  id_list.get(pos));
        startActivity(intent);*/

        Bundle bundle = new Bundle();
        bundle.putString("postId",  id_list.get(pos));

        Navigation.findNavController(view).navigate(R.id.publicationDetail, bundle);
    }

    @Override
    public void onResume() {

        super.onResume();

        post_list.clear();
        id_list.clear();

        Task<QuerySnapshot> data = db.collection("posts").whereEqualTo("category", category).get();
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
    }
}