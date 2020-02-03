package Test;

import java.util.Scanner;

public class Test10 {
    //利用if else 语句进行成绩判断
    public static void main(String[] args) {
        boolean flag;
        Scanner scanner=new Scanner(System.in);
        System.out.println("输入分数：");
        int score =scanner.nextInt();
        if(score>=60) {
            flag = true;
            System.out.println("分数是" + score + "：" + flag);
        }
        else if (score<=60) {
            flag = false;
            System.out.println("分数是" + score + "：" + flag);
        }
    }
}
