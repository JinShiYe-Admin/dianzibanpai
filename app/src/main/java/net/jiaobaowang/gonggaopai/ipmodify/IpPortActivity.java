package net.jiaobaowang.gonggaopai.ipmodify;

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

public class IpPortActivity extends BaseActivity {

    private EditText edit_ip;
    private EditText edit_port;
    private Button button_backward,cancelSocket,confirmSocket;
    String ip="";
    int port=0;
    @Override
    public int initLayout() {
        return R.layout.activity_ip_port;
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
        edit_ip= (EditText) findViewById(R.id.edit_ip);
        edit_port= (EditText) findViewById(R.id.duankouhao);
        button_backward=(Button)findViewById(R.id.button_backward);
        cancelSocket=(Button)findViewById(R.id.cancelSocket);
        confirmSocket=(Button)findViewById(R.id.confirmSocket);
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        ip = sp.getString(Const.socketip, Const.socketIp);
        port = sp.getInt(Const.socketport, Const.socketPort);
        edit_ip.setText(ip);
        edit_port.setText(String.valueOf(port));
        button_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancelSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirmSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip=edit_ip.getText().toString().trim();
                String iport=edit_port.getText().toString().trim();
                if(Validate.isNull(ip)){
                    Toast.makeText(cont,"请输入 IP 地址",Toast.LENGTH_LONG).show();
                    return ;
                }
                if(Validate.isNull(iport)){
                    Toast.makeText(cont,"请输入端口号",Toast.LENGTH_LONG).show();
                    return ;
                }
                if(!orIp(ip)){
                    Toast.makeText(cont,"IP 地址输入错误",Toast.LENGTH_LONG).show();
                    return ;
                }

                try {
                    port=Integer.parseInt(iport);
                }catch (Exception e){
                    Toast.makeText(cont,"端口号设置不正确",Toast.LENGTH_LONG).show();
                    return ;
                }
                Const.socketIp=ip;
                Const.socketPort=port;
                SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Const.socketip, ip);
                editor.putInt(Const.socketport, port);
                editor.commit();
                Toast.makeText(cont,"设置成功！",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
        return false;
    }

    /**
     * ip地址正则判断
     * @param ip
     * @return
     */
    public static boolean orIp(String ip) {
        if (ip == null || "".equals(ip))
            return false;
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        return ip.matches(regex);
    }
}
