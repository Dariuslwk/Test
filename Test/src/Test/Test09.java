package Test;

import java.util.Scanner;

public class Test09 {
    //通过Scanner从控制台获取数据，不同的字符调用不同的类型
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);//步骤一：创建scanner对象

        System.out.println("姓名：");//步骤二：向控制台输出文本
        String name=scanner.next();//步骤三：接收输入的名字字符串

        System.out.println("性别：");
        char sex = scanner.next().charAt(0);//获取输出台的同一个字符

        System.out.println("年龄：");
        int age =scanner.nextInt();

        System.out.println("身高：");
        double height = scanner.nextDouble();

        System.out.println("性格：");
        String type = scanner.next();

    }
}
