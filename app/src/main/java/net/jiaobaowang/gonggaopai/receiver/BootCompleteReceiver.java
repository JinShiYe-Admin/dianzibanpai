package net.jiaobaowang.gonggaopai.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.jiaobaowang.gonggaopai.main.MainActivity;


public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent thisIntent = new Intent(context, MainActivity.class);
            thisIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(thisIntent);
        }
    }

}