package leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 螺旋矩阵 II
 * 给你一个正整数 n ，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的 n x n 正方形矩阵 matrix 。
 */

public class GenerateMatrix {
    public int[][] generateMatrix(int n) {

        int[][] matrix = new int[n][n];

        int startX=0,startY=0;  //每一个圈的起始位置
        int cycleNums=n/2;  //一共循环几圈
        int count=1;  //用来给矩阵赋值

        int control=1; //控制每一步即每一条边遍历的长度

        int i,j;

        while(cycleNums-->0){

            //从左到右赋值
            for(j=startY;j<startY+n-control;j++){
                matrix[startX][j]=count++;
            }

            //从上到下
            for(i=startX;i<startX+n-control;i++){
                matrix[i][j]=count++;
            }

            //从右到左
            for(;j>startY;j--){
                matrix[i][j]=count++;
            }

            //从下到上
            for(;i>startX;i--) {
                matrix[i][j] = count++;
            }

            //起始位置加1
            startX++;
            startY++;
            //控制每一圈没一条边遍历的长度，每一圈缩小的长度为2，所以加2
            control+=2;
        }

        //如果n为奇数，则存在中间元素，单独给它赋值
        if(n%2!=0){
            matrix[n/2][n/2]=count;

        }
        return matrix;
    }
}