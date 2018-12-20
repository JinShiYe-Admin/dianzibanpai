package net.jiaobaowang.gonggaopai.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.serialport.SerialPort;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import net.jiaobaowang.gonggaopai.base.BaseActivityManager;
import net.jiaobaowang.gonggaopai.main.MainActivity;
import net.jiaobaowang.gonggaopai.util.BitConverter;
import net.jiaobaowang.gonggaopai.util.Const;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class ReaderService extends Service {

    FileInputStream mInputStream;
    SerialPort sp;
    ReadThread mReadThread;
    //        StringBuffer strbuffer;
    Handler mhandler;
    byte[] bytes = new byte[64];
    int length = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("ReaderService 已启动");
//        strbuffer=new StringBuffer();
        mhandler = new Handler();
        if (Const.DEBUG) {
            showToast(getApplicationContext(), "ReaderService 已启动");
        }
        try {
            sp = new SerialPort(new File("/dev/ttyS3"), 9600);
        } catch (SecurityException e) {
            if (Const.DEBUG) {
                showToast(getApplicationContext(), "无法获取串口权限 或者串口不存在");
            }
            e.printStackTrace();
        } catch (IOException e) {
            if (Const.DEBUG) {
                showToast(getApplicationContext(), "串口打开失败 IOException");
            }
            e.printStackTrace();
        }

        mReadThread = new ReadThread();
        mReadThread.start();

        try {
            mInputStream = (FileInputStream) sp.getInputStream();
        } catch (NullPointerException e) {
            if (Const.DEBUG) {
                showToast(getApplicationContext(), "SerialPort 空指针");
            }
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) {
                        return;
                    }
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    if (Const.DEBUG) {
                        showToast(getApplicationContext(), "流获取异常");
                    }
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    void onDataReceived(byte[] buffer, int size) {
        System.arraycopy(buffer, 0, bytes, length, size);
        length = length + size;
        for (int i = 0; i < size; i++) {
            byte b = buffer[i];
            if (b == 3) {
//                strbuffer.append(b);
                endBuffer();
            }
//            strbuffer.append(b).append(",");
        }
    }


    private void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    private void endBuffer() {
        final byte[] test = new byte[64];
        System.arraycopy(bytes, 0, test, 0, bytes.length);
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                if (Const.DEBUG) {
                    showToast(getApplicationContext(), "cardId:" + Arrays.toString(test));
                }
            }
        });
        if (test[2] == 0x01) {//IC
            byte[] bytesIC = new byte[4];
            System.arraycopy(test, 3, bytesIC, 0, 4);
            final String cardId = BitConverter.bytesToHexString(bytesIC);
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getBaseContext());
            System.out.println(BaseActivityManager.getAppManager().currentActivity());
            System.out.println(BaseActivityManager.getAppManager().currentActivity() instanceof MainActivity);
            if(BaseActivityManager.getAppManager().currentActivity() instanceof MainActivity){
                Intent cardIntent = new Intent();
                cardIntent.setAction(Const.ACTION_NAME);
                cardIntent.putExtra("cardId", cardId);
                manager.sendBroadcast(cardIntent);
            }
        } else if (test[2] == 0x02) {//ID
            byte[] bytesID = new byte[4];
            System.arraycopy(test, 4, bytesID, 0, 4);
            final String cardId2 = BitConverter.bytesToHexString(bytesID);
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getBaseContext());
            if(BaseActivityManager.getAppManager().currentActivity() instanceof MainActivity){
                Intent cardIntent = new Intent();
                cardIntent.setAction(Const.ACTION_NAME);
                cardIntent.putExtra("cardId", cardId2);
                manager.sendBroadcast(cardIntent);
            }
        }
//                sttb.setLength(0);
        bytes = new byte[64];
        length = 0;
    }

    @Override
    public void onDestroy() {
        System.out.println("service已关闭");
        super.onDestroy();
    }
}
