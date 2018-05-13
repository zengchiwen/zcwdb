package reshrink.zcw.com.study.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import reshrink.zcw.com.study.utils.LogUtils;

/**
 * Created by lenovo on 2018/3/8.
 */

public class BaseDao<T> implements IBaseDao<T> {

    private static SQLiteDatabase db;
    private static Class<?> mClass;
    private boolean isInit = false;
    private static Map<String, Field> cacheMap;
    private String s;

    public BaseDao() {
    }

    private BaseDao(SQLiteDatabase db, Class<?> mClass) {
        this.db = db;
        this.mClass = mClass;
        if (!isInit) {
            init();
            isInit = true;
        }
    }


    public static BaseDao dao;

    public synchronized BaseDao createDao(SQLiteDatabase db, Class<?> mClass) {
        synchronized (db) {
            if (dao == null) {
                synchronized (db) {
                    dao = new BaseDao(db, mClass);
                }
            }
        }
        return dao;
    }

    public void init() {
        String sql = createSql();
        if (db != null) {
            db.execSQL(sql);
        }

        cacheMap = initCacheMap();
    }

    static String tableName;
    static List<String> values = new ArrayList<>();

    private String createSql() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
        if (mClass.getAnnotation(DbTable.class) != null) {
            tableName = mClass.getAnnotation(DbTable.class).value();
        } else {
            tableName = mClass.getSimpleName();
        }
        stringBuilder.append(tableName + "(");
        Field[] fields = mClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        for (int i = 0; i < fields.length; i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }

