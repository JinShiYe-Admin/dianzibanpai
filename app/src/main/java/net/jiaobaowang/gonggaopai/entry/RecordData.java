package net.jiaobaowang.gonggaopai.entry;

import com.orm.SugarRecord;

import java.util.Arrays;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2018/11/23.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */

public class RecordData extends SugarRecord {
    public byte[] datas;
    public String sendTime;
    public int isUpload;//0 未发送 1已发送
    public RecordData(){

    }

    public RecordData( byte[] data,String sendTime,int isUpload){
        this.datas=data;
        this.sendTime=sendTime;
        this.isUpload=isUpload;
    }

    public byte[] getData() {
        return datas;
    }

    public void setData(byte[] data) {
        this.datas = data;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }



    @Override
    public String toString() {
        return "RecordData{" +
                "datas=" + Arrays.toString(datas) +
                ", sendTime='" + sendTime + '\'' +
                ", isUpload=" + isUpload +
                '}';
    }
}
