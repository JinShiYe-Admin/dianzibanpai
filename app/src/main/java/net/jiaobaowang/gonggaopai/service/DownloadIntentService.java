package net.jiaobaowang.gonggaopai.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadIntentService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;
    public static final int UPDATE_FALSE = 83456;
    public DownloadIntentService() {
        super("DownloadIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String urlToDownload = intent.getStringExtra("url");
        ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
        HttpURLConnection connection ;
        try {
            URL url = new URL(urlToDownload);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int fileLength = connection.getContentLength();
            Log.d("test","fileLength:"+fileLength);
            String sdpath = Environment.getExternalStorageDirectory() + "/Download";
            File file = new File(sdpath);
            if (!file.exists()) {
                file.mkdir();
            }
            InputStream input = connection.getInputStream();
            OutputStream output = new FileOutputStream(sdpath+"/"+"dianzibanpai.apk");
            byte data[] = new byte[2048];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                Bundle resultData = new Bundle();
                resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                receiver.send(UPDATE_PROGRESS, resultData);
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            Bundle resultData = new Bundle();
            resultData.putInt("progress" ,-1);
            receiver.send(UPDATE_FALSE, resultData);
            e.printStackTrace();
        }

    }
}
