package search;

import jdk.jfr.StackTrace;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class KnightOnChessBoard {

    @Test
    public void test(){
        Assert.assertArrayEquals(
                new int[][]{
                {4, 4, 2, 8},
                {4, 2, 4, 4},
                {2, 4, -1, -1},
                {8, 4, -1, 1}},
                knightlOnAChessboard(5)
                );
    }

    public static int[][] knightlOnAChessboard(int n) {

        int[][] values = new int[n-1][n-1];
        for(int smallStep=1; smallStep<n; smallStep++){
            for( int bigStep=smallStep; bigStep<n; bigStep++){
                boolean iterating = true;
                int count = 0;
                Set<String> alreadyVisited = new HashSet<>();
                Stack<String> toVisit = new Stack<>();
                Stack<String> toVisitNext = new Stack<>();
                toVisit.push("0-0");

                while (iterating){
                    count += 1;
                    while (!toVisit.isEmpty()){
                        String s = toVisit.pop();
                        System.out.println(s + "-"+ count);
                        alreadyVisited.add(s);
                        int startX = Integer.valueOf(s.split("-")[0]);
                        int startY = Integer.valueOf(s.split("-")[1]);
                        int[][] nextMoves = generateNextMoves(startX, startY, bigStep, smallStep);
                        for (int i = 0; i < 8; i++) {
                            int x = nextMoves[0][i];
                            int y = nextMoves[1][i];
                            if ((x == (n-1)) && (y == (n-1))){
                                iterating = false;
                            }
                            if (isValidMove(x, y, n, alreadyVisited)) {
                                toVisitNext.add(String.valueOf(x) + '-' + y);
                                alreadyVisited.add(String.valueOf(x) + '-' + y);
                            }
                        }
                    }
                    if(iterating && toVisitNext.empty()){
                        iterating = false;
                        count = -1;
                    }
                    toVisit = toVisitNext;
                    toVisitNext = new Stack<>();

                }
                values[smallStep-1][bigStep-1] = count;
                values[bigStep-1][smallStep-1] = count;
            }
        }

        return values;
    }

    public static int[][] generateNextMoves(int posX, int posY, int smallStep, int bigStep){
        int[] movesX = new int[]{
                posX + smallStep,
                posX + smallStep,
                posX + bigStep,
                posX + bigStep,
                posX - smallStep,
                posX - smallStep,
                posX - bigStep,
                posX - bigStep};

        int[] movesY = new int[]{
                posY + bigStep,
                posY - bigStep,
                posY + smallStep,
                posY - smallStep,
                posY + bigStep,
                posY - bigStep,
                posY + smallStep,
                posY - smallStep};

        return new int[][]{movesX, movesY};
    }

    public static boolean isValidMove(int posX, int posY, int n, Set<String> alreadyVisited){
        String positionKey = String.valueOf(posX) + '-' + posY;
        return ((posX >= 0) && (posX < n) && (posY >= 0) && (posY < n) && !alreadyVisited.contains(positionKey));
    }

}
