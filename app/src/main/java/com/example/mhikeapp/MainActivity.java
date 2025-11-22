package com.example.mhikeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvEmpty;
    FloatingActionButton fabAdd;
    SearchView searchView;
    ImageButton btnFilter;

    DatabaseHelper dbHelper;
    HikeAdapter adapter;
    List<Hike> hikeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewHikes);
        tvEmpty = findViewById(R.id.tvEmpty);
        fabAdd = findViewById(R.id.fabAdd);
        searchView = findViewById(R.id.searchView);
        btnFilter = findViewById(R.id.btnFilter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddHikeActivity.class);
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Hike> filteredList = dbHelper.searchHikes(newText);
                updateRecyclerView(filteredList);
                return true;
            }
        });

        btnFilter.setOnClickListener(v -> showAdvancedSearchDialog());

        loadHikes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHikes();
        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
    }

    private void loadHikes() {
        hikeList = dbHelper.getAllHikes();
        updateRecyclerView(hikeList);
    }

    private void updateRecyclerView(List<Hike> list) {
        adapter = new HikeAdapter(this, list, dbHelper);
        recyclerView.setAdapter(adapter);

        if (list.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    private void showAdvancedSearchDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_search, null);
        EditText etLoc = dialogView.findViewById(R.id.etSearchLocation);
        EditText etDate = dialogView.findViewById(R.id.etSearchDate);
        EditText etLen = dialogView.findViewById(R.id.etSearchLength);

        new AlertDialog.Builder(this)
                .setTitle("Advanced Search")
                .setView(dialogView)
                .setPositiveButton("Search", (dialog, which) -> {
                    String location = etLoc.getText().toString().trim();
                    String date = etDate.getText().toString().trim();
                    String length = etLen.getText().toString().trim();
                    String name = searchView.getQuery().toString().trim();

                    List<Hike> results = dbHelper.searchAdvanced(name, location, length, date);
                    updateRecyclerView(results);

                    Toast.makeText(this, "Found " + results.size() + " results", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    loadHikes();
                })
                .setNeutralButton("Clear Filter", (dialog, which) -> {
                    loadHikes();
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 2, 0, "Generate Sample Data");
        menu.add(0, 1, 1, "Reset Database");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                showResetConfirmation();
                return true;
            case 2:
                generateSampleData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showResetConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Database")
                .setMessage("Warning: This will delete ALL hikes and observations. Are you sure?")
                .setPositiveButton("Delete All", (dialog, which) -> {
                    dbHelper.resetDatabase();
                    loadHikes();
                    Toast.makeText(this, "Database has been reset", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void generateSampleData() {
        // Mẫu 1: Núi cao, Khó, Có chỗ đậu xe
        dbHelper.insertHike("Everest Base Camp", "Nepal", "10/10/2025", "No", "130.0", "High", "Once in a lifetime experience.", "Freezing", "Guided Tour");

        // Mẫu 2: Rừng rậm, Trung bình, Mưa
        dbHelper.insertHike("Amazon Rainforest Trek", "Brazil", "20/11/2025", "No", "15.0", "Medium", "Watch out for insects and snakes.", "Humid & Rainy", "Research Team");

        // Mẫu 3: Sa mạc, Dễ (nhưng nóng), Có chỗ đậu xe
        dbHelper.insertHike("Sahara Camel Ride", "Morocco", "05/12/2025", "Yes", "5.0", "Low", "Sunset view over the dunes.", "Very Hot", "Family");

        // Mẫu 4: Biển, Trung bình
        dbHelper.insertHike("Great Ocean Road", "Australia", "15/01/2026", "Yes", "25.0", "Medium", "Coastal walk with amazing rock formations.", "Windy", "Friends");

        // Mẫu 5: Núi lửa (Để test tìm kiếm chữ "Volcano")
        dbHelper.insertHike("Fuji Volcano Trail", "Japan", "01/08/2026", "Yes", "12.0", "High", "Steep climb with volcanic ash.", "Clear sky", "Solo");

        // Load lại danh sách để thấy ngay kết quả
        loadHikes();
        Toast.makeText(this, "Added 5 sample hikes!", Toast.LENGTH_SHORT).show();
    }
}