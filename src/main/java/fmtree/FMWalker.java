/*
 * Copyright (c) 2019 - 2021. FlashMonkey Inc. (https://www.flashmonkey.xyz) All rights reserved.
 *
 * License: This is for internal use only by those who are current employees of FlashMonkey Inc, or have an official
 *  authorized relationship with FlashMonkey Inc..
 *
 * DISCLAIMER OF WARRANTY.
 *
 * COVERED CODE IS PROVIDED UNDER THIS LICENSE ON AN "AS IS" BASIS, WITHOUT WARRANTY OF ANY
 *  KIND, EITHER EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION, WARRANTIES THAT THE COVERED
 *  CODE IS FREE OF DEFECTS, MERCHANTABLE, FIT FOR A PARTICULAR PURPOSE OR NON-INFRINGING. THE
 *  ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE COVERED CODE IS WITH YOU. SHOULD ANY
 *  COVERED CODE PROVE DEFECTIVE IN ANY RESPECT, YOU (NOT THE INITIAL DEVELOPER OR ANY OTHER
 *  CONTRIBUTOR) ASSUME THE COST OF ANY NECESSARY SERVICING, REPAIR OR CORRECTION. THIS
 *  DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.  NO USE OF ANY COVERED
 *  CODE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 *
 */

package fmtree;

 import flashmonkey.FlashCardMM;
 import flashmonkey.FlashCardOps;

 import java.util.ArrayList;
 import java.util.LinkedList;

/**
 * FMWalker
 */
public final class FMWalker<T extends Comparable<T>>  {

    private static FMWalker CLASS_INSTANCE;

    // The current node being used.
    private static Node currentNode;


    private static Node lowestNode;
    private static Node highestNode;
    private boolean heightChanged;
    private static int nodeCount = 0;

    private int index = 0;

    /**
     * no arg constructor
     */
    private FMWalker() { /* do nothing */ }

     /**
      * Returns thread safe Singleton instance of FMTWalker
      * @return If no other instance of FMTWalker is found,
      * returns a new FMTWalker.
      */
    public static FMWalker getInstance() {
        if(CLASS_INSTANCE == null) {
            CLASS_INSTANCE = new FMWalker();
        }
        return CLASS_INSTANCE;
    }


    /**
     * The current node to be used outside of the class
     * @return 
     */
    public Node getCurrentNode() {
        return currentNode;
    }

    /**
     * Returns the number of nodes in this tree. 
     * @return 
     */
    public int getCount() {
        return nodeCount;
    }

    public int length() {
        return FlashCardOps.getInstance().getFlashList().size();
    }

    /**
     * Returns the lowestChild in the tree.
     * NOTE: Caution, may be stale data
     * Set value should be called, if needed, to get current
     * data. Caution is advised if performance is an issue.
     */
    public Node getLowestNode() { return lowestNode; }

    /**
     * Returns the highestChild in the tree.
     * NOTE: Caution, may be stale data
     * Set value should be called, if needed, to get current
     * data. Caution is advised if performance is an issue.
     */
    public Node getHighestNode() { return highestNode; }

    // *** SETTERS ***

    public void setCurrentNode(FlashCardMM currentCard) {
        currentNode.data = currentCard;
        setCurrentNode(currentCard);
    }



    /**
     * add starter method. Swaps the last node with
     * parent node making the parent node the last 
     * node for the next iteration. 
     * @param item
     * @return 
     */
    public boolean add(T item) {
        FlashCardOps.getInstance().getFlashList().add((FlashCardMM) item);
        return true;
    }
    

    
    /**
     * Clear the data-structure
     * pre-conditions Assumes that either root is null
     * and .data have been initialized
     * to some value.
     */
    public void clear() {
        FlashCardOps.getInstance().getFlashList().clear();

    }
    
    
    
    public void setDataStructure(ArrayList<FlashCardMM> flashCards) {
       for(FlashCardMM f : flashCards) {
           FlashCardOps.getInstance().getFlashList().add(f);
       }
    }



    /**
     * Sets currentNode to the first node
     */
    public void setToFirst() {
        System.out.println(CLASS_INSTANCE);
        currentNode = new Node(FlashCardOps.getInstance().getFlashList().get(0));
        System.out.println(currentNode.data.toString());
    }

    /**
     * Sets currentNode to the last noode
     */
    public void setToLast() {
        ArrayList<FlashCardMM> fclist = FlashCardOps.getInstance().getFlashList();
        currentNode = new Node(fclist.get(fclist.size() -1));
    }


    public void getNext() {

        Node local = currentNode;

        try
        {
            currentNode = new Node(FlashCardOps.getInstance().getFlashList().get(index++));
        }
        catch (NullPointerException e) {

        }
        finally
        {
            currentNode = local;
            //return local;
        }
    }




    /** ****** INNER CLASS ******
      * Inner class used to create a binary tree node
      * Contains Node left and right. Contains full
      * constructor and toString method.
      *
      */
     public static class Node<E> {

         protected E data;


         /**
          * Constructor. Sets data to data and left and
          * right references.
          * @param data Type
          */
         public Node(E data) {
             this.data = data;

        }

         /**
          * Constructor. Sets data to data and parent referances
          * @param data
          * @param parent
          */
         public Node(E data, Node parent) {
             this.data = data;
         }

         public E getData() {
             FlashCardMM mm = (FlashCardMM) this.data;
             System.out.println("This.data called. " + mm.toString());
             return this.data;
         }

         /**
          * toString method
          * @return String
          */
         public String toString() {

             return this.data.toString();
         }

         }
     // ***** END INNER CLASS *****

}
