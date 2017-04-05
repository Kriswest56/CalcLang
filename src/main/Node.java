package main;

/*  a Node holds one node of a parse tree
    with several pointers to children used
    depending on the kind of node
*/

import java.util.ArrayList;
import java.io.*;
import java.awt.*;

public class Node {
    public static int count = 0;  // maintain unique id for each node

    private int id;

    private String kind;  // non-terminal or terminal category for the node
    private String info;  // extra information about the node such as
    // the actual identifier for an I
    String nodeOneKind = "";
    String nodeTwoKind = "";
    String nodeThreeKind = "";
    String nodeFourKind = "";

    // references to children in the parse tree
    // (uniformly use these from first to fourth)
    private Node first, second, third, fourth;

    public Node(String k, Node one, Node two, Node three, Node four) {
        kind = k;
        info = "";
        first = one;
        second = two;
        third = three;
        fourth = four;
        id = count;
        count++;
        System.out.println(this);
    }

    // construct a node that is essentially a token
    public Node(Token token) {
        kind = token.getKind();
        info = token.getDetails();
        first = null;
        second = null;
        third = null;
        fourth = null;
        id = count;
        count++;
        System.out.println(this);
    }

    public String toString() {
        return "#" + id + "[" + kind + "," + info + "]";
    }

    // produce array with the non-null children
    // in order
    private Node[] getChildren() {
        int count = 0;
        if (first != null) count++;
        if (second != null) count++;
        if (third != null) count++;
        if (fourth != null) count++;
        Node[] children = new Node[count];
        int k = 0;
        if (first != null) {
            children[k] = first;
            k++;
        }
        if (second != null) {
            children[k] = second;
            k++;
        }
        if (third != null) {
            children[k] = third;
            k++;
        }
        if (fourth != null) {
            children[k] = fourth;
            k++;
        }

        return children;
    }

    //******************************************************
    // graphical display of this node and its subtree
    // in given camera, with specified location (x,y) of this
    // node, and specified distances horizontally and vertically
    // to children
    public void draw(Camera cam, double x, double y, double h, double v) {

        System.out.println("draw node " + id);

        // set drawing color
        cam.setColor(Color.black);

        String text = kind;
        if (!info.equals("")) text += "(" + info + ")";
        cam.drawHorizCenteredText(text, x, y);

        // positioning of children depends on how many
        // in a nice, uniform manner
        Node[] children = getChildren();
        int number = children.length;
        System.out.println("has " + number + " children");

        double top = y - 0.75 * v;

        if (number == 0) {
            return;
        } else if (number == 1) {
            children[0].draw(cam, x, y - v, h / 2, v);
            cam.drawLine(x, y, x, top);
        } else if (number == 2) {
            children[0].draw(cam, x - h / 2, y - v, h / 2, v);
            cam.drawLine(x, y, x - h / 2, top);
            children[1].draw(cam, x + h / 2, y - v, h / 2, v);
            cam.drawLine(x, y, x + h / 2, top);
        } else if (number == 3) {
            children[0].draw(cam, x - h, y - v, h / 2, v);
            cam.drawLine(x, y, x - h, top);
            children[1].draw(cam, x, y - v, h / 2, v);
            cam.drawLine(x, y, x, top);
            children[2].draw(cam, x + h, y - v, h / 2, v);
            cam.drawLine(x, y, x + h, top);
        } else if (number == 4) {
            children[0].draw(cam, x - 1.5 * h, y - v, h / 2, v);
            cam.drawLine(x, y, x - 1.5 * h, top);
            children[1].draw(cam, x - h / 2, y - v, h / 2, v);
            cam.drawLine(x, y, x - h / 2, top);
            children[2].draw(cam, x + h / 2, y - v, h / 2, v);
            cam.drawLine(x, y, x + h / 2, top);
            children[3].draw(cam, x + 1.5 * h, y - v, h / 2, v);
            cam.drawLine(x, y, x + 1.5 * h, top);
        } else {
            System.out.println("no Node kind has more than 4 children???");
            System.exit(1);
        }

    }// draw

    public void execute(){
        System.out.println(kind + " : " + info);
        if(this.kind.equals("eof")){

            /** Handle EOF node type **/

            return;
        } else if(this.kind.equals("program")){

            /** Handle program node type **/

            if(first != null && first.kind.equals("statements")){
                first.execute();
            }
        } else if(this.kind.equals("statements")){

            /** Handle statements node type **/

            if(second.kind.equals("eof")){
                if(first.kind.equals("statement")){
                    first.execute();
                }
            } else{
                first.execute();
                second.execute();
            }
        } else if (this.kind.equals("statement")){

            /** Handle statement node type **/
            if(first != null){
                nodeOneKind = first.kind;
            }
            if(second != null){
                nodeTwoKind = second.kind;
            }
            if(third != null){
                nodeThreeKind = third.kind;
            }
            if(fourth != null){
                nodeFourKind = fourth.kind;
            }

            if(nodeOneKind.equals("msg")){
                if(second != null && second.kind.equals("string") && second.info != null){
                    System.out.print(second.info);
                }
            }

        }
    }


}// Node class
