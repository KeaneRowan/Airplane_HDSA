/*

 * Purpose: Data Structure and Algorithms Lab 3 Problem 2

 * Status: Complete and thoroughly tested

 * Last update: 09/23/19

 * Submitted:  09/24/19

 * Comment: test suite and sample run attached

 * @author: Thomas Keane

 * @version: 2019.09.08

 */

// ********************************************************
// Interface ListInterface for the ADT list.
// *********************************************************
public interface ListInterface <T>
{
  boolean isEmpty();
  int size();
  void add(int index, T item)
                  throws ListIndexOutOfBoundsException;
  T get(int index)
                    throws ListIndexOutOfBoundsException;
  void remove(int index)
                     throws ListIndexOutOfBoundsException;
  void removeAll();
}  // end ListInterface

// Please note that this code is slightly different from the textbook code
//to reflect the fact that the Node class is implemented using data encapsulation