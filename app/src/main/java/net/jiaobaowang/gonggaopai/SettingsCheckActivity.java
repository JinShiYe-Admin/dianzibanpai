package net.jiaobaowang.gonggaopai;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import net.jiaobaowang.gonggaopai.base.BaseActivity;
import net.jiaobaowang.gonggaopai.classes.SetClassesActivity;
import net.jiaobaowang.gonggaopai.htmladdress.HtmlAddressActivity;
import net.jiaobaowang.gonggaopai.ipmodify.IpPortActivity;
import net.jiaobaowang.gonggaopai.pwdmodify.PasswordModifyActivity;
import net.jiaobaowang.gonggaopai.service.DownloadIntentService;
import net.jiaobaowang.gonggaopai.style.StyleActivity;
import net.jiaobaowang.gonggaopai.timesetting.TimeSettingActivity;
import net.jiaobaowang.gonggaopai.updateaddress.AppUpdateAddressActivity;
import net.jiaobaowang.gonggaopai.util.CommonDialog;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.Validate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SettingsCheckActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private List<Map> mDatas;
    private HomeAdapter mAdapter;
    private GridLayoutManager manager;
    private Button button_backward;
    private ProgressDialog dialog;

    @Override
    public int initLayout() {
        return R.layout.activity_settings_check;
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
        String json=Const.settings_json;
        JSONArray array = JSON.parseArray(json);
        mDatas = new ArrayList<Map>();
        for (int i = 0; i <array.size() ; i++) {
            Map map = (Map) array.get(i);
            mDatas.add(map);
        }
        button_backward=(Button)findViewById(R.id.button_backward);
        mRecyclerView = (RecyclerView) findViewById(R.id.settings);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,1));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
        button_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSetBlandIDLV()){
                    Intent intent = new Intent();
                    setResult(1, intent);
                    finish();
                }else{
                    showToast("请先设置班牌类型和编号");
                }

            }
        });
        dialog= new ProgressDialog(this);
        dialog.setTitle("正在下载安装包...");
        dialog.setMessage("请稍候...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setProgress(0);
    }

    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
