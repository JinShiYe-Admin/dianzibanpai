package net.jiaobaowang.gonggaopai.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2018/11/21.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */

public class SocketUtils {

    private static SocketUtils socketUtils=null;
    private SocketConnect socketConnect=null;
    private Handler handler=null;
    private byte[] json=null;
    private Context conn;
    private SocketUtils() {
        super();
    }

    public static SocketUtils getInstance(Handler _handler, byte[] _json, Context conn){
        if(socketUtils==null){
            socketUtils=new SocketUtils();
        }
        socketUtils.handler=_handler;
        socketUtils.json=_json;
        socketUtils.conn=conn;
        return socketUtils;
    }

    public boolean isConnected(){
        if(socketConnect==null){
            return false;
        }
        return socketConnect.isConnected;
    }

    public void connect(){
        socketConnect = new SocketConnect(Const.socketIp, Const.socketPort,
                new SocketConnect.Callback() {
                    @Override
                    public void onSend() {
                        System.out.println("socket正在发送数据");
                    }

                    @Override
                    public void onReceived(byte[] msg) {
                        System.out.println("接收到的数据："+BitConverter.bytesToInt2(msg,0));
//                        System.out.println("接收到的数据："+new String(msg));
//                        System.out.println("接收到的数据："+"接收到的数据".getBytes());
//                        byte[] bbb="接收到的数据".getBytes();
                        int pckCommand=BitConverter.bytesToInt2(subByte(msg,4,4),0);
                        if(pckCommand==Const.CMD_SUBMIT_RESP){
                            Message msgs=new Message();
                            msgs.what=0x666;
                            msgs.obj=msg;
                            handler.sendMessage(msgs);
                        }else if(pckCommand==Const.CMD_TERMINATE){
                            System.out.println("接收到CMD_TERMINATE命令，连接已断开");
                            close();
                        }else if(pckCommand==Const.CMD_TERMINATE_RESP){
                            System.out.println("接收到CMD_TERMINATE_RESP命令，连接已断开");
                            close();
                        }

                    }

                    @Override
                    public void onError(String msg) {
                        System.out.println("连接失败:"+msg);
                        final CommonDialog dialog = new CommonDialog(conn);
                        Message msgs=new Message();
                        msgs.what=0x668;
                        msgs.obj=json;
                        handler.sendMessage(msgs);
                    }

                    @Override
                    public void onDisconnected() {
                        System.out.println("Socket由连接->断开");
                        Message msg=new Message();
                        msg.what=0x667;
                        msg.obj="socket 连接已释放";
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onConnected() {
                        System.out.println("连接成功");
                        Message msg=new Message();
                        msg.what=0x665;
                        msg.obj=json;
                        handler.sendMessage(msg);

                    }
                });
        socketConnect.connect();
    }

    public void send(byte[] json){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                socketConnect.send(json);
//            }
//        }).start();
    }

    public void close(){
        socketConnect.disconnect();
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
