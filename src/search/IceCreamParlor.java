package search;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class IceCreamParlor {

    @Test
    public void test(){
        int[] test = new int[]{1, 4, 5, 3, 2};
        Assert.assertArrayEquals(new int[]{1, 4}, icecreamParlor(4, test));
    }



    public static int[] icecreamParlor(int m, int[] arr) {
        int[] copy = arr.clone();
        Arrays.sort(copy);
        int up = copy.length - 1;
        int down = 0;
        int s = copy[up] + copy[down];
        while(s != m){
            if(s < m){
                down++;
            }
            if(s > m){
                up--;
            }
            s = copy[up] + copy[down];
        }
        down = Arrays.asList(arr).indexOf(copy[down]);
        arr[down] = -1;
        up = Arrays.asList(arr).indexOf(copy[up]);
        int[] solution = new int[]{ Math.min(down, up) + 1, Math.max(down, up) + 1 };
        return solution;
    }
}
