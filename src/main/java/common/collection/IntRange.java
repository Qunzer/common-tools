package common.collection;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * @author rq created on 2023/2/21
 * @version $
 */
public class IntRange implements Serializable {
    private int from;
    private int to;

    public IntRange(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public IntRange setFrom(int from) {
        this.from = from;
        return this;
    }

    public int getTo() {
        return to;
    }

    public IntRange setTo(int to) {
        this.to = to;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("from", from)
                .add("to", to)
                .toString();
    }
}
