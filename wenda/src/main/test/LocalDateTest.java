package test;

import java.time.LocalDate; // JDK 1.8 新提供的

public class LocalDateTest {

    public static void main(String[] args) {

        LocalDate date = LocalDate.now(); // 获得今天对应的日期
        System.out.println( date );

        LocalDate birthdate = LocalDate.of( 1998 , 9 , 31 );
        System.out.println( birthdate );

    }

}
