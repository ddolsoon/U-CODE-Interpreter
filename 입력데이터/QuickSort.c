/*
	임의의 정수를 입력받으며, 입력신호는 정수 0이고,
	퀵정렬을 이용하여, 오름차순으로 정렬한다.
*/
#include <stdio.h>

void Swap(int arr[], int idx1, int idx2)
{
	int temp = arr[idx1];
	arr[idx1] = arr[idx2];
	arr[idx2] = temp;
}

int Partition(int arr[], int left, int right)
{
	int pivot = arr[left];			//피벗의 위치는 가장 왼쪽
	int low = left + 1;
	int high = right;

	while (low <= high)
	{
		while (pivot > arr[low])
			low++;

		while (pivot < arr[high])
			high--;

		if (low <= high)
			Swap(arr, low, high);

	}

	Swap(arr, left, high);			//피벗과 high가 가리키는 대상 교환

	return high;					//옮겨진 피벗의 위치정보 반환
}

void QuickSort(int arr[], int left, int right)
{
	if (left <= right)
	{
		int pivot = Partition(arr, left, right);		//둘로 나눠서
		QuickSort(arr, left, pivot - 1);				//왼쪽 영역을 정렬
		QuickSort(arr, pivot + 1, right);				//오른쪽 영역을 정렬
	}
}

void main(void)
{
	int list[100];
	int element;
	int i;
	int len;
	i = 0;

	scanf("%d", &element);
	while (element != 0){
		list[i] = element;
		++i;
		scanf("%d", &element);
	}
	len = i;
	QuickSort(list, 0, i - 1);

	i = 0;
	for (; i < len; ++i)
		printf("%d ", list[i]);

	printf("\n");
	return;
}