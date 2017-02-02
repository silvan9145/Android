package com.davidsilvan.simon;

/**
 * Created by David on 5/12/2016.
 */
public class Node {

    private Node nextNode; //reference to the next node
    private String color; //color value of the node

    //creates a node with the passed in color value
    public Node(String color) {
        nextNode = null;
        this.color = color;
    }

    //getter method for the nextNode reference
    public Node getNext() {
        return nextNode;
    }

    //getter method for the color value of this node
    public String getColor() {
        return color;
    }

    //setter method for the nextNode reference
    public void setNext(Node n) {
        nextNode = n;
    }

    //getter method for the color value of this node
    public void setColor(String color) {
        this.color = color;
    }
}