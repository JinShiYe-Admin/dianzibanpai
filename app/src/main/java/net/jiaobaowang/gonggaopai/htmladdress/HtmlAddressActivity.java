package net.jiaobaowang.gonggaopai.htmladdress;

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

public class HtmlAddressActivity extends BaseActivity {

    private EditText editHtmlAddress;
    private Button button_backward,cancelHtml,confirmHtml;
    String htmlAddress="";
    @Override
    public int initLayout() {
        return R.layout.activity_html_address;
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
        editHtmlAddress= (EditText) findViewById(R.id.editHtmlAddress);
        button_backward=(Button)findViewById(R.id.button_backward);
        cancelHtml=(Button)findViewById(R.id.cancelHtml);
        confirmHtml=(Button)findViewById(R.id.confirmHtml);
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        htmlAddress = sp.getString(Const.htmladdress, Const.baseUrl);
        editHtmlAddress.setText(htmlAddress);
        button_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancelHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirmHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                htmlAddress=editHtmlAddress.getText().toString().trim();
                if(Validate.isNull(htmlAddress)){
                    Toast.makeText(cont,"请输入网页地址",Toast.LENGTH_LONG).show();
                    return ;
                }
                if(!orHtmlAddress(htmlAddress)){
                    Toast.makeText(cont,"网页地址输入错误",Toast.LENGTH_LONG).show();
                    return ;
                }
                Const.baseUrl=htmlAddress;
                SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Const.htmladdress, Const.baseUrl);
                editor.putBoolean(Const.reload, true);
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
