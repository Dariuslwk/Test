package org.malajava.foundation;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ClassPathResourceLoader implements ResourceLoader {
	
	/** 通过构造方法或init方法对 ClassLoader 进行初始化 */
	private ClassLoader loader ;
	
	/** 类路径的根目录，通过构造方法或 init 方法完成初始化 */
	private String classPathRoot;
	
	/** 用来对资源进行过滤的过滤器，通过 构造方法或 init 方法完成初始化 */
	private FileFilter filter ;

	/**用来存放被加载的所有类，通过构造方法完成初始化 */
	private List<Class<?>> classes ;
	
	/** 默认构造方法，对 ClassLoader 、classPathRoot 、filter 进行初始化 */
	public ClassPathResourceLoader() {
		// 获得当前线程对象
		Thread thread = Thread.currentThread();
		// 对 类加载器 进行初始化
		this.loader = thread.getContextClassLoader();
		this.initClassPathRoot();
		this.initFileFilter();

		// 创建用来存放被加载类的集合
		classes = new ArrayList<>();
		
	}

	/** 获取当前类路径根目录 */
	private void initClassPathRoot(){
		// 通过类加载器获取类路径根目录对应的URL对象
		URL url = loader.getResource( "/") ;
		URI uri = null ;
		try {
			uri = url.toURI() ; // 将 URL 转换为 URI
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Path path = Paths.get( uri ) ; // 获取 uri 所表示路径的 Path 实例
		//  将类路径的根目录以字符串形式保存到 classPathRoot 变量中
		this.classPathRoot = path.toString() ;
	}
	
	/** 初始化方法，可以实现对 ClassLoader 、classPathRoot 、filter 进行初始化 */
	private void initFileFilter() {
		this. filter = new FileFilter() {
			@Override
			public boolean accept(File fod) {
				String name = fod.getName();
				return fod.isDirectory() || ( name .endsWith( ".class" ) && !name.contains( "$" ) ) ;
			}
		};
	}
	
	/**
	 * 扫描某个包中，并递归加载该包中的所有类(以及所有子包中的所有类)
	 * @param packageName 需要被扫描的包的包名
	 */
	private void scanPack(final String packageName ) {

		String packagePath = packageName.replace( '.', '/' ) ;
		
		Path pack = Paths.get( classPathRoot , packagePath );

		if( Files.exists( pack ) && Files.isDirectory( pack ) ){
			File dir = pack.toFile();
			File[] fods = dir.listFiles( filter );

			if( fods != null && fods.length > 0 ) {
				for( File fod : fods ) {
					if( fod.isDirectory() ) {
						String name = fod.getName() ; // 获得子包的报名
						String subPackageName = packageName + "." + name ; // 连接子包的完整包名
						scanPack( subPackageName ) ; // 调用自身完成加载操作
					} 
					
					if( fod.isFile() ) {
						String filename = fod.getName();
						String simpleName = filename.substring( 0 , filename.indexOf( "." ) ) ;
						String className = packageName + "." + simpleName ;
						Class<?> c = loadClass( className );
						classes.add( c );
					}
				}
			}
			
		}
		
	}
	
	/** 使用指定的类加载器加载指定类 */
	private Class<?> loadClass( String className ) {
		try {
			return loader.loadClass( className );
		} catch (ClassNotFoundException e) {
			throw new RuntimeException( "在类路径中未找到[ " + className + " ]类", e) ;
		}
	}

	/**
	 *  调用 scanPack 方法 扫描并加载指定包中的所有类
	 * @param packageName 需要被扫描的包的包名，比如 com.malajava.controller
	 * @return 返回指定包中所有的类对应的 Class 对象组成的 List 集合
	 */
	public List<Class<?>> scan( final String packageName ){
		this.scanPack( packageName );
		return classes ;
	}

}
