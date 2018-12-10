package net.jiaobaowang.gonggaopai.classes;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.jiaobaowang.gonggaopai.R;
import net.jiaobaowang.gonggaopai.base.BaseActivity;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.Validate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetClassesActivity extends BaseActivity implements KeyboardAdapterClasses.OnKeyboardClickListener {

    private EditText etInput;
    private TextView selectId;
    private KeyboardViewClasses keyboardView;
    private List<String> datas;

    private SpinerPopWindow<String> mSpinerPopWindow;
    private List<Map> list;
    private String[] ids= Const.ids;
    String selectID="";

    @Override
    public int initLayout() {
        return R.layout.activity_classes;
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
        selectId=(TextView)findViewById(R.id.selectId);
        etInput= (EditText) findViewById(R.id.et_input_classes);
        keyboardView= (KeyboardViewClasses) findViewById(R.id.keyboard_view_classes);
        // 设置不调用系统键盘
        if (Build.VERSION.SDK_INT <= 10) {
            etInput.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(etInput, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        etInput.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideBottomKey();
            }
        });

        selectId.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideBottomKey();
            }
        });
        mSpinerPopWindow = new SpinerPopWindow<String>(this,  getList(),itemClickListener);
        selectId.setOnClickListener(clickListener);
        datas = keyboardView.getDatas();
        keyboardView.setOnKeyBoardClickListener(this);
    }

    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
        return false;
    }

    @Override
    public void onKeyClick(View view, RecyclerView.ViewHolder holder, int position) {
        Intent intent = new Intent();
        switch (position) {
            case 11:
                setResult(2, intent);
                finish();
                break;
            case 12:
                if(Validate.isNull(selectID)){
                    Toast.makeText(SetClassesActivity.this,"请选择班牌类型",Toast.LENGTH_LONG).show();
                }else if(Validate.isNull(etInput.getText().toString())){
                    Toast.makeText(SetClassesActivity.this,"请输入班级编号",Toast.LENGTH_LONG).show();
                }else{
                    intent.putExtra("action", "230");
                    intent.putExtra("blandlv", selectID);
                    intent.putExtra("blandid", etInput.getText().toString());
                    setResult(1, intent);
                    finish();
                }

                break;
            default: // 按下数字键
                etInput.setText(etInput.getText().toString().trim() + datas.get(position));
                etInput.setSelection(etInput.getText().length());
                break;
        }
    }

    @Override
    public void onDeleteClick(View view, RecyclerView.ViewHolder holder, int position) {
        // 点击删除按钮
        String num = etInput.getText().toString().trim();
        if (num.length() > 0) {
            etInput.setText(num.substring(0, num.length() - 1));
            etInput.setSelection(etInput.getText().length());
        }
    }

    @Override
    public void onBackPressed() {
        if (keyboardView.isVisible()) {
            return;
        }
        super.onBackPressed();
    }


    /**
     * 显示PopupWindow
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.selectId:
                    mSpinerPopWindow.setWidth(selectId.getWidth());
                    mSpinerPopWindow.showAsDropDown(selectId);
                    break;
            }
        }
    };

    private List getList() {
        list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            Map map =new HashMap();
            map.put("id",i);
            map.put("name",ids[i]);
            list.add(map);
        }
        return list;
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            selectId.setText(list.get(position).get("name").toString());
            selectID=list.get(position).get("id").toString();
//            Toast.makeText(SetClassesActivity.this, "点击了:" + list.get(position).get("name").toString()+","+list.get(position).get("id").toString(),Toast.LENGTH_LONG).show();
        }
    };

}
