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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity {

    private AgentWeb mAgentWeb;
    private FrameLayout baseLayout;
    private RelativeLayout floatingBtn;
    private AlphaAnimation mHideAnimation;
    private AlphaAnimation mShowAnimation;
    private WebView webview;
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private FloatingActionsMenu menuMultipleActions;
    private static final int BAIDU_READ_PHONE_STATE = 166;
    private int num=120;
    private AddFloatingActionButton mAddButton;
    private ScheduledExecutorService mScheduledExecutor  = Executors.newScheduledThreadPool(1);


    private CardIdReceiver cardIdReceiver; //广播接收者
    private LocalBroadcastManager manager;
    @Override
    public int initLayout() {
        return R.layout.activity_main;
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
        baseLayout=(FrameLayout)findViewById(R.id.baseLayout);
        webview =(WebView)findViewById(R.id.webView);
        floatingBtn=(RelativeLayout)findViewById(R.id.floatingBtn);
        manager = LocalBroadcastManager.getInstance(cont);

        /**
         * 判断打卡广播接收器是否注册
         */
        boolean isRegister = ReceiverAndServiceUtil.isRegister(manager, Const.ACTION_NAME);
        if (!isRegister) {
            cardIdReceiver = new CardIdReceiver();
            IntentFilter intentFilter = new IntentFilter(); //初始化意图过滤器
            intentFilter.addAction(Const.ACTION_NAME); //添加动作
            manager.registerReceiver(cardIdReceiver, intentFilter); //注册广播
        }
        SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        Const.socketIp = sp.getString(Const.socketip, Const.socketIp);
        Const.socketPort = sp.getInt(Const.socketport, Const.socketPort);
        Const.updateUrl = sp.getString(Const.updateaddress, Const.updateUrl);
        AECrashHelper.initCrashHandler(getApplication());
//        initTBS();//初始化腾讯内核
        mAddButton= (AddFloatingActionButton) findViewById(com.getbase.floatingactionbutton.R.id.fab_expand_menu_button);

        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions2);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        setHideAnimation(menuMultipleActions, 1000);
                        t.cancel();
                    }
                }, 5000);
                menuMultipleActions.toggle();
            }
        });

        final FloatingActionButton  actionA= (FloatingActionButton)findViewById(R.id.action_a);
        actionA.setColorNormalResId(R.color.pink);
        actionA.setColorPressedResId(R.color.pink_pressed);
//        actionB.setSize(FloatingActionButton.SIZE_MINI);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//设置班牌类型
                if (mAgentWeb != null) {
                        mAgentWeb.getUrlLoader().reload();
                }
            }
        });

        final FloatingActionButton  actionB= (FloatingActionButton)findViewById(R.id.action_b);
        actionB.setColorNormalResId(R.color.pink);
        actionB.setColorPressedResId(R.color.pink_pressed);
