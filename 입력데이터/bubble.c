/* 
  ������ ������ �Է¹޾� ������������ ����. 
  �Է��� �ִ� 100������ �����ϸ�
  �Է� ���� ��ȣ�� ���� 0
*/

#include <stdio.h>

void main (void)
{
	int list[100];
	int element;
	int total, i, top;
	int temp;

	i = 0;
	scanf("%d", &element);
	while (element != 0){   // 0: end of input
		list[i] = element;
		++i;
		scanf("%d", &element);
	}

	top = total = i - 1;
	while (top > 1) {
		for (i = 0; i < top; ++i) 
			if (list[i] > list[i+1]) {
			  temp = list[i];
			  list[i] = list[i+1];
			  list[i+1] = temp;
			}
		top--;
	}
	i = 0;
	while (i <= total) 
		printf("%d ", list[i++]);
	printf("\n");
	return;
}

