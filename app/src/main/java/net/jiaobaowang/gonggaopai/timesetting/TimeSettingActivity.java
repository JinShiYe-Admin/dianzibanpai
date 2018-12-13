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
import net.jiaobaowang.gonggaopai.util.CommonDialog;
import net.jiaobaowang.gonggaopai.util.Const;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
//                        if(hour<4||hour>12){
//                            Toast.makeText(cont, "开机时间段必须在：04:00 至 12:00 之间", Toast.LENGTH_SHORT).show();
//                        }else{
                            startTime.setText(time);
//                        }
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
//                        if(hour<16){
//                            Toast.makeText(cont, "关机时间段必须在：16:00 至 24:00 之间", Toast.LENGTH_SHORT).show();
//                        }else{
                            shutdownTime.setText(time);
//                        }
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
                final String startTimestr=startTime.getText().toString();
                final String shutdownTimestr=shutdownTime.getText().toString();
                if("点击选择自动开机时间".equals(startTimestr)){
                    Toast.makeText(cont,"请选择开机时间",Toast.LENGTH_LONG).show();
                }else if("点击选择自动关机时间".equals(shutdownTimestr)){
                    Toast.makeText(cont,"请选择关机时间",Toast.LENGTH_LONG).show();
                }else{
                    int hour=Integer.parseInt(shutdownTimestr.split(":")[0]);
                    int minute=Integer.parseInt(shutdownTimestr.split(":")[1]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    int currHour =calendar.get(Calendar.HOUR_OF_DAY);
                    int currMinute =calendar.get(Calendar.MINUTE);
                    if(hour<=currHour && minute<=currMinute){//设定的时间比当前时间早，设备会直接关机，到指定的开机时间时自动开机。
                        final CommonDialog dialog = new CommonDialog(cont);
                        dialog.setMessage(" ")
                                .setImageResId(-1)
                                .setTitle("您设定的关机时间比当前时间要早，设备会立即关机，直到指定开机时间时才会自动开机。")
                                .setPositive("重新调整")
                                .setNegtive("保存设置")
                                .setSingle(2)
                                .setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                                    @Override
                                    public void onPositiveClick() {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onNegtiveClick() {
                                        dialog.dismiss();
                                        Intent intent = new Intent();
                                        intent.putExtra("startTime", startTimestr);
                                        intent.putExtra("shutdownTime", shutdownTimestr);
                                        setResult(1, intent);
                                        finish();
                                    }
                                }).show();
                    }else{
                        Intent intent = new Intent();
                        intent.putExtra("startTime", startTimestr);
                        intent.putExtra("shutdownTime", shutdownTimestr);
                        setResult(1, intent);
                        finish();
                    }
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
