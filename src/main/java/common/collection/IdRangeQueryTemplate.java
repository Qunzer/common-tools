package common.collection;

import java.util.List;

/**
 * Created by rq on 2016/4/12.
 */
public interface IdRangeQueryTemplate<T> {
    List<T> query(int startId, int endId);
}
