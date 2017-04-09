<h1 style="text-align:center">大家可关注我的订阅号“ITkeys”</h1>
<img src="./images/erwema.jpg"></img>
现在主要是聊天功能<br/>
<h5>1.服务器配置</h5>
<p>首先要有一个服务器，我的是阿里云的，登录服务器，在里面安装好java环境，部署好Tomcat，（记得修改端口号为80运行，并把自带的SSI服务关闭，不然端口号会冲突)。然后记住自己的ECS的IP，比如我的是
<a href="http://120.25.192.32" target="_blank">120.25.192.32</a>或者通过域名<a href="http://smh2274.oicp.io" target="_blank">smh2274.oicp.io</a>
进行外网访问，如果出现TOMCAT主页，那么就成功了<br/></p>
<hr/>
<h5>2.代码讲解</h5>
<p>com.wu.bean里的代码为持久层代码，里面封装好了各个变量。<br/>
在类checkUtil中，里面有Token口令、signature签名、timestamp时间戳和nonce随机数，然后Token+timestamp+nonce通过SHA-1加密（具体实现方法在SHA_1中）
</p>
<p>
类MessageUtil实现了把微信客户端传过来的xml格式的数据转换为map，这样才能对其进行处理。
</p>
<p>
类TuLingMessageUtil则一些第三方API出问题时的一些静态常量
</p>
还在持续更新中。。。。。。。