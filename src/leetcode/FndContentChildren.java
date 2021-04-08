package leetcode;


import java.util.Arrays;

/**
 * 分发饼干
 * 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。
 *
 * 对每个孩子 i，都有一个胃口值g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；并且每块饼干 j，都有一个尺寸 s[j]。如果 s[j]>= g[i]，我们可以将这个饼干 j 分配给孩子 i ，这个孩子会得到满足。你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。
 *
 * 贪心
 */

public class FndContentChildren {
    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int indexS=s.length-1;  //饼干的位置
        int result=0;   //记录可以满足孩子的数量

        //从胃口值大到小遍历孩子
        for(int i=g.length-1;i>=0;i--){
            if(indexS>=0&&s[indexS]>=g[i]) {  //判断目前最大的饼干是否满足孩子的胃口值
                //满足
                indexS--;  //饼干被吃掉，饼干位置向前一个
                result++;  //结果值加一
            }
        }
        return result;
    }
}
