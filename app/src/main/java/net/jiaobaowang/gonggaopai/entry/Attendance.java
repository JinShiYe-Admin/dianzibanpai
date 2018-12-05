package net.jiaobaowang.gonggaopai.entry;

import com.orm.SugarRecord;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2018/11/22.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */
public class Attendance extends SugarRecord {
    public String cardId;//卡ID
    public Long timeStr;//签到时间戳
    public int lx;//打卡类型 0签到 1签退  扩展字段，暂时都默认为0
    public int isUpload;//是否已经上传 0没上传 1上传成功 2上传失败
    public int serNum;//流水号

    public Attendance(){
    }

    public Attendance(String cardId,Long timeStr,int lx,int isUpload,int serNum){
        this.cardId=cardId;
        this.timeStr=timeStr;
        this.lx=lx;
        this.isUpload=isUpload;
        this.serNum=serNum;
    }


    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Long getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(Long timeStr) {
        this.timeStr = timeStr;
    }

    public int getLx() {
        return lx;
    }

    public void setLx(int lx) {
        this.lx = lx;
    }

    public int getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }

    public int getSerNum() {
        return serNum;
    }

    public void setSerNum(int serNum) {
        this.serNum = serNum;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "cardId='" + cardId + '\'' +
                ", timeStr=" + timeStr +
                ", lx=" + lx +
                ", isUpload=" + isUpload +
                ", serNum=" + serNum +
                '}';
    }
}
