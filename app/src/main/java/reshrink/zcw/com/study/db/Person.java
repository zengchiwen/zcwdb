package reshrink.zcw.com.study.db;

/**
 * Created by lenovo on 2018/3/8.
 */
@DbTable("tb_person")
public class Person {
    @DbField("c_name")
    public String name;
    @DbField("c_age")
    public int age;
}
