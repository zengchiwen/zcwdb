package reshrink.zcw.com.study.db;

/**
 * Created by lenovo on 2018/3/8.
 */
@DbTable("tb_user")
public class User {
    @DbField("c_name")
    public String name;
    @DbField("c_pwd")
    public String pwd;
    @DbField("id")
    public int id;

    public User(String name, String pwd, int id) {
        this.name = name;
        this.pwd = pwd;
        this.id = id;
    }

    public User(int id) {
        this.id = id;
    }
}
