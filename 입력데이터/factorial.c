/*
  ��� ȣ�� ������� ���丮�� �� ���
*/

#include <stdio.h>

int factorial (int n) {
	if (n == 1)
		return 1;
	else
		return n * factorial(n-1);
}

void main (void)
{
	int n, f;
	
	scanf("%d", &n);
	f = factorial(n);
	printf("%d ", f);
	printf("\n");
	return;
}

