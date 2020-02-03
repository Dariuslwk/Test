package Test;

public class Test04 {
    public static void main(String[] args) {
        System.out.println(Integer.MIN_VALUE + "~" + Integer.MAX_VALUE);//Integer变量的最小值、最大值
        System.out.println(Byte.parseByte("108") + 10);//将字符串108转化为字节型变量
        System.out.println(Byte.MIN_VALUE + "~" + Byte.MAX_VALUE);//字节型变量的最小值、最大值
        System.out.println(Integer.parseInt("5000") + 5);//将字符串转化为Integer变量
        System.out.println(Integer.toBinaryString(16));//将16（十六进制）转化为整型变量（二进制）
    }
}
