package test;

public class USBTest {

    public static void main(String[] args) {

        // 借尸:  从形式上看，是通过 new 来创建 USB 的实例
        // 还魂: 实际上是在创建 那个实现了 USB 接口的匿名内部类的实例
        USB u = new USB() {
            @Override
            public void charge() {
                System.out.println( "充电开始" );
            }
        } ;

        System.out.println( u );

        u.charge();

    }

}
