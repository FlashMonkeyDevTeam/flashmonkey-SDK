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

 import java.util.LinkedList;

/**
 * FMWalker
 */
public final class FMWalker<T extends Comparable<T>>  {

    private static FMWalker CLASS_INSTANCE;

    private static LinkedList<Node> dataStructure;

    private static Node root;
    //private Node tempParent = null;
    // The current node being used.
    private static Node currentNode;

    // the root of the tree, not the branch
    //private static Node treeRoot;
    private static Node lowestNode;
    private static Node highestNode;
    private boolean heightChanged;
    private static int nodeCount = 0;

    /**
     * no arg constructor
     */
    private FMWalker() { /* do nothing */ }

     /**
      * Returns thread safe Singleton instance of FMTWalker
      * @return If no other instance of FMTWalker is found,
      * returns a new FMTWalker.
      */
    public synchronized static FMWalker getInstance() {
        if(CLASS_INSTANCE == null) {
            //System.out.println("in FMTWalker creating NEW instance");
            initializeWalker();
            CLASS_INSTANCE = new FMWalker();
        }
        return CLASS_INSTANCE;
    }

    private static void initializeWalker() {
        root = dataStructure.get(0);
    }

    /**
     * The current node to be used outside of the class
     * @return 
     */
    public static Node getCurrentNode()
    {
        return currentNode;
    }

    /**
     * Returns the number of nodes in this tree. 
     * @return 
     */
    public static int getCount() {
        return nodeCount;
    }

    /**
     * Returns the lowestChild in the tree.
     * NOTE: Caution, may be stale data
     * Set value should be called, if needed, to get current
     * data. Caution is advised if performance is an issue.
     */
    public static Node getLowestNode() { return lowestNode; }

    /**
     * Returns the highestChild in the tree.
     * NOTE: Caution, may be stale data
     * Set value should be called, if needed, to get current
     * data. Caution is advised if performance is an issue.
     */
    public static Node getHighestNode() { return highestNode; }

    // *** SETTERS ***

    /**
     * Setter for the current node to be used
     * outside of the class.
     * @param n
     */
    /*public static void setCurrentNode(Node n)
    {
        currentNode = n;
    }

    public void setCurrentNode(FlashCardMM currentCard) {
        T fc = (T) currentCard;
        Node n = findNode(fc);
        setCurrentNode(n);
    }

     */

    /**
     * add starter method. Swaps the last node with
     * parent node making the parent node the last 
     * node for the next iteration. 
     * @param item
     * @return 
     */
    public boolean add(T item) {

        dataStructure.add((Node) item);
        return true;
    }
    




    
    /**
     * Clear the data-structure
     * pre-conditions Assumes that either root is null
     * and .data have been initialized
     * to some value.
     */
    public void clear() {

        if(this.root != null) {
            this.root.parent = null;
            this.root.right = null;
            this.root.left = null;
            this.root.data = null;
            this.root = null;  // Trying to keep the reference
            this.nodeCount = 0;
        }
    }
    
    
    
    /**
     * Navigation provides the following methods to give the UI convenient and 
     * flexible navigation
     *  - getFirst()
     *  - getNext()
     *  - getLast()
     *  - getPrevious()
     * This class expects that the AVLTree is naturally ordered or it's order is 
     * set by its own comparator. 
     * @author humanfriendlyfolder
     */



        /**
         * Sets currentNode to the first node
         */       
        public void setToFirst()
        {
            currentNode = dataStructure.get(0);
        }

        /**
         * Sets currentNode to the last noode
         */
        public void setToLast() {
            currentNode = dataStructure.get(dataStructure.size());
        }


        public void getNext() {

            Node local = currentNode;
            
            try
            {

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
         protected Node<E> parent;
         public Node<E> left;
         public Node<E> right;
         protected int balance;


         /**
          * Constructor. Sets data to data and left and
          * right references.
          * @param data Type
          */
         public Node(E data) {
             this.data = data;
             parent = null;
             left = null;
             right = null;
             balance = 0;
         }

         /**
          * Constructor. Sets data to data and parent referances
          * @param data
          * @param parent
          */
         public Node(E data, Node parent) {
             this.data = data;
             this.parent = parent;
             left = null;
             right = null;
             balance = 0;
         }

         public E getData() {
             return this.data;
         }

         /**
          * toString method
          * @return String
          */
         public String toString() {

             return this.data.toString();
             //+ "\n       " + this.parent;
         }

         /**
          * getParent gets the next greater parent
          * @return the TWnode from the next greater parent
          */
         public Node<E> getParent() {
             return this.parent;
         }

     }
     // ***** END INNER CLASS *****

}
