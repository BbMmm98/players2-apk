package com.players2.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private EditText tokenInput;
    private Button connectButton;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenInput = findViewById(R.id.tokenInput);
        connectButton = findViewById(R.id.connectButton);
        statusText = findViewById(R.id.statusText);

        // Запуск фонового сервиса
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        startService(serviceIntent);

        // Запрос разрешений
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},
                    100);
        }

        // === ПРИВЯЗКА УСТРОЙСТВА ===
        connectButton.setOnClickListener(v -> {
            String token = tokenInput.getText().toString().trim();
            if (token.isEmpty()) {
                Toast.makeText(this, "Введите токен", Toast.LENGTH_SHORT).show();
                return;
            }

            statusText.setText("🔄 Привязка...");
            // Здесь будет отправка на сервер
            // Пока просто имитация
            Toast.makeText(this, "✅ Токен принят: " + token, Toast.LENGTH_SHORT).show();
            statusText.setText("✅ Устройство привязано");
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                statusText.setText("✅ Доступ к SMS есть");
            } else {
                statusText.setText("❌ Нет доступа к SMS");
            }
        }
    }
}
