package reshrink.zcw.com.study;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.litepal.LitePalApplication;

import io.objectbox.BoxStore;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import reshrink.zcw.com.study.objectbox.MyObjectBox;

//import io.realm.Realm;
//import io.realm.RealmConfiguration;

/**
 * Created by lenovo on 2018/3/11.
 */

public class MyApp extends LitePalApplication {
    /**
     * Global application context.
     */
    static Context sContext;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    public  static  MyApp instance;

    public static  MyApp getInstance(){
        return  instance;
    }
    /**
     * Construct of LitePalApplication. Initialize application context.
     */
    public MyApp() {
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }
    private BoxStore boxStore;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        setDatabase();
      boxStore = MyObjectBox.builder().androidContext(MyApp.this).build();
    }

    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "sport.db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();
        Realm.setDefaultConfiguration(config);
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

    public void setBoxStore(BoxStore boxStore) {
        this.boxStore = boxStore;
    }
}
