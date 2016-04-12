package common.collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by rq on 2016/4/12.
 */
public class QueryUtils {

    public static <T> Iterable<List<T>> splitQuery(final IdRangeQueryTemplate<T> template, final int startId, final int endId, final int limit) {
        Preconditions.checkArgument(template != null);
        Preconditions.checkArgument(startId >= 0);
        Preconditions.checkArgument(endId > 0);
        Preconditions.checkArgument(limit > 0);
        return new Iterable<List<T>>() {
            public Iterator<List<T>> iterator() {

                return new AbstractIterator<List<T>>() {
                    int start = startId;
                    int end = startId + limit;

                    @Override
                    protected List<T> computeNext() {
                        if (start > endId) {
                            return endOfData();
                        }
                        if (end > endId) {
                            end = endId + 1;
                        }
                        List<T> queryResult = template.query(start, end);
                        start = start + limit;
                        end = end + limit;
                        return queryResult;
                    }
                };
            }
        };
    }

    public static <T> Iterable<List<T>> splitQuery(final StartLimitQueryTemplate<T> template, final int limit) {
        Preconditions.checkNotNull(template);
        Preconditions.checkArgument(limit > 0);
        return new Iterable<List<T>>() {
            public Iterator<List<T>> iterator() {
                return new AbstractIterator<List<T>>() {
                    int start = 0;
                    boolean endFlag = false;
                    @Override
                    protected List<T> computeNext() {
                        if (endFlag) {
                            return endOfData();
                        }
                        List<T> queryResult = template.query(start, limit);
                        if (CollectionUtils.isEmpty(queryResult)) {
                            return endOfData();
                        }
                        if (queryResult.size() < limit)  {
                            endFlag = true;
                            return queryResult;
                        }
                        start = start + limit;
                        return queryResult;
                    }
                };
            }
        };
    }
}
