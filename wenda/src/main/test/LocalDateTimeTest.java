package test;

import java.time.LocalDateTime;

public class LocalDateTimeTest {

    public static void main(String[] args) {

        LocalDateTime current = LocalDateTime.now();
        System.out.println( current );

        LocalDateTime dateTime = LocalDateTime.of(1997 , 7 ,1  , 0  , 0 , 0 , 0 );
        System.out.println( dateTime );

    }

}
