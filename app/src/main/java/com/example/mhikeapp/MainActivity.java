package com.example.mhikeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvEmpty;
    FloatingActionButton fabAdd;

    DatabaseHelper dbHelper;
    HikeAdapter adapter;
    List<Hike> hikeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewHikes);
        tvEmpty = findViewById(R.id.tvEmpty);
        fabAdd = findViewById(R.id.fabAdd);

        dbHelper = new DatabaseHelper(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddHikeActivity.class);
            startActivity(intent);
        });

        loadHikes();
    }

    private void loadHikes() {
        hikeList = dbHelper.getAllHikes();
        adapter = new HikeAdapter(this, hikeList, dbHelper);
        recyclerView.setAdapter(adapter);

        if (hikeList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHikes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Reset Database");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            showResetConfirmation();
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
                    loadHikes(); // Load lại list (sẽ trống trơn)
                    Toast.makeText(this, "Database has been reset", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}