//        actionB.setSize(FloatingActionButton.SIZE_MINI);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//设置班牌类型
                Intent intent = new Intent();
                intent.putExtra("action",Const.GO_PASSWORD);
                intent.setClass(cont, PwdActivity.class);
                startActivityForResult(intent,Const.GO_PASSWORD);
            }
        });


        final FloatingActionButton actionD = (FloatingActionButton) findViewById(R.id.action_d);
        actionD.setColorNormalResId(R.color.pink);
        actionD.setColorPressedResId(R.color.pink_pressed);
        actionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("action",Const.EXIST);
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
                if(!mScheduledExecutor.isShutdown()){
                    mScheduledExecutor.shutdown();
                    mScheduledExecutor.shutdownNow();
                }
            }
        });
        setHideAnimation(menuMultipleActions, 1000);
        String blandlv=sp.getString(Const.blandlv, "");
        String blandid=sp.getString(Const.blandid, "");
        if(Validate.isNull(blandlv)&&Validate.isNull(blandid)){
            if(BaseActivityManager.getAppManager().isActivityStarted(PwdActivity.class)){

            }else{
                Intent intent = new Intent();
                intent.putExtra("action",Const.GO_PASSWORD);
                intent.setClass(cont, PwdActivity.class);
                startActivityForResult(intent, Const.GO_PASSWORD);
            }
        }

        if(getRunntime()){
            quanxian();
            setTime(System.currentTimeMillis());
            _startService();
        }else{//计时器，弹出关机提醒
            alertShutDownDialog();
        }
    }

    /**
     * 点击事件
     * @param keyCode
     * @param keyEvent
     * @return
     */
    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
        return false;
    }
    /**
     * 加载webview
     */
    private void initWeb() {
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        String blandlv = sp.getString(Const.blandlv, "");
        String blandid = sp.getString(Const.blandid, "");
        String styleid = sp.getString(Const.styleid, "");
        String cityName = sp.getString(Const.cityName, "");
        Const.baseUrl=  sp.getString(Const.htmladdress, Const.baseUrl);
        String url;
        try {
            if(blandlv==""||blandid==""){
                url=Const.baseUrl+"?cityName="+ cityName;
            }else{
                url=Const.baseUrl+"?blandlv="+blandlv+"&blandid="+blandid+"&styleid="+styleid+"&cityName="+ cityName;
            }
            String uid="&v="+System.currentTimeMillis();
            url+=uid;
            if (mAgentWeb != null) {
                IUrlLoader a = mAgentWeb.getUrlLoader();
               a.loadUrl(url);
            } else {
                mAgentWeb = AgentWeb.with(this)
                        .setAgentWebParent(webview, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                        .closeIndicator()
//                        .setWebSettings()
                        .setWebChromeClient(new MyWebChromeClient())
                        .createAgentWeb()
                        .ready()
                        .go(url);
                /**
                 * 屏幕tap事件,用于N分钟没有点击事件后返回主页面
                 */
                mAgentWeb.getWebCreator().getWebView().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_UP:
                                final Timer t =new Timer();
                                t.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        t.cancel();
                                        System.out.println("抬起时启动定时");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String url=mAgentWeb.getWebCreator().getWebView().getUrl();
                                                System.out.println(url);
                                                mScheduledExecutor.shutdown();
                                                mScheduledExecutor.shutdownNow();
                                                if(!url.contains("indexPage1.html")&&!url.contains("firstPage.html")){
                                                    mScheduledExecutor= Executors.newScheduledThreadPool(1);
                                                    mScheduledExecutor.scheduleAtFixedRate(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (mAgentWeb != null) {
                                                                initWeb();
                                                                mScheduledExecutor.shutdown();
                                                                mScheduledExecutor.shutdownNow();
                                                                System.out.println("执行了几次？");
                                                            }
                                                        }
                                                    }, Const.tapReturnTime, Const.tapReturnTime, TimeUnit.SECONDS);
                                                }
                                            }
                                        });
                                    }
                                },1000);
                        }
                        return false;
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
////        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "横屏模式", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(this, "竖屏模式", Toast.LENGTH_SHORT).show();
//        }
//    }

    private FrameLayout fullscreenContainer;
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    class MyWebChromeClient extends WebChromeClient {
        private View myView = null;
        // 全屏
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            ViewGroup parent = (ViewGroup) webview.getParent();
            parent.removeView(webview);
            parent.removeView(floatingBtn);
            FrameLayout decor = (FrameLayout) getWindow().getDecorView();
            fullscreenContainer = new FullscreenHolder(cont);
            fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
            decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
            myView = view;
            mAgentWeb.clearWebCache();
            setFullScreen();
        }

        // 退出全屏
        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            if (myView != null) {
                FrameLayout decor = (FrameLayout) getWindow().getDecorView();
                decor.removeView(fullscreenContainer);
                fullscreenContainer = null;
                baseLayout.addView(webview);
                baseLayout.addView(floatingBtn);
                myView = null;
                mAgentWeb.clearWebCache();
                quitFullScreen();
            }
        }
    }
    /**
     * 设置全屏
     */
    private void setFullScreen() {
        // 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 退出全屏
     */
    private void quitFullScreen() {
        // 声明当前屏幕状态的参数并获取
//        final WindowManager.LayoutParams attrs = this.getWindow().getAttributes();
//        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.getWindow().setAttributes(attrs);
//        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /** 全屏容器界面 */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        String blandlv = sp.getString(Const.blandlv, "");
        String blandid = sp.getString(Const.blandid, "");
        if(Validate.isNull(blandlv)||Validate.isNull(blandid)){
            BaseActivityManager manager=BaseActivityManager.getAppManager();
            manager.AppExit(cont);
        }
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                setHideAnimation(menuMultipleActions, 1000);
                t.cancel();
            }
        }, 5000);

        BaseActivityManager.getAppManager().finishOthersActivity(MainActivity.class);
        if(resultCode==0){
            boolean reload = sp.getBoolean(Const.reload, false);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(Const.reload, false);
            editor.commit();
            if(reload){
                initWeb();
            }
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
            SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Const.cityName,city);
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
            ActivityCompat.requestPermissions(cont, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE
            }, BAIDU_READ_PHONE_STATE);
        }else{
            SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
            String cityName = sp.getString(Const.cityName, "");
            if(Validate.isNull(cityName)){
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
        //清理已经上传过的超过7天的数据 TODO
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    List<RecordData> recordRespList=RecordData.find(RecordData.class,"IS_UPLOAD=? AND (?-SEND_TIME) > 100",new String[]{"1",System.currentTimeMillis()+""},null,"SEND_TIME ASC",null);
//                    for (int i = 0; i < recordRespList.size(); i++) {
//                        RecordData recordData=recordRespList.get(i);
//                        recordData.delete();
//                    }
//                    List<Attendance> attendanceList = Attendance.find(Attendance.class,"IS_UPLOAD=? AND (date(?)-date(TIME_STR)) > 100  ",new String[]{"1",System.currentTimeMillis()+""},null,"TIME_STR ASC",null);
//                    for (int i = 0; i < attendanceList.size(); i++) {
//                        Attendance attendance=attendanceList.get(i);
//                        attendance.delete();
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//        boolean isUploadServiceRunning= ReceiverAndServiceUtil.isServiceRunning(cont,"net.jiaobaowang.gonggaopai.service.UploadServiceScheduledExecutor");
//        boolean isReaderServiceRunning=ReceiverAndServiceUtil.isServiceRunning(cont,"net.jiaobaowang.gonggaopai.service.ReaderService");
//        if(!isUploadServiceRunning){
//            //启动定时任务
//            Intent startService = new Intent(cont,UploadServiceScheduledExecutor.class);
//            startService(startService);
//        }
//
//        if(!isReaderServiceRunning){
//            //启动串口读取服务
//            Intent startIntent = new Intent(cont,ReaderService.class);
//            startService(startIntent);
//        }
    }

    private void _stopService(){
//        boolean isUploadServiceRunning= ReceiverAndServiceUtil.isServiceRunning(cont,"net.jiaobaowang.gonggaopai.service.UploadServiceScheduledExecutor");
//        boolean isReaderServiceRunning=ReceiverAndServiceUtil.isServiceRunning(cont,"net.jiaobaowang.gonggaopai.service.ReaderService");
//        if(isUploadServiceRunning){
//            //关闭定时任务
//            Intent startService = new Intent(cont,UploadServiceScheduledExecutor.class);
//            stopService(startService);
//        }
//
//        if(isReaderServiceRunning){
//            //关闭串口读取服务
//            Intent startIntent = new Intent(cont,ReaderService.class);
//            stopService(startIntent);
//        }
    }

    /**
     * 判断一下开机时间是否在关机时间段内
     * @return true:执行其他操作
     */

    private boolean getRunntime(){
        SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        String startTime=sp.getString(Const.startTime, "");
        String shutdownTime=sp.getString(Const.shutdownTime, "");
        if(Validate.isNull(startTime)||Validate.isNull(shutdownTime)){
            return true;
        }else{
            int starthour=Integer.parseInt(startTime.split(":")[0]);
            int startminute=Integer.parseInt(startTime.split(":")[1]);
            int afstartminute=0;
            if(startminute==0){
                afstartminute=60;
            }
            int shutdownhour=Integer.parseInt(shutdownTime.split(":")[0]);
            int shutdownminute=Integer.parseInt(shutdownTime.split(":")[1]);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int currHour =calendar.get(Calendar.HOUR_OF_DAY);
            int currMinute =calendar.get(Calendar.MINUTE);
            if(currHour==shutdownhour&&currMinute>=shutdownminute){//当前小时与关机小时相同，当前分钟数比关机分钟数大，关机
                return false;
            }else if(currHour>shutdownhour){//当前小时比关机小时大，关机
                return false;
            }else if(starthour-currHour==1&&afstartminute-currMinute>=4){//当前小时比开机小时早一个小时，分钟数比开机时间早4分钟关机  比如7:56开机   开机时间是8:00 关机
                return false;
            }else if(starthour-currHour>1){//当前小时比开机小时早两个个小时以上 关机
                return false;
            }else if(currHour==starthour&&startminute!=0){//当前小时比开机小时相同 开机分钟不为0
                if(startminute-currMinute>=4){//开机分钟比当前分钟早4分钟 关机 比如8:20 开机时间是8:30  关机
                    return false;
                }else{
                    return true;
                }
            }else if(currHour==starthour&&startminute==0){//当前小时比开机小时相同 开机分钟为0  比如8:01 开机时间是8:00 开机
                return true;
            }else {
                return true;
            }
        }
    }

    /**
     * 根据网络时间或本机时间，设置自动开关机时间
     * @param timeMill
     */
    public void setTime(Long timeMill){
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        String startTime= sp.getString(Const.startTime, "");
        String shutdownTime= sp.getString(Const.shutdownTime, "");
        if(Validate.isNull(startTime)||Validate.isNull(shutdownTime)){
            if(Const.DEBUG){
                Toast.makeText(cont, "没有获取到自动开关机时间", Toast.LENGTH_LONG).show();
            }
        }else{
            String startTimes[] =startTime.split(":");
            String shutdownTimes[] =shutdownTime.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeMill);
            final int shutdownyear =calendar.get(Calendar.YEAR);
            final int shutdownmonth =calendar.get(Calendar.MONTH)+1;//得到实际月份 系统月份是0~11
            final int shutdownday =calendar.get(Calendar.DAY_OF_MONTH);
            final int shutdownhour =Integer.parseInt(shutdownTimes[0]);
            final int shutdownminute =Integer.parseInt(shutdownTimes[1]);

            calendar.add(Calendar.DAY_OF_MONTH,1);//第二天开机

            final int startyear =calendar.get(Calendar.YEAR);
            final int startmonth =calendar.get(Calendar.MONTH)+1;//得到开机的月份，跨月份
            final int startday =calendar.get(Calendar.DAY_OF_MONTH);//开机时间应该是第二天，而不是当天，所以开机时间要加1天
            final int starthour=Integer.parseInt(startTimes[0]);
            final int startminute =Integer.parseInt(startTimes[1]);

            Intent intent = new Intent("android.intent.action.setpoweronoff");
            int[] timeon = new int[]{startyear,startmonth,startday,starthour,startminute,0}; //开机时间
            intent.putExtra("timeon", timeon);
            int[] timeoff = new int[]{shutdownyear,shutdownmonth,shutdownday,shutdownhour,shutdownminute,0}; //关机时间
            intent.putExtra("timeoff", timeoff);
            intent.putExtra("enable", true); //true 为启用， false 为取消此功能
            sendBroadcast(intent);
            if(Const.DEBUG){
                Toast.makeText(cont, "设置自动开关机成功:"+startyear+"-"+startmonth+"-"+startday+" "+starthour+":"+startminute+"至"+shutdownyear+"-"+shutdownmonth+"-"+shutdownday+" "+shutdownhour+":"+shutdownminute, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 关机时间段 关机提醒
     */
    private void alertShutDownDialog(){
        final CommonDialog dialog = new CommonDialog(cont);
        final ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
                resetTime();
                mScheduledExecutorService.shutdownNow();
                Intent intent = new Intent("android.intent.action.shutdown");
                sendBroadcast(intent);
                t.cancel();
            }
        }, Const.closeTimeout);
        dialog.setMessage(" ")
                .setImageResId(-1)
                .setTitle("当前系统不在正常运行时间范围内，系统将在 "+Const.closeTimeout/1000+" 秒后自动关机！")
                .setPositive("取消关机")
                .setNegtive("")
                .setSingle(1)
                .setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        dialog.dismiss();
                        t.cancel();
                        resetTime();
                        quanxian();
                        _startService();
                    }

                    @Override
                    public void onNegtiveClick() {
                        dialog.dismiss();
                    }
                }).show();


        mScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.titleTv.setText("当前系统不在正常运行时间范围内，系统将在 "+num+" 秒后自动关机！");
                        num--;
                    }
                });
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void resetTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        final int currhour =calendar.get(Calendar.HOUR_OF_DAY);
        SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        String startTime=sp.getString(Const.startTime, "");
        String shutdownTime=sp.getString(Const.shutdownTime, "");
        int _shutdownhour=Integer.parseInt(shutdownTime.split(":")[0]);
        if(currhour-_shutdownhour>=0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        final int startyear =calendar.get(Calendar.YEAR);
        final int startmonth =calendar.get(Calendar.MONTH)+1;//得到开机的月份，跨月份
        final int startday =calendar.get(Calendar.DAY_OF_MONTH);//开机时间应该是第二天，而不是当天，所以开机时间要加1天
        final int starthour=Integer.parseInt(startTime.split(":")[0]);
        final int startminute =Integer.parseInt(startTime.split(":")[1]);

        final int shutdownyear =calendar.get(Calendar.YEAR);
        final int shutdownmonth =calendar.get(Calendar.MONTH)+1;//得到实际月份 系统月份是0~11
        final int shutdownday =calendar.get(Calendar.DAY_OF_MONTH);
        final int shutdownhour =Integer.parseInt(shutdownTime.split(":")[0]);
        final int shutdownminute =Integer.parseInt(shutdownTime.split(":")[1]);

        Intent intent = new Intent("android.intent.action.setpoweronoff");
        int[] timeon = new int[]{startyear,startmonth,startday,starthour,startminute,0}; //开机时间
        intent.putExtra("timeon", timeon);
        int[] timeoff = new int[]{shutdownyear,shutdownmonth,shutdownday,shutdownhour,shutdownminute,0}; //关机时间
        intent.putExtra("timeoff", timeoff);
        intent.putExtra("enable", true); //true 为启用， false 为取消此功能
        sendBroadcast(intent);
        if(Const.DEBUG){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(cont, "设置自动开关机成功:"+startyear+"-"+startmonth+"-"+startday+" "+starthour+":"+startminute+"至"+shutdownyear+"-"+shutdownmonth+"-"+shutdownday+" "+shutdownhour+":"+shutdownminute, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * 打卡提示
     */
    private void alertDialog(){
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
                    SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
                    String blandlv=sp.getString(Const.blandlv, "");
                    String blandid=sp.getString(Const.blandid, "");
                    if(Validate.noNull(blandlv)&&Validate.noNull(blandid)){
                        alertDialog();
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
