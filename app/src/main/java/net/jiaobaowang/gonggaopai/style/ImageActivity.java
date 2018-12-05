package net.jiaobaowang.gonggaopai.style;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import net.jiaobaowang.gonggaopai.R;
import net.jiaobaowang.gonggaopai.base.BaseActivity;

public class ImageActivity extends BaseActivity {

    @Override
    public int initLayout() {
        return R.layout.activity_image;
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
        ImageView view =(ImageView)findViewById(R.id.img_look);
        Intent in =getIntent();
        int id=in.getIntExtra("id",0);
        view.setImageResource(id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
        finish();
        return false;
    }

}
