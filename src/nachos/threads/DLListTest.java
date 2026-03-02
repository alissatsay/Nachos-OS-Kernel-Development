package nachos.threads;

/**
 * JUnit test class for DLL class.
 * 
 * I affirm that I have carried out the attached academic endeavors with full academic honesty,
 * in accordance with the Union College Honor Code and the course syllabus. [Alissa Tsay]
 *  
 * @author Alissa Tsay
 * @version Sep 10, 2025
 */
import org.junit.*;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class DLListTest {
    @Rule // a test will fail if it takes longer than 1/10 of a second to run
 	public Timeout timeout = Timeout.millis(100); 

    /**
	 * Create a linked list with default capacity and three elements.
	 */
	private DLList makeList() {
		DLList ll = new DLList();
        ll.prepend("d" + "i" + "n" + "n" + "e" + "r");
        ll.prepend("l" + "u" + "n" + "c" + "h");
        ll.prepend("b" + "r" + "e" + "a" + "k" + "f" + "a" + "s" + "t");
        return ll;
	}

    @Test // DLList should be empty upon construction with length 0 and firstNode pointing to null.
    public void testConstruct() {
        DLList ll = new DLList();
        assertEquals(0, ll.size());
        assertEquals("()", ll.toString());
    }

    @Test // DLList should be empty upon construction with length 0.
    public void testLengthEmpty() {
        DLList ll = new DLList();
        assertEquals(0, ll.size());

        //Length should change after adding an element
        ll.prepend("breakfast");
        assertEquals(1, ll.size());
    }

    @Test // DLList with 3 elements should have a size 3.
    public void testLengthNonEmpty() {
        DLList ll = makeList();
        assertEquals(3, ll.size());

        //Length should change after adding an element
        ll.prepend("breakfast");
        assertEquals(4, ll.size());
    }

    @Test // Inserting a new Node at the neginning of the linked list.
    public void testprepend() {
        DLList ll = new DLList();
        assertEquals(0, ll.size());
        assertEquals("()", ll.toString());

        //Method should just add an element when the list is empty
        ll.prepend("lunch");
        assertEquals("([0,lunch])", ll.toString());
        //length should change
        assertEquals(1, ll.size());

        //Method should add an element to the beginning of the list when it's nonempty
        ll.prepend("breakfast");
        assertEquals("([-1,breakfast] [0,lunch])", ll.toString());
        assertEquals(2, ll.size());
    }

    @Test // Inserting a new Node at the end of the linked list.
    public void testIsEmpty() {
        DLList ll = new DLList();
        assertEquals(true, ll.isEmpty());
        assertEquals("()", ll.toString());

        //Method should just add an element when the list is empty
        ll.prepend("lunch");
        assertEquals(false, ll.isEmpty());
        //length should change
        assertEquals(1, ll.size());
    }

    @Test // Inserting a new Node at desired position.
    public void testinsert() {
        DLList ll = makeList();
        assertEquals(3, ll.size());
        assertEquals("([-2,breakfast] [-1,lunch] [0,dinner])", ll.toString());

        //Adding an element at position 0
        ll.insert("wake up", 0);
        assertEquals("([-2,breakfast] [-1,lunch] [0,dinner] [0,wake up])", ll.toString());
        //length should change
        assertEquals(4, ll.size());

        //Adding an element at position legth-1 
        ll.insert("go to bed", ll.size());
        assertEquals("([-2,breakfast] [-1,lunch] [0,dinner] [0,wake up] [4,go to bed])", ll.toString());
        //length should change
        assertEquals(5, ll.size());

        //Method should add an element at position 2
        ll.insert("second breakfast", 2);
        assertEquals("([-2,breakfast] [-1,lunch] [0,dinner] [0,wake up] [2,second breakfast] [4,go to bed])", ll.toString());
        //length should change
        assertEquals(6, ll.size());

        //Giving it a position greater than length - should insert at the end
        ll.insert("wrong", 10);
        assertEquals("([-2,breakfast] [-1,lunch] [0,dinner] [0,wake up] [2,second breakfast] [4,go to bed] [10,wrong])", ll.toString());
        //length should not change
        assertEquals(7, ll.size());

        //Giving it a negative position - should inser at head
        ll.insert("wrong", -2);
        assertEquals("([-2,wrong] [-2,breakfast] [-1,lunch] [0,dinner] [0,wake up] [2,second breakfast] [4,go to bed] [10,wrong])", ll.toString());
        //length should not change
        assertEquals(8, ll.size());
    }

    @Test // Inserting a new Node at desired position when the list is empty.
    public void testinsertEmpty() {
        DLList ll = new DLList();
        assertEquals(0, ll.size());
        assertEquals("()", ll.toString());

        //Adding an element at position 0
        ll.insert("wake up", 0);
        assertEquals("([0,wake up])", ll.toString());
        //length should change
        assertEquals(1, ll.size());

        //Adding an element at a negative position
        ll.removeHead();
        ll.insert("go to bed", -1);
        assertEquals("([-1,go to bed])", ll.toString());
        //length should change
        assertEquals(1, ll.size());

        //Method should add an element at position greater than length
        ll.removeHead();
        ll.insert("second breakfast", 2);
        assertEquals("([2,second breakfast])", ll.toString());
        //length should change
        assertEquals(1, ll.size());
    }

    @Test // Removing an element from the beginning of a linked list.
    public void testremoveHead() {
        DLList ll = new DLList();
        ll.prepend("breakfast");
        assertEquals(1, ll.size());
        assertEquals("([0,breakfast])", ll.toString());

        //Method should remove an element when there is just one element
        ll.removeHead();
        assertEquals("()", ll.toString());
        //length should change
        assertEquals(0, ll.size());

        //Method should remove an element from the beginning of the list when there is more than 1 elem
        DLList l = makeList();
        l.removeHead();
        assertEquals("([-1,lunch] [0,dinner])", l.toString());
        //length should change
        assertEquals(2, l.size());
    }

    @Test // Emptying an empty list
    public void testToString() {
        DLList ll = makeList();
        assertEquals("([-2,breakfast] [-1,lunch] [0,dinner])", ll.toString());

        // Adding an element
        ll.prepend("wake up");
        assertEquals("([-3,wake up] [-2,breakfast] [-1,lunch] [0,dinner])", ll.toString());

        //Representing an empty list
        DLList emptyLL = new DLList();
        assertEquals("()", emptyLL.toString());
    }

    @Test // Emptying an empty list
    public void testReverseToString() {
        DLList ll = makeList();
        assertEquals("([0,dinner] [-1,lunch] [-2,breakfast])", ll.reverseToString());

        // Adding an element
        ll.prepend("wake up");
        assertEquals("([0,dinner] [-1,lunch] [-2,breakfast] [-3,wake up])", ll.reverseToString());

        //Representing an empty list
        DLList emptyLL = new DLList();
        assertEquals("()", emptyLL.reverseToString());
    }
}
