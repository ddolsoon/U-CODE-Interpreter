/*
����ڷκ��� 10���� �Է°��� Ȧ������
���� ���� ���� ����ϴ� ���α׷�
*/

#include <stdio.h>
void odd(int i, int a[]);

void main(void)
{
	int i;
	int result;
	int a[10];
	i = result = 0;
	while (i < 10)
	{
		odd(i, a);
		result = result + a[i];
		++i;
	}
	printf("%d ", result);

	printf("\n");
}

void odd(int i, int a[])
{
	int j;
	scanf_s("%d", &j);
	if (j % 2 == 0)
		j = 0;
	a[i] = j;
}