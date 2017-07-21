### 最大递增（递减）子序列问题

***解题思路***

1个整数序列389 207 155 300 299 170 158 65 求其最大递增序列，如给出的序列有8个元素，如果知道前7个元素中从第i个元素开始对应的最大上升子序列个数，用第8个数与前面的7个数比较，在小于第8个数中取其对应子序列最大的值加1即为8个元素的最大上升子序列

```c++
#include<iostream>
using namespace std;

int max(int a,int b)
{
	return a>b?a:b; 
}

int main()
{
	int N;
	cin>>N;
	while(N--)
	{
		int m;
		cin>>m;
		int *p = new int[m];
		int *dp = new int[m+1];
		//0    1   2   3   4   5   6  7   8
		//389 207 155 300 299 170 158 65  ?
		//数组dp用于存放每个状态当前最大降序列的个数
		//例：dp[5]=4表示当取到170时，当前最大下降序列元素的个数为4 
		for(int i=0;i<m;i++)
		{
			cin>>p[i];
			dp[i] = 1;
		}
		dp[m]=0;
		for(int i=1;i<=m;i++)
		{
			for(int j=0;j<i;j++)
			{
				if(i==m || p[j]>p[i])
				{
					dp[i] = max(dp[i],dp[j]+1);
				}
			}
		}
		cout<<dp[m]-1<<endl;
		delete[] dp;
		delete[] p;
	}
	return 0;
}
```

