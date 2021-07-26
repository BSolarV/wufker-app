package com.messirvedevs.wufker;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.messirvedevs.wufker.adapters.AnswerListAdapter;
import com.messirvedevs.wufker.databinding.ActivityForoBinding;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private List<Answer> ans_list = new ArrayList();

    private AnswerListAdapter adapter;

    private String id,category;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        id = getArguments().getString("postId");

        adapter = new AnswerListAdapter(getContext(), ans_list );
        ListView InForoAnswers = (ListView) getView().findViewById(R.id.InForoAnswers);

        InForoAnswers.setAdapter(adapter);


    }
    @Override
    public void onResume() {
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
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override public int compare(DocumentSnapshot u1, DocumentSnapshot u2) {
                        if( Objects.isNull( u1.get("datetime", Timestamp.class) ) || Objects.isNull( u2.get("datetime", Timestamp.class) )  )
                            return (u1.get("datetime", Timestamp.class)).compareTo( u2.get("datetime", Timestamp.class) );
                        else return 0;
                    } });
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