//        Intent intent = new Intent();
//        setResult(2, intent);
//        finish();
        return false;
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            HomeAdapter.MyViewHolder holder = new HomeAdapter.MyViewHolder(LayoutInflater.from(
                    SettingsCheckActivity.this).inflate(R.layout.settings_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int position)
        {
            Map map =mDatas.get(position);
            final String keys= map.get("key").toString();
            final String texts= map.get("text").toString();
            String urls= map.get("url").toString();
            holder.settings_left.setText(texts);
            final int drawableId_left = cont.getResources().getIdentifier(urls,"drawable", cont.getPackageName());
            holder.settings_img_left.setImageResource(drawableId_left);
            SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
            if("blandCheck".equals(keys)){
                String blandlv = sp.getString(Const.blandlv, "");
                String blandid = sp.getString(Const.blandid, "");
                if("0".equals(blandlv)){
                    holder.settings_right.setText("班级班牌（"+blandid+"）");
                }else if("1".equals(blandlv)){
                    holder.settings_right.setText("年级班牌（"+blandid+"）");
                }else if("2".equals(blandlv)){
                    holder.settings_right.setText("学校班牌（"+blandid+"）");
                }
            }else if("styleCheck".equals(keys)){
                String styleid = sp.getString(Const.styleid, "");
                String stylename = sp.getString(Const.stylename, "");
                if(Validate.noNull(styleid)){
                    holder.settings_right.setText(stylename+"（"+styleid+"）");
                }
            }else if("timeCheck".equals(keys)){
                String startTime = sp.getString(Const.startTime, "");
                String shutdownTime = sp.getString(Const.shutdownTime, "");
                if(Validate.noNull(startTime)) {
                    holder.settings_right.setText("开机时间：" + startTime + "，关机时间：" + shutdownTime);
                }
            }else if("ipseeting".equals(keys)){
                String socketip = sp.getString(Const.socketip, "");
                int socketport = sp.getInt(Const.socketport, 0);
                if(Validate.noNull(socketip)) {
                    holder.settings_right.setText(socketip + ":" + socketport);
                }else{
                    holder.settings_right.setText(Const.socketIp + ":" + Const.socketPort);
                }
            }else{
                holder.settings_right.setText("");
            }
            holder.left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (keys){
                        case "appUpdate"://系统更新
                            final CommonDialog dialog2 = new CommonDialog(cont);
                            dialog2.setMessage("检测到新版本APP，是否立即下载？")
                                    .setDetail("*")
                                    .setImageResId(-1)
                                    .setTitle("系统提醒")
                                    .setPositive("立即下载")
                                    .setNegtive("取消")
                                    .setSingle(2)
                                    .setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                                        @Override
                                        public void onPositiveClick() {
                                            dialog2.dismiss();
                                            dialog.show();
                                            Intent intent = new Intent(cont, DownloadIntentService.class);
                                            intent.putExtra("url",Const.updateUrl);
                                            intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                                            startService(intent);
                                        }

                                        @Override
                                        public void onNegtiveClick() {
                                            dialog2.dismiss();
                                        }
                                    }).show();

                            break;
                        case "blandCheck"://班牌类型设置
                            Intent blandCheck = new Intent();
                            blandCheck.setClass(cont, SetClassesActivity.class);
                            startActivity(blandCheck);
                            break;
                        case "styleCheck"://班牌主题设置
                            SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
                            String blandlv=sp.getString(Const.blandlv, "");
                            String blandid=sp.getString(Const.blandid, "");
                            if(Validate.isNull(blandlv)&&Validate.isNull(blandid)){
                                Toast.makeText(cont,"请先设置班牌类型和编号，再选择班牌主题",Toast.LENGTH_LONG).show();
                            }else{
                                Intent styleCheck = new Intent();
                                styleCheck.putExtra("blandlv",blandlv);
                                styleCheck.setClass(cont, StyleActivity.class);
                                startActivity(styleCheck);
                            }
                            break;
                        case "timeCheck"://自动开关机时间
                            Intent timeCheck = new Intent();
                            timeCheck.setClass(cont, TimeSettingActivity.class);
                            startActivity(timeCheck);
                            break;
                        case "passwordsetting"://班牌密码
                            Intent passwordsetting = new Intent();
                            passwordsetting.setClass(cont, PasswordModifyActivity.class);
                            startActivity(passwordsetting);
                            break;
                        case "ipseeting"://socket ip 端口号
                            Intent ipseeting = new Intent();
                            ipseeting.setClass(cont, IpPortActivity.class);
                            startActivity(ipseeting);
                            break;
                        case "htmlseeting"://网页地址设置
                            Intent htmlseeting = new Intent();
                            htmlseeting.setClass(cont, HtmlAddressActivity.class);
                            startActivity(htmlseeting);
                            break;
                        case "appupdateseeting"://APP更新地址设置
                            Intent appupdateseeting = new Intent();
                            appupdateseeting.setClass(cont, AppUpdateAddressActivity.class);
                            startActivity(appupdateseeting);
                            break;
                        case "closeSystem"://关机
                            final CommonDialog dialog3 = new CommonDialog(cont);
                            dialog3.setMessage("确定要关闭该设备吗？")
                                    .setDetail("*")
                                    .setImageResId(-1)
                                    .setTitle("系统提醒")
                                    .setPositive("立即关机")
                                    .setNegtive("取消")
                                    .setSingle(2)
                                    .setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                                        @Override
                                        public void onPositiveClick() {
                                            dialog3.dismiss();
                                            Intent intent = new Intent("android.intent.action.shutdown");
                                            sendBroadcast(intent);
                                        }

                                        @Override
                                        public void onNegtiveClick() {
                                            dialog3.dismiss();
                                        }
                                    }).show();
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            LinearLayout left;
            TextView settings_left,settings_right;
            ImageView settings_img_left;
            public MyViewHolder(View view)
            {
                super(view);
                left = (LinearLayout) view.findViewById(R.id.left);
                settings_left = (TextView) view.findViewById(R.id.settings_left);
                settings_right= (TextView) view.findViewById(R.id.settings_right);
                settings_img_left = (ImageView) view.findViewById(R.id.settings_img_left);
            }
        }
    }

    /**
     * 判断是否设置了班牌编号和类型
     * @return
     */
    private boolean getSetBlandIDLV(){
        SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        String blandlv=sp.getString(Const.blandlv, "");
        String blandid=sp.getString(Const.blandid, "");
        if(Validate.isNull(blandlv)&&Validate.isNull(blandid)){
            return false;
        }else{
            return true;
        }
    }

    private  void showToast(String text){
        Toast.makeText(cont,text,Toast.LENGTH_LONG).show();
    }

    public class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadIntentService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                //(true)就是根据你的进度可以设置现在的进度值。
                //(false)就是滚动条的当前值自动在最小到最大值之间来回移动，形成这样一个动画效果
                dialog.setIndeterminate(false);
                dialog.setProgress(progress);
                if (progress == 100) {
                    dialog.dismiss();
                    //自动安装下载的apk
                    String sdpath = Environment.getExternalStorageDirectory() + "/Download";
                    File file=new File(sdpath+"/"+"dianzibanpai.apk");
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(installIntent);
                }
            }else if(resultCode == DownloadIntentService.UPDATE_FALSE){
                dialog.dismiss();
                final CommonDialog dialog = new CommonDialog(cont);
                dialog.setMessage("网络连接异常，请稍后再试！")
                        .setDetail("网络连接异常，请稍后再试！")
                        .setTitle("系统提示")
                        .setSingle(0)
                        .show();
                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        t.cancel();
                    }
                }, 2000);
            }
        }
    }
}
