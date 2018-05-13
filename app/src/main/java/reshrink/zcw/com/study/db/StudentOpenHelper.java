package reshrink.zcw.com.study.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2017/4/13 0013.
 */

public class StudentOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataEventInfoOpenHelper";

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "student.db";
    public final static String MY_TABLE_NAME = "student";

    private Context context;

    public StudentOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION, null);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();

        try {
            db.execSQL("create table if not exists student (" +
                    "name varchar , pwd varchar )");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
       }

    }







    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO 版本变换后会执行该操作
    }

    public  void extractSql(String sql){
        SQLiteDatabase db=null;
        synchronized (this) {

            long rowId=-1;

            db = getWritableDatabase();
            //插入数据库中,返回新添记录的行号，该行号是一个内部值，与主键id无关，发生错误返回-1
            db.execSQL(sql);

        }
    }

    //插入方法
    public long insert(ContentValues values) {
      SQLiteDatabase db=null;
            synchronized (this) {

                long rowId=-1;

                    db = getWritableDatabase();
                    //插入数据库中,返回新添记录的行号，该行号是一个内部值，与主键id无关，发生错误返回-1
                  rowId = db.insert(MY_TABLE_NAME, null, values);
                if (db!=null){
                    db.close();
                }

                    return rowId;

            }


    }








    //查询方法






}
