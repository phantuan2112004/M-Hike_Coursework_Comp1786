package com.example.mhikeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HikeDetailActivity extends AppCompatActivity {
    TextView tvName, tvLocation, tvDate, tvParking, tvLength, tvDifficulty, tvDesc, tvWeather, tvCompanions;
    TextView tvEmptyObs;
    Button btnEdit, btnAddObs;
    RecyclerView recyclerViewObs;

    DatabaseHelper dbHelper;
    int hikeId;
    ObservationAdapter adapter;
    List<Observation> observationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        dbHelper = new DatabaseHelper(this);
        hikeId = getIntent().getIntExtra("HIKE_ID", -1);

        initViews();

        recyclerViewObs.setLayoutManager(new LinearLayoutManager(this));

        loadHikeDetails();

        loadObservations();

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(HikeDetailActivity.this, AddHikeActivity.class);
            intent.putExtra("HIKE_ID", hikeId);
            startActivity(intent);
        });

        btnAddObs.setOnClickListener(v -> {
            Intent intent = new Intent(HikeDetailActivity.this, AddObservationActivity.class);
            intent.putExtra("HIKE_ID", hikeId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHikeDetails();
        loadObservations();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvDetailName);
        tvLocation = findViewById(R.id.tvDetailLocation);
        tvDate = findViewById(R.id.tvDetailDate);
        tvParking = findViewById(R.id.tvDetailParking);
        tvLength = findViewById(R.id.tvDetailLength);
        tvDifficulty = findViewById(R.id.tvDetailDifficulty);
        tvDesc = findViewById(R.id.tvDetailDesc);
        tvWeather = findViewById(R.id.tvDetailWeather);
        tvCompanions = findViewById(R.id.tvDetailCompanions);

        btnEdit = findViewById(R.id.btnEditHike);
        btnAddObs = findViewById(R.id.btnAddObservation);
        recyclerViewObs = findViewById(R.id.recyclerObservations);
        tvEmptyObs = findViewById(R.id.tvEmptyObs);
    }

    private void loadHikeDetails() {
        Hike hike = dbHelper.getHike(hikeId);
        if (hike != null) {
            tvName.setText(hike.getName());
            tvLocation.setText("Location: " + hike.getLocation());
            tvDate.setText("Date: " + hike.getDate());
            tvParking.setText("Parking: " + hike.getParking());
            tvLength.setText("Length: " + hike.getLength());
            tvDifficulty.setText("Difficulty: " + hike.getDifficulty());
            tvDesc.setText(hike.getDescription());
            tvWeather.setText("Weather: " + hike.getWeather());
            tvCompanions.setText("Companions: " + hike.getCompanions());
        }
    }

    private void loadObservations() {
        observationList = dbHelper.getObservationsByHikeId(hikeId);
        adapter = new ObservationAdapter(this, observationList, dbHelper);
        recyclerViewObs.setAdapter(adapter);

        if (observationList.isEmpty()) {
            tvEmptyObs.setVisibility(View.VISIBLE);
        } else {
            tvEmptyObs.setVisibility(View.GONE);
        }
    }
}