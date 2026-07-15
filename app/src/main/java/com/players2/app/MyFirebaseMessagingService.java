package com.players2.app;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FCM", "Получено сообщение: " + remoteMessage.getData());
        // Тут будет логика: прочитать SMS и отправить подтверждение
    }

    @Override
    public void onNewToken(String token) {
        Log.d("FCM", "Новый токен: " + token);
        // Отправить токен на сервер
    }
}
