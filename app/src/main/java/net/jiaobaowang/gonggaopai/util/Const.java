package net.jiaobaowang.gonggaopai.util;


public class Const {

    /****可维护字段*****/
    public static final boolean DEBUG =false; // 是否显示toast

//  public static final String baseUrl="http://192.168.1.203:8080/banPaiIndex/test12/html/index/firstPage.html";    //设置班牌类型成功后跳转的页面路径
//  public static final String defaultUrl="http://192.168.1.203:8080/banPaiIndex/test12/html/index/firstPage.html";    //第一次进入或没设置班牌类型时跳转的页面路径
//  public static String updateUrl="http://192.168.1.203:8080/app/a/apk";    //App更新路径
//  public static final String socketIp="192.168.1.121";    //顾工 socket数据交互接口
//  public static final int socketPort=8086;    //顾工 socket数据交互端口号

    public static final String    baseUrl="https://zyja.zhuxue101.net/appsources/html/index/firstPage.html";//    设置班牌类型成功后跳转的页面路径
    public static final String defaultUrl="https://zyja.zhuxue101.net/appsources/html/index/firstPage.html";//    第一次进入或没设置班牌类型时跳转的页面路径
    public static String updateUrl="https://boss.zhuxue101.net:446/banpai/dianzibanpai.apk";//    App更新路径
    public static final String socketIp="192.168.1.121";//    顾工 socket数据交互接口
    public static final int socketPort=8086;

    public static final  String PWD="20182018"; //班牌密码
    public static String[] ids={"班级班牌","年级班牌","学校班牌"}; //班牌类型
    public static String xx_json="[{\"key\":\"20180001\",\"text\":\"学校皮肤\",\"url\":\"xx_style_1\"}]";  //学校班牌皮肤 key：班牌编号  text：皮肤名称  url：drawable下存放的皮肤图片名称 xx开头
    public static String nj_json="[{\"key\":\"20180002\",\"text\":\"年级皮肤\",\"url\":\"nj_style_1\"}]";    //年级班牌皮肤 key：班牌编号  text：皮肤名称  url：drawable下存放的皮肤图片名称 nj开头
    public static String bj_json="[{\"key\":\"20180003\",\"text\":\"班级皮肤\",\"url\":\"bj_style_1\"}]";    //班级班牌皮肤 key：班牌编号  text：皮肤名称  url：drawable下存放的皮肤图片名称 bj开头

    public static String settings_json="[{\"key\":\"appUpdate\",\"text\":\"系统更新\",\"url\":\"settings_1\"}," +
            "{\"key\":\"blandCheck\",\"text\":\"班牌类型\",\"url\":\"settings_2\"}," +
            "{\"key\":\"styleCheck\",\"text\":\"主题设置\",\"url\":\"settings_3\"}," +
            "{\"key\":\"timeCheck\",\"text\":\"运行时间\",\"url\":\"settings_4\"}]";  //设置选项
    /*********/

    public static String cityName="";//城市名称
    public static String blandlv="";//班牌类型
    public static String blandid="";//班牌编号
    public static String styleid="";//主题编号

    public static final int CMD_ERROR = 0xFFFFFFFF;//命令错误
    public static final int CMD_CONNECT = 0x00000001;//连接
    public static final int CMD_CONNECT_RESP = 0x80000001;//连接反馈
    public static final int CMD_TERMINATE = 0x00000002; // 终止连接
    public static final int CMD_TERMINATE_RESP = 0x80000002; // 终止连接应答
    public static final int CMD_SUBMIT = 0x00000004; // 提交短信
    public static final int CMD_SUBMIT_RESP = 0x80000004; // 提交短信应答


    public static final int GO_PASSWORD = 0x110; // 输入密码页
    public static final int EXIST = 0x120; // 退出APP
    public static final int GO_SETTINGSCHECKED = 0x220;// 设置选择页
    public static final int GO_CLASSESSETTING = 0x230;// 班牌设置页
    public static final int GO_STYLESETTING = 0x240;// 主题设置页
    public static final int GO_TIMESETTING = 0x300;// 开机关机时间设置页
    public static final String SPNAME="GGP";//SharedPreferences名称
    public static final String ACTION_NAME="net.jiaobaowang.carid";//广播接收action

    public static int serNum=0;//流水号
    public static final String kID="11";//子卡机ID
    public static final int SOCKETCLOSETIME=1000*10;
    public static final int SOCKETTIMEOUTCLOSETIME=1000*10;//socket等待反馈时长，超时则关闭
    public static final int MESSAGE_DELAY=1000*10;//打卡批量发送等待时长，此时间段内如果有新打卡，则重新计算等待时长
    public static final int TIME=1000*40;//调度任务执行周期 @TEST
    public static final int TIMEOUT=1000*10;//socket连接超时等待时长
    public static final int JGTIME=1000*60;//两次打卡允许的间隔时间差
    public static final int MAXUPLOADNUM=50;//队列数上限
}
