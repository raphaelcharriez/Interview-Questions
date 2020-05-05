package search;

import org.junit.Assert;
import org.junit.Test;

import java.util.TreeSet;

public class MinimumClass {

    @Test
    public void test(){
        long[] test = new long[]{5, 10, 3};
        Assert.assertEquals(2L, minimumLoss(test));
    }

    public static long minimumLoss(long[] price) {
        TreeSet<Long> values = new TreeSet<>();
        long min = Long.MAX_VALUE;
        for (long value : price ) {
            Long higher = values.higher(value);
            if (higher != null) {
                min = Math.min(min, Math.abs(higher - value));
            }
            values.add(value);
        }
        return min;
    }

}
