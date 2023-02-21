package common.collection;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Comparator;
import java.util.List;

/**
 * @author rq created on 2023/2/21
 * @version $
 */
public class IntRangeUtils {
    /**
     * 连续的区间合并， 比如[1,3],[3,5]合并成[1,5]
     *
     * @param intRangeList
     * @return
     */
    public static List<IntRange> mergeIntRange(List<IntRange> intRangeList) {
        if (CollectionUtils.size(intRangeList) <= 1) {
            return intRangeList;
        }
        //先按照fromTime排序
        intRangeList.sort(Comparator.comparing(IntRange::getFrom));
        List<IntRange> results = Lists.newArrayList();
        for (IntRange intRange : intRangeList) {
            int from = intRange.getFrom();
            int to = intRange.getTo();
            if (CollectionUtils.isEmpty(results) || from > (results.get(results.size() - 1).getTo())) {
                results.add(new IntRange(from, to));
            } else {
                IntRange lastIntRange = results.get(results.size() - 1);
                if (lastIntRange.getTo() < to) {
                    lastIntRange.setTo(to);
                }
            }
        }
        return results;
    }

    public static void main(String[] args) {
        List<IntRange> intRanges = mergeIntRange(Lists.newArrayList(new IntRange(2, 7), new IntRange(7, 9), new IntRange(9, 12)));
        System.out.println(intRanges);
    }
}
