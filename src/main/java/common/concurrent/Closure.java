package common.concurrent;

import java.util.List;

/**
 * Created by rq on 2016/4/18.
 */
public interface Closure<T> {
    void process(List<T> tList);
}
