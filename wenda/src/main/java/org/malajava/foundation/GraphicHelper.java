package org.malajava.foundation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

/**
 * 这是一个 与图形、图像 有关的工具类
 */
public final class GraphicHelper {
	
	public static void main(String[] args) throws Exception {
		String characters = GraphicHelper.create( 4 , true , 180 , 50 , new FileOutputStream( "D:/character.jpg" )  , 50);
		System.out.println( characters );
		String letters = GraphicHelper.create( 6 , false , 180 , 50 , new FileOutputStream( "D:/letter.jpg" )  , 50 );
		System.out.println( letters );
	}
	
	/** 声明并创建一个随机数产生器 */
	private static final Random RANDOM = new Random();
	/** 声明并创建一个颜色数组，用于随机选取字体的颜色 */
	private static final Color[] FONT_COLORS = { Color.BLUE,Color.MAGENTA,Color.RED, Color.BLACK,Color.ORANGE };
	/** 声明并创建一个颜色数组，用于随机选取干扰线的颜色 */
	private static final Color[] INTERFERE_COLORS = {Color.LIGHT_GRAY,Color.GRAY,Color.DARK_GRAY, Color.PINK };
	/** 声明一个汉字字典，用来随机产生汉字验证码 */
	private static final String DICTIONARY  ;

	private static final String DEFAULT_DICTIONARY = "\u843d\u971e\u4e0e\u5b64\u9e5c\u9f50\u98de\u79cb\u6c34\u5171\u957f\u5929\u4e00\u8272" ;
	/** 声明并创建一个字符缓冲区 */
	private static final StringBuffer BUFFER = new StringBuffer();
	/** 声明并指定 180 度对应的浮点数 */
	private static final double DEGREES = Math.PI / 180 ;
	/** 指定默认的英文字体 */
	private static final Font ENGLISH_FONT = new Font("Arial",Font.PLAIN,30) ;
	/** 指定默认的中文字体 */
	private static final Font SINAEAN_FONT = new Font("宋体",Font.PLAIN,30) ;
	/** 指定图片默认的背景颜色 */
	private static final Color BACKGROUND_COLOR = Color.getColor( "F8F8F8" );

	// 用于对汉字字典进行初始化的静态代码块
	static {
		StringBuffer buffer = readDictionary( "dictionary.txt"  );
		if( buffer == null || buffer.length() == 0 ) {
			// 落霞与孤鹜齐飞
			buffer.append ("\u843d\u971e\u4e0e\u5b64\u9e5c\u9f50\u98de" );
			//  秋水共长天一色
			buffer.append( "\u79cb\u6c34\u5171\u957f\u5929\u4e00\u8272" );
		}
		DICTIONARY = buffer.toString() ;
	}

