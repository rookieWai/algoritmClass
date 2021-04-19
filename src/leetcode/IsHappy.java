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

    public int conversion(int num){
        int sum=0;
        while(num>0){
            sum+=(num%10)*(num%10);
            num/=10;
        }
        return sum;
    }

    public boolean isHappy(int n) {
        Set<Integer> result=new HashSet<>();
        int changeN=conversion(n);
        while(!result.contains(changeN)){


            if(changeN==1){
                return true;
            }

            result.add(changeN);
            changeN=conversion(changeN);

        }
        return false;
    }
}
