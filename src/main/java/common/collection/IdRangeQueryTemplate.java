package common.collection;

import java.util.List;

/**
 * Created by rq on 2016/4/12.
 */
public interface IdRangeQueryTemplate<T> {
    /**
     *
     * @param startId
     * @param endId
     * @return
     */
    List<T> query(int startId, int endId);
}
