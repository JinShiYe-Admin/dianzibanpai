package net.jiaobaowang.gonggaopai.util;


public class Const {

  public static final boolean DEBUG =false; // 是否显示toast
    /****可维护字段*****/
    //开发服务器
//    public static String baseUrl="http://192.168.1.203:8080/banPaiIndex/test12/html/index/firstPage.html";    //设置班牌类型成功后跳转的页面路径
//    public static String updateUrl="http://192.168.1.203:8080/app/a.apk";    //App更新路径
//    public static String socketIp="192.168.1.121";    //顾工 socket数据交互接口
//    public static int socketPort=8086;    //顾工 socket数据交互端口号

    //正式服务器
//    public static String baseUrl="https://zyja.zhuxue101.net/appsources/html/index/firstPage.html";//    设置班牌类型成功后跳转的页面路径
//    public static String updateUrl="https://boss.zhuxue101.net:446/banpai/dianzibanpai.apk";//    App更新路径
//    public static String socketIp="118.190.81.221";//    顾工 socket数据交互接口
//    public static int socketPort=8086;

    //测试服务器
    public static String baseUrl="http://gxcs.jiaobaowang.net/appsources/html/index/firstPage.html";//设置班牌类型成功后跳转的页面路径
    public static String updateUrl="http://zhxy.jiaobaowang.net:8015/appupdate/banpai/dianzibanpai-test.apk"; //App更新路径
    public static String socketIp="118.190.81.221";//顾工 socket数据交互接口
    public static int socketPort=8086;

    public static String[] ids={"班级班牌","年级班牌","学校班牌"}; //班牌类型
    public static String xx_json="[{\"key\":\"20180001\",\"text\":\"学校皮肤\",\"url\":\"xx_style_1\"}]";  //学校班牌皮肤 key：班牌编号  text：皮肤名称  url：drawable下存放的皮肤图片名称 xx开头
    public static String nj_json="[{\"key\":\"20180002\",\"text\":\"年级皮肤\",\"url\":\"nj_style_1\"}]";    //年级班牌皮肤 key：班牌编号  text：皮肤名称  url：drawable下存放的皮肤图片名称 nj开头
    public static String bj_json="[{\"key\":\"20180003\",\"text\":\"班级皮肤\",\"url\":\"bj_style_1\"}]";    //班级班牌皮肤 key：班牌编号  text：皮肤名称  url：drawable下存放的皮肤图片名称 bj开头

    public static String settings_json="[" +
            "{\"key\":\"blandCheck\",\"text\":\"班牌类型\",\"url\":\"settings_2\"}," +
            "{\"key\":\"styleCheck\",\"text\":\"班牌主题\",\"url\":\"settings_3\"}," +
            "{\"key\":\"timeCheck\",\"text\":\"自动开关机时间\",\"url\":\"settings_4\"}," +
            "{\"key\":\"passwordsetting\",\"text\":\"班牌密码\",\"url\":\"passwordsetting\"}," +
//            "{\"key\":\"ipseeting\",\"text\":\"考勤数据上传地址\",\"url\":\"ipseeting\"}," +
//            "{\"key\":\"htmlseeting\",\"text\":\"主页地址\",\"url\":\"html_address\"}," +
//            "{\"key\":\"appupdateseeting\",\"text\":\"APP更新地址\",\"url\":\"settings_1\"}," +
            "{\"key\":\"appUpdate\",\"text\":\"系统更新\",\"url\":\"download_address\"},"+
            "{\"key\":\"closeSystem\",\"text\":\"关机\",\"url\":\"close_system\"}" +
            "]";  //设置选项
    /*********/

    public static final String cityName="cityName";//城市名称
    public static final String blandlv="blandlv";//班牌类型
    public static final String blandid="blandid";//班牌编号
    public static final String styleid="styleid";//主题编号
    public static final String stylename="styleName";//主题名称
    public static final String reload="reload";//加载webview
    public static final String startTime="startTime";//自动开机时间
    public static final String shutdownTime="shutdownTime";//自动关机时间
    public static final String socketip="socketip";//socket ip
    public static final String socketport="socketport";//socket port
    public static final String htmladdress="htmladdress";//html网页地址
    public static final String updateaddress="updateaddress";//APP更新地址

    public static final String PASSWORD="20192019";//班牌超管密码
    public static final String PASSWORDTEACHER="12345678";//班牌教师管理密码

    public static final int CMD_ERROR = 0xFFFFFFFF;//命令错误
    public static final int CMD_CONNECT = 0x00000001;//连接
    public static final int CMD_CONNECT_RESP = 0x80000001;//连接反馈
    public static final int CMD_TERMINATE = 0x00000002; // 终止连接
    public static final int CMD_TERMINATE_RESP = 0x80000002; // 终止连接应答
    public static final int CMD_SUBMIT = 0x00000004; // 提交短信
    public static final int CMD_SUBMIT_RESP = 0x80000004; // 提交短信应答


    public static final int GO_PASSWORD = 0x110; // 输入密码页
    public static final int EXIST = 0x120; // 退出APP
    public static final String SPNAME="GGP";//SharedPreferences名称
    public static final String ACTION_NAME="net.jiaobaowang.carid";//广播接收action

    public static final int closeTimeout=1000*60*2;//异常开机后自动关机等待时间
    public static final int tapReturnTime=60*30;//无点击事件后30分钟返回主页面
    //upload相关
    public static int serNum=0;//流水号
    public static final String kID="11";//子卡机ID
    public static final int SOCKETKEEPTIME=1000*10;//socket通道保持时长没如果没有数据发送则关闭
    public static final int SOCKETTIMEOUTCLOSETIME=1000*10;//socket等待反馈时长，超时则关闭
    public static final int SOCKETRCONNECTTIME=1500*10;//socket重连等待周期
    public static final int TIME=500;//调度任务执行周期 @TEST
    public static final int TIMEOUT=1000*10;//socket连接超时等待时长
    //save相关
    public static final int JGTIME=1000*60;//两次打卡允许的间隔时间差





    //暂时没用了
    public static final int MAXUPLOADNUM=50;//队列数上限
    public static final int MESSAGE_DELAY=1000*10;//打卡批量发送等待时长，此时间段内如果有新打卡，则重新计算等待时长

}
