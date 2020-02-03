package Test;

public class Test03 {
    /*两种方法将（111111111）二进制转化为十进制*/
    public static void main(String[] args){
        System.out.println(Math.pow(2,7)+
                                        Math.pow(2,6)+
                                        Math.pow(2,5)+
                                        Math.pow(2,4)+
                                        Math.pow(2,3)+
                                        Math.pow(2,2)+
                                        Math.pow(2,1)+
                                        Math.pow(2,0));
        System.out.println(Math.pow(2,8)-1);
    }
}
