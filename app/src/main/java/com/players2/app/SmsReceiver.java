package com.players2.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                    String message = sms.getMessageBody();
                    String sender = sms.getOriginatingAddress();

                    Log.d("SMS", "От: " + sender + " Сообщение: " + message);

                    // Показываем уведомление на экране
                    Toast.makeText(context, "📩 SMS от " + sender + ": " + message, Toast.LENGTH_LONG).show();

                    // Здесь будет отправка на сервер
                }
            }
        }
    }
}
