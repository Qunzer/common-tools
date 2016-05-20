package common.diff;

import com.sksamuel.diffpatch.DiffMatchPatch;

import java.util.LinkedList;

/**
 * Created by rq on 2016/5/20.
 */
public class DiffTest {
    public static void main(String[] args) {
        DiffMatchPatch diffMatchPatch = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diffs = diffMatchPatch.diff_main("abc", "bcd", false);

        diffMatchPatch.diff_cleanupSemantic(diffs);
        for (DiffMatchPatch.Diff diff : diffs) {
            System.out.println("操作：" + diff.operation.name());
            System.out.println("内容：" + diff.text);
        }
    }
}