            Field field = fields[i];
            Class<?> type = field.getType();
            String name = "";
            if (field.getAnnotation(DbField.class) != null) {
                name = field.getAnnotation(DbField.class).value();
            } else {
                name = field.getName();
            }
            values.add(name);
            String sType = "";
            if (type == int.class) {
                sType = " integer";
            } else if (type == String.class) {
                sType = " text";
            } else if (type == float.class || type == double.class) {
                sType = " real";
            } else {
                sType = " blob";
            }
            stringBuilder.append(name + sType);

        }
        if (stringBuilder.toString().charAt(',') == stringBuilder.toString().length() - 1) {
            stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public long insert(T bean) {
        Map<String, Field> cacheMap = initCacheMap();
        ContentValues values = getContentValue(bean);
        return db.insert(tableName, null, values);
    }

    @Override
    public void insertValues(List<T> beans) {
        try {
            if (beans == null && beans.size() == 0) {
                return;
            }


            Field[] fields = mClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
            db.rawQuery("PRAGMA synchronous = OFF", null);
            db.rawQuery("PRAGMA journal_mode=WAL;", null);
            db.beginTransaction();
            ;

            for (int i = 0; i < beans.size(); i = i + 500) {

                StringBuilder sql = new StringBuilder();
//                sql.append("begin transaction;");
                sql.append("insert into " + tableName + " values ");
                for (int j = 0; j < ((beans.size() - i) < 500 ? (beans.size() - i) : 500); j++) {
                    if (j != 0) {
                        sql.append(",");
                    }
                    T bean = beans.get(j + i);

                    try {
                        appendBan(sql, fields, bean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                sql.append(";");
//                sql.append("commit  transaction;");
                db.execSQL(sql.toString());

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    private void appendBan1(SQLiteStatement sql, Field[] fields, T bean) {
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Class<?> type = field.getType();

            try {
                if (field.get(bean) == null) {
                    sql.bindNull(i);
                }
                if (type == String.class) {

                    s = "" + field.get(bean) + "";
                    sql.bindString(i + 1, s);
                } else if (type == Integer.class) {
                    sql.bindLong(i, field.getLong(bean));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    private void appendBan(StringBuilder sql, Field[] fields, T bean) throws IllegalAccessException {
        sql.append("(");
        for (int i = 0; i < fields.length; i++) {

            if (i != 0) {
                sql.append(",");
            }
            Field field = fields[i];
            Class<?> type = fields[i].getType();
            if (type == String.class) {
                sql.append(field.get(bean) == null ? "NULL" : "'" + field.get(bean) + "'");

            } else {
                sql.append(field.get(bean) == null ? "NULL" : field.get(bean));
            }
        }
        sql.append(")");
    }


    @Override
    public void insertBySql(T bean) {
        try {
            if (bean == null) {
                return;
            }
            StringBuilder sql = new StringBuilder();
            sql.append("insert into " + tableName + " values (");
            Field[] fields = mClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
            appendBan(sql, fields, bean);
            sql.append(");");
            db.execSQL(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private Map<String, String> getValues(T bean) {
        Map<String, String> map = new HashMap<>();
        Map<String, Field> cacheMap = initCacheMap();
        Set<String> strings = cacheMap.keySet();
        for (String string : strings) {
            Field field = cacheMap.get(string);
            try {
                if (field.get(bean) == null) {
                    continue;
                }
                map.put(string, field.get(bean).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private ContentValues getContentValue(T bean) {
        ContentValues contentValues = new ContentValues();
        Map<String, String> values = getValues(bean);
        Set<String> strings = values.keySet();
        for (String string : strings) {
            contentValues.put(string, values.get(string));
        }
        return contentValues;
    }

    private Map<String, Field> initCacheMap() {
        Map<String, Field> map = new HashMap<>();

        Field[] fields = mClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        for (String val : values) {
            String name = "";
            for (Field field : fields) {
                if (field.getAnnotation(DbField.class) != null) {
                    name = field.getAnnotation(DbField.class).value();
                } else {
                    name = field.getName();
                }
                if (name.equals(val)) {
                    map.put(name, field);
                    break;
                }
            }
        }
        return map;
    }

    @Override
    public int update(T bean, T whereBean) {
        ContentValues contentValue = getContentValue(bean);
        Map<String, String> values = getValues(whereBean);
        Condition condition = new Condition(values);
        return db.update(tableName, contentValue, condition.whereCondition, condition.whereCause);
    }

    @Override
    public void updateBySql(T bean, T whereBean) {
        try {
            db.rawQuery("PRAGMA synchronous = OFF", null);
            db.rawQuery("PRAGMA journal_mode=WAL;", null);
            db.beginTransaction();

            StringBuilder sql = new StringBuilder();
            sql.append(" update  " + tableName + " set ");
            appendupdateValues(sql, whereBean);
            appendupdateWhereCondition(sql, whereBean);
            db.execSQL(sql.toString());

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void appendupdateValues(StringBuilder sql, T whereBean) {
        try {

            Set<String> stringSet = cacheMap.keySet();

            int count = 0;
            for (String string : stringSet) {
                if (count != 0) {
                    sql.append(",");
                }
                Field field = cacheMap.get(string);
                if (field.get(whereBean) != null) {
                    sql.append(string + " = ");
                    Class<?> type = field.getType();
                    if (type == String.class) {
                        sql.append("'" + field.get(whereBean) + "'");
                    } else {
                        sql.append(field.get(whereBean));
                    }
                }
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendupdateWhereCondition(StringBuilder sql, T whereBean) {

        try {
            sql.append(" where  1=1 ");

            Set<String> stringSet = cacheMap.keySet();
            for (String string : stringSet) {
                Field field = cacheMap.get(string);
                if (field.get(whereBean) != null) {
                    sql.append(" and " + string + " = ");
                    Class<?> type = field.getType();
                    if (type == String.class) {
                        sql.append("'" + field.get(whereBean) + "'");
                    } else {
                        sql.append(field.get(whereBean));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void UpdateExecSql(String sql) {
        SQLiteStatement sqLiteStatement = db.compileStatement(sql);
        db.beginTransaction();
        int swql = sqLiteStatement.executeUpdateDelete();
        db.endTransaction();
    }

    @Override
    public void updateValues(List<T> values, List<T> wheres) {
        try {
            if (values == null || wheres == null) {
                return;
            }
            if (values.size() != wheres.size()) {
                return;
            }
            Set<String> map = cacheMap.keySet();
            String id = "";
            for (String str : map) {
                if (str.toLowerCase().equals("id")) {
                    id = str.toLowerCase();
                    break;
                }
            }
            for (T val : wheres) {
                if (cacheMap.get(id).get(val) == null) {
                    return;
                }
            }

            for (T val : wheres) {
                if (cacheMap.get(id).get(val) == null) {
                    return;
                }
            }


            int count = 0;
            Field field = cacheMap.get(id);

            for (String str : map) {
                if (str.toLowerCase().equals("id")) {
                    continue;
                }
                Field fieldCacheMap = cacheMap.get(str);
                for (int i = 0; i < values.size(); i += 10000) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(" update " + tableName + " set ");
                    stringBuilder.append(str + " = CASE " + id);
                    StringBuffer whereBuilder = new StringBuffer();
                    whereBuilder.append("(");


                    for (int j = 0; j < ((values.size() - i) < 10000 ? (values.size() - i) : 10000); j++) {
                        stringBuilder.append(" when " + field.get(wheres.get(i + j)) + " ");

                        if (fieldCacheMap.getType() == String.class) {
                            stringBuilder.append(" then '" + fieldCacheMap.get(values.get(i + j)) + "'");
                        } else {
                            stringBuilder.append(" then " + fieldCacheMap.get(values.get(i + j)) + "");
                        }
                        if (j != 0) {
                            whereBuilder.append(",");

                        }
                        whereBuilder.append(field.get(wheres.get(i + j)));
                    }
                    whereBuilder.append(")");
                    stringBuilder.append(" end ");
                    stringBuilder.append(",");
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);

                    stringBuilder.append(" where id in " + whereBuilder);

                    LogUtils.printLogSD(LogUtils.ERROR, "i" + i);
                    db.execSQL(stringBuilder.toString());
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long delete(T bean) {
        Map<String, String> values = getValues(bean);
        Condition condition = new Condition(values);
        return db.delete(tableName, condition.whereCondition, condition.whereCause);
    }

    @Override
    public int deleteExecSql(T bean) {
        int sql = deleteSingle(bean);
        return sql;
    }

    private int deleteSingle(T bean) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete  from " + tableName);
        Condition condition = convertCondition(bean);
        sb.append(" where  " + condition.convertSql());
        SQLiteStatement sqLiteStatement = db.compileStatement(sb.toString());
        db.beginTransaction();
        int sql = sqLiteStatement.executeUpdateDelete();
        db.endTransaction();
        return sql;
    }

    private Condition convertCondition(T bean) {
        Map<String, String> values = getValues(bean);
        Condition condition = new Condition(values);
        return condition;
    }

    @Override
    public int deleteBySql(String sql) {
        SQLiteStatement sqLiteStatement = db.compileStatement(sql);
        db.beginTransaction();
        int swql = sqLiteStatement.executeUpdateDelete();
        db.endTransaction();
        return swql;
    }

    @Override
    public void deleteValues(List<T> beans) {

        try {
            db.rawQuery("PRAGMA synchronous = OFF", null);
            db.rawQuery("PRAGMA journal_mode=WAL;", null);
            db.beginTransaction();

            StringBuilder sql = new StringBuilder();
            sql.append(" delete from " + tableName);
            Set<String> stringSet = cacheMap.keySet();
            boolean isContains = true;
            for (String string : stringSet) {
                if (!TextUtils.isEmpty(isContains(string, cacheMap.get(string), beans))) {
                    isContains = false;
                    appendDELETE(sql, isContains(string, cacheMap.get(string), beans));
                    break;
                }
            }

            if (isContains) {
                throw new RuntimeException("please the value must have value");

            }


            db.execSQL(sql.toString());

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    private void appendDELETE(StringBuilder sql, String whereConditio) {
        sql.append(whereConditio);
    }

    private String isContains(String string, Field field, List<T> beans) {
        StringBuilder sb = new StringBuilder();
        sb.append("  where " + string + " in (");
        try {
            boolean isContains = true;
            for (int i = 0; i < beans.size(); i++) {
                if (i != 0) {
                    sb.append(",");
                }
                T t = beans.get(i);
                Class<?> type = field.getType();
                if (field.get(t) != null) {
                    if (type == String.class) {
                        sb.append("'" + field.get(t) + "'");
                    } else {
                        sb.append(field.get(t));
                    }

                } else {
                    isContains = false;
                    break;
                }
            }

            if (isContains) {
                sb.append(")");
            } else {
                sb = new StringBuilder();
                sb.append("");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    @Override
    public List<T> query(T bean) {
        return query(bean, null, null, null, null);
    }

    @Override
    public List<T> query(T bean, String orderBy, String groupBy, Integer limit, Integer size) {
        StringBuilder stringBuilder = new StringBuilder();
        if (limit != null && size != null) {
            stringBuilder.append(limit + "," + size);
        }
        String[] value = values.toArray(new String[values.size()]);
        Map<String, String> values = getValues(bean);
        Condition condition = new Condition(values);
        Cursor cursor = db.query(tableName, value, condition.whereCondition, condition.whereCause, groupBy, null, orderBy, stringBuilder.toString());
        List<T> beans = queryCursor(bean, cursor);
        return beans;
    }

    @Override
    public List<T> queryAll(T bean, List<Integer> values) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (int i = 0; i < values.size(); i++) {
            stringBuilder.append(values.get(i) + "");
            if (i != values.size() - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(")");
        Cursor cursor = db.rawQuery("select * from " + tableName + " where id in (?)", new String[]{stringBuilder.toString()});
        List<T> beans = queryCursor(bean, cursor);
        return beans;
    }

    private List<T> queryCursor(T bean, Cursor cursor) {
        List<T> val = new ArrayList<>();
        Object object = null;
        while (!cursor.moveToNext()) {
            try {
                object = bean.getClass().newInstance();
                Map<String, Field> cacheMap = initCacheMap();
                Set<String> stringSet = cacheMap.keySet();
                for (String string : stringSet) {
                    Field field = cacheMap.get(string);
                    Class<?> type = field.getType();
                    if (type == int.class) {
                        field.set(object, cursor.getInt(cursor.getColumnIndex(string)));
                    } else if (type == byte.class) {
                        field.set(object, cursor.getBlob(cursor.getColumnIndex(string)));
                    } else if (type == String.class) {
                        field.set(object, cursor.getString(cursor.getColumnIndex(string)));
                    } else if (type == double.class) {
                        field.set(object, cursor.getDouble(cursor.getColumnIndex(string)));
                    } else if (type == long.class) {
                        field.set(object, cursor.getLong(cursor.getColumnIndex(string)));
                    } else if (type == short.class) {
                        field.set(object, cursor.getShort(cursor.getColumnIndex(string)));
                    } else {
                        continue;
                    }
                }
                val.add((T) object);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return val;

    }


    public class Condition {
        private String whereCondition;
        private String[] whereCause;

        private Map<String, String> value;

        public Condition(Map<String, String> value) {
            this.value = value;
            StringBuilder sb = new StringBuilder();
            ArrayList list = new ArrayList();
            Set<String> strings = value.keySet();
            for (String val : strings) {
                sb.append(" 1=1");
                String valu = value.get(val);
                if (!TextUtils.isEmpty(valu)) {
                    sb.append(" and " + val + " = ?");
                    list.add(valu);
                }

            }
            whereCondition = sb.toString();
            whereCause = (String[]) list.toArray(new String[list.size()]);
        }

        public String convertSql() {
            StringBuilder sb = new StringBuilder();
            Set<String> strings = value.keySet();
            sb.append(" 1=1");
            for (String val : strings) {

                String valu = value.get(val);
                if (!TextUtils.isEmpty(valu)) {
                    sb.append(" and " + val + " = '" + value.get(val) + "'");

                }
            }
            return sb.toString();

        }
    }
}
