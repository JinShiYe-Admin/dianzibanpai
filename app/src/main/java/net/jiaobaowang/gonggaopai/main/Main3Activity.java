package net.jiaobaowang.gonggaopai.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.jiaobaowang.gonggaopai.R;
import net.jiaobaowang.gonggaopai.base.BaseActivity;
import net.jiaobaowang.gonggaopai.entry.Attendance;
import net.jiaobaowang.gonggaopai.util.BitConverter;
import net.jiaobaowang.gonggaopai.util.CommonDialog;
import net.jiaobaowang.gonggaopai.util.Const;
import net.jiaobaowang.gonggaopai.util.MyQueue;
import net.jiaobaowang.gonggaopai.util.SocketUtils;
import net.jiaobaowang.gonggaopai.util.Validate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main3Activity extends BaseActivity {
    private List<Map> idList;
    private SocketUtils utils;
    private EditText text;
    private Button btn;
    private Handler socketTimeHandler;
    private Runnable socketRunnable,socketCancelRunnable;
    private MyQueue queue;
    private Handler mHandler;
    private Runnable mScanningFishedRunnable;
    private ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(5);
    @Override
    public int initLayout() {
        return R.layout.activity_main3;
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
    int zzzzz=0;
    Timer t;
    @Override
    public void doBusiness(Context mContext) {
        socketTimeHandler =new Handler();
        mHandler=new Handler();
        queue=new MyQueue();
        final Handler socketHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x665://连接已开启
                        byte[] json = (byte[]) msg.obj;
                        System.out.println("创建连接并发送");
                        utils.send(json);
                        break;
                    case 0x666://数据反馈
                        byte[] responseText = (byte[]) msg.obj;
                        int pckLength=BitConverter.bytesToInt2(subByte(responseText,0,4),0);
                        int pckCommand=BitConverter.bytesToInt2(subByte(responseText,4,4),0);
                        int serNum=BitConverter.bytesToInt2(subByte(responseText,8,4),0);
                        byte state=subByte(responseText,12,1)[0];
                        System.out.println("接收到CMD_SUBMIT_RESP命令");
                        System.out.println("包长度:"+pckLength);
                        System.out.println("包命令:"+pckCommand);
                        System.out.println("流水号:"+serNum);
                        System.out.println("状态:"+state);
                        socketTimeHandler.removeCallbacks(socketRunnable);
                        socketTimeHandler.postDelayed(socketRunnable,Const.SOCKETKEEPTIME);
                        break;
                    case 0x667://socket 连接已关闭
                        System.out.println("连接已释放");
                        if(Const.DEBUG){
                            Toast.makeText(cont,"连接已释放",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 0x668://socket 连接失败
                        System.out.println("连接失败");
                        if(Const.DEBUG) {
                            Toast.makeText(cont, "socket 连接失败:" + msg.obj, Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        };
        idList=new ArrayList<>();
        String newId1= Validate.str2HexStr("2000111201");
        Map map=new HashMap();
        map.put("id",newId1);
        map.put("timestr","1124115206");
        idList.add(map);
        btn=(Button)findViewById(R.id.socketTest);
        text=(EditText) findViewById(R.id.cardId);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent startIntent = new Intent(cont,ReaderService.class);
//                startService(startIntent);





//
//                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(cont);
//                Intent cardIntent = new Intent(Const.ACTION_NAME);
//                if(zzzzz==0||zzzzz==10){
//                    cardIntent.putExtra("cardId", "123");
//                }else if(zzzzz==4||zzzzz==6){
//                    cardIntent.putExtra("cardId", "456");
//                }else{
//                    cardIntent.putExtra("cardId", "789");
//                }
//                zzzzz++;
//                manager.sendBroadcast(cardIntent);

//                Intent intent = new Intent("android.intent.action.setpoweronoff");
//                int[] timeon = new int[]{2018,12,14,9,3,0}; //开机时间
//                intent.putExtra("timeon", timeon);
//                int[] timeoff = new int[]{2018,12,13,11,39,0}; //关机时间
//                intent.putExtra("timeoff", timeoff);
//                intent.putExtra("enable", true); //true 为启用， false 为取消此功能
//                sendBroadcast(intent);

                mScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        if(t!=null){
                            t.cancel();
                            t=null;
                        }
                        t=new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                System.out.println("timer已经关闭");
                                t.cancel();
                            }
                        }, 500);
                    }
                }, 1, 1000, TimeUnit.MILLISECONDS);





