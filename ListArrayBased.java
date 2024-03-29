//package version1;

//********************************************************
//Array-based implementation of the ADT list.
//*********************************************************

/*

 * Purpose: Data Structure and Algorithms Lab 2 Problem 1

 * Status: Complete and thoroughly tested

 * Last update: 09/08/19

 * Submitted:  09/10/19

 * Comment: test suite and sample run attached

 * @author: Thomas Keane

 * @version: 2019.09.08

 */
public class ListArrayBased<T> implements ListInterface<T>
{

 private static final int MAX_LIST = 3;
 protected T []items;  // an array of list items
 protected int numItems;  // number of items in list

 public ListArrayBased()
 {
     items = (T[])new Object[MAX_LIST];
     numItems = 0;
 }  // end default constructor

 public boolean isEmpty()
 {
     return (numItems == 0);
 } // end isEmpty

 public int size()
 {
     return numItems;
 }  // end size

 public void removeAll()
 {
     // Creates a new array; marks old array for
     // garbage collection.
     items = (T[])new Object[MAX_LIST];
     numItems = 0;
 } // end removeAll

 public void add(int index, T item)
 throws  ListIndexOutOfBoundsException
 {
	 if (numItems >= items.length) //Fixes implementation error
     {
         throw new ListException("ListException on add");
     }  // end if
     if (index >= 0 && index <= numItems)
     {
         // make room for new element by shifting all items at
         // positions >= index toward the end of the
         // list (no shift if index == numItems+1)
         for (int pos = numItems-1; pos >= index; pos--)  //textbook code modified to eliminate logic error causing ArrayIndexOutOfBoundsException
         {
             items[pos+1] = items[pos];
         } // end for
         // insert new item
         items[index] = item;
         numItems++;
     }
     else
     {
         // index out of range
         throw new ListIndexOutOfBoundsException(
             "ListIndexOutOfBoundsException on add");
     }  // end if
 } //end add

 public T get(int index)
 throws ListIndexOutOfBoundsException
 {
     if (index >= 0 && index < numItems)
     {
         return items[index];
     }
     else
     {
         // index out of range
         throw new ListIndexOutOfBoundsException(
             "ListIndexOutOfBoundsException on get");
     }  // end if
 } // end get

 public void remove(int index)
 throws ListIndexOutOfBoundsException
 {
     if (index >= 0 && index < numItems)
     {
         // delete item by shifting all items at
         // positions > index toward the beginning of the list
         // (no shift if index == size)
         for (int pos = index+1; pos < numItems; pos++) //textbook code modified to eliminate logic error causing ArrayIndexOutOfBoundsException

         {
             items[pos-1] = items[pos];
         }  // end for
         items[--numItems] = null; //Fixes memory leak error
     }
     else
     {
         // index out of range
         throw new ListIndexOutOfBoundsException(
             "ListIndexOutOfBoundsException on remove");
     }  // end if
 } //end remove
}

