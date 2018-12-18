package net.jiaobaowang.gonggaopai.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * 类名：com.gxcwyth.base.BaseActivity.class
 * 描述：应用程序基类
 * Created by 刘帅 on 2016/8/9.
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 注意：这里的boolean，每个activity都继承BaseActivity，当activity初始化时，
     * BaseActivity便完成一次初始化 ，这样就可以实现每个activity都可以单独的进行显示状态的设置
     * **/

    /**
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = false;

    /**
     * 是否允许全屏
     **/
    private boolean allowFullScreen = false;


    /**
     * 是否允许快速点击
     **/
    private boolean allowQuickClick = false;


    /**
     * 当前activity渲染的视图View
     **/
    private View mContextView = null;

    /**
     * 获取当前包名的TAG
     **/
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 定义全局Handler
     **/
    public UIHandler handler = new UIHandler(Looper.getMainLooper());

    /**
     * 定义全局Contex
     **/
    public Activity cont = this;

    /**
     * 快速点击周期
     **/
    private long lastClick = 0;

    /***************************************************************************
     *
     * 打印Activity生命周期
     *
     ***************************************************************************/
    /**
     * onCreate 创建
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        $Log(TAG + "        ------------------> Activity:onCeat <------------------");
        Bundle bundle = getIntent().getExtras();
        mContextView = LayoutInflater.from(this).inflate(initLayout(), null);

        if (allowFullScreen) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        if (isSetStatusBar) // 是否沉浸状态栏
        {
            steepStatusBar();
        }


        initParms(bundle);
        setContentView(mContextView);
        BaseActivityManager.getAppManager().addActivity(this);
        setHandler();
        initQtData();
        doBusiness(this);
    }

    /**
     * onStart 启动
     */
    @Override
    protected void onStart() {
        super.onStart();
        $Log(TAG + "        ------------------> Activity:onStart <------------------");
//        hideBottomKey();
    }

    /**
     * onResume 恢复
     */
    @Override
    protected void onResume() {
        super.onResume();
        $Log(TAG + "        ------------------> Activity:onResume <------------------");
//        hideBottomKey();
    }

    /**
     * onPause 暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
        $Log(TAG + "        ------------------> Activity:onPause <------------------");
//        hideBottomKey();
    }

    /**
     * onRestart 重启
     */

    @Override
    protected void onRestart() {
        super.onRestart();
        $Log(TAG + "        ------------------> Activity:onRestart <------------------");
//        hideBottomKey();
    }

    /**
     * onStop 停止
     */
    @Override
    protected void onStop() {
        super.onStop();
        $Log(TAG + "        ------------------> Activity:onStop<------------------");
    }

    /**
     * onDestroy 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        $Log(TAG + "        ------------------> Activity:onDestroy()<------------------");
    }

    /**
     * [日志输出]
     * <p/>
     * msg
     */
    protected void $Log(String msg) {
        Log.i("电子班牌", msg);
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * [防止快速点击]
     *
     * @return
     */
    public boolean fastClick() {
        if (!allowQuickClick) {// 不允许快速点击
            if (System.currentTimeMillis() - lastClick <= 1000) {
                return false;
            }
            lastClick = System.currentTimeMillis();
            return true;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return widgetOnKey(keyCode,event);
    }

    /**
     * [内部类]
     * <p/>
     * 重写Handler
     */
    public class UIHandler extends Handler {

        private IHandler handler;// 回调接口，消息传递给注册者

        public UIHandler(Looper looper) {
            super(looper);
        }

        public UIHandler(Looper looper, IHandler handler) {
            super(looper);
            this.handler = handler;
        }

        public void setHandler(IHandler handler) {
            this.handler = handler;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (handler != null) {
                handler.handleMessage(msg);// 有消息，就传递
            }
        }
    }

    public interface IHandler {
        void handleMessage(Message msg);
    }

    private void setHandler() {
        handler.setHandler(new IHandler() {
            @Override
            public void handleMessage(Message msg) {
                widgetHandle(msg);// 有消息就提交给子类实现的方法
            }
        });
    }

    /********************************************************************************
     *
     * [1.绑定布局 2.初始化Handler 3.初始化Bundle参数
     * 4.初始化其他数据 5.业务操作 6.物理键监听]
     *
     ********************************************************************************/

    /**
     * [绑定布局]
     *
     * @return
     */
    public abstract int initLayout();

    /**
     * [初始化Handler]
     */
    public abstract void widgetHandle(Message msg);

    /**
     * [初始化Bundle参数]
     *
     * @param bundle
     */
    public abstract void initParms(Bundle bundle);

    /**
     * [初始化其他数据]
     */
    public abstract void initQtData();


    /**
     * [业务操作]
     *
     * @param mContext
     */
    public abstract void doBusiness(Context mContext);

    /**
     * [物理键监听]
     *
     * @param keyEvent
     */
    public abstract boolean widgetOnKey(int keyCode,KeyEvent keyEvent);



    /**
     * 隐藏底部状态栏
     */
    protected void hideBottomKey() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            Window window = getWindow();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            window.getDecorView().setSystemUiVisibility(uiOptions);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(Color.TRANSPARENT);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = cont.getWindow();
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


}
