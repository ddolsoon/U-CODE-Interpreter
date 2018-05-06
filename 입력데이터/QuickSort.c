/*
	������ ������ �Է¹�����, �Է½�ȣ�� ���� 0�̰�,
	�������� �̿��Ͽ�, ������������ �����Ѵ�.
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
	int pivot = arr[left];			//�ǹ��� ��ġ�� ���� ����
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

	Swap(arr, left, high);			//�ǹ��� high�� ����Ű�� ��� ��ȯ

	return high;					//�Ű��� �ǹ��� ��ġ���� ��ȯ
}

void QuickSort(int arr[], int left, int right)
{
	if (left <= right)
	{
		int pivot = Partition(arr, left, right);		//�ѷ� ������
		QuickSort(arr, left, pivot - 1);				//���� ������ ����
		QuickSort(arr, pivot + 1, right);				//������ ������ ����
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