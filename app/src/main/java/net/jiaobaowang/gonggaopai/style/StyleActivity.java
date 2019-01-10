package net.jiaobaowang.gonggaopai.style;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import net.jiaobaowang.gonggaopai.R;
import net.jiaobaowang.gonggaopai.base.BaseActivity;
import net.jiaobaowang.gonggaopai.util.CommonDialog;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StyleActivity extends BaseActivity{

    private RecyclerView mRecyclerView;
    private List<Map> mDatas;
    private HomeAdapter mAdapter;
    private GridLayoutManager manager;
    private  String styleid,stylename;
    @Override
    public int initLayout() {
        return R.layout.activity_style;
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
        TextView lx_name =(TextView)findViewById(R.id.lx_name);
        Button button_backward=(Button)findViewById(R.id.button_backward);
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        String blandlv = sp.getString(Const.blandlv, "");
        styleid= sp.getString(Const.styleid, "");
        stylename= sp.getString(Const.stylename, "");
        String json="";
        switch (blandlv){
            case "0":
                json= Const.bj_json;
                lx_name.setText("班  级  班  牌");
                if(Validate.noNull(json)){
                    JSONArray array = JSON.parseArray(json);
                    if(Validate.isNull(styleid)){
                        Map map= (Map) array.get(0);
                        styleid=map.get("key").toString();
                        stylename=map.get("text").toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Const.styleid,styleid );
                        editor.putString(Const.stylename,stylename );
                        editor.commit();
                    }
                }
                break;
            case "1":
                json= Const.nj_json;
                lx_name.setText("年  级  班  牌");
                if(Validate.noNull(json)){
                    JSONArray array1 = JSON.parseArray(json);
                    if(Validate.isNull(styleid)){
                        Map map= (Map) array1.get(0);
                        styleid=map.get("key").toString();
                        stylename=map.get("text").toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Const.styleid,styleid );
                        editor.putString(Const.stylename,stylename );
                        editor.commit();
                    }
                }
                break;
            case "2":
                json= Const.xx_json;
                lx_name.setText("学  校  班  牌");
                if(Validate.noNull(json)){
                    JSONArray array2 = JSON.parseArray(json);
                    if(Validate.isNull(styleid)){
                        Map map= (Map) array2.get(0);
                        styleid=map.get("key").toString();
                        stylename=map.get("text").toString();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Const.styleid,styleid );
                        editor.putString(Const.stylename,stylename );
                        editor.commit();
                    }
                }
                break;
        }
        JSONArray array = JSON.parseArray(json);
        mDatas = new ArrayList<Map>();
        for (int i = 0; i <array.size() ; i++) {
            Map map = (Map) array.get(i);
            mDatas.add(map);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        manager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
        button_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    StyleActivity.this).inflate(R.layout.style_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position)
        {
            Map map =mDatas.get(position);
            String keys= map.get("key").toString();
            final String texts= map.get("text").toString();
            String urls= map.get("url").toString();
            System.out.println(map.toString());
            holder.id_num_left.setText(texts);
            holder.id_num_left_id.setText(keys);
            if(keys.equals(styleid)){
                holder.is_checked.setVisibility(View.VISIBLE);
            }else{
                holder.is_checked.setVisibility(View.GONE);
            }
            final int drawableId_left = cont.getResources().getIdentifier(urls,"drawable", cont.getPackageName());
            holder.style_img_left.setImageResource(drawableId_left);
            holder.left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Map map=mDatas.get(position);
                    final CommonDialog dialog = new CommonDialog(cont);
                    dialog.setMessage(" ")
                            .setImageResId(-1)
                            .setTitle("系统提醒")
                            .setPositive("设为主题")
                            .setNegtive("预览主题")
                            .setSingle(2)
                            .setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                        @Override
                        public void onPositiveClick() {
                            dialog.dismiss();
                            SharedPreferences sp = cont.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(Const.styleid, map.get("key").toString());
                            editor.putString(Const.stylename, map.get("text").toString());
                            editor.putBoolean(Const.reload, true);
                            editor.commit();
                            Toast.makeText(StyleActivity.this,"设置成功！",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNegtiveClick() {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("id", drawableId_left);
                            intent.setClass(cont,ImageActivity.class);
                            startActivity(intent);
                        }
                    }).show();

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
            TextView id_num_left,id_num_left_id;
            ImageView style_img_left;
            ImageView is_checked;
            public MyViewHolder(View view)
            {
                super(view);
                left = (LinearLayout) view.findViewById(R.id.left);
                id_num_left = (TextView) view.findViewById(R.id.id_num_left);
                id_num_left_id = (TextView) view.findViewById(R.id.id_num_left_id);
                style_img_left = (ImageView) view.findViewById(R.id.style_img_left);
                is_checked = (ImageView) view.findViewById(R.id.is_checked);
            }
        }
    }

}
