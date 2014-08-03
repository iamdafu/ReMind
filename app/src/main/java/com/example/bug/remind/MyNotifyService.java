package com.example.bug.remind;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by bug on 15.07.14.
 */
public class MyNotifyService extends Service {
    NotificationManager nm;
    NotesDBHelper ndbh;

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        ndbh = new NotesDBHelper(new NotesDb(getApplicationContext()));
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        updateTask();
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotif() {
        Calendar c = Calendar.getInstance();
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int now = hh * 60 + mm;
        for (Note nn : ndbh.getTodayNotes()) {
            if (getTime(nn) <= now && nn.getDone() == false) {
                Notification notif = new Notification(R.drawable.ic_launcher, nn.getName(),
                        System.currentTimeMillis());
                Intent intent = new Intent(this, MainActivity.class);

                PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

                notif.setLatestEventInfo(this, nn.getName(), nn.getText(), pIntent);


                notif.flags |= Notification.FLAG_AUTO_CANCEL;

                notif.defaults = Notification.DEFAULT_VIBRATE;
                notif.sound = Uri.parse("android.resource://com.example.bug.remind/" + R.raw.sfx);
                nm.notify((int) nn.getId(), notif);
                nn.setDone(true);
                ndbh.updateNote(nn);
            }
        }

    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public int getTime(Note n) {
        String[] stime = n.getTime().split(":");
        return Integer.parseInt(stime[0]) * 60 + Integer.parseInt(stime[1]);

    }

    void updateTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                    //TODO: remove this.
                    //Log.d("Service", "CHECK");
                    sendNotif();
                }
            }
        }).start();
    }
}
