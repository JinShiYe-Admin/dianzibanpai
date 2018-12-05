package net.jiaobaowang.gonggaopai.pwd;

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

public class KeyboardView extends RelativeLayout {

    private RelativeLayout rlBack;
    private RecyclerView recyclerView;
    private List<String> datas;
    private KeyboardAdapter adapter;
    private Animation animationIn;
    private Animation animationOut;
    private Context context;

    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.layout_key_board, this);
        rlBack = (RelativeLayout) findViewById(R.id.rl_back);
        rlBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) { // 点击关闭键盘
                dismiss();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        initData();
        initView();
        initAnimation();
    }

    // 填充数据
    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            if (i < 9) {
                datas.add(String.valueOf(i + 1));
            } else if (i == 9) {
                datas.add("0");
            } else {
                datas.add("");
            }
        }
    }

    // 设置适配器
    private void initView() {
        GridLayoutManager llmv = new GridLayoutManager(getContext(),3);
        llmv.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position==10){
                    return 2;
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(llmv);
        adapter = new KeyboardAdapter(getContext(), datas);
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

    public void setOnKeyBoardClickListener(KeyboardAdapter.OnKeyboardClickListener listener) {
        adapter.setOnKeyboardClickListener(listener);
    }

    public List<String> getDatas() {
        return datas;
    }

    public RelativeLayout getRlBack() {
        return rlBack;
    }
}
