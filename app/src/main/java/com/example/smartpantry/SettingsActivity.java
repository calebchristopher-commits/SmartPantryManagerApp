package com.example.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantry.database.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {
    private static final String PREFS = "smart_pantry_settings";
    private static final String EXPIRY_ALERTS = "expiry_alerts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch expirySwitch = findViewById(R.id.switchExpiryAlerts);
        Button clearButton = findViewById(R.id.btnClearPantry);
        TextView versionText = findViewById(R.id.txtVersion);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        expirySwitch.setChecked(prefs.getBoolean(EXPIRY_ALERTS, true));
        expirySwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(EXPIRY_ALERTS, isChecked).apply());

        clearButton.setOnClickListener(v -> new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Clear pantry?")
                .setMessage("This removes all ingredients currently stored in your pantry.")
                .setPositiveButton("Clear", (dialog, which) -> {
                    DatabaseHelper db = new DatabaseHelper(this);
                    db.clearPantry();
                    Toast.makeText(this, "Pantry cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show());

        versionText.setText("Smart Pantry Manager\nVersion 1.0\nBuilt with Java, Android Studio and SQLite");
    }
}
