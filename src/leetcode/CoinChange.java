package leetcode;



/**
 * 给定不同面额的硬币 coins 和一个总金额 amount。编写一个函数来计算可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
 *
 * 你可以认为每种硬币的数量是无限的。
 *
 *
 * 解题思路：
 * 本题是求最少的情况，最优解，为动态规划问题
 * 1、确定状态(开一个数组)
 * db[i]：最少用多少个硬币才达到i元
 *
 * 2.初始化和边界
 * 0元时需要0个硬币，所以db[0]=0;
 * 当i<0时，（1）设为正无穷,通过初始化实现 （2）也可采用将db中所有元素赋值为amount+1
 *
 * 3.确定状态转义方程
 * 子问题为db[i],每个子问题的最后一步为db[i-coin]+1，所以方程为
 * db[i]=min{db[i-coin]+1,db[i]}
 *
 */

public class CoinChange {

    public int coinChange(int[] coins, int amount) {

        int[] db=new int[amount+1];
        int n=coins.length;

        //初始化
        db[0]=0;

        for(int i=1;i<=amount;i++){
            //初始化为正无穷
            db[i]=Integer.MAX_VALUE;
            for(int j=0;j<n;j++){
                //判断当前金额是否大于硬币面值、数组是否越界
                if(i>=coins[j] && db[i-coins[j]]!=Integer.MAX_VALUE)
                db[i]=Math.min(db[i-coins[j]]+1,db[i]);
            }
        }

        if(db[amount]==Integer.MAX_VALUE){
             db[amount]=-1;
        }

        return db[amount];
    }

}

