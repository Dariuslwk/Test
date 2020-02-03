package Test;

import java.util.Scanner;

public class Test13 {
    //利用switch 语句输出各月份有多少天
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println("输入月份：");
        int month=scanner.nextInt();
        switch(month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                System.out.println("该月有31天，属于大月！");
                break;
            case 2:
                System.out.println("该月有28/29天，属于闰月");
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                System.out.println("该月有30天，属于小月！");
                default:
                    break;
        }

    }
}
