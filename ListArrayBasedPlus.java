//package version1;

/*

 * Purpose: Data Structure and Algorithms Lab 2 Problem 1

 * Status: Complete and thoroughly tested

 * Last update: 09/08/19

 * Submitted:  09/10/19

 * Comment: test suite and sample run attached

 * @author: Thomas Keane

 * @version: 2019.09.08

 */

import java.util.Arrays;
import java.util.List;

public class ListArrayBasedPlus<T> extends ListArrayBased <T>{

	public ListArrayBasedPlus() 
	{
		
	}
	
	private void resize()
	{
		T[] temp = items;
		items = (T[]) new Object[(int) (items.length * 1.5)];
		for(int i = 0; i < numItems; i++)
		{
			items[i] = temp[i];
		}
	}
	
	public long reverseAttempt1()
	{
		int resizedSize = (numItems % 2 == 0)? numItems/2: (numItems-1)/2; //Pun completely intended
		long startTime = System.nanoTime();
		for(int i = 0; i < resizedSize; i++)
		{
			T leftmost = items[i];
			items[i] = items[numItems - i - 1];
			items[numItems - i - 1] = leftmost;
		}
		
		return System.nanoTime() - startTime;
	}
	
	public long reverseAttempt2()
	{
		int resizedSize = (numItems % 2 == 0)? numItems/2: (numItems-1)/2; //Pun completely intended
		long startTime = System.nanoTime();
		T[] temp = items;
		for(int i = 0; i < resizedSize; i++)
		{
			items[i] = temp[numItems - i - 1];
		}
		
		return System.nanoTime() - startTime;
	}
	
	public String toString()
	{
		StringBuilder output = new StringBuilder("List of size " + numItems + " has the following items : ");
		for(int i = 0; i < numItems; i++)
		{
			if(items[i] == null)
			{

				output.append("null ");
			}
			else
			{
				output.append(items[i].toString() + " ");
			}
		}
		return output.toString();
	}
	
	public void add(int index, T item)
	{
		try
		{
			super.add(index, item);
		} catch(ListException l)
		{
			resize();
			super.add(index, item);
		}
	}
	
	public void betterAdd(int index, T item)
	{
		T[] temp = items;
		if (numItems == items.length) //Fixes implementation error
	     {
			items = (T[])new Object[(int)(items.length * 1.5)];
			
	     }  // end if
	     if (index >= 0 && index <= numItems)
	     {
				items[index] = item;
				numItems++;
				for(int i = index + 1; i < numItems; i++)
				{
					items[i] = temp[i-1];
				}
				
	     }
	     else
	     {
	         // index out of range
	         throw new ListIndexOutOfBoundsException(
	             "ListIndexOutOfBoundsException on add");
	     }  // end if
	}
}
