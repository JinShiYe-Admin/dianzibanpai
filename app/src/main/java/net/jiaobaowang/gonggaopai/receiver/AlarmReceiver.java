package net.jiaobaowang.gonggaopai.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.jiaobaowang.gonggaopai.service.UploadService;

/**
 * 广播接收器，用于定时任务的广播接收，调用service开启签到信息上传
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, UploadService.class);
        context.startService(i);
    }

}
