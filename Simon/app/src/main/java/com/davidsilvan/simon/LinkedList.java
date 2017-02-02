package com.davidsilvan.simon;

/**
 * Created by David on 5/12/2016.
 */
public class LinkedList {

    private Node head; //the first node (first color in the color sequence)
    private Node tail; //the last node (last color in the color sequence)

    //Creates a new linked list with no nodes
    public LinkedList() {
        head = null;
        tail = null;
    }

    //Creates a new linked list with the passed in color
    public LinkedList(String color) {
        head = new Node(color);
        tail = head; //linked list only has 1 element so the tail is the head
    }

    //Appends a new node to the end of the linked list and sets the tail to be the appended node
    public void add(String color) {
        if (head == null) {
            head = new Node(color);
            tail = head; //linked list only has 1 element so the tail is the head
        }
        else {
            tail.setNext(new Node(color));
            tail = tail.getNext();
        }
    }

    //moves the front of the queue to the back of the queue (like a cycle) and returns the color
    //of the previous head
    public String moveToEnd() {
        String color = head.getColor(); //The head node's color value
        if (head != tail) {
            Node temp = head; //temporary Node for holding original 1st value
            head = head.getNext(); //2nd value becomes the 1st value
            tail.setNext(temp);
            tail = tail.getNext(); //original first value is put at the back of the queue
        }
        return color;
    }

    //returns the head node
    public Node getHead() {
        return head;
    }

    //returns the tail node
    public Node getTail() {
        return tail;
    }
}
