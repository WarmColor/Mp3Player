package net.warmcolor.blog.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import net.warmcolor.blog.model.Mp3Info;

public class DownloadService extends Service {

    public IBinder onBind(Intent intent) {
        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
        return super.onStartCommand(intent, flags, startId);
    }
}
