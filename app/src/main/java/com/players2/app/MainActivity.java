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

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

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

            String fcmToken = getSharedPreferences("app", MODE_PRIVATE)
                    .getString("fcm_token", null);

            if (fcmToken == null) {
                statusText.setText("❌ Токен FCM не получен");
                Toast.makeText(this, "Подождите 10 секунд, FCM ещё не готов", Toast.LENGTH_LONG).show();
                return;
            }

            statusText.setText("🔄 Привязка...");
            sendTokenToServer(token, fcmToken);
        });
    }

    private void sendTokenToServer(String token, String fcmToken) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("token", token);
            json.put("fcm_token", fcmToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            json.toString()
        );

        Request request = new Request.Builder()
                .url("https://players2pay.com/api/device/register")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    statusText.setText("❌ Ошибка: " + e.getMessage());
                    Toast.makeText(MainActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        statusText.setText("✅ Устройство привязано");
                        Toast.makeText(MainActivity.this, "Привязка успешна!", Toast.LENGTH_SHORT).show();
                    } else {
                        statusText.setText("❌ Ошибка: " + response.code());
                        Toast.makeText(MainActivity.this, "Ошибка привязки", Toast.LENGTH_SHORT).show();
                    }
                });
            }
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
