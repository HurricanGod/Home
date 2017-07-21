### 二叉树

**二叉树的性质**
1. 第i(i>=1)层最多有2^(i-1)个结点
2. 深度为k(k>=0)的二叉树最少有k个结点，最多有2^k -1个结点
3. 任意1棵非空二叉树，如果其叶结点数为n0,度为2的非叶结点数为n2，则有n0 = 1 + n2
   ***证明：***有n个结点的二叉树有n-1条边，在1棵二叉树中设度为0的结点为n0,度为1
   的结点为n1,度为2的结点为n2,则有n = n0 + n1 + n2,叶结点即为度为0的结点，
   有**n - 1 = ((n0 + n1 + n2) - 1) = n1 + 2 * n2  => n0 - 1 = n2**
4. 具有n个结点的完全二叉树的深度为log2(n+1)的值向上取整


-----

**完全二叉树**
若1棵具有n个结点深度为k的二叉树，从第1层到第k-1层的各层结点都是满的，仅第k层可以不满且缺的部分只能是从右往左连续缺若干结点的二叉树为**完全二叉树**

#### 堆
优先级队列的各种实现中，堆是最高效的数据结构。

任1关键码均小于或等于它左右子节点关键码，位于堆顶的结点的关键码是集合中最小的则称为最小堆。

最小堆或最大堆是1棵**完全二叉树**


