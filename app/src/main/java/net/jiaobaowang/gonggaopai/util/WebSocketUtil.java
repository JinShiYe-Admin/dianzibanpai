package net.jiaobaowang.gonggaopai.util;


import android.os.Handler;
import android.os.Message;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2018/11/19.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */

public class WebSocketUtil {
    private static WebSocketUtil util=null;
    private WebSocket socket=null;
    private Handler handler=null;
    private ByteString json=null;
    private  OkHttpClient client=null;
    private EchoWebSocketListener listener=null;
    private WebSocketUtil() {
        super();
    }

    public  static WebSocketUtil getInstance(Handler _handler, ByteString _json){
        if(util==null){
            util=new WebSocketUtil();
        }
        util.handler=_handler;
        util.json=_json;
        return util;
    }

    public boolean connect() {
        boolean isAlsoOpen=false;
        if(listener==null||client==null) {
            listener = new EchoWebSocketListener();
            Request request = new Request.Builder().url(Const.socketIp).build();
            client = new OkHttpClient();
            client.newWebSocket(request, listener);
//            client.dispatcher().executorService().shutdown();
        }else{
            isAlsoOpen=true;
        }
        return isAlsoOpen;
    }

    public void send(String json){
        if(socket!=null) {
            socket.send(json);
        }
    }
    public void send(ByteString json){
        if(socket!=null) {
            socket.send(json);
        }
    }

    public void close(){
        if(socket!=null) {
            socket.close(1000, "disconnected");
        }
    }

    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            socket=webSocket;
            Message msg=new Message();
            msg.what=0x665;
            msg.obj=json;
            handler.sendMessage(msg);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
//            System.out.println("onMessage: " + text);
            Message msg=new Message();
            msg.what=0x666;
            msg.obj=text;
            handler.sendMessage(msg);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
//            System.out.println("onMessage byteString: " + bytes);
            Message msg=new Message();
            msg.what=0x666;
            msg.obj=new String(bytes.toByteArray());
            handler.sendMessage(msg);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            //onClosing是当远程端暗示没有数据交互时回调（即此时准备关闭，但连接还没有关闭）
            webSocket.close(1000, null);
//            System.out.println("onClosing: " + code + "/" + reason);
            Message msg=new Message();
            msg.what=0x669;
            msg.obj="socket 连接即将关闭";
            handler.sendMessage(msg);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            //onClosed就是当连接已经释放的时候被回调
//            System.out.println("onClosed: " + code + "/" + reason);
            Message msg=new Message();
            msg.what=0x667;
            msg.obj="socket 连接已释放";
            handler.sendMessage(msg);
            listener=null;
            client=null;
            socket=null;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            t.printStackTrace();
            //onFailure当然是失败时被回调（包括连接失败，发送失败等
//            System.out.println("onFailure: " + t.getMessage());
            Message msg=new Message();
            msg.what=0x668;
            msg.obj="socket 连接失败:"+t.getMessage();
            handler.sendMessage(msg);
            listener=null;
            client=null;
            socket=null;
        }
    }

}
