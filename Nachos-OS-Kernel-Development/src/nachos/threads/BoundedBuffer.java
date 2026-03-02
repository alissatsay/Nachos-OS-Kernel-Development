package nachos.threads;

/**
 * Bounded buffer monitor using condition variables.
 */
public class BoundedBuffer {

    private final char[] buf;
    private final int capacity;

    private int head = 0; 
    private int tail = 0;  
    private int count = 0; 

    private final Lock lock;
    private final Condition2 notEmpty;
    private final Condition2 notFull;

    public BoundedBuffer(int maxsize) {
        if (maxsize <= 0) throw new IllegalArgumentException();
        this.capacity = maxsize;
        this.buf = new char[maxsize];

        lock = new Lock();
        notEmpty = new Condition2(lock);
        notFull  = new Condition2(lock);
    }

    /*
     * Read a character from the buffer, blocking until there is a char
     * in the buffer to satisfy the request. Return the char read.
     */

    public char read() {
        lock.acquire();

        while (count == 0) {
            notEmpty.sleep();         
        }

        char c = buf[head];
        head = (head + 1) % capacity;
        count--;

        notFull.wake();

        lock.release();
        return c;
    }


    /*
     * Write the given character c into the buffer, blocking until
     * enough space is available to satisfy the request.
     */
    public void write(char c) {
        lock.acquire();

        while (count == capacity) {
            notFull.sleep();       
        }

        buf[tail] = c;
        tail = (tail + 1) % capacity;
        count++;

        notEmpty.wake();

        lock.release();
    }

    /*
     * Prints the contents of the buffer
     */
    public void print() {
        lock.acquire();

        String s = "Bounded Buffer [cap=" + capacity + ", size=" + count + "]: (";
        for (int i = 0; i < count; i++) {
            int idx = (head + i) % capacity;
            s += buf[idx];
            if (i + 1 < count) s += ' ';
        }
        s += ")";
        System.out.println(s);

        lock.release();
    }
}

