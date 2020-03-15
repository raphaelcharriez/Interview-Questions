/*
 * Problem: https://www.hackerrank.com/challenges/climbing-the-leaderboard/problem
 * Difficulty: Medium
 * Themes: implementation, array, sorting
 */


package implementation;

import org.junit.Assert;
import org.junit.Test;

import static java.util.Arrays.binarySearch;
import static java.util.stream.IntStream.range;


public class ClimbingTheLeaderboard {

    @Test
    public void test() {
        int[] scores = new int[]{100, 100, 50, 40, 40, 20, 10};
        int[] alice = new int[]{5, 20, 50, 120};
        Assert.assertArrayEquals(new int[]{6,4,2,1}, climbLeaderboard(scores, alice));

    }

    static int[] climbLeaderboard(int[] scores, int[] alice) {
        int[] result = new int[alice.length];
        int[] discreteScores = range(0, scores.length).map(i->scores[scores.length-i-1]).distinct().toArray();
        int index = 0;
        for(int i = 0; i<alice.length; i++){

            index = binarySearch(discreteScores, index<0?0:index, discreteScores.length, alice[i]);

            // the binarySearch implementation returns -(low -1) if the value is not found in the array.
            // In this case Alice is just after the low index.
            if (index<0){
                index = - index -2 ;
            }

            result[i] = discreteScores.length - index;
        }
        return result;
    }



}
