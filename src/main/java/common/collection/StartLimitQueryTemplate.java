package common.collection;

import java.util.List;

/**
 * Created by rq on 2016/4/12.
 */
public interface StartLimitQueryTemplate<T> {
    /**
     *
     * @param start
     * @param limit
     * @return
     */
    List<T> query(int start, int limit);
}
