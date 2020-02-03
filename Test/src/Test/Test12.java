package Test;

import java.util.Scanner;

public class Test12 {
    //利用运算关系和if else语句取三者之间的最高值
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println("输入语文的分数：");
        int Chinese=scanner.nextInt();
        System.out.println("输入数学的分数：");
        int Math=scanner.nextInt();
        System.out.println("输入英语的分数：");
        int English=scanner.nextInt();

        if(Chinese>=Math & Chinese>=English){
            System.out.println("三科中分数最高的是：" + Chinese);
        }else if(Math>=Chinese & Math>=English ){
            System.out.println("三科中分数最高的是：" + Math);
        }else if (English>=Chinese & English>=Math){
            System.out.println("三科中分数最高的是：" + English);
        }

    }
}
