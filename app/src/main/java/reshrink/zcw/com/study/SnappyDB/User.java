package reshrink.zcw.com.study.SnappyDB;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/4/10.
 */

public class User implements Serializable {
    private int userId;//id,主键
    private String userName;
    private String pwd;

    public User(String name, String pwd,int id) {
        userName = name;
        this.pwd = pwd;
        this.userId=id;
    }


    public User() {
    }
}
