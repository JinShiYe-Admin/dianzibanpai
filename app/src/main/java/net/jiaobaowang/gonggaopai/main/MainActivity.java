package net.jiaobaowang.gonggaopai.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IUrlLoader;
import com.mauiie.aech.AECrashHelper;

import net.jiaobaowang.gonggaopai.R;
import net.jiaobaowang.gonggaopai.base.BaseActivity;
import net.jiaobaowang.gonggaopai.base.BaseActivityManager;
import net.jiaobaowang.gonggaopai.entry.Attendance;
import net.jiaobaowang.gonggaopai.pwd.PwdActivity;
import net.jiaobaowang.gonggaopai.service.ReaderService;
import net.jiaobaowang.gonggaopai.service.UploadServiceScheduledExecutor;
import net.jiaobaowang.gonggaopai.util.CommonDialog;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.NetUtil;
import net.jiaobaowang.gonggaopai.util.ReceiverAndServiceUtil;
import net.jiaobaowang.gonggaopai.util.Validate;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {

    private AgentWeb mAgentWeb;
    private AlphaAnimation mHideAnimation;
    private AlphaAnimation mShowAnimation;
    private RelativeLayout base;
    private LinearLayout view;
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private FloatingActionsMenu menuMultipleActions;
    private static final int BAIDU_READ_PHONE_STATE = 166;



    private CardIdReceiver cardIdReceiver; //广播接收者
    private LocalBroadcastManager manager;
    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void widgetHandle(Message msg) {
        switch (msg.what){
//            case 0x666://获取到网络时间，则以网络时间为准
//                setTime((Long) msg.obj);
//                break;
            case 0x667:
                //设置定时开关机时间
                if(Const.DEBUG) {
                    Toast.makeText(cont, "本机时间", Toast.LENGTH_LONG).show();
                }
                setTime(System.currentTimeMillis());
                break;
        }
    }

    @Override
    public void initParms(Bundle bundle) {
    }

    @Override
    public void initQtData() {
    }

    @Override
    public void doBusiness(Context mContext) {
        base = (RelativeLayout) findViewById(R.id.baseLayout);
        view = (LinearLayout) findViewById(R.id.webView);
        manager = LocalBroadcastManager.getInstance(cont);

        boolean isRegister = ReceiverAndServiceUtil.isRegister(manager, Const.ACTION_NAME);
        if (!isRegister) {
            cardIdReceiver = new CardIdReceiver();
            IntentFilter intentFilter = new IntentFilter(); //初始化意图过滤器
            intentFilter.addAction(Const.ACTION_NAME); //添加动作
            manager.registerReceiver(cardIdReceiver, intentFilter); //注册广播
        }
        quanxian();
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        Const.blandlv = sp.getString("blandlv", "");
        Const.blandid = sp.getString("blandid", "");
        Const.styleid = sp.getString("styleid", "");

        AECrashHelper.initCrashHandler(getApplication());
        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions2);


        final FloatingActionButton  actionB= (FloatingActionButton)findViewById(R.id.action_b);
        actionB.setColorNormalResId(R.color.pink);
        actionB.setColorPressedResId(R.color.pink_pressed);
//        actionB.setSize(FloatingActionButton.SIZE_MINI);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//设置班牌类型
                Intent intent = new Intent();
                intent.putExtra("action","110");
                intent.setClass(cont, PwdActivity.class);
                startActivityForResult(intent, Const.GO_PASSWORD);
            }
        });


        final FloatingActionButton actionD = (FloatingActionButton) findViewById(R.id.action_d);
        actionD.setColorNormalResId(R.color.pink);
        actionD.setColorPressedResId(R.color.pink_pressed);
        actionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("action","120");
                intent.setClass(cont, PwdActivity.class);
                startActivityForResult(intent, Const.EXIST);
            }
        });

        final FloatingActionButton menuMultipleActions_right = (FloatingActionButton) findViewById(R.id.multiple_actions_right);
        menuMultipleActions_right.setColorNormalResId(R.color.pink);
        menuMultipleActions_right.setColorPressedResId(R.color.pink_pressed);
        menuMultipleActions_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAgentWeb != null) {
                    String url=mAgentWeb.getWebCreator().getWebView().getUrl();
                    if(!url.contains("indexPage1.html")){
                        mAgentWeb.back();
                    }

                }
            }
        });
        setHideAnimation(menuMultipleActions, 1000);
        if(Validate.isNull(Const.blandlv)&&Validate.isNull(Const.blandid)){
            if(BaseActivityManager.getAppManager().isActivityStarted(PwdActivity.class)){

            }else{
                Intent intent = new Intent();
                intent.putExtra("action","110");
                intent.setClass(cont, PwdActivity.class);
                startActivityForResult(intent, Const.GO_PASSWORD);
            }
        }
        getNetTime();
        _startService();
    }

    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                mAgentWeb.back();
                break;
        }
        return false;
    }

    /**
     * 加载webview
     */
    private void initWeb() {
        String url="";
        try {
            if(Const.blandlv==""||Const.blandid==""){
                url=Const.defaultUrl+"?blandlv="+Const.blandlv+"&blandid="+Const.blandid+"&styleid="+Const.styleid+"&cityName="+ Const.cityName;
            }else{
                url=Const.baseUrl+"?blandlv="+Const.blandlv+"&blandid="+Const.blandid+"&styleid="+Const.styleid+"&cityName="+ Const.cityName;
            }
            String uid="&v="+System.currentTimeMillis();
            url+=uid;
            if (mAgentWeb != null) {
                IUrlLoader a = mAgentWeb.getUrlLoader();
                a.loadUrl(url);
            } else {
                mAgentWeb = AgentWeb.with(this)
                        .setAgentWebParent(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                        .closeIndicator()
                        .createAgentWeb()
                        .ready()
                        .go(url);
            }
        }catch (Exception e){
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                setHideAnimation(menuMultipleActions, 1000);
                t.cancel();
            }
        }, 1000);
        BaseActivityManager.getAppManager().finishOthersActivity(MainActivity.class);
        if (resultCode == 1 && requestCode == Const.GO_PASSWORD) {
            String actionP=data.getStringExtra("action");
            if("240".equals(actionP)){
                String styleid=data.getStringExtra("styleid");
                String stylename=data.getStringExtra("stylename");
                if (Validate.noNull(styleid)) {
                    SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("styleid", styleid);
                    editor.commit();
                    Const.styleid=styleid;
                    Toast.makeText(cont, "主题选择成功，名称："+stylename+"，编号：" + Const.styleid, Toast.LENGTH_LONG).show();
                    initWeb();
                }else{
                    if(Const.DEBUG) {
                        Toast.makeText(cont, "主题选择失败", Toast.LENGTH_LONG).show();
                    }
                }
            }else if("230".equals(actionP)){
                String blandlv=data.getStringExtra("blandlv");
                String blandid = data.getStringExtra("blandid");
                if (Validate.noNull(blandlv)||Validate.noNull(blandid)) {
                    SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("blandlv", blandlv);
                    editor.putString("blandid", blandid);
                    editor.commit();
                    Const.blandlv=blandlv;
                    Const.blandid=blandid;
                    if(Const.DEBUG) {
                        Toast.makeText(cont, "班级设置成功，" + "班牌类型：" + Const.blandlv + "，班牌ID：" + Const.blandid + ",cityName=" + Const.cityName, Toast.LENGTH_LONG).show();
                    }
                    initWeb();
                }else{
                    setShowAnimation(menuMultipleActions, 500);
                    if(Const.DEBUG) {
                        Toast.makeText(cont, "设置班级失败", Toast.LENGTH_LONG).show();
                    }
                }
            }else if("300".equals(actionP)){
                String startTime=data.getStringExtra("startTime");
                String shutdownTime = data.getStringExtra("shutdownTime");
                if(Const.DEBUG){
                    Toast.makeText(cont, startTime+"至"+shutdownTime, Toast.LENGTH_SHORT).show();
                }
                SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("startTime", startTime);
                editor.putString("shutdownTime", shutdownTime);
                editor.commit();
            }
        }else if(resultCode == 1 && requestCode == Const.EXIST){
            BaseActivityManager manager=BaseActivityManager.getAppManager();
            manager.AppExit(cont);
        }else {
            setShowAnimation(menuMultipleActions, 500);
//            Toast.makeText(cont, "主题选择成功", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 隐藏悬浮按钮
     * @param view
     * @param duration
     */
    public void setHideAnimation(final View view,final int duration) {
        if (null == view || duration < 0) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                menuMultipleActions.collapse();
                if (null != mHideAnimation) {
                    mHideAnimation.cancel();
                }
                // 监听动画结束的操作
                mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
                mHideAnimation.setDuration(duration);
                mHideAnimation.setFillAfter(true);
                view.startAnimation(mHideAnimation);
            }
        });
    }

    /**
     * 显示悬浮按钮
     * @param view
     * @param duration
     */
    public void setShowAnimation(View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
        if (null != mShowAnimation) {
            mShowAnimation.cancel();
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        view.startAnimation(mShowAnimation);
    }

    private void initMap(){
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        getLocation();
    }
    private void getLocation(){
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);

        mLocationClient.setLocOption(option);
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            String city = location.getCity();    //获取城市
            Const.cityName=city;
            SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("cityName",city);
            editor.commit();
            initWeb();
        }
    }
    /**
     *获取定位权限，没有定位权限，页面的天气预报将无法显示
     */
    public void quanxian(){
        NetUtil.init(cont);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(cont, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }else{
            SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
            Const.cityName = sp.getString("cityName", "");
            if(Validate.isNull(Const.cityName)){
                initMap();
                mLocationClient.start();
            }else{
                //开机后wifi还没准备好，需要延时加载webview
                final Handler mHandler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if(NetUtil.getNetWorkState()==-1){
                            mHandler.postDelayed(this, 200);
                        }else{
                            mHandler.removeCallbacks(this);
                            initWeb();
                        }

                    }
                };
                mHandler.postDelayed(r, 100);//延时100毫秒
            }
        }
    }

    @Override
    protected void onDestroy() {
        _stopService();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initMap();
                    mLocationClient.start();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void _startService(){
        boolean isUploadServiceRunning= ReceiverAndServiceUtil.isServiceRunning(cont,"net.jiaobaowang.gonggaopai.service.UploadServiceScheduledExecutor");
        boolean isReaderServiceRunning=ReceiverAndServiceUtil.isServiceRunning(cont,"net.jiaobaowang.gonggaopai.service.ReaderService");
        if(!isUploadServiceRunning){
            //启动定时任务
            Intent startService = new Intent(cont,UploadServiceScheduledExecutor.class);
            startService(startService);
        }

        if(!isReaderServiceRunning){
            //启动串口读取服务
            Intent startIntent = new Intent(cont,ReaderService.class);
            startService(startIntent);
        }
    }

    private void _stopService(){
        boolean isUploadServiceRunning= ReceiverAndServiceUtil.isServiceRunning(cont,"net.jiaobaowang.gonggaopai.service.UploadServiceScheduledExecutor");
        boolean isReaderServiceRunning=ReceiverAndServiceUtil.isServiceRunning(cont,"net.jiaobaowang.gonggaopai.service.ReaderService");
        if(isUploadServiceRunning){
            //关闭定时任务
            Intent startService = new Intent(cont,UploadServiceScheduledExecutor.class);
            stopService(startService);
        }

        if(isReaderServiceRunning){
            //关闭串口读取服务
            Intent startIntent = new Intent(cont,ReaderService.class);
            stopService(startIntent);
        }
    }
//    /**
//     * 获取网络时间，然后设置本机时间为网络时间，同时设置班级的自动开关机时间
//     */
//    private void getNetTime(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                URL url = null;//取得资源对象
//                try {
//                    url = new URL("http://www.baidu.com");
//                    //url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
//                    //url = new URL("http://www.bjtime.cn");
//                    URLConnection uc = url.openConnection();//生成连接对象
//                    uc.connect(); //发出连接
//                    final long ld = uc.getDate(); //取得网站日期时间
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(ld);
//                    final int year =calendar.get(Calendar.YEAR);
//                    final int month =calendar.get(Calendar.MONTH);
//                    final int date =calendar.get(Calendar.DATE);
//                    final int hour =calendar.get(Calendar.HOUR_OF_DAY);
//                    final int minute =calendar.get(Calendar.MINUTE);
//                    final int second =calendar.get(Calendar.SECOND);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //重置本地时间为网络时间
//                            Intent intent = new Intent("android.intent.action.settime");
//                            int[] settime= new int[]{year,month,date,hour,minute,second};
//                            intent.putExtra("settime", settime);
//                            sendBroadcast(intent);
//                            Message message=new Message();
//                            message.what=0x666;
//                            message.obj=ld;
//                            handler.sendMessage(message);
//                        }
//                    });
//                } catch (Exception e) {
//                    Message message=new Message();
//                    message.what=0x667;
//                    handler.sendMessage(message);
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
    /**
     * 参照本机时间设置班级的自动开关机时间
     */
    private void getNetTime() {
        final Handler mHandler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                Message message=new Message();
                message.what=0x667;
                handler.sendMessage(message);
            }
        };
        mHandler.postDelayed(r, 10000);//延时10秒
    }

    /**
     * 根据网络时间或本机时间，设置自动开关机时间
     * @param timeMill
     */
    public void setTime(Long timeMill){
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        String startTime= sp.getString("startTime", "");
        String shutdownTime= sp.getString("shutdownTime", "");
        if(Validate.isNull(startTime)||Validate.isNull(shutdownTime)){
            if(Const.DEBUG){
                Toast.makeText(cont, "没有获取到自动开关机时间", Toast.LENGTH_LONG).show();
            }
        }else{
            String startTimes[] =startTime.split(":");
            String shutdownTimes[] =shutdownTime.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeMill);
            final int year =calendar.get(Calendar.YEAR);
            final int shutdownmonth =calendar.get(Calendar.MONTH)+1;//得到实际月份 系统月份是0~11
            final int shutdowndate =calendar.get(Calendar.DATE);
            final int shutdownminute =Integer.parseInt(shutdownTimes[0]);
            final int shutdownsecond =Integer.parseInt(shutdownTimes[1]);

            calendar.add(Calendar.DAY_OF_MONTH,1);

            final int startdate =calendar.get(Calendar.DATE);//开机时间应该是第二天，而不是当天，所以开机时间要加1天
            final int startmonth =calendar.get(Calendar.MONTH)+1;//得到开机的月份，跨月份
            final int startminute =Integer.parseInt(startTimes[0]);
            final int startsecond =Integer.parseInt(startTimes[1]);

            Intent intent = new Intent("android.intent.action.setpoweronoff");
            int[] timeon = new int[]{year,startmonth,startdate,startminute,startsecond,0}; //开机时间
            intent.putExtra("timeon", timeon);
            int[] timeoff = new int[]{year,shutdownmonth,shutdowndate,shutdownminute,shutdownsecond,0}; //关机时间
            intent.putExtra("timeoff", timeoff);
            intent.putExtra("enable", true); //true 为启用， false 为取消此功能
            sendBroadcast(intent);
            if(Const.DEBUG){
                Toast.makeText(cont, "设置自动开关机成功111111111:"+year+"-"+startmonth+"-"+startdate+" "+startminute+":"+startsecond+"至"+year+"-"+shutdownmonth+"-"+shutdowndate+" "+shutdownminute+":"+shutdownsecond, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 打卡提示
     */
    private void alertDialog(String id){
        final CommonDialog dialog = new CommonDialog(cont);
        dialog.setMessage(" ")
                .setImageResId(R.drawable.success)
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
        }, 800);
    }

    /**
     * 判断两次打卡时间差是否在限定时间内
     * @param afterCardTime
     * @param preCardTime
     * @return
     */
    private boolean getTime(Long afterCardTime,Long preCardTime){
        if(afterCardTime-preCardTime>Const.JGTIME) {
            return true;
        } else {
            System.out.println((Const.JGTIME/1000/60)+"分钟内重复打卡");
            return false;
        }
    }

    /**
     * 班牌设置提示
     */
    private void alertFalseDialog(){
        final CommonDialog dialog = new CommonDialog(cont);
        dialog.setMessage("请先设置班牌类型!")
                .setDetail(" ")
                .setImageResId(R.drawable.rmor)
                .setTitle("系统提示")
                .setSingle(-1)
                .show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
                t.cancel();
            }
        }, 2500);
    }

    /**
     * 保存单次打卡数据
     * @param id
     * @param time
     */
    private void saveInfo(String id,Long time){
        Attendance attendance=new Attendance();
        attendance.setCardId(id);
        attendance.setTimeStr(time);
        attendance.setLx(0);
        attendance.setIsUpload(0);
        attendance.save();
    }

    /**
     * 打卡签到广播接收
     */
    class CardIdReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            runOnUiThread(new Runnable() { //运行在主线程
                @Override
                public void run() {
                    String cardId = intent.getStringExtra("cardId");
                    if(Const.DEBUG){
                        Toast.makeText(cont,"cardId2222:"+cardId,Toast.LENGTH_SHORT).show();
                    }
                    if(Validate.noNull(Const.blandlv)&&Validate.noNull(Const.blandid)){
                        alertDialog(cardId);
                        Long timestr= System.currentTimeMillis();
                        Map info=new HashMap();
                        info.put("id",cardId);
                        info.put("timestr",timestr);
                        List<Attendance> attendanceList = Attendance.find(Attendance.class,"CARD_ID=?",new String[]{cardId},null,"TIME_STR DESC","0,1");
                        if(attendanceList.size()==0){
                            saveInfo(cardId,timestr);
                        }else{
                            Attendance attend=attendanceList.get(0);
                            Long preTimeStr=attend.getTimeStr();
                            if(getTime(timestr,preTimeStr)){
                                saveInfo(cardId,timestr);
                            }
                        }
                    }else{
                        alertFalseDialog();
                    }
                }
            });
        }
    }
}