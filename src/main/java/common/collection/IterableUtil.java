package common.collection;

import com.google.common.collect.AbstractIterator;

import java.util.Iterator;

/**
 * Created by rq on 2016/4/12.
 */
public class IterableUtil {

    public static <T> Iterable<T> flatIterable(final Iterator<? extends Iterable<T>> iterators) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() {
                    Iterator<? extends Iterable<T>> outIterable = iterators;
                    Iterator<T> innerIterable = null;

                    @Override
                    protected T computeNext() {
                        if (innerIterable == null) {
                            if (!outIterable.hasNext()) {
                                return endOfData();
                            }
                            innerIterable = outIterable.next().iterator();
                        }
                        if (innerIterable.hasNext()) {
                            return iterator().next();
                        }
                        innerIterable = null;
                        return computeNext();
                    }
                };
            }
        };
    }
}