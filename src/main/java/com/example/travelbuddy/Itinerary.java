package com.example.travelbuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Itinerary extends AppCompatActivity {

    TextView titlepage, endpage;
    FloatingActionButton btnAddNew;
    DatabaseReference reference;
    RecyclerView itineraryTasks;
    ArrayList<ItineraryTask> ItineraryTasks;
    ItineraryAdapter itineraryAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_itinerary);
        titlepage = findViewById(R.id.titlepage);
        endpage = findViewById(R.id.endpage);
        btnAddNew = findViewById(R.id.btnAddNew);

        btnAddNew.setOnClickListener(v -> {
            Vibrator vibe = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE) ;
            vibe.vibrate(15);
            Intent a = new Intent(Itinerary.this, ItinerarySearch.class);
            startActivity(a);
        });


        // working with data
        itineraryTasks = findViewById(R.id.itineraryTasks);
        itineraryTasks.setLayoutManager(new LinearLayoutManager(this));
        ItineraryTasks = new ArrayList<>();
        itineraryAdapter = new ItineraryAdapter(Itinerary.this, ItineraryTasks);
        itineraryTasks.setAdapter(itineraryAdapter);
//
        // get data from firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Itinerary");
        Query query = reference.orderByChild("itineraryDate");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // set code to retrieve data and replace layout
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    ItineraryTask p = dataSnapshot1.getValue(ItineraryTask.class);
                    ItineraryTasks.add(p);

                }
                itineraryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}