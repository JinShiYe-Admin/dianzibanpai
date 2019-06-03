package net.jiaobaowang.gonggaopai.entry;

import com.orm.SugarRecord;
import com.orm.dsl.Column;

import java.io.Serializable;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2019/6/1.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */

public class Pageurl extends SugarRecord  implements Serializable {
    public String url;//主页面路径
    @Column(name = "pId", unique = true)
    public String pId;

    public  Pageurl(){}

    public  Pageurl(String pId,String url){
        this.pId=pId;
        this.url=url;
    }

    @Override
    public String toString() {
        return "Pageurl{" +
                "url='" + url + '\'' +
                ", pId='" + pId + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
