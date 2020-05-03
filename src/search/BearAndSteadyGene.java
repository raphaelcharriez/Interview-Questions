package search;
/*
https://www.hackerrank.com/challenges/bear-and-steady-gene/problem difficulty Medium
 */

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BearAndSteadyGene {

    @Test
    public void test(){
        String t = "GAAATAAA";
        Assert.assertEquals(5, steadyGene("GAAATAAA"));
    }

    public static int steadyGene(String gene){
        Map<Character, Integer> count = new HashMap<Character, Integer>();
        Character[] genesList = new Character[]{'A', 'C', 'G', 'T'};
        for (char c: genesList){
            count.put(c, 0);
        }

        for (char c: gene.toCharArray()){
            if(!count.containsKey(c)){
                count.put(c, 0);
            }
            count.put(c, count.get(c) +1);
        }
        
        int expectedOccurences = gene.length() / genesList.length;
        if(Arrays.stream(genesList).map(g -> count.get(g) == expectedOccurences).reduce(true, (a, b) -> a&&b)){
            return 0;
        }
        int upperInclusive = 0;
        int lowerExcludingOne = 0;
        int minLength = gene.length();
        char x;
        while ((upperInclusive < gene.length()) && (lowerExcludingOne < gene.length())){
            while((upperInclusive < gene.length()) && Arrays.stream(genesList).map(c -> (count.get(c) > expectedOccurences)).reduce(false, (a, b) -> a||b)){
                x = gene.charAt(upperInclusive);
                count.put(x, count.get(x)-1);
                upperInclusive += 1;
            }
            while(Arrays.stream(genesList).map(c -> (count.get(c) <= expectedOccurences)).reduce( true, (a,b) -> a&&b)){
                x = gene.charAt(lowerExcludingOne);
                count.put(x, count.get(x)+1);
                lowerExcludingOne +=1;
            }
            minLength = Math.min(upperInclusive - lowerExcludingOne + 1, minLength);
        }
        return minLength;
    }

}


