/*
  Palindrome:
  �տ����� �ڿ����� ���� ������ ������ ��츦 �ν�
  12321, -3553 ��
*/

#include <stdio.h>

void main (void)
{
	int org, rev;
	int i, j;

	scanf("%d", &org);
	if (org < 0)
		org = (-1) * org;
	i = org;
	rev = 0;
	while (i != 0) {
		j = i % 10;
		rev = rev * 10 + j;
		i /= 10;
	}
	if (rev == org)
		printf("%d ", org);
	printf("\n");
	return;
}
