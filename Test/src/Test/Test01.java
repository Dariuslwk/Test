package Test;

public class Test01 {
    public static void main(String[] args){
        /*两种方法计算(sqrt(20)+sqrt(10))/(sqrt(20)-sqrt(10))*/
        double sqrt20=Math.sqrt(20);
        double sqrt10=Math.sqrt(10);
        double result;
        result=(sqrt20+sqrt10)/(sqrt20-sqrt10);
        System.out.println("(sqrt(20)+sqrt(10))/(sqrt(20)-sqrt(10)) = "+Math.round(10*result)/10.0);
        System.out.println("(sqrt(20)+sqrt(10))/(sqrt(20)-sqrt(10)) + "+ Math.round(10*(Math.sqrt(20)+Math.sqrt(10))/(Math.sqrt(20)-Math.sqrt(10)))/10.0);
    }
}
