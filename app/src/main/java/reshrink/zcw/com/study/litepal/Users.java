package reshrink.zcw.com.study.litepal;

//import io.realm.RealmObject;


import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by lenovo on 2018/4/9.
 */

public class Users extends DataSupport {

    private int userId;//id,主键
    private String userName;
    private String pwd;

    public Users(String name, String pwd,int id) {
        userName = name;
        this.pwd = pwd;
        this.userId=id;
    }


    public Users() {
    }
}
