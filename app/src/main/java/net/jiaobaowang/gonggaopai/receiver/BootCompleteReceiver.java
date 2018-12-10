package net.jiaobaowang.gonggaopai.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import net.jiaobaowang.gonggaopai.main.MainActivity;


public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"6666666666666666666",Toast.LENGTH_SHORT).show();
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent thisIntent = new Intent(context, MainActivity.class);
//            thisIntent.setAction("android.intent.action.MAIN");
//            thisIntent.addCategory("android.intent.category.LAUNCHER");
            thisIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(thisIntent);
        }
    }

}