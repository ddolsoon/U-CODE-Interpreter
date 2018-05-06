/* 
  100보다 작은 완전수 찾기
  완전수: 자신을 제외한 자신의 인수들의 곱이
  자신과 같은 수
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
