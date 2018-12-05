package net.jiaobaowang.gonggaopai.classes;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import net.jiaobaowang.gonggaopai.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2017/6/23.
 */

public class KeyboardViewClasses extends RelativeLayout {

    private RelativeLayout rlBack;
    private RecyclerView recyclerView;
    private List<String> datas;
    private KeyboardAdapterClasses adapter;
    private Animation animationIn;
    private Animation animationOut;


    public KeyboardViewClasses(Context context) {
        this(context, null);
    }

    public KeyboardViewClasses(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardViewClasses(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_key_board_classes, this);
        rlBack = (RelativeLayout) findViewById(R.id.rl_back_classes);
        rlBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) { // 点击关闭键盘
                dismiss();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_classes);

        initData();
        initView();
        initAnimation();
    }

    // 填充数据
    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            if (i < 9) {
                datas.add(String.valueOf(i + 1));
            } else if (i == 9) {
                datas.add("0");
            } else if (i == 10){
                datas.add("");
            } else if (i == 11){
                datas.add("取消");
            } else if (i == 12){
                datas.add("确定");
            }
        }
    }

    // 设置适配器
    private void initView() {
        GridLayoutManager llmv = new GridLayoutManager(getContext(),4);
        llmv.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position==10||position==11||position==12){
                    return 2;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(llmv);
        adapter = new KeyboardAdapterClasses(getContext(), datas);
        recyclerView.setAdapter(adapter);
    }

    // 初始化动画效果
    private void initAnimation() {
        animationIn = AnimationUtils.loadAnimation(getContext(), R.anim.keyboard_in);
        animationOut = AnimationUtils.loadAnimation(getContext(), R.anim.keyboard_out);
    }

    // 弹出软键盘
    public void show() {
        startAnimation(animationIn);
        setVisibility(VISIBLE);
    }

    // 关闭软键盘
    public void dismiss() {
        if (isVisible()) {
            startAnimation(animationOut);
            setVisibility(GONE);
        }
    }

    // 判断软键盘的状态
    public boolean isVisible() {
        if (getVisibility() == VISIBLE) {
            return true;
        }
        return false;
    }

    public void setOnKeyBoardClickListener(KeyboardAdapterClasses.OnKeyboardClickListener listener) {
        adapter.setOnKeyboardClickListener(listener);
    }

    public List<String> getDatas() {
        return datas;
    }

    public RelativeLayout getRlBack() {
        return rlBack;
    }
}
