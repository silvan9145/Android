package com.davidsilvan.simon;

/**
 * Created by David on 5/12/2016.
 */
public class LinkedQueue {

    private int num = 0; //Holds the size of the current color sequence
    private LinkedList list; //the linked list that will be used to implement the queue

    //Create new empty LinkedQueue
    public LinkedQueue() {
        list = null;
    }

    //Create new LinkedQueue with first color value
    public LinkedQueue(String color) {
        list = new LinkedList(color);
        num = 1;
    }

    //Add a color to the color sequence
    public void add(String color) {
        list.add(color);
        num++;
    }

    //Used for cycling through the color sequence. Returns the color of the head node, then moves
    //the head to the end (like a cycle)
    public String moveToEnd() {
        return list.moveToEnd();
    }

    //Returns the size of the queue
    public int getSize() {
        return num;
    }

    //Returns the color value of the first color in the color sequence
    public String getFirstColor() {
        return list.getHead().getColor();
    }

    //Returns the color value of the last color in the color sequence
    public String getLastColor() {
        return list.getTail().getColor();
    }
}
