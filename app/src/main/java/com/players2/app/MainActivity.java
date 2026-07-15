package com.players2.app;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private SwitchCompat switchCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
Intent serviceIntent = new Intent(this, ForegroundService.class);
startService(serviceIntent);
        statusText = findViewById(R.id.statusText);
        switchCapture = findViewById(R.id.switchCapture);

        // === ЗАПРОС РАЗРЕШЕНИЙ ===
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},
                    100);
        } else {
            statusText.setText("🟢 Доступ к SMS есть");
        }

        // === ПЕРЕКЛЮЧАТЕЛЬ ===
        switchCapture.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                statusText.setText("🟢 Активен");
                Toast.makeText(this, "Перехват включён", Toast.LENGTH_SHORT).show();
            } else {
                statusText.setText("🔴 Остановлен");
                Toast.makeText(this, "Перехват выключен", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // === ОБРАБОТКА ОТВЕТА ПОЛЬЗОВАТЕЛЯ НА РАЗРЕШЕНИЯ ===
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                statusText.setText("🟢 Доступ к SMS есть");
                Toast.makeText(this, "✅ Доступ к SMS разрешён", Toast.LENGTH_SHORT).show();
            } else {
                statusText.setText("🔴 Нет доступа к SMS");
                Toast.makeText(this, "❌ Без доступа к SMS приложение не будет работать", Toast.LENGTH_LONG).show();
            }
        }
    }
}
