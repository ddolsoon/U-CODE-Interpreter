/* 
  100���� ���� ������ ã��
  ������: �ڽ��� ������ �ڽ��� �μ����� ����
  �ڽŰ� ���� ��
*/

#include <stdio.h>

const int max=100;

void main (void)
{
	int i, j, k;
	int rem, sum;

	i = 2;
	while (i <= max) {
		sum = 0;
		k = i / 2;
		j = 1;
		while (j <= k) {
			rem = i % j;
			if (rem == 0)
				sum += j;
			++j;
		}
		if (i == sum)
			printf("%d ", i);
		++i;
	}
	printf("\n");
	return;
}
