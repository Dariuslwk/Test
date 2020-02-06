package test;

import org.malajava.foundation.EncryptHelper;
import org.malajava.foundation.EncryptType;

public class EncryptTest {

    public static void main(String[] args) {

        final String p = "nishengri" ;
        System.out.println( p );
        System.out.println( p.length() );

        System.out.println( "~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~" );

        String e1 = EncryptHelper.encrypt( p , EncryptType.MD5 );
        System.out.println( e1 );
        System.out.println( e1.length() );

        System.out.println( "~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~" );

        String e2 = EncryptHelper.encrypt( p , EncryptType.SHA1 );
        System.out.println( e2 );
        System.out.println( e2.length() );

        System.out.println( "~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~" );

        String e3 = EncryptHelper.encrypt( p , EncryptType.SHA512 );
        System.out.println( e3 );
        System.out.println( e3.length() );

    }

}
