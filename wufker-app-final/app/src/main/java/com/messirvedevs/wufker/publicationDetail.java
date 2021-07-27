package com.messirvedevs.wufker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.messirvedevs.wufker.adapters.AnswerListAdapter;
import com.messirvedevs.wufker.databinding.ActivityForoBinding;
import com.messirvedevs.wufker.objects.Answer;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link publicationDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class publicationDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //windu
    private ActivityForoBinding binding;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Answer> answers;
    private TextView inForoTitle,InForoUser,InForoQuestion;
    private LinearLayout card_menu;

    private List<Answer> ans_list = new ArrayList();

    private AnswerListAdapter adapter;

    private String id,category,titulo,question,user;

    public static final String SHARED_PREFS = "USER_DATA_WUFKER";
    public static final String EMAIL = "EMAIL";

    public publicationDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment publicationDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static publicationDetail newInstance(String param1, String param2) {
        publicationDetail fragment = new publicationDetail();
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
        id = getArguments().getString("postId");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publication_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // set user an question
        inForoTitle = (TextView) getView().findViewById(R.id.inForoTitle);
        InForoQuestion = (TextView) getView().findViewById(R.id.InForoQuestion);
        InForoUser = (TextView) getView().findViewById(R.id.InForoUser);




        card_menu = (LinearLayout) getView().findViewById(R.id.cardFor_contextMenu);


        FloatingActionButton btn =  getView().findViewById(R.id.fab2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //Intent ii = new Intent(view.getContext(), ResponderPostActivity.class);
                //ii.putExtra("category",  );

                category = getArguments().getString("category");
                Bundle bundle = new Bundle();

                bundle.putString("id_post", id);
                bundle.putString("category" , category);
                bundle.putString("postTitle", inForoTitle.getText().toString());
                bundle.putString("postContent", InForoQuestion.getText().toString());

                Navigation.findNavController(view).navigate(R.id.answerPublicationFragment, bundle);

                //startActivity(ii);

            }
        });


        adapter = new AnswerListAdapter(getContext(), ans_list );
        ListView InForoAnswers = (ListView) getView().findViewById(R.id.InForoAnswers);

        InForoAnswers.setAdapter(adapter);
        Task<DocumentSnapshot> data = db.collection("posts").document(id).get();
        data.addOnSuccessListener(command -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            if (command.get("authorEmail").toString().equals(sharedPreferences.getString(EMAIL, ""))) {
                registerForContextMenu(card_menu);
            }
        });


    }

    @Override
    public void onCreateContextMenu(@NonNull @NotNull ContextMenu menu, @NonNull @NotNull View v, @Nullable @org.jetbrains.annotations.Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Opciones");
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editar:
                //Toast.makeText(getContext(), "Editar", Toast.LENGTH_SHORT).show();
                Task<DocumentSnapshot> data = db.collection("posts").document(id).get();
                data.addOnSuccessListener(command -> {
                    titulo = command.get("title").toString();
                    question = command.get("content").toString();
                    user = command.get("authorEmail").toString();

                    Bundle bundle = new Bundle();
                    bundle.putString("id_post", id);
                    bundle.putString("category", category);
                    bundle.putString("titulo", titulo);
                    bundle.putString("question", question);
                    bundle.putString("user", user);

                    Navigation.findNavController(getView()).navigate(R.id.editPublication, bundle);
                });



                return true;
            case R.id.eliminar:
                //Toast.makeText(getContext(), "Eliminar", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("¿Estás seguro?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                                //Eliminar publicación y saltar atrás
                                db.collection("posts").document(id).delete();
                                //Toast.makeText(getContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                                Navigation.findNavController(getView()).popBackStack();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                });
                AlertDialog alerta = alert.create();
                alerta.setTitle("Eliminar Publicación");
                alerta.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }


    }
    @Override
    public void onResume() {
        super.onResume();


        ans_list.clear();

        // Get post and answers from database

        Task<DocumentSnapshot> data = db.collection("posts").document(id).get();
        data.addOnSuccessListener(command -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            inForoTitle.setText(command.get("title").toString());
            InForoQuestion.setText(command.get("content").toString());
            InForoUser.setText(command.get("authorEmail").toString());
            if (command.get("authorEmail").toString() == sharedPreferences.getString(EMAIL, "")) {
                registerForContextMenu(card_menu);
            }
            Task<QuerySnapshot> answersQuery = db.collection("answers").whereEqualTo("postId", id).get();
            answersQuery.addOnSuccessListener(content -> {
                List<DocumentSnapshot> docList = content.getDocuments();
                Collections.sort(docList, new Comparator<DocumentSnapshot>() {
                    @Override
                    public int compare(DocumentSnapshot u1, DocumentSnapshot u2) {
                        try{
                            return u1.get("datetime", Date.class).compareTo(u2.get("datetime", Date.class));
                        }catch (Exception e){
                            return 0;
                        }
                    }
                });
                if (answersQuery.isComplete()) {
                    int i = 0;
                    while (i < docList.size()) {
                        DocumentSnapshot doc = docList.get(i);
                        Answer ans = doc.toObject(Answer.class);
                        ans.setId( doc.getId() );
                        ans_list.add( ans );
                        i++;

                    }
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }
}