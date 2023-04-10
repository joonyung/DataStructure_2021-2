import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		String option = sc.nextLine();
		option = option.toUpperCase();
		
		int N = sc.nextInt();
		int[] unsorted_array = new int[N];
		for(int i=0; i<N; i++)
		{
			unsorted_array[i] = sc.nextInt();
		}

		if(option.equals("B"))
		{
			BubbleSort(unsorted_array);
		}
		else if(option.equals("I"))
		{
			InsertionSort(unsorted_array);
		}
		else if(option.equals("Q"))
		{
			QuickSort(unsorted_array);
		}
		else if(option.equals("T"))
		{
			ThreeWayQuickSort(unsorted_array);
		}
		else if(option.equals("M"))
		{
			MergeSort(unsorted_array);
		}
		else if(option.equals("R"))
		{
			RadixSort(unsorted_array);
		}
	}
	
	private static void BubbleSort(int[] arr) {
		int temp = 0;
		for(int i = 0; i < arr.length - 1; i++) {
			for(int j = 0; j < arr.length - i - 1; j++) {
				if(arr[j] > arr[j+1]) {
					temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}
			}
		}
		
		for(int i = 0; i < arr.length-1; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println(arr[arr.length-1]);
		
	}

	private static void InsertionSort(int[] arr) {
		for(int i = 1; i < arr.length; i++) {
			int temp = arr[i];
			int j = i;
			
			while(j > 0 && arr[j-1] > temp) {
				arr[j] = arr[j-1];
				j--;
			}
			arr[j] = temp;
		}
		
		for(int i = 0; i < arr.length-1; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println(arr[arr.length-1]);
	}
	
	private static void QuickSort(int[] arr) {
		RecQuickSort(arr);
		
		for(int i = 0; i < arr.length-1; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println(arr[arr.length-1]);
	}
	
	private static void RecQuickSort(int[] arr) {
		if(arr == null || arr.length <= 1) return;		
		
		int pivotIndex = 0;
		
		int Pivot = arr[pivotIndex];
		
		int numLeft = 0;
		int numRight = 0;
		
		for(int i = 1; i < arr.length; i++) {
			if (arr[i] <= Pivot) {
				numLeft++;
			}
			else if (arr[i] > Pivot) {
				numRight++;
			}
		}
		
		int[] left = new int[numLeft];
		int[] right = new int[numRight];
		
		int l = 0;
		int r = 0;
		
		for(int i = 1; i < arr.length; i++) {
			if (arr[i] <= Pivot) {
				left[l++] = arr[i];
			}
			else if (arr[i] > Pivot) {
				right[r++] = arr[i];
			}
		}
		
		RecQuickSort(left);
		RecQuickSort(right);
	
		
		for(int i = 0; i < left.length; i++) {
			arr[i] = left[i];
		}
		arr[l] = Pivot;
		for(int i = 0; i < right.length; i++) {
			arr[l+1+i] = right[i];
		}
		
		return;
	}
	
	private static void ThreeWayQuickSort(int[] arr) {	
		RecThreeWayQuickSort(arr);
		
		for(int i = 0; i < arr.length-1; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println(arr[arr.length-1]);
	}
	
	private static void RecThreeWayQuickSort(int[] arr) {
		if(arr == null || arr.length <= 1) return;
		
		if (arr.length == 2) {
			if(arr[0] > arr[1]) {
				int tmp = arr[0];
				arr[0] = arr[1];
				arr[1] = tmp;
			}
			return;
		}
		
		
		int pivotIndex1 = 0;
		int pivotIndex2 = arr.length - 1;
		
		int minPivot = 0;
		int maxPivot = 0;
		
		if (arr[pivotIndex1] >= arr[pivotIndex2]) {
			maxPivot = arr[pivotIndex1];
			minPivot = arr[pivotIndex2];
		}
		else {
			minPivot = arr[pivotIndex1];
			maxPivot = arr[pivotIndex2];
			
		}
		
		int numLeft = 0;
		int numMiddle = 0;
		int numRight = 0;
		
		for(int i = 1; i < arr.length - 1; i++) {
			if (arr[i] <= minPivot) {
				numLeft++;
			}
			else if (arr[i] <= maxPivot && arr[i] > minPivot) {
				numMiddle++;
			}
			else if (arr[i] > maxPivot) {
				numRight++;
			}
		}
		
		int[] left = new int[numLeft];
		int[] middle = new int[numMiddle];
		int[] right = new int[numRight];
		
		int l = 0;
		int m = 0;
		int r = 0;
		
		for(int i = 1; i < arr.length - 1; i++) {
			if (arr[i] <= minPivot) {
				left[l++] = arr[i];
			}
			else if (arr[i] <= maxPivot && arr[i] > minPivot) {
				middle[m++] = arr[i];
			}
			else if (arr[i] > maxPivot) {
				right[r++] = arr[i];
			}
		}
		
		RecThreeWayQuickSort(left);
		RecThreeWayQuickSort(middle);
		RecThreeWayQuickSort(right);
	
		
		for(int i = 0; i < left.length; i++) {
			arr[i] = left[i];
		}
		arr[l] = minPivot;
		for(int i = 0; i < middle.length; i++) {
			arr[l+1+i] = middle[i];
		}
		arr[l+1+m] = maxPivot;
		for(int i = 0; i < right.length; i++) {
			arr[l+m+2+i] = right[i];
		}
		
		return;
	}
	
	
	private static void MergeSort(int[] arr) {		
		RecMergeSort(arr);
		
		for(int i = 0; i < arr.length-1; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println(arr[arr.length-1]);
	}
	
	private static void RecMergeSort(int[] arr) {
		if(arr == null || arr.length <= 1) return;
		
		int middle = arr.length/2;
		int[] left = new int[middle];
		int[] right = new int[arr.length - middle];
		
		for(int i = 0; i < middle; i++) left[i] = arr[i];
		for(int j = 0; j < arr.length - middle; j++) right[j] = arr[middle + j];
		
		RecMergeSort(left);
		RecMergeSort(right);
		
		int l = 0;
		int r = 0;
		for(int i = 0; i < arr.length; i++) {
			if (r >= right.length || (l < left.length && left[l] < right[r])) arr[i] = left[l++];
			else arr[i] = right[r++];
		}
		return;
	}
	
	private static void RadixSort(int[] arr) {
		int max = getMax(arr);
		
		for(int exp = 1; max / exp > 0; exp *= 10) {
			countSort(arr, exp);
		}
		
		for(int i = 0; i < arr.length-1; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println(arr[arr.length-1]);
	}
	
	private static int getMax(int[] arr) {
		int max = arr[0];
		for(int i = 1; i < arr.length; i++) {
			if (arr[i] > max) max = arr[i];
		}
		return max;
	}
	
	private static void countSort(int[] arr, int exp) {
		int[] output = new int [arr.length];
		int[] count = new int [10];
		
		for(int i = 0; i < count.length ; i++) {
			count[i] = 0;
		}
		
		for(int i = 0; i < arr.length; i++) {
			count[(arr[i] / exp) % 10] += 1;
		}
		for(int i = 1; i < count.length; i++) {
			count[i] += count[i-1];
		}
		for(int i = arr.length-1; i >=0; i--) {
			output[count[(arr[i] / exp) % 10] - 1] = arr[i];
			count[(arr[i] / exp) % 10] -= 1;
		}
		for(int i = 0; i < arr.length; i++) {
			arr[i] = output[i];
		}
		
	}
	
}