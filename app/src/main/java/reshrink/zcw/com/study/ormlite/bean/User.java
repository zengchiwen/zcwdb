package reshrink.zcw.com.study.ormlite.bean;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="tb_user")
public class User {

	@DatabaseField(columnName="name")
	private String name;
	@DatabaseField(columnName="pwd")
	private String pwd;

	@DatabaseField( generatedId = true, id = false)
	private int id;

	public User()
	{
		
	}

	public User(String name, String pwd, int id) {
		this.name = name;
		this.pwd = pwd;
		this.id = id;
	}
	public User(String name, String pwd) {
		this.name = name;
		this.pwd = pwd;
		this.id = id;
	}
}
