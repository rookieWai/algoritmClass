package leetcode;

/**
 * 最长公共子序列
 * 题目：
 * 给定两个字符串text1和text2，返回这两个字符串的最长公共子序列的长度。
 * 一个字符串的子序列是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下删除某些字符（也可以不删除任何字符）后组成的新字符串。
 * 例如，"ace" 是 "abcde" 的子序列，但 "aec" 不是 "abcde" 的子序列。两个字符串的「公共子序列」是这两个字符串所共同拥有的子序列。
 * 若这两个字符串没有公共子序列，则返回 0。
 *
 *
 * 解题思路：
 * 寻找最长公共子序列，存在性最值问题，可判断为动态规划问题，基本步骤为
 * 1.确定状态(开一个数组）
 * db[i][j]
 *
 */
public class LongestCommonSubsequence {
    public static int execute(String text1,String text2){

        int len1=text1.length(),len2=text2.length();
        int [][]db=new int [len1+1][len2+1];

        //初始化
        for(int i=0;i<=len1;i++){
            db[i][0]=0;
        }
        for(int j=0;j<=len2;j++){
            db[0][j]=0;
        }

        for(int i=1;i<=len1;i++){
            for(int j=1;j<=len2;j++){
                if(text1.charAt(i-1)==text2.charAt(j-1)){
                    db[i][j]=db[i-1][j-1]+1;
                }
                else{
                    db[i][j]=Math.max(db[i][j-1],db[i-1][j]);
                }
            }
        }
        return db[len1][len2];
    }
}
