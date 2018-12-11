package net.jiaobaowang.gonggaopai.pwd;

import android.content.Context;
import android.content.Intent;
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
import net.jiaobaowang.gonggaopai.SettingsCheckActivity;
import net.jiaobaowang.gonggaopai.base.BaseActivity;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.Validate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PwdActivity extends BaseActivity implements KeyboardAdapter.OnKeyboardClickListener {

    private EditText etInput1,etInput2,etInput3,etInput4,etInput5,etInput6,etInput7,etInput8;
    private KeyboardView keyboardView;
    private Button confirmBtn,returnBtn;
    private List<String> datas;
    private String action="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && requestCode == Const.GO_SETTINGSCHECKED) {
            String action= data.getStringExtra("action");
            if("240".equals(action)){
                String styleid=data.getStringExtra("styleid");
                String stylename=data.getStringExtra("stylename");
                Intent intent = new Intent();
                intent.putExtra("action", "240");
                intent.putExtra("styleid", styleid);
                intent.putExtra("stylename", stylename);
                setResult(1, intent);
                finish();
            }else if("230".equals(action)){
                String blandlv= data.getStringExtra("blandlv");
                String blandid= data.getStringExtra("blandid");
                Intent intent = new Intent();
                intent.putExtra("action", "230");
                intent.putExtra("blandlv", blandlv);
                intent.putExtra("blandid", blandid);
                setResult(1, intent);
                finish();
            }else if("300".equals(action)){
                String startTime=data.getStringExtra("startTime");
                String shutdownTime=data.getStringExtra("shutdownTime");
                Intent intent = new Intent();
                intent.putExtra("action", "300");
                intent.putExtra("startTime", startTime);
                intent.putExtra("shutdownTime", shutdownTime);
                setResult(1, intent);
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
                break;
        }
        return false;
    }

    @Override
    public int initLayout() {
        return R.layout.activity_pwd;
    }

    @Override
    public void widgetHandle(Message msg) {

    }

    @Override
    public void initParms(Bundle bundle) {
        Intent intent=getIntent();
        action=intent.getStringExtra("action");
    }

    @Override
    public void initQtData() {

    }

    @Override
    public void doBusiness(Context mContext) {

        confirmBtn= (Button) findViewById(R.id.confirmBtn);
        returnBtn= (Button) findViewById(R.id.returnBtn);
        etInput1= (EditText) findViewById(R.id.et_input1);
        etInput2= (EditText) findViewById(R.id.et_input2);
        etInput3= (EditText) findViewById(R.id.et_input3);
        etInput4= (EditText) findViewById(R.id.et_input4);
        etInput5= (EditText) findViewById(R.id.et_input5);
        etInput6= (EditText) findViewById(R.id.et_input6);
        etInput7= (EditText) findViewById(R.id.et_input7);
        etInput8= (EditText) findViewById(R.id.et_input8);
        keyboardView= (KeyboardView) findViewById(R.id.keyboard_view);
        // 设置不调用系统键盘
        if (Build.VERSION.SDK_INT <= 10) {
            etInput1.setInputType(InputType.TYPE_NULL);
            etInput2.setInputType(InputType.TYPE_NULL);
            etInput3.setInputType(InputType.TYPE_NULL);
            etInput4.setInputType(InputType.TYPE_NULL);
            etInput5.setInputType(InputType.TYPE_NULL);
            etInput6.setInputType(InputType.TYPE_NULL);
            etInput7.setInputType(InputType.TYPE_NULL);
            etInput8.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(etInput1, false);
                setShowSoftInputOnFocus.invoke(etInput2, false);
                setShowSoftInputOnFocus.invoke(etInput3, false);
                setShowSoftInputOnFocus.invoke(etInput4, false);
                setShowSoftInputOnFocus.invoke(etInput5, false);
                setShowSoftInputOnFocus.invoke(etInput6, false);
                setShowSoftInputOnFocus.invoke(etInput7, false);
                setShowSoftInputOnFocus.invoke(etInput8, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        datas = keyboardView.getDatas();

        keyboardView.setOnKeyBoardClickListener(this);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("110".equals(action)){
                    Intent intent = new Intent();
                    setResult(2, intent);
                    finish();
                }else if("120".equals(action)){
                    finish();
                }

            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=getValue();
                if(Const.PWD.equals(pass)){
                    if("110".equals(action)){
                        Intent intent=new Intent();
                        intent.setClass(PwdActivity.this,SettingsCheckActivity.class);
                        startActivityForResult(intent,Const.GO_SETTINGSCHECKED);
                    }else if("120".equals(action)){
                        Intent intent = new Intent();
                        setResult(1, intent);
                        finish();
                    }

                }else{
                    Toast.makeText(PwdActivity.this,"密码不正确",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
        Intent intent = new Intent();
        intent.putExtra("classId", "");
        setResult(1, intent);
        finish();
        return false;
    }

    @Override
    public void onKeyClick(View view, RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            default: // 按下数字键
                switch (getText().size()+1){
                    case 1:
                        etInput1.setText(etInput1.getText().toString().trim() + datas.get(position));
                        break;
                    case 2:
                        etInput2.setText(etInput2.getText().toString().trim() + datas.get(position));
                        break;
                    case 3:
                        etInput3.setText(etInput3.getText().toString().trim() + datas.get(position));
                        break;
                    case 4:
                        etInput4.setText(etInput4.getText().toString().trim() + datas.get(position));
                        break;
                    case 5:
                        etInput5.setText(etInput5.getText().toString().trim() + datas.get(position));
                        break;
                    case 6:
                        etInput6.setText(etInput6.getText().toString().trim() + datas.get(position));
                        break;
                    case 7:
                        etInput7.setText(etInput7.getText().toString().trim() + datas.get(position));
                        break;
                    case 8:
                        etInput8.setText(etInput8.getText().toString().trim() + datas.get(position));
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onDeleteClick(View view, RecyclerView.ViewHolder holder, int position) {
        switch (getText().size()){
            case 1:
                etInput1.setText("");
                break;
            case 2:
                etInput2.setText("");
                break;
            case 3:
                etInput3.setText("");
                break;
            case 4:
                etInput4.setText("");
                break;
            case 5:
                etInput5.setText("");
                break;
            case 6:
                etInput6.setText("");
                break;
            case 7:
                etInput7.setText("");
                break;
            case 8:
                etInput8.setText("");
                break;
            default:
                break;
        }
    }

    private List<String> getText(){
        List<String> list=new ArrayList<>();
        if(Validate.noNull(etInput1.getText().toString())){
            list.add("1");
        }
        if(Validate.noNull(etInput2.getText().toString())){
            list.add("1");
        }
        if(Validate.noNull(etInput3.getText().toString())){
            list.add("1");
        }
        if(Validate.noNull(etInput4.getText().toString())){
            list.add("1");
        }
        if(Validate.noNull(etInput5.getText().toString())){
            list.add("1");
        }
        if(Validate.noNull(etInput6.getText().toString())){
            list.add("1");
        }
        if(Validate.noNull(etInput7.getText().toString())){
            list.add("1");
        }
        if(Validate.noNull(etInput8.getText().toString())){
            list.add("1");
        }
        return list;
    }

    private String getValue(){
        String value=etInput1.getText().toString()+etInput2.getText().toString()+
                etInput3.getText().toString()+etInput4.getText().toString()+
                etInput5.getText().toString()+etInput6.getText().toString()+
                etInput7.getText().toString()+etInput8.getText().toString();
        return value;
    }

    @Override
    public void onBackPressed() {
        if (keyboardView.isVisible()) {
//            keyboardView.dismiss();
            return;
        }
        super.onBackPressed();
    }



}
