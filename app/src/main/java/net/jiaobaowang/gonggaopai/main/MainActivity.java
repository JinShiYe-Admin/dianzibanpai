package net.jiaobaowang.gonggaopai.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
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
import net.jiaobaowang.gonggaopai.pwd.PwdActivity;
import net.jiaobaowang.gonggaopai.service.UploadService;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.Validate;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        base = (RelativeLayout) findViewById(R.id.baseLayout);
        view = (LinearLayout) findViewById(R.id.webView);
        int result = 0;
        int resourceId = cont.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = cont.getResources().getDimensionPixelSize(resourceId);
        }
        base.setPadding(0, result, 0, 0);
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        Const.blandlv = sp.getString("blandlv", "");
        Const.blandid = sp.getString("blandid", "");
        Const.styleid = sp.getString("styleid", "");
        Const.serNum = sp.getInt("serNum", 0);
        if(Const.serNum ==0){
            Const.serNum=10000;
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("serNum",Const.serNum);
            editor.commit();
        }
        AECrashHelper.initCrashHandler(getApplication());
        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions2);


        final FloatingActionButton  actionB= (FloatingActionButton)findViewById(R.id.action_b);
        actionB.setColorNormalResId(R.color.pink);
        actionB.setColorPressedResId(R.color.pink_pressed);
//        actionB.setSize(FloatingActionButton.SIZE_MINI);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//设置班牌类型
                setHideAnimation(menuMultipleActions, 500);
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
                setHideAnimation(menuMultipleActions, 500);
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

//        boolean isUploadServiceRunning=ReceiverAndServiceUtil.isServiceRunning(cont,"net.jiaobaowang.gonggaopai.service.UploadService");
//        boolean isReaderServiceRunning=ReceiverAndServiceUtil.isServiceRunning(cont,"net.jiaobaowang.gonggaopai.service.ReaderService");
//        if(!isUploadServiceRunning){
//            //启动定时任务
//            Intent startService = new Intent(cont,UploadService.class);
//            startService(startService);
//        }
//
//        if(!isReaderServiceRunning){
//            //启动串口读取服务
//            Intent startIntent = new Intent(cont,ReaderService.class);
//            startService(startIntent);
//        }

        quanxian();
        if(Validate.isNull(Const.blandlv)&&Validate.isNull(Const.blandid)){
            if(BaseActivityManager.getAppManager().isActivityStarted(PwdActivity.class)){

            }else{
                setHideAnimation(menuMultipleActions, 500);
                Intent intent = new Intent();
                intent.putExtra("action","110");
                intent.setClass(cont, PwdActivity.class);
                startActivityForResult(intent, Const.GO_PASSWORD);
            }
        }
        Intent intent = new Intent(" android.intent.action.hidebar");
        sendBroadcast(intent);
        getNetTime();
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
                        .useDefaultIndicator()
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
                    SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);;
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
            }
        }else if(resultCode == 1 && requestCode == Const.EXIST){
            BaseActivityManager manager=BaseActivityManager.getAppManager();
            manager.AppExit(cont);
        }else {
            setShowAnimation(menuMultipleActions, 500);
//            Toast.makeText(cont, "主题选择成功", Toast.LENGTH_LONG).show();
        }
    }

    public void setHideAnimation(View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }

        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }
        // 监听动画结束的操作
        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        view.startAnimation(mHideAnimation);
    }

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
            if (Const.blandlv!=""&&Const.blandid != "") {
                setHideAnimation(menuMultipleActions, 0);
            }
            initWeb();

        }
    }

    public void quanxian(){
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
            initMap();
            mLocationClient.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent stopIntent = new Intent(cont,UploadService.class);
        stopService(stopIntent);
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
    private void getNetTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;//取得资源对象
                try {
                    url = new URL("http://www.baidu.com");
                    //url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
                    //url = new URL("http://www.bjtime.cn");
                    URLConnection uc = url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    long ld = uc.getDate(); //取得网站日期时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(ld);
                    final String format = formatter.format(calendar.getTime());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "当前网络时间为: \n" + format, Toast.LENGTH_SHORT).show();
                            //TODO 设定系统时间为当前网络时间
                            //TODO 设置定时开关机时间
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}


