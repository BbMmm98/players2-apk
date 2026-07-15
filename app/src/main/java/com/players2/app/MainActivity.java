package com.players2.app;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private SwitchCompat switchCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.statusText);
        switchCapture = findViewById(R.id.switchCapture);

        switchCapture.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                statusText.setText("🟢 Активен");
                // Включить перехват
            } else {
                statusText.setText("🔴 Остановлен");
                // Выключить перехват
            }
        });
    }
}
