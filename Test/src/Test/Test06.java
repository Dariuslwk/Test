package Test;

public class Test06 {
    //打印出Asc码对应的表
    public static void main(String[] args){
        System.out.println((char)0+ ":" +0);
        System.out.println((char)1+ ":" +1);
        System.out.println((char)2+ ":" +2);

        for ( int i=0;i<=127;i++){
            System.out.println((char)i+ ":" +i);
        }
    }
}
