package reshrink.zcw.com.study.db;

import java.util.List;

/**
 * Created by lenovo on 2018/3/8.
 */

public interface IBaseDao<T> {

    long insert(T bean);

    void insertValues(List<T> bean);

    void insertBySql(T bean);

    int update(T bean, T whereBean);

    void updateBySql(T bean, T whereBean);

    void UpdateExecSql(String sql);

    void updateValues(List<T> values,List<T> wheres);

    long delete(T bean);

    int deleteExecSql(T bean);

    int deleteBySql(String sql);

    void deleteValues(List<T> beans);

    List<T> query(T bean);

    List<T> query(T bean, String orderBy, String groupBy, Integer limit, Integer size);

    List<T> queryAll(T bean,List<Integer> values);
}
