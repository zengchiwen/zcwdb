package reshrink.zcw.com.study.realeam;

//import io.realm.RealmObject;


import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * reelm必須要有空的构造函数
 * Created by lenovo on 2018/4/9.
 */

public class Users extends  RealmObject implements RealmModel{

    public static final String USERID="userId";
    @PrimaryKey
    private int userId;//id,主键
    @Required
    private String userName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Required
    private String pwd;

    public Users(String name, String pwd, int id) {
        userName = name;
        this.pwd = pwd;
        this.userId=id;
    }


    public Users() {
    }
}
