package Test;

import java.util.Scanner;

public class Test11 {
    //利用逻辑关系与、非进行数值的判断
    public static void main(String[] args) {
        boolean flag;
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入一个数字：");
        int num=scanner.nextInt();
        if(num>=0 && num<5){
            flag=true;
            System.out.println("该数字在0~5之间：" + flag );
        } else if (!(num>=0 && num<5)){
            flag=false;
            System.out.println("该数字在0~5之间：" + flag);
        }

    }
}
