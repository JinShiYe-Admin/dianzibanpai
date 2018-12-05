package net.jiaobaowang.gonggaopai.util;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2018/12/3.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */

public class ReceiverAndServiceUtil {

    public static boolean isServiceRunning(Context context, String serviceName){
        // 校验服务是否还存在
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : services) {
            String name = info.service.getClassName();
            if (serviceName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRegister(LocalBroadcastManager manager, String action) {
        boolean isRegister = false;
        try {
            Field mReceiversField = manager.getClass().getDeclaredField("mReceivers");
            mReceiversField.setAccessible(true);
//            String name = mReceiversField.getName();
            HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = (HashMap<BroadcastReceiver, ArrayList<IntentFilter>>) mReceiversField.get(manager);

            for (BroadcastReceiver key : mReceivers.keySet()) {
                ArrayList<IntentFilter> intentFilters = mReceivers.get(key);
                for (int i = 0; i < intentFilters.size(); i++) {
                    IntentFilter intentFilter = intentFilters.get(i);
                    Field mActionsField = intentFilter.getClass().getDeclaredField("mActions");
                    mActionsField.setAccessible(true);
                    ArrayList<String> mActions = (ArrayList<String>) mActionsField.get(intentFilter);
                    for (int j = 0; j < mActions.size(); j++) {
                        if (mActions.get(i).equals(action)) {
                            isRegister = true;
                            break;
                        }
                    }
                }
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return isRegister;
    }
}
