package queue;

//import GenericShape;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class provides an efficent implementation of a Queue for FIFO operations.
 *
 *     @author Lowell Stadelman
 */
public class ArrayQueue<E> extends AbstractQueue<E> implements java.util.Queue<E>
{

    /** Index of the front of the queue. */
    private int front;
    /** Index of the rear of the queue. */
    private int rear;
    /** Current size of the queue. */
    private int size;
    /** Current capacity of the queue. */
    private int capacity;
    /** Default capacity of the queue. */
    private static final int DEFAULT_CAPACITY = 10;
    /** Array to hold the data. */
    private E[] genericAry;

    @SuppressWarnings("unchecked")
    public ArrayQueue()
    {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ArrayQueue(int initCapacity)
    {
        capacity = initCapacity;
        genericAry = (E[]) new Object[capacity];
        front = 0;
        rear = capacity - 1;
        size = 0;
    }

    @Override public Iterator<E> iterator()
    {
        return new QueueIterator<>(this.front, this.size, this.genericAry, this.capacity);
    }

    @Override
    public int size()
    {
        return size;
    }

    public E getLast() { return genericAry[rear]; }

    /**
     * Inserts an item at the rear of the queue.
     *  post-condition item is added to the rear of the queue.
     *  @param item The element to add
     *  @return true (always successful)
     *
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     *         this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *         prevents it from being added to this queue
     */
    @Override
    public boolean offer(E item)
    {
        if(size == capacity) {
            reallocate();
        }
        size++;
        rear = (rear + 1) % capacity;
        genericAry[rear] = item;
        return true;
    }

    /**
     * Returns the item at the front of the queue without removing it.
     * @return The item at the front of the queue if successful;
     * return null if the queue is empty
     */
    @Override
    public E peek() {
        if (size == 0) {
            return null;
        } else {
            return genericAry[front];
        }
    }

    /**
     * Removes the entry at the front of the queue and returns it
     * if the queue is not empty.
     * post-conditions front references item that was second in the queue.
     * @return The item removed if successful or null if not
     */
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = genericAry[front];
        front = (front + 1) % capacity;
        size--;
        return result;
    }


    /**
     * Will error if arayQueue is empty.
     */
    public void print() {

        System.out.println("\nPrinting ArrayQueue");
        System.out.println("\t Length: " + genericAry.length );

        for(int i = 0; i < size; i++) {

            System.out.println("\t" + i + ") " + ( genericAry[i]).toString());
        }
    }



    // Private Methods
    /**
     * Double the capacity and reallocate the data.
     * pre-condition The array is filled to capacity.
     * post-condition The capacity is doubled and the first half of the
     *       expanded array is filled with data.
     */
    @SuppressWarnings("unchecked")
    private void reallocate() {
        int newCapacity = 2 * capacity;
        E[] newData = (E[]) new Object[newCapacity];
        int j = front;
        for (int i = 0; i < size; i++)
        {
            newData[i] = genericAry[j];
            j = (j + 1) % capacity;
        }
        front = 0;
        rear = size - 1;
        capacity = newCapacity;
        genericAry = newData;
    }



}
