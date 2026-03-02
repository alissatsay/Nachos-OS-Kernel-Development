package nachos.threads;

/**
 *  Model a LinkedList class.
 * 
 * I affirm that I have carried out the attached academic endeavors with full academic honesty,
 * in accordance with the Union College Honor Code and the course syllabus. [Alissa Tsay]
 *  
 * @author Alissa Tsay
 * @version Sep 10, 2025
 */

public class DLList
{
    private DLLElement first;  // pointer to first node
    private DLLElement last;   // pointer to last node
    private int size;          // number of nodes in list
    private Lock lock;
    private Condition2 listNotEmpty;

    /**
     * Creates an empty sorted doubly-linked list.
     */ 
    public DLList() {
        size=0;
        first=null;
        last=null;
        lock = new Lock();
	    listNotEmpty = new Condition2(lock);
    }

    /**
     * Add item to the head of the list, setting the key for the new
     * head element to min_key - 1, where min_key is the smallest key
     * in the list (which should be located in the first node).
     * If no nodes exist yet, the key will be 0.
     */
    public void prepend(Object item) {
        lock.acquire();
        boolean wasEmpty = privIsEmpty();
        int min_key = 0;
        if (!privIsEmpty()){
            min_key = first.key - 1;
        }
        privInsert (item, min_key);
        if (wasEmpty) listNotEmpty.wake();
        lock.release();
    }

    /**
     * Removes the head of the list and returns the data item stored in
     * it.  Returns null if no nodes exist.
     *
     * @return the data stored at the head of the list or null if list empty
     */
    public Object removeHead() {
        lock.acquire();
        while (privIsEmpty()){
            listNotEmpty.sleep();
        }

        Object toReturn = first.data;

        if (privSize() == 1){
            KThread.yieldIfShould(0);
            first = last = null;
        }
        else{
            first = first.next;
            KThread.yieldIfShould(1);
            first.prev = null;
        }
        size--;
        lock.release();
        return toReturn;
    }

    /**
     * Remove first n items from the head of the list
     * @param n the number of items to remove
     * @return removed items as a list
     */
    public Object[] removeFirstN(int n) {
        lock.acquire();
        if (n < 0) return null;
        if (size < n) return null; 

        Object[] out = new Object[n];
        for (int i = 0; i < n; i++) {
            Object item = first.data;
            if (size == 1) {
                first = last = null;
            } else {
                first = first.next;
                first.prev = null;
            }
            size--;
            out[i] = item;
        }
        lock.release();
        return out;
    }

    /**
     * Tests whether the list is empty.
     *
     * @return true iff the list is empty.
     */
    public boolean isEmpty() {
        lock.acquire();
        boolean toReturn = privIsEmpty();
        lock.release();
        return toReturn;
    }

    /**
     * Tests whether the list is empty.
     *
     * @return true iff the list is empty.
     */
    private boolean privIsEmpty() {
        return size == 0;
    }

    /**
     * returns number of items in list
     * @return
     */
    public int size(){
        lock.acquire();
        int toReturn = privSize();
        lock.release();
        return toReturn;
    }

    /**
     * returns number of items in list
     * @return
     */
    private int privSize(){
        return size;
    }


    /**
     * Inserts item into the list in sorted order according to sortKey.
     */
    public void insert(Object item, Integer sortKey) {
        lock.acquire();
        privInsert(item, sortKey);
        listNotEmpty.wake();
        lock.release();
    }

    /**
     * Inserts item into the list in sorted order according to sortKey.
     */
    private void privInsert(Object item, Integer sortKey) {
        DLLElement newNode = new DLLElement(item, sortKey);

        if (privIsEmpty()){
            first = newNode;
            last = newNode;
        }
        else if (sortKey <= first.key){
            newNode.next=first;
            first.prev = newNode;
            first=newNode;
        }
        else if (sortKey >= last.key){
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }
        else{
            DLLElement runner = first;

            while (runner.key <= sortKey){
                runner = runner.next;
            }
            DLLElement nodeBefore = runner.prev;

            newNode.prev = nodeBefore;
            newNode.next = runner;
            runner.prev = newNode;
            nodeBefore.next = newNode;
        }
        size++;
    }


    /**
     * returns list as a printable string. A single space should separate each list item,
     * and the entire list should be enclosed in parentheses. Empty list should return "()"
     * @return list elements in order
     */
    public String toString() {
        lock.acquire();
        String toReturn = "(";
		DLLElement runner = first;
		while(runner != null){
			toReturn = toReturn + runner.toString();
			runner = runner.next;
			if(runner != null){
				toReturn = toReturn + " ";
			}
		}
		toReturn = toReturn + ")";
        lock.release();
		return toReturn;
    }

    /**
     * returns list as a printable string, from the last node to the first.
     * String should be formatted just like in toString.
     * @return list elements in backwards order
     */
    public String reverseToString(){
        lock.acquire();
        String toReturn = "(";
		DLLElement runner = last;
		while(runner != null){
			toReturn = toReturn + runner.toString();
			runner = runner.prev;
			if(runner != null){
				toReturn = toReturn + " ";
			}
		}
		toReturn = toReturn + ")";
        lock.release();
		return toReturn;
    }

    /**
     *  inner class for the node
     */
    private class DLLElement
    {
        private DLLElement next; 
        private DLLElement prev;
        private int key;
        private Object data;

        /**
         * Node constructor
         * @param item data item to store
         * @param sortKey unique integer ID
         */
        public DLLElement(Object item, int sortKey)
        {
        	key = sortKey;
        	data = item;
        	next = null;
        	prev = null;
        }

        /**
         * returns node contents as a printable string
         * @return string of form [<key>,<data>] such as [3,"ham"]
         */
        public String toString(){
            return "[" + key + "," + data + "]";
        }
    }
}