	/**
	 * 从当前类路径根目录下读取指定文件名称对应的文件中的内容
	 * @param filename 被读取文件的文件名称
	 * @return 以字符串缓冲区形式返回文件中所有内容(字符数据)
	 */
	private static StringBuffer readDictionary( String filename ){
		// 创建字符串缓冲区用于缓存文件中的字符
		StringBuffer buffer = new StringBuffer();
		// 获取一个输入流，用于从类路径的根目录中读取指定名称的文件
		InputStream input = GraphicHelper.class.getResourceAsStream( "/" + filename );
		if( input == null ){
			return buffer ;
		}
		// 使用当前默认字符编码方案将字节输入流包装成字符输入流
		InputStreamReader reader = new InputStreamReader( input );
		// 将字符输入流包装成字符缓冲输入流
		BufferedReader br = new BufferedReader( reader );
		String s = null ;
		try {
			// 从字符流中逐行读取字符数据
			while ( ( s = br.readLine() ) != null) {
				// 判断当前行是否为空白行
				if (!( s = s.trim() ).isEmpty()) {
					buffer.append( s ); //若当前行不为空白则将其添加到字符缓冲区
				}
			}
			br.close(); // 关闭字符缓冲流
			reader.close(); // 关闭字符转换流
			input.close(); // 关闭字节输入流
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return buffer ;
	}

	/**
	 * 以字符串形式返回生成的验证码，同时输出一个图片
	 * @param n 字符个数
	 * @param isSinaean 所产生验证码是否为中文字符 ( true 表示中文字符，false 表示字符英文 )
	 * @param width 图片的宽度
	 * @param height 图片的高度
	 * @param output 图片的输出流(图片将输出到这个流中)
	 * @param interfere 干扰线个数
	 * @return 返回所生成的验证码(图片上的字符组成的字符串)
	 */
	public static final String create( final int n , boolean isSinaean , final int width,final int height , OutputStream output , final int interfere ){
		// 创建"画板"
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		// 获取"画笔"
		Graphics graphic = image.getGraphics();
		// 设置"画板"背景色
		graphic.setColor(BACKGROUND_COLOR);
		// 填充整个"画板"
		graphic.fillRect(0,0,width,height);
		// 设置字体样式( 中文验证码使用 SINAEAN_FONT ，英文验证码使用 ENGLISH_FONT )
		graphic.setFont( isSinaean ? SINAEAN_FONT : ENGLISH_FONT );
		// 生成图片上所显示的字符对应的字符串
		String code = isSinaean ? characters(n) : letters(n) ;
		// 绘制字符
		drawCharacters( graphic , width , height );
		// 绘制干扰线
		drawInterfere( graphic , width , height , 30 , interfere );
		graphic.dispose();
		try{
			// 将 image 中包含的图片数据以jpeg格式写入到指定的字节输出流中
			ImageIO.write(image,"JPEG",output);
		}catch(IOException e){
			e.printStackTrace();
		}
		return code ;
	}

	/**
	 * 专门用来绘制干扰线条的方法
	 * @param graphic 画笔
	 * @param width 画板宽度
	 * @param height 画板高度
	 * @param length 线条的水平长度或竖直长度
	 * @param n 线条数目
	 */
	private static final void drawInterfere(final Graphics graphic , final int width , final int height , final int length , final int  n ) {
		// 在 "画板"上生成干扰线条
		for (int i = 0; i < n ; i++) {
			graphic.setColor( INTERFERE_COLORS[RANDOM.nextInt(INTERFERE_COLORS.length)]);
			final int x = RANDOM.nextInt(width);
			final int y = RANDOM.nextInt(height);
			final int w = RANDOM.nextInt(length);
			final int h = RANDOM.nextInt(length);
			final int signA = RANDOM.nextBoolean() ? 1 : -1;
			final int signB = RANDOM.nextBoolean() ? 1 : -1;
			graphic.drawLine(x, y, x + w * signA, y + h * signB);
		}
	}
	
	/**
	 * 将字符缓冲区中的字符"绘制"到画板上
	 * @param graphic 画笔
	 * @param width 画板宽度
	 * @param height 画板高度
	 * @return
	 */
	private static final String drawCharacters( final Graphics graphic ,final int width , final int height ){
		String s = "" ;
		Graphics2D g = (Graphics2D) graphic ;
		// 获取字符个数
		final int n = BUFFER.length();
		// 以"画板"高度的六分之一为图片内边距(每条边)
		final int padding = height / 6 ;
		// 计算每个字符所占的宽度
		final int w = ( width - padding * 2 ) / n ;
		// 计算每个字符所占的高度
		final int h = height - padding * 2 ;
		// 计算单个字所占宽度的中心位置(水平)
		final int m = w / 2 ;
		// 每个字符所占高度的一半即为中心位置
		final int y = h / 2 ;
		//在 "画板"上绘制字母
		for( int i = 0 ; i < n ; i++ ){
			// 获取单个字符
			char temp = BUFFER.charAt( i ) ;
			// 随机选择一个颜色设置为当前字符的颜色
			g.setColor(FONT_COLORS[RANDOM.nextInt(FONT_COLORS.length)]);
			// 计算即将显示的字符的中心位置(水平)
			int x = padding + w * ( i + 1 ) - m ; 
			// 设置字体旋转角度
            double deg = RANDOM.nextInt() % 30 * DEGREES ;
            // 正向角度
            g.rotate( deg , x ,  y );
			// 绘制字符
			g.drawString( temp + "" ,  padding + w * i , h );
			// 反向角度
            g.rotate( -deg , x , y );
		}
		s = BUFFER.toString() ;
		return s ;
	}
	
	/**
	 * @param n 指定所产生的字符个数
	 * @return 返回验证码字符串
	 */
	private static final String letters( final int n ){
		BUFFER.setLength( 0 );
		for( int i = 0 ; i < n  ; i++ ){
			int s = RANDOM.nextBoolean() ? 'A' : 'a' ;
			int x = RANDOM.nextInt( 26 );
			char ch = (char)( s + x );
			boolean notExists = true ;
			// 检查本次产生的字符在字符缓冲区的前 i 个字符中是否存在
			if( exists( i , ch ) ) {
				i-- ; // 若 ch 字符已经存在，则控制循环重新生成字符
			} else {
				// 若缓存区前i个字符中不存在 ch 字符则将其添加到缓冲区中
				BUFFER.append( ch );
			}
		}
		return BUFFER.toString() ;
	}
	
	/**
	 * @param n 指定所产生的字符个数
	 * @return 返回验证码字符串
	 */
	private static final String characters( final int n ){
		BUFFER.setLength( 0 );
		for( int i = 0 ; i < n ; i++ ){
			char ch = DICTIONARY.charAt( RANDOM.nextInt( DICTIONARY.length() ) );
			boolean notExists = true ;
			// 检查本次产生的字符在字符缓冲区的前 i 个字符中是否存在
			if( exists( i , ch ) ) {
				i-- ; // 如果缓存区已经存在 ch 字符，则控制循环重新生成字符
			} else {
				// 如果缓存区中不存在字符则将 ch 字符添加到缓冲区中
				BUFFER.append( ch );
			}
		}
		return BUFFER.toString() ;
	}

	/**
	 * 判断参数指定的字符是否存在于字符缓冲区的前n个字符中
	 * @param n 指定对字符串缓冲区的检查范围
	 * @param ch 需要检查的字符
	 * @return 如果缓冲区中存在指定字符则返回true
	 */
	private static boolean exists( int n , char ch ){
		for( int j = 0 ; j < n ; j++ ) {
			char t = BUFFER.charAt( j ) ;
			if( t == ch ) {
				return true ;
			}
		}
		return false ;
	}
}
