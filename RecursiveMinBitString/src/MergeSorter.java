
public class MergeSorter 
{
	private static BitString[] numbers = null;
	private static BitString[] temp = null;
	private static int length = 0;
	
	public static BitString[] mergeSort(BitString[] rows)
	{
		numbers = rows;
		length = rows.length;
		temp = new BitString[length];
		if(numbers == null)
			return null;
		mergeSort(0, length - 1);
		
		BitString[] retVal = numbers;
		numbers = null;
		temp = null;
		length = 0;
		
		return retVal;
	}
	
	private static void mergeSort(int left, int right)
	{
		if( left < right )
		{
			int middle = (left+right) / 2;
			mergeSort(left, middle);
			mergeSort(middle+1, right);
			merge(left, middle+1, right);
		}
	}
	
	private static void merge(int left, int right, int end)
	{
		int start = right-1;
        int k = left;
        int num = end - left+1;

        while(left <= start && right <= end)
            if(numbers[left].getString().compareTo(numbers[right].getString()) <0)
            	temp[k++] = numbers[left++];
            else
            	temp[k++] = numbers[right++];

        while(left <= start)    // Copy rest of first half
        	temp[k++] = numbers[left++];

        while(right <= end)  // Copy rest of right half
        	temp[k++] = numbers[right++];

        // Copy temp back
        for(int i = 0; i < num; i++, end--)
        	numbers[end] = temp[end];
	}
}
