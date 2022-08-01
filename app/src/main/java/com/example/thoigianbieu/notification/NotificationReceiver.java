package com.example.thoigianbieu.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.SuKienActivity;
import com.example.thoigianbieu.database.sukien.SuKien;
import com.example.thoigianbieu.MyApplication;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("package");
        SuKien suKien = (SuKien) bundle.getSerializable("sukien");
        sendNotification(suKien, context);
    }

    public void sendNotification(SuKien suKien, Context context){
        Intent intent = new Intent(context, SuKienActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sukien", suKien);
        intent.putExtra("package", bundle);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(suKien.getId(), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MyApplication.CHANNEL_SUKIEN);
        builder.setContentTitle(suKien.getTieuDe())
                .setContentText(suKien.getNoiDung())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(suKien.getNoiDung()))
                .setSmallIcon(R.drawable.ic_sukien)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(suKien.getId(), notification);
    }
}
