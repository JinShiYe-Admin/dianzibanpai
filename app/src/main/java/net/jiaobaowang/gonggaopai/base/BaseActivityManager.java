package net.jiaobaowang.gonggaopai.base;

import android.app.Activity;
import android.content.Context;

import java.util.Iterator;
import java.util.Stack;

/**
 * 类名：com.gxcwyth.base.BasActivityManager.class
 * 描述：应用程序Activity管理类：用于Activity管理和应用程序退出
 * Created by 刘帅 on 2016/8/9.
 */
public class BaseActivityManager {
    private static Stack<BaseActivity> activityStack;
    private static BaseActivityManager instance;

    private BaseActivityManager() {
    }

    /**
     * 单实例 , UI无需考虑多线程同步问题
     */
    public static BaseActivityManager getAppManager() {
        if (instance == null) {
            instance = new BaseActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(BaseActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<BaseActivity>();
        }
        activityStack.add(activity);

    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public BaseActivity currentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        BaseActivity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public BaseActivity findActivity(Class<?> cls) {
        BaseActivity activity = null;
        for (BaseActivity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        BaseActivity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public boolean isActivityStarted(Class<?> cls) {
        boolean isContains=false;
        for (BaseActivity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                isContains=true;
            }
        }
        return isContains;
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        Iterator<BaseActivity> iterator = activityStack.iterator();
        while(iterator.hasNext()){
            BaseActivity activity = iterator.next();
            if(!activity.getClass().equals(cls)){
                iterator.remove();
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        Iterator<BaseActivity> iterator = activityStack.iterator();
        while(iterator.hasNext()){
            BaseActivity activity = iterator.next();
            activity.finish();
            iterator.remove();
            finishActivity(activity);
        }
        activityStack.clear();
    }

    /**
     * 应用程序退出
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
//            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            activityMgr.killBackgroundProcesses(context.getPackageName());
//            System.exit(0);
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
