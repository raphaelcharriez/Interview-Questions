/*
 * Problem: https://www.hackerrank.com/challenges/ctci-queue-using-two-stacks/
 * Difficulty: Medium
 * Themes: Stacks And Queues
 */

package stacks_and_queues;

import org.junit.Assert;
import org.junit.Test;

import java.util.Stack;

public class CctiQueueUsingTwoStacks {

    @Test
    public void test(){
        TwoStacksQueue<Integer> queue = new TwoStacksQueue<>();
        queue.enqueue(1);
        Assert.assertEquals(Integer.valueOf(1), queue.peek());
        queue.enqueue(2);
        Assert.assertEquals(Integer.valueOf(1), queue.dequeue());
        queue.enqueue(3 );
        Assert.assertEquals(Integer.valueOf(2), queue.dequeue());
    }

    public static class TwoStacksQueue<T> {
        private Stack<T> writeStack = new Stack<>();
        private Stack<T> readStack = new Stack<>();

        public void enqueue(T i){
            writeStack.push(i);
        }

        public T dequeue(){
            if (readStack.empty() && writeStack.empty()){
                System.out.println("Stack Empty");
                System.exit(0);
            }
            if (readStack.empty()){
                while (!writeStack.empty()){
                    readStack.push(writeStack.pop());
                }
            }
            return readStack.pop();
        }

        public T peek(){
            if (readStack.empty() && writeStack.empty()){
                System.out.println("Stack Empty");
                System.exit(0);
            }
            if (readStack.empty()){
                while (!writeStack.empty()){
                    readStack.push(writeStack.pop());
                }
            }

            return readStack.peek();
        }
    }

}
