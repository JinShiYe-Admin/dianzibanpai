package net.jiaobaowang.gonggaopai.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.IUrlLoader;

import net.jiaobaowang.gonggaopai.R;
import net.jiaobaowang.gonggaopai.base.BaseActivity;
import net.jiaobaowang.gonggaopai.util.Const;

public class MainTestActivity extends BaseActivity {

    private AgentWeb mAgentWeb;
    private WebView webview;


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
        webview =(WebView)findViewById(R.id.webView);
        manager = LocalBroadcastManager.getInstance(cont);
        /**
         * 判断打卡广播接收器是否注册
         */
        SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        Const.socketIp = sp.getString(Const.socketip, Const.socketIp);
        Const.socketPort = sp.getInt(Const.socketport, Const.socketPort);
        Const.updateUrl = sp.getString(Const.updateaddress, Const.updateUrl);
        initWeb();
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
            final String newUrl=url;
            if (mAgentWeb != null) {
                IUrlLoader a = mAgentWeb.getUrlLoader();
               a.loadUrl(url);
            } else {
                mAgentWeb = AgentWeb.with(cont)
                        .setAgentWebParent(webview, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                        .closeIndicator()
//                        .setWebSettings()
                        .createAgentWeb()
                        .ready()
                        .go(newUrl);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
