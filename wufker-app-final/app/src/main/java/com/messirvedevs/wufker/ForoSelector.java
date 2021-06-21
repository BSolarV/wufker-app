package com.messirvedevs.wufker;

import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForoSelector#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForoSelector extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ForoSelector() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForoSelector.
     */
    // TODO: Rename and change types and number of parameters
    public static ForoSelector newInstance(String param1, String param2) {
        ForoSelector fragment = new ForoSelector();
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

        return inflater.inflate(R.layout.fragment_foro_selector, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Foro Selector
        TextView enf =  (TextView) getView().findViewById(R.id.CategoriasEnfemedades);
        TextView cons =  (TextView) getView().findViewById(R.id.CategoriasConsejos);
        TextView comp =  (TextView) getView().findViewById(R.id.CategoriasComportamiento);
        TextView  ali =  (TextView) getView().findViewById(R.id.CategoriasAlimentacion);
        TextView misc =  (TextView) getView().findViewById(R.id.CategoriasMiscelaneos);
        Button mapButton =  (Button) getView().findViewById(R.id.mapButton);

        enf.setOnClickListener((View.OnClickListener) this);
        cons.setOnClickListener((View.OnClickListener) this);
        comp.setOnClickListener((View.OnClickListener) this);
        ali.setOnClickListener((View.OnClickListener) this);
        misc.setOnClickListener((View.OnClickListener) this);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.googleMap);
                //startActivity(mapa);
            }
        });
    }
    @Override
    public void onClick(View v) {

        int i = v.getId();
        TextView ans = (TextView) getView().findViewById(i);
        String selected = ans.getText().toString();

        /*Intent foro = new Intent(this, ForoDetailActivity.class);
        foro.putExtra("category", selected);*/

        Bundle bundle = new Bundle();
        bundle.putString("selected" ,selected);


        Navigation.findNavController(v).navigate(R.id.publicationSelector, bundle);
        //startActivity(foro);
    }
}