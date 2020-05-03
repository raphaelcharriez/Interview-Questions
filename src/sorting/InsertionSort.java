/*
 * Problem: https://www.hackerrank.com/challenges/heavy-light-white-falcon/problem
 * Difficulty: Advanced
 * Themes: Heavy Light Weight Trees
 * Good Example of Fenwick Tree Application.
 * Methods to count inversion: actual sorting, merge sort, AVL Tree, Fenwick Tree, Trie
 */

package sorting;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class InsertionSort {

    @Test
    public void test(){
        int[] sortedTestArray = {1, 1, 1, 2, 2};
        int[] randomTestArray = {2, 1, 3, 1, 2};
        Assert.assertEquals(0, countNumberOfInversionsWithFenwickTree(sortedTestArray));
        Assert.assertEquals(4, countNumberOfInversionsWithFenwickTree(randomTestArray));
    }

    static int countNumberOfInversionsWithFenwickTree(int[] arr) {
        int max = Arrays.stream(arr).max().getAsInt();
        int[] fenwickArray = new int[max+1];
        Arrays.fill(fenwickArray, 0);
        int permutations = 0;
        for(int n=0; n<arr.length; n++){
            int partialSum = getSumFenwickTree(fenwickArray, arr[n]);
            permutations += n - partialSum;
            updateFenwickTree(fenwickArray, arr[n], 1);

        }
        return permutations;
    }

    static int h(int i){
        return i | (i+1);
    }

    static int g(int i){
        return i & (i+1);
    }

    static void updateFenwickTree(int[] fenwickTree, int index, int delta){
        int n = fenwickTree.length;
        for(int j = index; j<n ; j=h(j)){
            fenwickTree[j]+=delta;
        }
    }

    static int getSumFenwickTree(int[] fenwickTree, int index){
        int n = fenwickTree.length;
        int i = index;
        int total = 0;
        while (i >= 0){
            total += fenwickTree[i];
            i = g(i) - 1;
        }
        return total;
    }


}
