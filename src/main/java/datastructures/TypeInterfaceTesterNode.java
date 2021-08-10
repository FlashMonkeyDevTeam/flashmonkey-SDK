package datastructures;

public class TypeInterfaceTesterNode {

    public TypeInterfaceTesterNode() {
        /* no args constructor */
    }

    /** ****** INNER CLASS ******
     * Inner class to create a node
     * Contains Node left and right. Contains full
     * constructor and toString method.
     *
     */
    public static class Node<E> {

        protected E data;
        protected int balance;


        /**
         * Constructor. Sets data to data and left and
         * right references.
         * @param data Type
         */
        public Node(E data) {
            this.data = data;
            balance = 0;
        }

        /**
         * Constructor. Sets data to data and parent referances
         * @param data
         */
        public Node(E data, Node notused) {
            this.data = data;
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
        }


    }
    // ***** END INNER CLASS *****
}
