/*
 * Problem: https://www.hackerrank.com/challenges/heavy-light-white-falcon/problem
 * Difficulty: Hard
 * Themes: Heavy Light Weight Trees
 */

package trees;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class HeavyLightWhiteFalcon {

    @Test
    public void testMaxSegmentTree(){
        int[] arr = {1, 3, 5, 7, 9, 11};
        // Segment Tree Creation
        MaxSegmentTree tree = new MaxSegmentTree(arr);
        Assert.assertArrayEquals(tree.getSegmentTree(), new Integer[]{11,5,11,3,5,9,11,1,3,null,null,7,9,null,null});
        // Segment Tree Update
        tree.updateValue(0, 30);
        Assert.assertArrayEquals(tree.getSegmentTree(), new Integer[]{30,30,11,30,5,9,11,30,3,null,null,7,9,null,null});
        // Segment Tree Lookup
        Assert.assertEquals(tree.getIntervalAggregate(1,3), 7);
        Assert.assertEquals(tree.getIntervalAggregate(0,3), 30);
        Assert.assertEquals(tree.getIntervalAggregate(5, 5), 11);
    }

    @Test
    public void testHeavyLightTree(){
        HeavyLightTree t = new HeavyLightTree(new int[][]{{1,2}, {0,1}, {2,3}, {0,6}});
        Assert.assertArrayEquals(t.root.children.toArray(), new Integer[]{1, 6});
        t.computeNodesMetadata();
        TreeNode node = t.myTree[t.root.children.get(0)];
        Assert.assertEquals(1, node.depth);
        Assert.assertEquals(5, t.root.subtreeSize);
        t.assignChains();
        Assert.assertEquals(0, node.chain);
        Assert.assertEquals(1, node.positionInChain);
        t.processChains();
        Assert.assertEquals(0, t.chainSegments[0].getIntervalAggregate(0,1));
        Assert.assertArrayEquals(new int[]{0, 0, 0, 0}, t.chainSegments[0].originalArray);
    }

    @Test
    public void solveProblem(){
        int[][] queries = new int[][]{{1, 0, 1}, {1, 1, 2}, {2, 0, 2}};
        int[][] tree = new int[][]{{0,1}, {1,2}};
        Assert.assertArrayEquals(new int[]{2}, solve(tree, queries));
        queries = new int[][]{{1, 0, 1}, {1, 1, 2}, {2, 0, 2}, {2, 0, 4}};
        tree = new int[][]{{0,1}, {1,2}, {2, 3}, {0, 4}};
        Assert.assertArrayEquals(new int[]{2, 1}, solve(tree, queries));
    }


    public int[] solve(int[][] tree, int[][] queries) {
        // 1 Create a tree
        HeavyLightTree heavyLightTree = new HeavyLightTree(tree);
        // 2 Set up the subtree size, depth and parent for each node (using a DFS)
        heavyLightTree.computeNodesMetadata();
        // 3 Decompose the tree into disjoint chains
        heavyLightTree.assignChains();
        // 4 Implement the Segment Trees
        heavyLightTree.processChains();
        // 5 solve

        List<Integer> solution = new ArrayList<>();

        for (int[] q: queries){
            if(q[0] == 1){
                heavyLightTree.updateWeight(q[1], q[2]);
            }
            if(q[0] == 2){
                solution.add(heavyLightTree.getMaxPath(q[1], q[2]));
            }
        }

        int[] getSolution = solution.stream().mapToInt(Integer::intValue).toArray();
        return getSolution;
    }

    class HeavyLightTree {
        TreeNode[] myTree;
        TreeNode root;
        int[] nodeToChain;
        ArrayList<Integer>[] chainToNodes;
        MaxSegmentTree[] chainSegments;
        int numberOfChains;

        HeavyLightTree(int[][] tree){
            int max = -1;
            for (int[] branch: tree){
                max = Math.max(max, branch[0] + 1);
                max = Math.max(max, branch[1] + 1);
            }

            myTree = new TreeNode[max];
            for(int i = 0; i < max; i++){
                myTree[i] = new TreeNode(i);
            }
            for(int[] branch : tree){
                myTree[branch[0]].children.add(branch[1]);
                myTree[branch[1]].parrentIndex = branch[0];
            }

            root = myTree[tree[0][0]];
            while (root.parrentIndex != -1 ){
                root = myTree[root.parrentIndex];
            }
        }

        public void computeNodesMetadata() {
            root.depth = 0;
            int subsize = 1;
            for (int to : root.children) {
                subsize += computeNodesMetadata(root.index, to);
            }
            root.subtreeSize = subsize;
        }

        private int computeNodesMetadata(int from, int to){
            myTree[to].depth = myTree[from].depth + 1;
            int subsize = 1;
            for(int i: myTree[to].children) {
                subsize += computeNodesMetadata(to, i);
            }
            myTree[to].subtreeSize = subsize;
            return subsize;
        }

        public void assignChains(){
            int chain = 0;
            int position = 0;
            nodeToChain = new int[myTree.length];
            numberOfChains = 1;
            nodeToChain[root.index] = chain;
            root.chain = chain;
            root.positionInChain = position;
            int maxIndice = -1;
            int maxWeight = -1;
            for(int child: root.children) {
                if(myTree[child].subtreeSize > maxWeight){
                    maxWeight = myTree[child].subtreeSize;
                    maxIndice = child;
                }
            }
            for(int child: root.children){
                if (child == maxIndice){
                    assignChains(chain, child, position + 1);
                }
                else {
                    numberOfChains +=1 ;
                    assignChains(numberOfChains -1 , child, 0);
                }
            }
        }

        public void assignChains(int chain, int nodeIndex, int position){
            nodeToChain[nodeIndex] = chain;
            TreeNode node = myTree[nodeIndex];
            node.positionInChain = position;
            node.chain = chain;
            int maxWeight = -1;
            int maxIndice = -1;
            for(int child: node.children) {
                if(myTree[child].subtreeSize > maxWeight ){
                    maxWeight = myTree[child].subtreeSize;
                    maxIndice = child;
                }
            }
            for(int child: node.children){
                if (child == maxIndice){
                    assignChains(chain, child, position + 1);
                }
                else {
                    numberOfChains +=1 ;
                    assignChains(numberOfChains -1 , child, 0);
                }
            }
        }

        public void processChains(){
            chainToNodes = new ArrayList[numberOfChains];
            for (int t=0; t<numberOfChains; t++){
                chainToNodes[t] = new ArrayList<>();
            }
            chainToNodes[root.chain].add(root.index);
            for (int child : root.children){
                processChains(child);
            }
            chainSegments = new MaxSegmentTree[numberOfChains];
            for (int i=0; i< numberOfChains; i++) {
                chainSegments[i] = new MaxSegmentTree(chainToNodes[i].stream().mapToInt(v -> myTree[v].weight).toArray());
            }
        }

        public void processChains(int nodeIndex){
            TreeNode node = myTree[nodeIndex];
            chainToNodes[node.chain].add(node.index);
            for (int child : node.children){
                processChains(child);
            }
        }

        public void updateWeight(int nodeIndex, int newValue){
            TreeNode node = myTree[nodeIndex];
            chainSegments[node.chain].updateValue(node.positionInChain, newValue);
        }

        public int getMaxPath(int firstNode, int secondNode){
            int maxSeen = 0 ;
            while (myTree[firstNode].chain != myTree[secondNode].chain){
                if (myTree[firstNode].depth < myTree[secondNode].depth) {
                    int tempNode = firstNode;
                    firstNode = secondNode;
                    secondNode = tempNode;
                }
                int segmentIndex = myTree[firstNode].chain;
                MaxSegmentTree currentSegment = chainSegments[segmentIndex];
                maxSeen = Math.max(
                        maxSeen,
                        currentSegment.getIntervalAggregate(
                                0,
                                myTree[firstNode].positionInChain)
                );
                // go to start of the chain we just checked and jump to parrent
                firstNode = myTree[chainToNodes[segmentIndex].get(0)].parrentIndex;
            };
            int segmentIndex = myTree[firstNode].chain;
            MaxSegmentTree currentSegment = chainSegments[segmentIndex];
            maxSeen = Math.max(
                    maxSeen,
                    currentSegment.getIntervalAggregate(
                            Math.min( myTree[firstNode].positionInChain, myTree[secondNode].positionInChain),
                            Math.max( myTree[firstNode].positionInChain, myTree[secondNode].positionInChain))
            );


            return maxSeen;

        }

    }

    class TreeNode {
        List<Integer> children;
        int depth;
        int subtreeSize;
        int parrentIndex;
        int weight;
        int index;
        int positionInChain;
        int chain;

        TreeNode(int index){
            this.children = new ArrayList<>();
            this.depth = -1;
            this.subtreeSize = -1;
            this.parrentIndex = -1;
            this.weight = 0;
            this.index = index;
        }
    }


    class MaxSegmentTree {
        /*
            Segment Trees are used to store the result of an aggregate functions and retrieve them in log(n) time instead of computing them in linear time.
         */

        private Integer[] segmentTreeArray;
        private int[] originalArray;

        MaxSegmentTree (int[] inputArray) {
            int height = (int) (Math.ceil(Math.log(inputArray.length)/Math.log(2)));
            int maximumSize = 2 * (int) Math.pow(2, height) - 1;

            segmentTreeArray = new Integer[maximumSize];
            originalArray = inputArray;

            constructSegmentTreeRecursively(inputArray, 0, inputArray.length -1, 0);

        }

        private int getMid(int s, int e){
            return s + (e - s) / 2;
        }

        private int constructSegmentTreeRecursively(int[] inputArray, int intervalStart, int intervalEnd, int segmentTreeArrayPosition){
            if (intervalStart == intervalEnd){
                segmentTreeArray[segmentTreeArrayPosition] = inputArray[intervalStart];
                return inputArray[intervalStart];
            }

            int mid = getMid(intervalStart, intervalEnd);
            segmentTreeArray[segmentTreeArrayPosition] = Math.max(
                    constructSegmentTreeRecursively(inputArray, intervalStart, mid, segmentTreeArrayPosition * 2 + 1),
                    constructSegmentTreeRecursively(inputArray, mid+1, intervalEnd, segmentTreeArrayPosition * 2 + 2));

            return segmentTreeArray[segmentTreeArrayPosition];

        }

        Integer[] getSegmentTree() {
            return segmentTreeArray;
        }

        int[] getOriginalArray() {
            return originalArray;
        }

        int getValue(int index){
            return originalArray[index];
        }

        void updateValue(int index, int newValue){
            originalArray[index] = newValue;
            updateValue(index, newValue, 0, originalArray.length-1, 0);
        }

        private void updateValue(int index, int newValue, int startIndex, int endIndex, int positionSTI){
            /* We go through the array the same way we did to create it, except we just update the path of the updated value
            *  That traversal is the most complicated concept of segment Trees. To do once oneself.
            * */
            segmentTreeArray[positionSTI] = Math.max(newValue, segmentTreeArray[positionSTI]);

            int mid = getMid(startIndex, endIndex);
            boolean stop = (startIndex == endIndex);

            if( !stop && (index <= mid)){
                updateValue(index, newValue, startIndex, mid, positionSTI * 2 + 1);
            }
            if( !stop && (index > mid)){
                updateValue(index, newValue, mid+1, endIndex, positionSTI * 2 + 2);
            }
        }

        int getIntervalAggregate(int targetFromIndex, int targetToIndex){
            if (targetFromIndex < 0) { targetFromIndex = 0;}
            if (targetToIndex > originalArray.length -1) { targetToIndex = originalArray.length -1; }
            return getIntervalAggregate(0, targetFromIndex, targetToIndex, 0, originalArray.length -1);
        }

        private int getIntervalAggregate(int position, int targetFromIndex, int targetToIndex, int exploreFromIndex, int exploreToIndex){
            boolean rangeCovered = (exploreFromIndex >= targetFromIndex) && (exploreToIndex <= targetToIndex);
            boolean outsideOfRange = (exploreToIndex < targetFromIndex) || (exploreFromIndex > targetToIndex);
            if (outsideOfRange){
                return Integer.MIN_VALUE;
            }
            if (rangeCovered){
                return segmentTreeArray[position];
            }
            int mid = getMid(exploreFromIndex, exploreToIndex);

            return Math.max(
                    getIntervalAggregate(2 * position + 1, targetFromIndex, targetToIndex, exploreFromIndex, mid),
                    getIntervalAggregate(2 * position + 2, targetFromIndex, targetToIndex, mid+1, exploreToIndex)
            );

        }


    }


}


