package com.example.mhikeapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddObservationActivity extends AppCompatActivity {

    EditText etName, etTime, etComment;
    Button btnSave;
    DatabaseHelper dbHelper;
    int hikeId = -1;
    int obsId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        dbHelper = new DatabaseHelper(this);
        hikeId = getIntent().getIntExtra("HIKE_ID", -1);

        initViews();

            if (obsId == -1) {
            String currentTime = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
            etTime.setText(currentTime);
        }

        if (getIntent().hasExtra("OBS_ID")) {
            obsId = getIntent().getIntExtra("OBS_ID", -1);
            String name = getIntent().getStringExtra("OBS_NAME");
            String time = getIntent().getStringExtra("OBS_TIME");
            String comment = getIntent().getStringExtra("OBS_COMMENT");

            etName.setText(name);
            etTime.setText(time);
            etComment.setText(comment);
            btnSave.setText("Update Observation");
        }

        btnSave.setOnClickListener(v -> {
            if (validate()) saveObservation();
        });
    }

    private void initViews() {
        etName = findViewById(R.id.etObsName);
        etTime = findViewById(R.id.etObsTime);
        etComment = findViewById(R.id.etObsComment);
        btnSave = findViewById(R.id.btnSaveObs);
    }

    private boolean validate() {
        if (etName.getText().toString().isEmpty()) {
            etName.setError("Required");
            return false;
        }
        if (etTime.getText().toString().isEmpty()) {
            etTime.setError("Required");
            return false;
        }
        return true;
    }

    private void saveObservation() {
        String name = etName.getText().toString();
        String time = etTime.getText().toString();
        String comment = etComment.getText().toString();

        if (obsId == -1) {
            if (hikeId != -1) {
                dbHelper.insertObservation(hikeId, name, time, comment);
                Toast.makeText(this, "Observation added", Toast.LENGTH_SHORT).show();
            }
        } else {
            Observation obs = new Observation(obsId, hikeId, name, time, comment);
            dbHelper.updateObservation(obs);
            Toast.makeText(this, "Observation updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}