package reshrink.zcw.com.study;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.snappydb.DB;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

//import io.realm.Realm;
import io.objectbox.Box;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import reshrink.zcw.com.study.bean.MessageEvent;
import reshrink.zcw.com.study.db.DaoManagerFactory;
import reshrink.zcw.com.study.db.StudentOpenHelper;
import reshrink.zcw.com.study.db.User;
import reshrink.zcw.com.study.db.UserDao;
import reshrink.zcw.com.study.eventbus.EventBus;
import reshrink.zcw.com.study.eventbus.Subscribe;
import reshrink.zcw.com.study.eventbus.ThreadMode;
import reshrink.zcw.com.study.ormlite.db.DatabaseHelper;
import reshrink.zcw.com.study.litepal.Users;
import reshrink.zcw.com.study.utils.LogUtils;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private DB snappyDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        realm = Realm.getDefaultInstance();
        try {
            snappyDB = new SnappyDB.Builder(this)
                    .directory(Environment.getExternalStorageDirectory().getAbsolutePath()) //optional
                    .name("books")//optional
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event) {

        LogUtils.printLogSD(LogUtils.ERROR, "MainActivity:" + event.toString());
    }


    public void jumpEvent(View view) {
//        BActivity.start(this);

        insertCustomer();
        updateCustomerixstion();
        selectCustomerization();
        deleteCustomerization();


        insertRealm();
        updateRealm();
        selectRealm();
        deleteRealM();

        insertObjectBox();
        updateObjectBox();
        selectObjectBox();
        deleteObjectBox();

        insertSnappy();

        insertLitepal();
        updateLitepal();
        selectLitepal();
        deleteLitePal();

        insertOrmLite();
        updateOrmLite();
        selectOrmLite();
        deleteOrmLite();

        insertGreenDao();
        updateGreenDao();
        selectGreenDao();
        deleteGreenDao();




    }

    private void selectGreenDao() {
    }

    private void selectOrmLite() {
    }

    private void selectLitepal() {
    }

    private void selectObjectBox() {
    }

    private void selectRealm() {
    }

    private void selectCustomerization() {

        long currentTimeMills = System.currentTimeMillis();
        Log.e("jett", currentTimeMills + "");
        List<User> users = new ArrayList<>();
        UserDao dataHelper = DaoManagerFactory.getInstance().getDataHelper(UserDao.class, User.class);

        List<Integer> values=new ArrayList<Integer>();
        for (int i = 0; i < 100000; i++) {
                       values.add(i);
        }
        dataHelper.queryAll(values);
        Log.e("jett", "自定义数据库框架删除十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
    }

    private void updateGreenDao() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            MyApp instance = MyApp.getInstance();

            reshrink.zcw.com.study.UserDao userDao = instance.getDaoSession().getUserDao();
            for (int i = 0; i < 100000; i++) {
                reshrink.zcw.com.study.greendao.bean.User user = new reshrink.zcw.com.study.greendao.bean.User("jason", "abc@123", i);

                userDao.update(user);


            }
            Log.e("jett", "greendao更新十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOrmLite() {

        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            DatabaseHelper helper = DatabaseHelper.getHelper(this);
            for (int i = 0; i < 100000; i++) {
                reshrink.zcw.com.study.ormlite.bean.User user = new reshrink.zcw.com.study.ormlite.bean.User("jason", "abc$123", i);

                helper.getUserDao().update(user);
            }
            Log.e("jett", "ormlite更新十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLitepal() {

        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");

            for (int i = 0; i < 100000; i++) {
                Users user = new Users("jason", "abc123", i);

                user.update(i);

            }
            Log.e("jett", "litepal更新十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateObjectBox() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            List<reshrink.zcw.com.study.objectbox.User> lis = new ArrayList<>();
            Box<reshrink.zcw.com.study.objectbox.User> beanBox = ((MyApp) getApplication())
                    .getBoxStore().boxFor(reshrink.zcw.com.study.objectbox.User.class);
            for (int i = 0; i < 100000; i++) {

                reshrink.zcw.com.study.objectbox.User user;
                user = beanBox.get(i+1);
                user.setName("jjjjjjjjj");
                user.setPwd("aaaaaa");
                lis.add(user);

            }
            beanBox.put(lis);

//新增和修改，put 的参数可以是 list

            Log.e("jett", "ObjectBox插入十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateRealm() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            List<reshrink.zcw.com.study.realeam.Users> lis = new ArrayList<>();

            for (int i = 0; i < 100000; i++) {
                final  int inde=i;
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        reshrink.zcw.com.study.realeam.Users bean = realm.where(reshrink.zcw.com.study.realeam.Users.class)
                                .equalTo(reshrink.zcw.com.study.realeam.Users.USERID, inde)
                                .findFirst();

                        bean.setPwd("bbbbbb");
                        bean.setUserName("aaaabb");
                        realm.insertOrUpdate(bean);
                    }
                });
            }
            Log.e("jett", "realm更新十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCustomerixstion() {

        long currentTimeMills = System.currentTimeMillis();
        Log.e("jett", currentTimeMills + "");
        List<User> values = new ArrayList<>();
        UserDao dataHelper = DaoManagerFactory.getInstance().getDataHelper(UserDao.class, User.class);

        for (int i = 0; i < 100000; i++) {
            User user = new User("jason", "bbc332", i);
            values.add(user);
        }
        List<User> wheres = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            User user = new User(i);
            wheres.add(user);
        }
        dataHelper.updateValues(values, wheres);
        Log.e("jett", "自定义数据库框架更新十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
    }

    private void deleteObjectBox() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            List<reshrink.zcw.com.study.objectbox.User> users = new ArrayList<>();
            Box<reshrink.zcw.com.study.objectbox.User> beanBox = ((MyApp) getApplication())
                    .getBoxStore().boxFor(reshrink.zcw.com.study.objectbox.User.class);

            for (int i = 0; i < 100000; i++) {


//新增和修改，put 的参数可以是 list
                reshrink.zcw.com.study.objectbox.User user = new reshrink.zcw.com.study.objectbox.User("jett", "123", i + 1);

                users.add(user);

            }
            beanBox.remove(users);
            Log.e("jett", "objectbox删除十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertObjectBox() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            List<reshrink.zcw.com.study.objectbox.User> lis = new ArrayList<>();
            for (int i = 0; i < 100000; i++) {

                reshrink.zcw.com.study.objectbox.User user = new reshrink.zcw.com.study.objectbox.User("jett", "123");
                lis.add(user);

            }
            Box<reshrink.zcw.com.study.objectbox.User> beanBox = ((MyApp) getApplication())
                    .getBoxStore().boxFor(reshrink.zcw.com.study.objectbox.User.class);

//新增和修改，put 的参数可以是 list
            beanBox.put(lis);
            Log.e("jett", "ObjectBox插入十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteGreenDao() {

        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            MyApp instance = MyApp.getInstance();

            reshrink.zcw.com.study.UserDao userDao = instance.getDaoSession().getUserDao();
            for (int i = 0; i < 100000; i++) {
                userDao.deleteByKey((long) i);


            }
            Log.e("jett", "greendao删除十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteOrmLite() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            DatabaseHelper helper = DatabaseHelper.getHelper(this);
            for (int i = 0; i < 100000; i++) {
                reshrink.zcw.com.study.ormlite.bean.User user = new reshrink.zcw.com.study.ormlite.bean.User("jett", "abc123", i);

                helper.getUserDao().delete(user);
            }
            Log.e("jett", "ormlite删除十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteLitePal() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");

            for (int i = 0; i < 100000; i++) {
                Users user = new Users("jett", "abc123", i);

                user.delete();

            }
            Log.e("jett", "litepal删除十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteRealM() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            List<reshrink.zcw.com.study.realeam.Users> lis = new ArrayList<>();
            for (int i = 0; i < 100000; i++) {

                reshrink.zcw.com.study.realeam.Users bean = realm.where(reshrink.zcw.com.study.realeam.Users.class)
                        .equalTo(reshrink.zcw.com.study.realeam.Users.USERID, i)
                        .findFirst();

                realm.beginTransaction();
                bean.deleteFromRealm();

                realm.commitTransaction();

            }
            Log.e("jett", "realm上传十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCustomerization() {
        long currentTimeMills = System.currentTimeMillis();
        Log.e("jett", currentTimeMills + "");
        List<User> users = new ArrayList<>();
        UserDao dataHelper = DaoManagerFactory.getInstance().getDataHelper(UserDao.class, User.class);

        for (int i = 0; i < 100000; i++) {
            User user = new User("jett", "abc123", i);
            users.add(user);
    }
        dataHelper.deleteValues(users);
        Log.e("jett", "自定义数据库框架删除十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
    }

    private void insertGreenDao() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            MyApp instance = MyApp.getInstance();

            reshrink.zcw.com.study.UserDao userDao = instance.getDaoSession().getUserDao();
            for (int i = 0; i < 100000; i++) {
                reshrink.zcw.com.study.greendao.bean.User user = new reshrink.zcw.com.study.greendao.bean.User("jett", "abc123", i);

                userDao.insert(user);


            }
            Log.e("jett", "greendao插入十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertOrmLite() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            DatabaseHelper helper = DatabaseHelper.getHelper(this);
            for (int i = 0; i < 100000; i++) {
                reshrink.zcw.com.study.ormlite.bean.User user = new reshrink.zcw.com.study.ormlite.bean.User("jett", "abc123", i);

                helper.getUserDao().create(user);
            }
            Log.e("jett", "ormlite插入十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertLitepal() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");

            for (int i = 0; i < 100000; i++) {
                Users user = new Users("jett", "abc123", i);

                user.save();
            }
            Log.e("jett", "litepal插入十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertSnappy() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            List<reshrink.zcw.com.study.SnappyDB.User> lis = new ArrayList<>();
            for (int i = 0; i < 100000; i++) {

                reshrink.zcw.com.study.SnappyDB.User user = new reshrink.zcw.com.study.SnappyDB.User("jett", "123", i);
                lis.add(user);

            }
            snappyDB.put("list", lis.toArray());

            Log.e("jett", "snappyDB插入十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertRealm() {
        try {
            long currentTimeMills = System.currentTimeMillis();
            Log.e("jett", currentTimeMills + "");
            List<reshrink.zcw.com.study.realeam.Users> lis = new ArrayList<>();
            for (int i = 0; i < 100000; i++) {

                reshrink.zcw.com.study.realeam.Users user = new reshrink.zcw.com.study.realeam.Users("jett", "123", i);
                lis.add(user);

            }
            realm.beginTransaction();
            realm.insert(lis);
            realm.commitTransaction();
            Log.e("jett", "realm插入十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long insertCustomer() {
        StudentOpenHelper openHelper = new StudentOpenHelper(this);
        long currentTimeMills = System.currentTimeMillis();
        Log.e("jett", currentTimeMills + "");


        List<User> users = new ArrayList<>();
        UserDao dataHelper = DaoManagerFactory.getInstance().getDataHelper(UserDao.class, User.class);

        for (int i = 0; i < 100000; i++) {
            User user = new User("jett", "abc123", i);
            users.add(user);
        }
        dataHelper.insertValues(users);
        Log.e("jett", "自定义数据库框架插入十万条数据时间差为" + (System.currentTimeMillis() - currentTimeMills) + "毫秒");
        currentTimeMills = System.currentTimeMillis();
        Log.e("jett", currentTimeMills + "");
        return currentTimeMills;
    }

    public void sendEvent(View view) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setMsgId(1000);
        messageEvent.setMsgContent("发送消息：aaaaaaaaaaaaaaa");
        EventBus.getDefault().post(messageEvent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