//                boolean isToLarge=alertDialog(text.getText().toString());
//                text.setText("");
//                if(isToLarge){
//                    mHandler.removeCallbacks(mScanningFishedRunnable);
//                    mHandler.post(mScanningFishedRunnable);
//                }else{
//                    mHandler.removeCallbacks(mScanningFishedRunnable);
//                    mHandler.postDelayed(mScanningFishedRunnable,Const.MESSAGE_DELAY);
//                }
//                List<RecordData> recordResp2= RecordData.listAll(RecordData.class);
//                    System.out.println(recordResp2.toString());
//
//                List list =new ArrayList();
//                list.addAll(idList);
//                List<Map> li=Validate.removeDuplicate(list);
////                idList.clear();
//                StringBuffer buffer=new StringBuffer();
//                for (int i = 0; i <5; i++) {
//                    Map qdMap=li.get(0);
//                    String sysId="11";
//                    String cardId=qdMap.get("id").toString();
//                    String timeStr=qdMap.get("timestr").toString();
//                    buffer.append(sysId+timeStr+cardId);
//                }
//                int packageLength=buffer.toString().getBytes().length+17;//包长度
//                byte[] pLength= BitConverter.intToByte2(packageLength);
//                int packageCommand=Const.CMD_SUBMIT;//包命令
//                byte[] pCommand= BitConverter.intToByte2(packageCommand);
//                int serNum= Const.serNum=(Const.serNum+1);//流水号
//                byte[] sNum= BitConverter.intToByte2(serNum);
////                int blandId= Integer.parseInt(Const.blandid);//设备号
//                int blandId= 100130;//设备号
//                byte[] bId= BitConverter.intToByte2(blandId);
//                String blandLv= "0";//设备类型
//                byte[] bLv =new byte[1];
//                bLv[0]=(byte)Integer.parseInt(blandLv);
//                byte[] content=buffer.toString().getBytes();
////提交数据
//                final byte[] bytes=new byte[packageLength];
//                System.arraycopy(pLength,0,bytes,0,pLength.length);
//                System.arraycopy(pCommand,0,bytes,pLength.length,pCommand.length);
//                System.arraycopy(sNum,0,bytes,pLength.length+pCommand.length,sNum.length);
//                System.arraycopy(bId,0,bytes,pLength.length+pCommand.length+sNum.length,bId.length);
//                System.arraycopy(bLv,0,bytes,pLength.length+pCommand.length+sNum.length+bId.length,bLv.length);
//                System.arraycopy(content,0,bytes,pLength.length+pCommand.length+sNum.length+bId.length+bLv.length,content.length);
//
//                System.out.println("包长度:"+ BitConverter.ToInt32(subByte(bytes,0,4),0));
//                System.out.println("包命令:"+BitConverter.ToInt32(subByte(bytes,4,4),0));
//                System.out.println("流水号:"+BitConverter.ToInt32(subByte(bytes,8,4),0));
//                System.out.println("设备号:"+BitConverter.ToInt32(subByte(bytes,12,4),0));
//                System.out.println("设备类型:"+subByte(bytes,16,1)[0]);
//                System.out.println("包体结构:"+new String(subByte(bytes,17,bytes.length-17)));
//                String pacDe=new String(subByte(bytes,17,bytes.length-17));
//                if(Validate.noNull(pacDe)){
//                    String pac=pacDe.substring(0,20);
//                    String zkjId=pac.substring(0,2);
//                    String tim=pac.substring(2,12);
//                    String kId=pac.substring(12,20);
//                    System.out.println("子卡机ID:"+zkjId);
//                    System.out.println("卡ID(16进制):"+kId);
//                    System.out.println("卡ID:"+Validate.hexStr2Str(kId));
//                    System.out.println("时间戳:"+tim);
//                }
//                utils=SocketUtils.getInstance(socketHandler,bytes,cont);
//                if(!utils.isConnected()){
//                    utils.connect();
//                }else{
//                    utils.send(bytes);
//                    li.clear();
//                }

            }
        });
        socketRunnable = new Runnable() {
            @Override
            public void run() {
                int packageLength=12;//包长度
                byte[] pLength= BitConverter.intToByte2(packageLength);
                int packageCommand=Const.CMD_TERMINATE;//包命令
                byte[] pCommand= BitConverter.intToByte2(packageCommand);
                int serNum= Const.serNum=(Const.serNum+1);//流水号
                byte[] sNum= BitConverter.intToByte2(serNum);
                final byte[] bytes=new byte[12];
                System.arraycopy(pLength,0,bytes,0,pLength.length);
                System.arraycopy(pCommand,0,bytes,pLength.length,pCommand.length);
                System.arraycopy(sNum,0,bytes,pLength.length+pCommand.length,sNum.length);
                utils=SocketUtils.getInstance(socketHandler,bytes,cont);
                if(utils.isConnected()){
                    utils.send(bytes);
                }
                socketTimeHandler.removeCallbacks(socketCancelRunnable);
                socketTimeHandler.postDelayed(socketCancelRunnable,Const.SOCKETTIMEOUTCLOSETIME);

            }
        };
        socketCancelRunnable = new Runnable() {
            @Override
            public void run() {
                if(utils.isConnected()){
                    utils.close();
                }
            }
        };
        mScanningFishedRunnable = new Runnable() {
            @Override
            public void run() {
                LinkedList list =new LinkedList();
                list.addAll(queue.getList());
                queue.clear();
                ListIterator iterator=list.listIterator();
                while (iterator.hasNext()){
                    Map info = (Map) iterator.next();
                    String id= (String) info.get("id");
                    Long time= (Long) info.get("timestr");
                    System.out.println("id:"+id+"time:"+time);
                    List<Attendance> attendanceList = Attendance.find(Attendance.class,"CARD_ID=?",new String[]{id},null,"TIME_STR DESC","0,1");
                    if(attendanceList.size()==0){
                        saveInfo(id,time);
                    }else{
                        Attendance attend=attendanceList.get(0);
                        Long preTimeStr=attend.getTimeStr();
                        if(getTime(time,preTimeStr)){
                            saveInfo(id,time);
                        }
                    }
                }

            }
        };
    }
    private void saveInfo(String id,Long time){
        Attendance attendance=new Attendance();
        attendance.setCardId(id);
        attendance.setTimeStr(time);
        attendance.setLx(0);
        attendance.setIsUpload(0);
        attendance.save();

//        List<Attendance> list=Attendance.listAll(Attendance.class);
//        System.out.println(list.toString());
    }


    private int getSerNum(){//获取流水号
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME,Context.MODE_PRIVATE);
        int serNum=sp.getInt("serNumNNN", 0);
        return serNum;
    }

    private void setSerNum(int serNum){//保存流水号
        SharedPreferences sp = this.getSharedPreferences(Const.SPNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("serNumNNN",serNum);
        editor.commit();
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

    private boolean alertDialog(String id){
        final CommonDialog dialog = new CommonDialog(cont);
        dialog.setMessage(" ")
                .setImageResId(R.drawable.success)
                .setTitle("系统提示")
                .setSingle(0)
                .show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
                t.cancel();
            }
        }, 800);

        String newId=id;
        Long timestr= System.currentTimeMillis();
        Map map=new HashMap();
        map.put("id",id);
        map.put("timestr",timestr);
        if(queue.QueueLength()>0){
            ListIterator iterator=queue.QueueAll();
            boolean canIn=true;
            while (iterator.hasNext()){//遍历队列，看之前这个卡ID是否有签到，如果有，判断是否可以再次签到，可以就放入队列
                Map info = (Map) iterator.next();
                String qdId= (String) info.get("id");
                if(newId.equals(qdId)){
                    Long preTime= (Long) info.get("timestr");
                    canIn=getTime(timestr,preTime);
                    if(!canIn){
                        System.out.println("允许时间外打卡");
                    }
                }
            }
            if(canIn){
                System.out.println("新插入的map:"+map.toString());
                queue.enQueue(map);
            }
        }else{
            queue.enQueue(map);
        }
        //如果队列 长度大于100，则直接通知存储到数据库,否则使用延时存储
        if(queue.QueueLength()>=Const.MAXUPLOADNUM){
            return true;
        }else{
            return false;
        }
    }

    private boolean getTime(Long afterCardTime,Long preCardTime){
        Long cti=afterCardTime-preCardTime;

        String time =new SimpleDateFormat("MMddHHmmss").format(preCardTime);
        String time2 =new SimpleDateFormat("MMddHHmmss").format(afterCardTime);

        System.out.println("前一次签到："+time+",后一次签到："+time2+",两次签到时间差"+cti);
        if(cti>Const.JGTIME) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean widgetOnKey(int keyCode, KeyEvent keyEvent) {
        return false;
    }
}
