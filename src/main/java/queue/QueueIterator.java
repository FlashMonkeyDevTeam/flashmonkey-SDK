package queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates through a queue
 *     @author Lowell Stadelman
 */
public class QueueIterator<E> implements Iterator<E>{

    /*<listing chapter="4" number="5">*/

    int size;
    private E[] genericAry;
    int capacity;

    // Data Fields
    // Index of next element
    private int index;
    // Count of elements accessed so far
    private int count = 0;

    // Methods
    // Constructor
    /** Initializes the Iter object to reference the
     first queue element.
     */
    public QueueIterator(int front, int size, E[] genericArray, int capacity) {
        index = front;
        this.size = size;
        genericAry = genericArray;
        this.capacity = capacity;
    }

    /**
     * Returns true if there are more elements in the queue to access.
     * @return true if there are more elements in the queue to access.
     */
    @Override
    public boolean hasNext() {
        return count < size;
    }

    /**
     * Returns the next element in the queue.
     * pre-condition index references the next element to access.
     * post-condition: index and count are incremented.
     * @return The element with subscript index
     */
    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        E returnValue = genericAry[index];
        index = (index + 1) % capacity;
        count++;
        return returnValue;
    }

    /**
     * Remove the item accessed by the Iter object -- not implemented.
     * @throws UnsupportedOperationException when called
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();

    }
}
