package leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * <全排列>
 * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
 *
 * @author rq created on 2023/2/21
 * @version $
 */
public class PermuteSolution {
    private void generatePermute(int[] nums, boolean[] visited, int curSize, int len, Stack<Integer> path, List<List<Integer>> res) {
        if (curSize == len) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < len; i++) {
            if (!visited[i]) {
                path.push(nums[i]);
                visited[i] = true;
                generatePermute(nums, visited, curSize + 1, len, path, res);
                path.pop();
                visited[i] = false;
            }
        }
    }

    public List<List<Integer>> permute(int[] nums) {
        int len = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        boolean[] used = new boolean[len];
        if (len == 0) {
            return res;
        }
        generatePermute(nums, used, 0, len, new Stack<>(), res);
        return res;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3,4,5};
        PermuteSolution solution = new PermuteSolution();
        List<List<Integer>> permute = solution.permute(nums);
        System.out.println(permute.size());
        for (int i = 0; i < permute.size(); i++) {
            System.out.println(permute.get(i));
        }
    }
}
