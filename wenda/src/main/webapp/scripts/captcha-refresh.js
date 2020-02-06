( function () {

    // 返回 匹配于 指定选择器 的首个元素
    let image = document.querySelector( ".sign .captcha-row>div span img" );
    // 当引用变量 image 不为 null 时、不为 undefined 时就可以当作 true 来使用
    if( image ) { // 如果 image 存在则表示 true ，因此可以执行 if 内部的代码
        let listener = function( e ){
            e.preventDefault(); // 阻止事件的默认行为
            e.stopPropagation(); // 停止事件传播

            // 实现图片的刷新
            image.setAttribute( "src"  , "/captcha/produce?" + Date.now() );
            // image.src = "/captcha/produce?" + Date.now() ;

        }
        // 为 image 对应的 img 元素绑定点击事件的处理函数
        image.addEventListener( "click" , listener , false );
    }

} )();