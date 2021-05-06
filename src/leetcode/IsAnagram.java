package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

/**
 * 有效的字母异位词
 *
 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
 *
 * 你可以假设字符串只包含小写字母。
 */

public class IsAnagram {
    //普通解法
    public boolean isAnagram(String s, String t) {
        //两字符串不相等，就不会互是字母异位词
        if (s.length() != t.length()) {
            return false;
        }



        char[] strS = s.toCharArray();
        char[] strT = t.toCharArray();

        //两字符串数组排序
        Arrays.sort(strT);
        Arrays.sort(strS);

        //排好序后比较是否相等,相等则是字母异位词返回true
        return Arrays.equals(strT,strS);
    }


    //哈希表解法
    public boolean isAnagram2(String s, String t) {
        //两字符串不相等，就不会互是字母异位词
        if (s.length() != t.length()) {
            return false;
        }

        //记录字符次数的哈希表
        int []hashTable=new int[26];
        //将s字符串中的字符映射到哈希表中，只需给该位置的元素加一
        for(int i=0;i<s.length();i++){
            hashTable[s.charAt(i)-'a']++;
        }
        //将t字符串中的字符映射到哈希表中，只需给该位置的元素减一
        for(int i=0;i<t.length();i++){
            hashTable[t.charAt(i)-'a']--;
        }
        //循环查看哈希表中所以元素，有不为零则表示两字符串不是字母异位词，返回false
        for(int i=0;i<hashTable.length;i++){
            if(hashTable[i]!=0){
                return false;
            }
        }

        return true;
    }

}
