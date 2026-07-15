package com.players2.app;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FCM", "Получено сообщение: " + remoteMessage.getData());
    }

    @Override
    public void onNewToken(String token) {
        Log.d("FCM", "Новый токен: " + token);
        // Сохраняем токен локально, чтобы отправить при привязке
        getSharedPreferences("app", MODE_PRIVATE)
            .edit()
            .putString("fcm_token", token)
            .apply();
    }
}
