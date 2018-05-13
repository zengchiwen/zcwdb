package reshrink.zcw.com.study.objectbox;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.NameInDb;

/**
 * Created by lenovo on 2018/4/9.
 */

@Entity
public class User {

    @NameInDb("name")
    private String name;
    @NameInDb( "pwd")
    private String pwd;
    @Id
    private  long id;

    public User(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

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
    public User(String name, String pwd, long id) {
        this.name = name;
        this.pwd = pwd;
        this.id = id;
    }

    public User() {
    }
}
