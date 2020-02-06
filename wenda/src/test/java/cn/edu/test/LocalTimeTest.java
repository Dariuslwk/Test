package cn.edu.test;

import java.time.LocalTime;

public class LocalTimeTest {

    public static void main(String[] args) {

        LocalTime current = LocalTime.now(); // 获得现在对应的时间
        System.out.println( current );

        LocalTime time = LocalTime.of( 22 , 10 );
        System.out.println( time );

        time = LocalTime.of( 22 , 10 , 20 );
        System.out.println( time );

        //   1s = 1000 ms ( 毫秒 )
        // 1ms = 1000 us ( 微妙 )
        // 1us  = 1000 ns ( 纳秒 / 毫微秒 )
        time = LocalTime.of( 22 , 10 , 30 , 100200300 );
        System.out.println( time );
    }

}
