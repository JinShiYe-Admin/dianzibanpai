package net.jiaobaowang.gonggaopai.updateaddress;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.jiaobaowang.gonggaopai.R;
import net.jiaobaowang.gonggaopai.base.BaseActivity;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.Validate;

public class AppUpdateAddressActivity  extends BaseActivity {

    private EditText editAppUpdate;
    private Button button_backward,cancelAppUpdate,confirmAppUpdate;
    String appUpdateAddress="";
    @Override
    public int initLayout() {
        return R.layout.activity_app_update_address;
    }

    @Override
    public void widgetHandle(Message msg) {

    }

    @Override
    public void initParms(Bundle bundle) {

    }

    @Override
    public void initQtData() {

    }


    @Override
    public void doBusiness(Context mContext) {
        editAppUpdate= (EditText) findViewById(R.id.editAppAddress);
        button_backward=(Button)findViewById(R.id.button_backward);
        cancelAppUpdate=(Button)findViewById(R.id.cancelApp);
        confirmAppUpdate=(Button)findViewById(R.id.confirmApp);
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        appUpdateAddress = sp.getString(Const.updateaddress,Const.updateUrl);
        editAppUpdate.setText(appUpdateAddress);
        button_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancelAppUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirmAppUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdateAddress=editAppUpdate.getText().toString().trim();
                if(Validate.isNull(appUpdateAddress)){
                    Toast.makeText(cont,"请输入APP更新地址",Toast.LENGTH_LONG).show();
                    return ;
                }
                if(!orHtmlAddress(appUpdateAddress)){
                    Toast.makeText(cont,"网页APP更新输入错误",Toast.LENGTH_LONG).show();
                    return ;
                }
                Const.updateUrl=appUpdateAddress;
                SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Const.updateaddress, Const.updateUrl);
                editor.commit();
                Toast.makeText(cont,"设置成功！",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 网址正则判断
     * @param htmlAddress
     * @return
     */
    public static boolean orHtmlAddress(String htmlAddress) {
        if (htmlAddress == null || "".equals(htmlAddress))
            return false;
        String regex = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&amp;%\\$#\\=~_\\-@]*)*$";
        return htmlAddress.matches(regex);
    }

    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
        return false;
    }

}
