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
public class Password extends SugarRecord {
    public String password;//卡ID

    public Password(){
    }

    public Password(String password){
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Password{" +
                "password='" + password + '\'' +
                '}';
    }
}
