/*
 * Problem: https://www.hackerrank.com/challenges/magic-square-forming/copy-from/112597793
 * Difficulty: Medium
 * Themes: implementation, 2D array, loops
 * Comments: For interviews, quickly give the list of all possible magic arrays if candidate gets stuck
 * Follow up question: how can we generate the list of all possible magic arrays?
 */


package implementation;

import org.junit.Assert;
import org.junit.Test;



public class MagicSquareForming {

    @Test
    public void test(){
        int [][] testArrayA = new int [][] {{4,8,2}, {4, 5, 7}, {6, 1, 6}};
        Assert.assertEquals(4, formingMagicSquare(testArrayA));
        int [][] testArrayB = new int [][] {{4, 9, 2}, {3, 5, 7}, {8, 1, 5}};
        Assert.assertEquals(1, formingMagicSquare(testArrayB));
    }

    static int [][][] allPossibleMagicArrays = new int [][][] {
            {{8, 1, 6}, {3, 5, 7}, {4, 9, 2}},
            {{6, 1, 8}, {7, 5, 3}, {2, 9, 4}},
            {{4, 9, 2}, {3, 5, 7}, {8, 1, 6}},
            {{2, 9, 4}, {7, 5, 3}, {6, 1, 8}},
            {{8, 3, 4}, {1, 5, 9}, {6, 7, 2}},
            {{4, 3, 8}, {9, 5, 1}, {2, 7, 6}},
            {{6, 7, 2}, {1, 5, 9}, {8, 3, 4}},
            {{2, 7, 6}, {9, 5, 1}, {4, 3, 8}},
    };

    static int formingMagicSquare(int[][] s) {

        // since s[i][j] is in [1,9], the minimum distance is always < 9*9
        int minDist = 81;
        int a,i,j;
        for(a=0; a<allPossibleMagicArrays.length; a++){
            int dist = 0;
            for(i=0; i<3; i++){
                for(j=0; j<3; j++){
                    dist += Math.abs(allPossibleMagicArrays[a][i][j] - s[i][j]);
                }
            }
            if (dist < minDist){ minDist = dist ; }
        }

        return minDist;

    }

}
