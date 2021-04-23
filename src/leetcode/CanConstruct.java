package leetcode;

import java.lang.reflect.Array;

/**
 * 383. 赎金信
 * 给定一个赎金信 (ransom) 字符串和一个杂志(magazine)字符串，判断第一个字符串 ransom 能不能由第二个字符串 magazines 里面的字符构成。如果可以构成，返回 true ；否则返回 false。
 *
 * (题目说明：为了不暴露赎金信字迹，要从杂志上搜索各个需要的字母，组成单词来表达意思。杂志字符串中的每个字符只能在赎金信字符串中使用一次。)
 */

public class CanConstruct {
    public boolean canConstruct(String ransomNote, String magazine) {

        //大小为26的数组哈希表
        int hashTable[]=new int[26];

        //将magazines映射到哈希表中
        for(char i : magazine.toCharArray()){
            hashTable[i-'a']++;
        }

        //遍历ransomNote的字符
        for(char j : ransomNote.toCharArray()){
            //ransomNote字符在哈希表中对应位置的值减1
            hashTable[j-'a']--;
            //如果当前哈希表中对应位置的值小于0，说明magazines字符串的字符满足不了ransomNote字符串
            if(hashTable[j-'a']<0){
                return false;
            }
        }
        return true;
    }
}
