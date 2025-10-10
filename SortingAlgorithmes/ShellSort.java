// code based on code by diptangsu from https://github.com/diptangsu/Sorting-Algorithms/tree/master
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShellSort {

    /**
     * Performs Shell Sort on the input array and returns all intermediate steps.
     *
     * @param input The array to sort
     * @return A list of array snapshots after each modification
     */
    public static List<int[]> shellSort(int[] input) {
        List<int[]> steps = new ArrayList<>();
        int[] nums = Arrays.copyOf(input, input.length);
        int n = nums.length;

        int h = 1;
        while (h < n) {
            h = h * 3 + 1;
        }
        h = h / 3;

        while (h > 0) {
            for (int i = h; i < n; i++) {
                int c = nums[i];
                int j = i;
                while (j >= h && nums[j - h] > c) {
                    nums[j] = nums[j - h];
                    j = j - h;
                    // Visualizer.update(nums);
                    steps.add(Arrays.copyOf(nums, nums.length));
                }
                nums[j] = c;
                // Visualizer.update(nums);
                steps.add(Arrays.copyOf(nums, nums.length));
            }
            h = h / 2;
        }

        return steps;
    }
}
