package net.jiaobaowang.gonggaopai.timesetting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import net.jiaobaowang.gonggaopai.R;
import net.jiaobaowang.gonggaopai.base.BaseActivity;
import net.jiaobaowang.gonggaopai.util.Const;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeSettingActivity extends BaseActivity {
    private Button cancelBtn,confirmBtn;
    private EditText startTime,shutdownTime;

    @Override
    public int initLayout() {
        return R.layout.activity_time_setting;
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
        cancelBtn=(Button)findViewById(R.id.cancelTime);
        confirmBtn=(Button)findViewById(R.id.confirmTime);
        startTime=(EditText)findViewById(R.id.startTime);
        startTime.setFocusable(false);
        startTime.setFocusableInTouchMode(false);
        shutdownTime=(EditText)findViewById(R.id.shutdownTime);
        shutdownTime.setFocusable(false);
        shutdownTime.setFocusableInTouchMode(false);

        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        startTime.setText(sp.getString("startTime", "点击选择自动开机时间"));
        shutdownTime.setText(sp.getString("shutdownTime", "点击选择自动关机时间"));
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(cont, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time= getTime(date);
                        if(Const.DEBUG){
                            Toast.makeText(cont, time, Toast.LENGTH_SHORT).show();
                        }
                        int hour=Integer.parseInt(time.split(":")[0]);
                        if(hour<4||hour>16){
                            Toast.makeText(cont, "开机时间段必须在：04:00 至 12:00 之间", Toast.LENGTH_SHORT).show();
                        }else{
                            startTime.setText(time);
                        }
                    }
                }) .setType(new boolean[]{false, false, false, true, true, false})// 默认全部显示
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setLabel("","","","时","分","")//默认设置为年月日时分秒
                        .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .isDialog(false)//是否显示为对话框样式
                        .build();
                pvTime.show();
            }
        });
        shutdownTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(cont, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time= getTime(date);
                        if(Const.DEBUG){
                            Toast.makeText(cont, time, Toast.LENGTH_SHORT).show();
                        }
                        int hour=Integer.parseInt(time.split(":")[0]);
                        if(hour<14){
                            Toast.makeText(cont, "关机时间段必须在：16:00 至 24:00 之间", Toast.LENGTH_SHORT).show();
                        }else{
                            shutdownTime.setText(time);
                        }
                    }
                }) .setType(new boolean[]{false, false, false, true, true, false})// 默认全部显示
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setLabel("","","","时","分","")//默认设置为年月日时分秒
                        .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .isDialog(false)//是否显示为对话框样式
                        .build();
                pvTime.show();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startTimestr=startTime.getText().toString();
                String shutdownTimestr=shutdownTime.getText().toString();
                if("点击选择自动开机时间".equals(startTimestr)){
                    Toast.makeText(cont,"请选择开机时间",Toast.LENGTH_LONG).show();
                }else if("点击选择自动关机时间".equals(shutdownTimestr)){
                    Toast.makeText(cont,"请选择关机时间",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("startTime", startTimestr);
                    intent.putExtra("shutdownTime", shutdownTimestr);
                    setResult(1, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
        return false;
    }

    public String getTime(Date date){
        return new SimpleDateFormat("HH:mm").format(date);
    }
}
