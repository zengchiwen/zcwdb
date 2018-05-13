package reshrink.zcw.com.study.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lenovo on 2018/4/9.
 */
@Entity
public class User {

    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "pwd")
    private String pwd;
    @Id(autoincrement = true)
    private  long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Generated(hash = 1758203792)
    public User(String name, String pwd, long id) {
        this.name = name;
        this.pwd = pwd;
        this.id = id;
    }

    @Generated(hash = 586692638)
    public User() {
    }
}
