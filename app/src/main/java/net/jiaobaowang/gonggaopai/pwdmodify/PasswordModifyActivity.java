package net.jiaobaowang.gonggaopai.pwdmodify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.jiaobaowang.gonggaopai.R;
import net.jiaobaowang.gonggaopai.base.BaseActivity;
import net.jiaobaowang.gonggaopai.classes.KeyboardAdapterClasses;
import net.jiaobaowang.gonggaopai.classes.KeyboardViewClasses;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.Validate;

import java.lang.reflect.Method;
import java.util.List;

public class PasswordModifyActivity extends BaseActivity implements KeyboardAdapterClasses.OnKeyboardClickListener{

    private EditText edit_password;
    private KeyboardViewClasses keyboardView;
    private List<String> datas;
    private Button button_backward;
    String password="";

    @Override
    public int initLayout() {
        return R.layout.activity_password_modify;
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
        edit_password= (EditText) findViewById(R.id.edit_password);
        button_backward=(Button)findViewById(R.id.button_backward);
        keyboardView= (KeyboardViewClasses) findViewById(R.id.keyboard_view_classes);
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        String password = sp.getString(Const.password, "");
        edit_password.setText(password);

        button_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 设置不调用系统键盘
        if (Build.VERSION.SDK_INT <= 10) {
            edit_password.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edit_password, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        datas = keyboardView.getDatas();
        keyboardView.setOnKeyBoardClickListener(this);
    }

    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void onKeyClick(View view, RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 11:
                finish();
                break;
            case 12:
                password=edit_password.getText().toString().trim();
                if(Validate.isNull(password)){
                    Toast.makeText(cont,"请输入班牌密码",Toast.LENGTH_LONG).show();
                }else if(password.length()!=8){
                    Toast.makeText(cont,"班牌密码长度必须为8位",Toast.LENGTH_LONG).show();
                }else{
                    SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Const.password, password);
                    editor.commit();
                    Toast.makeText(cont,"设置成功！",Toast.LENGTH_LONG).show();
                }
                break;
            default: // 按下数字键
                edit_password.setText(edit_password.getText().toString().trim() + datas.get(position));
                edit_password.setSelection(edit_password.getText().length());
                break;
        }
    }

    @Override
    public void onDeleteClick(View view, RecyclerView.ViewHolder holder, int position) {
        // 点击删除按钮
        String num = edit_password.getText().toString().trim();
        if (num.length() > 0) {
            edit_password.setText(num.substring(0, num.length() - 1));
            edit_password.setSelection(edit_password.getText().length());
        }
    }

    @Override
    public void onBackPressed() {
        if (keyboardView.isVisible()) {
            return;
        }
        super.onBackPressed();
    }
}
