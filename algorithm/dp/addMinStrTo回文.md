## 题目描述

所谓回文字符串，就是一个字符串，从左到右读和从右到左读是完全一样的，比如"aba"。当然，我们给你的问题不会再简单到判断一个字符串是不是回文字符串。现在要求你，给你一个字符串，可在任意位置添加字符，最少再添加几个字符，可以使这个字符串成为回文字符串。

输入

第一行给出整数N（0<N<100）

第一行给出整数N（0<N<100）
接下来的N行，每行一个字符串，每个字符串长度不超过1000.

输出

每行输出所需添加的最少字符数

样例输入

```
1
Ab3bd
```

样例输出

```
2
```

***解题思路***

把字符串逆序，再与原字符串求公共子串。原串长度 - 公共字串长度即为所求。

如：

abcca  -> accba

原串oldstr与逆串reversestr的最大公共子串为acca，len(abcca) - len(acca)即为需要添加的新串。

```
状态方程： dp[i][j] = {dp[i-1][j-1]+1 | max(dp[i-1][j],dp[i][j-1])}
dp[i][j]表示oldstr前i个字符组成的子串与逆串reversest中前j个字符组成字串所含有最大公共字串长度
```



```c++
#include<iostream>
#include<string.h>
using namespace std;
int max(int a,int b)
{
	return a>b?a:b;
}

int huiwenproblem(char* str)
{
	int len = strlen(str);
	char *cpstr = new char[len];
	int **dp = new int*[len];
	for(int i=0;i<len;i++)
	{
		dp[i] = new int[len];
		cpstr[i] = str[len-1-i];
			
	}
	int rowmax = 0;	
	int colmax = 0;
	for(int i=0;i<len;i++)
	{
		if(str[0]==cpstr[i])
		{
			rowmax=1;
		}
		dp[0][i] = rowmax==0?0:1;
		if(cpstr[0]==str[i])
		{
			colmax = 1;
		}
		dp[i][0] = colmax==0?0:1;
	}
	for(int i=0;i<len;i++)
	{
		for(int j=0;j<len;j++)
		{
			if(i>=1&&j>=1)
			{
				if(str[i]==cpstr[j])
				{
					dp[i][j] = dp[i-1][j-1]+1;
				}
				else
				{
					dp[i][j] = max(dp[i-1][j],dp[i][j-1]);
				}
			}
			
		}
	}
	int res = dp[len-1][len-1];
	for(int i=0;i<len;i++)
		delete[]dp[i];
	return len-res;
}

int main()
{
	int n;
	cin>>n;
	while(n--)
	{
		char s[1000];
		cin>>s;
		int res = huiwenproblem(s);
		cout<<res<<endl;
	}
	return 0;
}
```





```
3
admin
4
aaas
1
assa
0
```

