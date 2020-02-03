package Test;

public class Test08 {
    //利用通过args数组获取数据，设置run Configerations 里的 Program Arguments的数值
    public static void main(String[] args){
        int a=Integer.parseInt(args[0]);
        int b=Integer.parseInt(args[1]);
        System.out.println(a+ "+" + b + " = " + (a+b));
        System.out.println(a+ "-" + b + " = " + (a-b));
        System.out.println(a+ "*" + b + " = " + (a*b));
    }
}
