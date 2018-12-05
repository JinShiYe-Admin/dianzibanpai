package net.jiaobaowang.gonggaopai.util;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2018/10/10.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */

public class ToolScanner {

    private StringBuffer mStringBufferResult;

    public Handler mHandler;

    private boolean mCaps;

    private OnScanSuccessListener mOnScanSuccessListener;

    private Runnable mScanningFishedRunnable;

    private static  final  int MESSAGE_DELAY =3000;

    private Context cont;
    /**
     * 扫码设备事件解析
     * @param event
     */
    public void analysisKeyEvent(KeyEvent event) {

        int keyCode = event.getKeyCode();


        //字母大小写判断
        checkLetterStatus(event);

        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            char aChar = getInputCode(event);

            if (aChar != 0) {
                mStringBufferResult.append(aChar);
            }
            System.out.println("mStringBufferResult=="+mStringBufferResult);
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //若为回车键，直接返回
                mStringBufferResult.setLength(0);
                mStringBufferResult.append("KEYCODE_ENTER");
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.post(mScanningFishedRunnable);
            } else if(keyCode == KeyEvent.KEYCODE_BACK){
                mStringBufferResult.setLength(0);
                mStringBufferResult.append("KEYCODE_BACK");
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.post(mScanningFishedRunnable);
            }else {
                //延迟post，若500ms内，有其他事件
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.post(mScanningFishedRunnable);
//                mHandler.postDelayed(mScanningFishedRunnable, MESSAGE_DELAY);
            }

        }
    }

    /**
     * shift键检查
     * @param event
     */
    private void checkLetterStatus(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                //按着shift键，表示大写
                mCaps = true;
            } else {
                //松开shift键，表示小写
                mCaps = false;
            }
        }
    }

    //获取扫描内容
    private char getInputCode(KeyEvent event) {

        int keyCode = event.getKeyCode();

        char aChar;

        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            //字母
            aChar = (char) ((mCaps ? 'A' : 'a') + keyCode - KeyEvent.KEYCODE_A);
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            //数字
            aChar = (char) ('0' + keyCode - KeyEvent.KEYCODE_0);
        } else {
            //其他符号
            switch (keyCode) {
                case KeyEvent.KEYCODE_PERIOD:
                    aChar = '.';
                    break;
                case KeyEvent.KEYCODE_MINUS:
                    aChar = mCaps ? '_' : '-';
                    break;
                case KeyEvent.KEYCODE_SLASH:
                    aChar = '/';
                    break;
                case KeyEvent.KEYCODE_BACKSLASH:
                    aChar = mCaps ? '|' : '\\';
                    break;
                default:
                    aChar = 0;
                    break;
            }
        }

        return aChar;

    }

    //接口回调
    public interface OnScanSuccessListener {
        void onScanSuccess(String barcode);
    }

    /**
     * 返回扫码成功后的结果
     */
    private void performScanSuccess() {
        String barcode = mStringBufferResult.toString();
        if (mOnScanSuccessListener != null) {
            mOnScanSuccessListener.onScanSuccess(barcode);
        }
        mStringBufferResult.setLength(0);
    }

    public ToolScanner(Context con,OnScanSuccessListener onScanSuccessListener) {
        this.cont =con;
        mOnScanSuccessListener = onScanSuccessListener ;
        mStringBufferResult = new StringBuffer();
        mHandler =new Handler() ;
        mScanningFishedRunnable = new Runnable() {
            @Override
            public void run() {
                performScanSuccess();
            }
        };
    }

}
