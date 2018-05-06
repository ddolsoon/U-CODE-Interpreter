/*
    피보나치 수열의 1번째 값부터 10번째 값까지 출력한다.
*/
#include <stdio.h>

int Fibo(int n)
{
	if (n == 1)
		return 0;
	else if (n == 2)
		return 1;
	else
		return Fibo(n - 1) + Fibo(n - 2);
}


void main(void)
{
	int i;
	for (i = 1; i < 10; ++i)
		printf("%d ", Fibo(i));

	printf("\n");

	return;
}