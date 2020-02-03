package Test;

public class Test07 {
    //利用boolean类型变量进行判断
    public static void main(String[] args){
        boolean flag;
        flag=true;
        System.out.println("考试通过：" + flag);

        flag=false;
        System.out.println("考试挂了：" + flag);

        flag=65>=60;
        System.out.println(65 + "分及格：" + flag);
    }
}
