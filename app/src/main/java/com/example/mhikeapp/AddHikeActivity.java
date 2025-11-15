package com.example.mhikeapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddHikeActivity extends AppCompatActivity {

    EditText etName, etLocation, etDate, etLength, etDesc, etWeather, etCompanions;
    RadioGroup radioGroupParking;
    RadioButton radioYes, radioNo;
    Spinner spinnerDifficulty;
    Button btnSave;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        dbHelper = new DatabaseHelper(this);

        initViews();

        setupSpinner();

        etDate.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                showConfirmationDialog();
            }
        });
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);
        etDate = findViewById(R.id.etDate);
        etLength = findViewById(R.id.etLength);
        etDesc = findViewById(R.id.etDescription);
        etWeather = findViewById(R.id.etWeather);
        etCompanions = findViewById(R.id.etCompanions);

        radioGroupParking = findViewById(R.id.radioGroupParking);
        radioYes = findViewById(R.id.radioYes);
        radioNo = findViewById(R.id.radioNo);

        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        btnSave = findViewById(R.id.btnSaveHike);
    }

    private void setupSpinner() {
        String[] levels = {"High", "Medium", "Low"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    // Month bắt đầu từ 0 nên cần +1
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    etDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private boolean validateInput() {
        if (etName.getText().toString().isEmpty()) {
            etName.setError("Name is required");
            return false;
        }
        if (etLocation.getText().toString().isEmpty()) {
            etLocation.setError("Location is required");
            return false;
        }
        if (etDate.getText().toString().isEmpty()) {
            etDate.setError("Date is required");
            return false;
        }
        if (etLength.getText().toString().isEmpty()) {
            etLength.setError("Length is required");
            return false;
        }
        // Kiểm tra RadioButton Parking
        if (radioGroupParking.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select Parking availability", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showConfirmationDialog() {
        String name = etName.getText().toString();
        String location = etLocation.getText().toString();
        String date = etDate.getText().toString();
        String length = etLength.getText().toString();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();

        String parking = radioYes.isChecked() ? "Yes" : "No";

        String message = "Name: " + name + "\n" +
                "Location: " + location + "\n" +
                "Date: " + date + "\n" +
                "Parking: " + parking + "\n" +
                "Length: " + length + "\n" +
                "Difficulty: " + difficulty;

        new AlertDialog.Builder(this)
                .setTitle("Confirm Details")
                .setMessage(message)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    saveHikeToDatabase(name, location, date, parking, length, difficulty);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveHikeToDatabase(String name, String location, String date, String parking, String length, String difficulty) {
        String desc = etDesc.getText().toString();
        String weather = etWeather.getText().toString();
        String companions = etCompanions.getText().toString();

        long id = dbHelper.insertHike(name, location, date, parking, length, difficulty, desc, weather, companions);

        if (id > 0) {
            Toast.makeText(this, "Hike saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save hike", Toast.LENGTH_SHORT).show();
        }
    }
}