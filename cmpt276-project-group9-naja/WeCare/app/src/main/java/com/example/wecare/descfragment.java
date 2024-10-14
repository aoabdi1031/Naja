package com.example.wecare;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class descfragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String name, invCount;
    private DatabaseReference mDatabase;

    public descfragment() {
        // Required empty public constructor
    }

    public descfragment(String name, String invCount) {
        this.name = name;
        this.invCount = invCount;
    }


    public static descfragment newInstance(String param1, String param2) {
        descfragment fragment = new descfragment();
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
        View view = inflater.inflate(R.layout.fragment_descfragment, container, false);

        String strEmail = getArguments().getString("email");

        Button addButton = view.findViewById(R.id.buttonAdd);
        Button subButton = view.findViewById(R.id.buttonSub);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Database Entry
                String strEmail = getArguments().getString("email");
                Toast.makeText(getContext(), "Added 1 tablet to Inventory Count", Toast.LENGTH_SHORT).show();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(strEmail).child("medicationList");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer count = 1;
                        Medicine med = new Medicine(name, invCount);
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Medicine currentMed = snap.getValue(Medicine.class);
                            boolean state = med.isEqual(currentMed);
                            if (state) {
                                Integer num = Integer.valueOf(invCount);
                                num++;
                                invCount = String.valueOf(num);
                                mDatabase.child(String.valueOf(count)).child("invCount").setValue(invCount);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    getFragmentManager().beginTransaction().detach(descfragment.this).commitNow();
                                    getFragmentManager().beginTransaction().attach(descfragment.this).commitNow();
                                } else {
                                    getFragmentManager().beginTransaction().detach(descfragment.this).attach(descfragment.this).commit();
                                }
                            }
                            count++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Database Entry
                String strEmail = getArguments().getString("email");
                Toast.makeText(getContext(), "Removed 1 tablet from Inventory Count", Toast.LENGTH_SHORT).show();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(strEmail).child("medicationList");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer count = 1;
                        Medicine med = new Medicine(name, invCount);
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Medicine currentMed = snap.getValue(Medicine.class);
                            boolean state = med.isEqual(currentMed);
                            if (state) {
                                Integer num = Integer.valueOf(invCount);
                                num--;
                                invCount = String.valueOf(num);
                                mDatabase.child(String.valueOf(count)).child("invCount").setValue(invCount);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    getFragmentManager().beginTransaction().detach(descfragment.this).commitNow();
                                    getFragmentManager().beginTransaction().attach(descfragment.this).commitNow();
                                } else {
                                    getFragmentManager().beginTransaction().detach(descfragment.this).attach(descfragment.this).commit();
                                }
                            }
                            count++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        TextView nameholder = view.findViewById(R.id.nameholder);
        TextView invholder = view.findViewById(R.id.invholder);

        nameholder.setText(name);
        invholder.setText("Tablets Remaining: " + invCount);

        return view;
    }


    public void onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new recfragment()).addToBackStack(null).commit();
    }
}