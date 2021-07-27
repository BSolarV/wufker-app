package com.messirvedevs.wufker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.messirvedevs.wufker.objects.ForoPost;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link publicationSelector#newInstance} factory method to
 * create an instance of this fragment.
 */
public class publicationSelector extends ListFragment implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

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

        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    /*@Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {

        MenuItem search = (MenuItem) menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search Something!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }*/

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
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Buscar");

        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

        Bundle bundle = new Bundle();
        bundle.putString("postId",  id_list.get(pos));
        bundle.putString("category", category);

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

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }


}