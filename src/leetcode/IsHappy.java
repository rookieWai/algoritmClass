package leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * 202快乐数
 *
 * 编写一个算法来判断一个数 n 是不是快乐数。
 * 「快乐数」定义为：
 *
 * 对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和。
 * 然后重复这个过程直到这个数变为 1，也可能是 无限循环 但始终变不到 1。
 * 如果 可以变为1，那么这个数就是快乐数。
 * 如果 n 是快乐数就返回 true ；不是，则返回 false 。
 */
public class IsHappy {

    //进行运算
    public int conversion(int num){
        int sum=0;
        while(num>0){
            sum+=(num%10)*(num%10);
            num/=10;
        }
        return sum;
    }

    public boolean isHappy(int n) {
        //集合保存结果
        Set<Integer> result=new HashSet<>();
        //进行一次运算后的值
        int changeN=conversion(n);
        //判断运算后的结果是否出现在集合里
        while(!result.contains(changeN)){
            //结果为1，为快乐数返回true
            if(changeN==1){
                return true;
            }
            //将结果保存到集合里
            result.add(changeN);
            //做一次运算
            changeN=conversion(changeN);

        }
        return false;
    }
}
