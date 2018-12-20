package net.jiaobaowang.gonggaopai.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import net.jiaobaowang.gonggaopai.entry.Attendance;
import net.jiaobaowang.gonggaopai.entry.RecordData;
import net.jiaobaowang.gonggaopai.util.BitConverter;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.SocketSqlUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UploadServiceScheduledExecutor extends Service {
    private SocketSqlUtils socketSqlUtils;
    private Handler socketSqlHandler;
    private Runnable socketRConnectRunnable;
    private Handler mHandler,socketRConnectHandlere;
    private List<Attendance> attendanceList=new ArrayList<>();
    private boolean canUpload=true;
    private Timer t;
    // 通过静态方法创建ScheduledExecutorService的实例
    private ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(1);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("UploadService 已启动");
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        attendanceList = Attendance.find(Attendance.class,"IS_UPLOAD=?",new String[]{"0"},null,"TIME_STR ASC","0,"+ Const.MAXUPLOADNUM+"");
                        System.out.println("获取到未上传数据条数："+attendanceList.size()+"条,"+new Date().toString());
                        if(attendanceList.size()>0){
                            canUpload=false;
                            byte[] data=getByte(attendanceList);
                            sendListBySocket(data);
                        }
                        break;
                }
            }
        };
        socketRConnectHandlere=new Handler();
        // 循环任务，以上一次任务的结束时间计算下一次任务的开始时间
        mScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if(canUpload){
                    mHandler.sendEmptyMessage(1);
                }else{
                    System.out.println("当前存在未成功上传数据");
                }
            }
        }, 1, Const.TIME, TimeUnit.MILLISECONDS);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mScheduledExecutorService.shutdownNow();
        System.out.println("service已关闭");
        super.onDestroy();
    }

    private void sendListBySocket(byte[] data){
        socketSqlHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x665://连接已开启
                        byte[] json = (byte[]) msg.obj;
                        if(json.length>0){
                            System.out.println("创建连接并发送"+json.length);
                            int serNum= BitConverter.bytesToInt2(subByte(json,8,4),0);
                            String senTime=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                            RecordData record=new RecordData(json,senTime,0);
                            record.setId(Long.valueOf(serNum));
                            record.save();
                            socketSqlUtils.send(json);
                        }else if(json.length==0){
                            reUpload();
                        }
                        break;
                    case 0x666://数据反馈
                        byte[] responseText = (byte[]) msg.obj;
                        if(Const.DEBUG){
                            System.out.println("包长度:"+BitConverter.bytesToInt2(subByte(responseText,0,4),0));
                            System.out.println("包命令:"+BitConverter.bytesToInt2(subByte(responseText,4,4),0));
                            System.out.println("流水号:"+BitConverter.bytesToInt2(subByte(responseText,8,4),0));
                            System.out.println("状态:"+subByte(responseText,12,1)[0]);
                        }
                        int state=subByte(responseText,12,1)[0];
                        int idNum=BitConverter.bytesToInt2(subByte(responseText,8,4),0);
                        int serNumS=BitConverter.bytesToInt2(subByte(responseText,8,4),0);
                        if(state==1){//命令接收成功，更改数据库中的数据
                            RecordData recordResp=RecordData.findById(RecordData.class,Long.valueOf(idNum));
                            recordResp.setIsUpload(1);
                            recordResp.save();
                            for (int i = 0; i <attendanceList.size() ; i++) {
                                Attendance attendance=attendanceList.get(i);
                                attendance.setIsUpload(1);
                                attendance.setSerNum(serNumS);
                                attendance.save();
                            }
                            System.out.println("数据发送成功");
                            reUpload();
                        }
                        if(t!=null){
                            t.cancel();
                            t=null;
                        }
                        t=new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                byte[] bytes=getCancelByte();
                                socketSqlUtils= SocketSqlUtils.getInstance(socketSqlHandler,bytes);
                                if(socketSqlUtils.isConnected()){
                                    socketSqlUtils.send(bytes);
                                }
                                t.cancel();
                            }
                        }, Const.SOCKETKEEPTIME);
                        break;
                    case 0x667://socket 连接已关闭
                        System.out.println("连接已释放");
                        break;
                    case 0x668://socket 连接失败
                        System.out.println("连接失败，正在尝试重连");
                        socketRConnectHandlere.removeCallbacks(socketRConnectRunnable);
                        socketRConnectHandlere.postDelayed(socketRConnectRunnable,Const.SOCKETRCONNECTTIME);
                        break;
                }
            }
        };
        socketRConnectRunnable = new Runnable() {
            @Override
            public void run() {
                socketSqlUtils = SocketSqlUtils.getInstance(socketSqlHandler,new byte[0]);
                socketSqlUtils.connect();
            }
        };
        byte[] byteString=data;
        if(Const.DEBUG){
            System.out.println("要发送的数据包长度："+data.length+",数据内容："+new String(data) );
        }
        socketSqlUtils = SocketSqlUtils.getInstance(socketSqlHandler,byteString);
        if(!socketSqlUtils.isConnected()){
            socketSqlUtils.connect();
        }else{
            int serNum= BitConverter.bytesToInt2(subByte(byteString,8,4),0);
            String senTime=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            RecordData record=new RecordData(byteString,senTime,0);
            record.setId(Long.valueOf(serNum));
            record.save();
            socketSqlUtils.send(byteString);
        }
    }

    /**
     * 将list封装成指定的byte[]
     */
    private byte[] getByte(List<Attendance> li){
        StringBuffer buffer=new StringBuffer();
        for (int i = 0; i <li.size(); i++) {
            Attendance attendance=li.get(i);
            String sysId=Const.kID;
            String cardId=attendance.getCardId();
            Long timeStr=attendance.getTimeStr();
            String tS=new SimpleDateFormat("MMddHHmmss").format(timeStr);
            buffer.append(sysId+tS+cardId);
        }
        int packageLength=buffer.toString().getBytes().length+17;//包长度
        byte[] pLength= BitConverter.intToByte2(packageLength);
        int packageCommand=Const.CMD_SUBMIT;//包命令
        byte[] pCommand= BitConverter.intToByte2(packageCommand);
        int serNum = getSerNum();//流水号
        serNum=serNum+1;
        setSerNum(serNum);

        byte[] sNum= BitConverter.intToByte2(serNum);
        int blandId= Integer.parseInt(getBlandId());//设备号
        byte[] bId= BitConverter.intToByte2(blandId);
        String blandLv= getBlandLv();//设备类型
        byte[] bLv =new byte[1];
        bLv[0]=(byte)Integer.parseInt(blandLv);
        byte[] content=buffer.toString().getBytes();
        byte[] bytes=new byte[packageLength];

        System.arraycopy(pLength,0,bytes,0,pLength.length);
        System.arraycopy(pCommand,0,bytes,pLength.length,pCommand.length);
        System.arraycopy(sNum,0,bytes,pLength.length+pCommand.length,sNum.length);
        System.arraycopy(bId,0,bytes,pLength.length+pCommand.length+sNum.length,bId.length);
        System.arraycopy(bLv,0,bytes,pLength.length+pCommand.length+sNum.length+bId.length,bLv.length);
        System.arraycopy(content,0,bytes,pLength.length+pCommand.length+sNum.length+bId.length+bLv.length,content.length);

        return bytes;
    }

    private byte[] getCancelByte(){
        System.out.println("发送关闭命令");
        int packageLength=12;//包长度
        byte[] pLength= BitConverter.intToByte2(packageLength);
        int packageCommand=Const.CMD_TERMINATE;//包命令
        byte[] pCommand= BitConverter.intToByte2(packageCommand);
        int serNum = getSerNum();//流水号
        serNum=serNum+1;
        setSerNum(serNum);
        byte[] sNum= BitConverter.intToByte2(serNum);
        final byte[] bytes=new byte[12];
        System.arraycopy(pLength,0,bytes,0,pLength.length);
        System.arraycopy(pCommand,0,bytes,pLength.length,pCommand.length);
        System.arraycopy(sNum,0,bytes,pLength.length+pCommand.length,sNum.length);
        return bytes;
    }

    /**
     * 截取byte数组   不改变原数组
     * @param b 原数组
     * @param off 偏差值（索引）
     * @param length 长度
     * @return 截取后的数组
     */
    public byte[] subByte(byte[] b,int off,int length){
        byte[] b1 = new byte[length];
        System.arraycopy(b, off, b1, 0, length);
        return b1;
    }

    private String getBlandId(){//获取班牌id
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        String blandlv=sp.getString("blandlv", "");
        return blandlv;
    }

    private String getBlandLv(){//获取班牌类型
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        String blandid= sp.getString("blandid", "");
        return blandid;
    }

    private int getSerNum(){//获取流水号
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        int serNum=sp.getInt("serNum", 0);
        return serNum;
    }

    private void setSerNum(int serNum){//保存流水号
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("serNum",serNum);
        editor.commit();
    }

    private void reUpload(){
        canUpload=true;
        attendanceList=new ArrayList<>();
    }
}
