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
在类checkUtil中，里面有Token口令、signature签名、timestamp时间戳和nonce随机数，然后Token+timestamp+nonce通过SHA-1加密（具体实现方法在SHA_1中）这样从微信端发过来的验证signature是否相等，相等则验证通过，（注意！微信发过来验证都是doget！）
</p>
<p>
类MessageUtil实现了把微信客户端传过来的xml格式的数据转换为map，这样才能对其进行处理。
</p>
<p>
类TuLingMessageUtil则一些第三方API出问题时的一些静态常量
</p>
<p>
weixinservlet类，主要是doget方法进行Token的验证，和图灵机器人的API的调用
</p>
<p>
导入Webcontent/WEB-INF/lib里面的jar包<br/>
在Webcontent/WEB-INF/web.xml配置相应的servlet,url-pattern中随你自己命名，比如我的是/wc.do,那么到时候你微信服务器配置的url应为IP+项目名称/wc.do
</p>
<p>
<strong>最后检查你的代码有无错误，如若没有错误，把项目export成WAR包，然后把WAR包放到你服务器里面的Tomcat/webapps/目录下（记得要运行起来你的tomcat）</strong>
</p>
<hr/>
<h5>图灵机器人</h5>
<p>
可登录<a href="http://www.tuling123.com/">图灵机器人官网</a>进行注册，然后创建机器人，然后复制机器人的API到java代码里
</p>
还在更新完善呢。。。。。