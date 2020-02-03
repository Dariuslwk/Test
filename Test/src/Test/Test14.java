package Test;

public class Test14 {
    //利用while循环输出26个大写、小写英文字母对应的Unicode码
    public static void main(String[] args) {
        int i=65;
        int j=97;
        while(i<=65+25 && j<=97+25){
            System.out.println((char)i + ":" + i);
            System.out.println((char)j + ":" + j);
            i++;
            j++;
        }

    }
}
