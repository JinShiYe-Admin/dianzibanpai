package net.jiaobaowang.gonggaopai.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import net.jiaobaowang.gonggaopai.entry.Attendance;
import net.jiaobaowang.gonggaopai.entry.RecordData;
import net.jiaobaowang.gonggaopai.receiver.AlarmReceiver;
import net.jiaobaowang.gonggaopai.util.BitConverter;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.SocketSqlUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台服务类，用于定时查找数据库未上传数据并上传
 */
public class UploadService extends Service {
    public int number = 0; //记录alertdialog出现次数
    private AlarmManager manager;
    private PendingIntent pi;
    private SocketSqlUtils socketSqlUtils;
    private  Handler socketSqlHandler;
    private Runnable socketRunnable,socketCancelRunnable,socketRConnectRunnable;
    private Handler mHandler,socketTimeHandler,socketRConnectHandlere;
    private List<Attendance> attendanceList=new ArrayList<>();
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
                        attendanceList = Attendance.find(Attendance.class,"IS_UPLOAD=?",new String[]{"0"},null,"TIME_STR ASC","0,"+Const.MAXUPLOADNUM+"");
                        System.out.println("获取到未上传数据条数："+attendanceList.size());
                        if(attendanceList.size()>0){

                            byte[] data=getByte(attendanceList);
                            sendListBySocket(data);
                        }
                        break;
                }
            }
        };
        socketTimeHandler=new Handler();
        socketRConnectHandlere=new Handler();
        if (number!=0) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    Log.e("bai", "executed at " + new Date().toString());
                    mHandler.sendEmptyMessage(1);
//                }
//            }).start();
        }
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this,AlarmReceiver.class);
        pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+Const.TIME,pi);//这个闹钟时间被安卓（5.1）限定为最短5S，无法打到秒级定时任务的要求。
        if(number>Integer.MAX_VALUE){
            number=1;
        }
        number++;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.cancel(pi);
        System.out.println("service已关闭");
    }

    private void sendListBySocket(byte[] data){
        socketSqlHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x665://连接已开启
                        byte[] json = (byte[]) msg.obj;
                        System.out.println("创建连接并发送");
                        int serNum=BitConverter.bytesToInt2(subByte(json,8,4),0);
                        String senTime=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        RecordData record=new RecordData(json,senTime,0);
                        record.setId(Long.valueOf(serNum));
                        record.save();
//                        Toast.makeText(UploadService.this,"创建连接并发送",Toast.LENGTH_LONG).show();
                        socketSqlUtils.send(json);
                        break;
                    case 0x666://数据反馈
                        byte[] responseText = (byte[]) msg.obj;
                        System.out.println("包长度:"+BitConverter.bytesToInt2(subByte(responseText,0,4),0));
                        System.out.println("包命令:"+BitConverter.bytesToInt2(subByte(responseText,4,4),0));
                        System.out.println("流水号:"+BitConverter.bytesToInt2(subByte(responseText,8,4),0));
                        System.out.println("状态:"+subByte(responseText,12,1)[0]);
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
                        }

                        socketTimeHandler.removeCallbacks(socketRunnable);
                        socketTimeHandler.post(socketRunnable);
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
        socketRunnable = new Runnable() {
            @Override
            public void run() {
                byte[] bytes=getCancelByte();
                socketSqlUtils= SocketSqlUtils.getInstance(socketSqlHandler,bytes);
                if(socketSqlUtils.isConnected()){
                    socketSqlUtils.send(bytes);
                }
                socketTimeHandler.removeCallbacks(socketCancelRunnable);
                socketTimeHandler.postDelayed(socketCancelRunnable,Const.SOCKETTIMEOUTCLOSETIME);
            }
        };
        socketCancelRunnable = new Runnable() {
            @Override
            public void run() {
                if(socketSqlUtils.isConnected()){
                    socketSqlUtils.close();
                }
            }
        };
        socketRConnectRunnable = new Runnable() {
            @Override
            public void run() {
                if(socketSqlUtils!=null){
                    socketSqlUtils.connect();
                }
            }
        };
        byte[] byteString=data;
        if(Const.DEBUG){
            StringBuffer buffer=new StringBuffer();
            for (int i = 0; i <byteString.length ; i++) {
                buffer.append(byteString[i]);
            }
            System.out.println("要发送的数据："+buffer.toString());
        }
        socketSqlUtils = SocketSqlUtils.getInstance(socketSqlHandler,byteString);
        if(!socketSqlUtils.isConnected()){
            socketSqlUtils.connect();
        }else{
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
        int serNum = Const.serNum+1;//流水号
        Const.serNum=serNum;
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("serNum",Const.serNum);
        editor.commit();

        byte[] sNum= BitConverter.intToByte2(serNum);
        int blandId= Integer.parseInt(Const.blandid);//设备号
        byte[] bId= BitConverter.intToByte2(blandId);
        String blandLv= Const.blandlv;//设备类型
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
        int packageLength=12;//包长度
        byte[] pLength= BitConverter.intToByte2(packageLength);
        int packageCommand=Const.CMD_TERMINATE;//包命令
        byte[] pCommand= BitConverter.intToByte2(packageCommand);
        int serNum= Const.serNum+1;//流水号
        Const.serNum=serNum;
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("serNum",Const.serNum);
        editor.commit();
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
}