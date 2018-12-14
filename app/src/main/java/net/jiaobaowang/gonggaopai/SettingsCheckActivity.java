package net.jiaobaowang.gonggaopai;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
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
import net.jiaobaowang.gonggaopai.style.StyleActivity;
import net.jiaobaowang.gonggaopai.timesetting.TimeSettingActivity;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingsCheckActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private List<Map> mDatas;
    private HomeAdapter mAdapter;
    private GridLayoutManager manager;
    private Button button_backward;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && requestCode == Const.GO_CLASSESSETTING) {
            String blandlv= data.getStringExtra("blandlv");
            String blandid= data.getStringExtra("blandid");
            Intent intent = new Intent();
            intent.putExtra("action", "230");
            intent.putExtra("blandlv", blandlv);
            intent.putExtra("blandid", blandid);
            setResult(1, intent);
            finish();
        }else if(resultCode == 1 && requestCode == Const.GO_STYLESETTING){
            String styleid=data.getStringExtra("styleid");
            String stylename=data.getStringExtra("stylename");
            Intent intent = new Intent();
            intent.putExtra("action", "240");
            intent.putExtra("styleid", styleid);
            intent.putExtra("stylename", stylename);
            setResult(1, intent);
            finish();
        }else if(resultCode == 1 && requestCode == Const.GO_TIMESETTING){
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
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
        button_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
            }
        });
    }

    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
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
            System.out.println(map.toString());
            holder.settings_left.setText(texts);
            final int drawableId_left = cont.getResources().getIdentifier(urls,"drawable", cont.getPackageName());
            holder.settings_img_left.setImageResource(drawableId_left);
            holder.left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (keys){
                        case "appUpdate":
                            Intent appUpdate = new Intent();
                            appUpdate.setData(Uri.parse(Const.updateUrl));
                            appUpdate.setAction(Intent.ACTION_VIEW);
                            startActivity(appUpdate);
                            break;
                        case "blandCheck":
                            Intent blandCheck = new Intent();
                            blandCheck.setClass(cont, SetClassesActivity.class);
                            startActivityForResult(blandCheck, Const.GO_CLASSESSETTING);
                            break;
                        case "styleCheck":
                            if(Validate.isNull(Const.blandlv)&&Validate.isNull(Const.blandid)){
                                Toast.makeText(cont,"请先选择班牌类型，再设置班牌主题",Toast.LENGTH_LONG).show();
                            }else{
                                Intent styleCheck = new Intent();
                                styleCheck.putExtra("blandlv",Const.blandlv);
                                styleCheck.setClass(cont, StyleActivity.class);
                                startActivityForResult(styleCheck, Const.GO_STYLESETTING);
                            }
                            break;
                        case "timeCheck":
                            Intent timeCheck = new Intent();
                            timeCheck.setClass(cont, TimeSettingActivity.class);
                            startActivityForResult(timeCheck, Const.GO_TIMESETTING);
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
            TextView settings_left;
            ImageView settings_img_left;
            public MyViewHolder(View view)
            {
                super(view);
                left = (LinearLayout) view.findViewById(R.id.left);
                settings_left = (TextView) view.findViewById(R.id.settings_left);
                settings_img_left = (ImageView) view.findViewById(R.id.settings_img_left);
            }
        }
    }
}
