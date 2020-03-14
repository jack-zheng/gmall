# gmall
SpringBoot 商城项目实践 - B站地址[https://www.bilibili.com/video/av55643074]

## 笔记

### P7-P14 准备 DB，创建第一个 user module

1. 新建 module -> spring web -> mysql 驱动， jdbc, mybatis
1. 写 controller， service
1. 写 bean 映射类， 映射数据库 ums_user_member
1. 写具体功能

> 项目突然报错了，maven tab 说不能为 pom.xml resolve dependency  
> [StackOverflow](https://stackoverflow.com/questions/5074063/maven-error-failure-to-transfer)

```cmd
# for linux
find ~/.m2  -name "*.lastUpdated" -exec grep -q "Could not transfer" {} \; -print -exec rm {} \;

# for windows
cd %userprofile%\.m2\repository
for /r %i in (*.lastUpdated) do del %i
```

Idea @Autowired 自动导包错误，到 settings -> inspections -> Spring -> Spring Core -> Code -> Autowiring for Bean class 勾去掉

windows 版本的 git commit 要用**双引号**

> tk: 通用方法整合
1. 导入 dependency
1. mapper 实现 Mapper 接口
1. bean 配置 mapper 注解 @Id,@GeneratedValue(strategy = GenerationType.IDENTITY)
1. 修改启动文件的 MapperScan 引用

> 传文件给树莓派 scp [本地文件路径] pi@host:[pi 下的目标路径]

windows 下通过 ide 工具导入 sql 出现乱码，将文件传到树莓派 source 导入，没有乱码

### P15-21 项目预览

里面给出来的项目分层思想还是挺有用的，和公司现在用的那一套很像

新建 parent module, 选择 maven project 做包版本管理, 作为公用的包管理工程

修改 project structure 里的 language level，保存后经常重置，可以在 pom 中通过 plugin 里面指定 java 版本来解决

> 通用框架，所有应用工程都需要引入：springboot, common-beanutils, common-langs 等  
> web-util, 大概是 controller 相关的包  
> service-util 大概是 service, db 相关的

### P22-29 dubbo 服务配置

SOA: service oriented architecture

Dubbo 是和 SpringCloud 对标的产品，区别是协议，Dubbo 是 Dubbo 协议，springcloud 是 http (reset) 风格

* sudo apt-get install openjdk-8-jdk
    - 一开始还安装失败了，sudo apt-get update 一下才好的
* dubbo 打包：去 git 上下载 dubbo [项目](https://github.com/apache/dubbo)，cd 到 dubbo-admin 目录下面，运行 command ` mvn package -Dmaven.skip.test=true -e` 可以在目录下的 target 包下找到编译好的 war 包
    - 一开始编译失败了，root 下的 pom 指定的是 1.6 的 Java 版本，你根据本地情况改一下就行了。我本地用的 13， 但是 pom 里改 1.8 就过了
* 拷贝 tomcat, zookeeper, dubbo-admin 到 /opt 目录下，chmod 777 福泉， tar -zxvf 解压
* unzip dubbo-admin.war -d dubbo
* tomcat/conf/server.xml 中关联 dubbo, 在末尾， host tag 上方添加配置 `<Context path="/dubbo" docBase="/opt/dubbo" debug="0" privileged="true"/>`
* 到 tomcat/bin 下 ./startup.sh 启动服务器 访问 host:8080, host:8080/dubbo 查看是否启动成功， dubbo 默认账号 root/root
* tar 解压 zookeeper 包，到 conf 目录下做配置, 修改 zoo.cfg 指定的 dataDir 目录
    - 查看 zookeeper/logs 下的log, 启动失败. 新版的 zk 会内嵌一个控制台，默认绑定 8080 端口，可以通过在 cfg 总添加 `admin.serverPort=8081` 的方式解决
    - ./zkServer.sh start & ./zkServer.sh status
* 配置开机自启动, 目录 /etc/init.d
    - Debian 中 chkconfig 已经被 sysv-rc-conf 替代，先安装 `apt-get install sysv-rc-conf` 再使用  sysv-rc-conf --add zookeeper dubbo-admin
    - `sysv-rc-conf dubbo-admin on` 开机自启使能
    
```dubbo-admin
#!/bin/bash
#chkconfig:2345 20 90
#description:dubbo-admin
#processname:dubbo-admin
CATALANA_HOME=apache-tomcat-8.5.51
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-armhf
case $1 in
start)  
    echo "Starting Tomcat..."  
    $CATALANA_HOME/bin/startup.sh  
    ;;  
  
stop)  
    echo "Stopping Tomcat..."  
    $CATALANA_HOME/bin/shutdown.sh  
    ;;  
  
restart)  
    echo "Stopping Tomcat..."  
    $CATALANA_HOME/bin/shutdown.sh  
    sleep 2  
    echo  
    echo "Starting Tomcat..."  
    $CATALANA_HOME/bin/startup.sh  
    ;;  
*)  
    echo "Usage: tomcat {start|stop|restart}"  
    ;; esac
``` 

```zookeeper
#!/bin/bash
#chkconfig:2345 20 90
#description:zookeeper
#processname:zookeeper
ZK_PATH=/opt/apache-zookeeper-3.6.0-bin
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-armhf
case $1 in
         start) sh  $ZK_PATH/bin/zkServer.sh start;;
         stop)  sh  $ZK_PATH/bin/zkServer.sh stop;;
         status) sh  $ZK_PATH/bin/zkServer.sh status;;
         restart) sh $ZK_PATH/bin/zkServer.sh restart;;
         *)  echo "require start|stop|status|restart"  ;;
esac
``` 

### 重构为 Dubbo 架构

service / controller 拆分好处：可以动态的根据负载扩展服务

1. user 拆分为 user-service + user-web
1. common util 引入 dubbo
1. 分拆 controller， service 代码放到 web/service project 中，启动服务查看结果
1. 修改 @Service 和 @Autowired 注解成 @Service 和 @Reference
1. 添加 application.properties 配置

用 Spring2.2.5 会启动失败， 降到 1.5.21 成功，版本之间兼容性还是有问题

service 拆分成功，可以再 console 看到心跳 log, 前提是 log level 设置成 'debug':
> 2020-03-13 18:14:46.278 DEBUG 7912 --- [168.1.106:2181)]  
> org.apache.zookeeper.ClientCnxn: Got ping response for sessionid: 0x1001839862b0003 after 20ms

bean 对象需要实现 Serializable 接口

spring.dubbo.consumer.timeout=600000 # 指定超时时间

spring.dubbo.consumer.check=false # 指定是否再 consumer 和 provider 里设置顺序检测

## P30-39 PMS 模块实现

gmall-manage-service: 8071
gmall-manage-web: 8081

sku + spu: sku - stock keeping unit, spu - standard product unit

下载 gmall-admin project，在 config 下做前端项目配置，修改 dev.env.js 中服务端口信息, index.js 中修改前端UI服务器端口，到 gmall-admin 下运行 `npm run dev` 启动服务

```exception
./src/styles/index.scss (./node_modules/css-loader??ref--11-1!./node_modules/postcss-loader/lib??ref--11-2!./node_modules/sass-loader/lib/loader.js??ref--11-3!./src/styles/index.scss)
Module build failed (from ./node_modules/sass-loader/lib/loader.js):
Error: Node Sass does not yet support your current environment: Windows 64-bit with Unsupported runtime (72)
For more information on which environments are supported please see:
https://github.com/sass/node-sass/releases/tag/v4.11.0
    at module.exports (C:\Users\jack\IdeaProjects\gmall\gmall-admin\node_modules\node-sass\lib\binding.js:13:13)
    at Object.<anonymous> (C:\Users\jack\IdeaProjects\gmall\gmall-admin\node_modules\node-sass\lib\index.js:14:35)
    at Module._compile (internal/modules/cjs/loader.js:1158:30)
    at Object.Module._extensions..js (internal/modules/cjs/loader.js:1178:10)
    at Module.load (internal/modules/cjs/loader.js:1002:32)
    at Function.Module._load (internal/modules/cjs/loader.js:901:14)
    at Module.require (internal/modules/cjs/loader.js:1044:19)
    at require (internal/modules/cjs/helpers.js:77:18)
    at Object.sassLoader (C:\Users\jack\IdeaProjects\gmall\gmall-admin\node_modules\sass-loader\lib\loader.js:24:22)
```

运行前端项目报错，根据弹幕提示更新了 node-sass 包，解决了问题，最好换国内阿里源加速，不然贼慢！！ `cnpm install node-sass@latest`

创建 gmall-manage-web 用来和 gmall-admin 交互。 记得加上 @CorssOrigin 注解支持跨域请求，问了检测 web 是不是启动成功，你可以直接用 browser 访问 http://127.0.0.1:8081/getCatalog1 查看结果

启动 service 失败报错：

```error
Description:
Field pmsBaseCatalog1Mapper in com.atguigu.gmall.manage.service.impl.CatalogServiceImpl required a bean of type 'com.atguigu.gmall.manage.mapper.PmsBaseCatalog1Mapper' that could not be found.
```

application 启动文件中完了加 MapperScan 了

启动 web 交互报错：

```response
{"timestamp":1584180223861,"status":500,"error":"Internal Server Error","exception":"org.thymeleaf.exceptions.TemplateInputException","message":"Error resolving template \"attrInfoList\", template might not exist or might not be accessible by any of the configured Template Resolvers","path":"/attrInfoList"}
```
> Controller 对用的方法忘记添加 @ResponseBody 的注解了

## P40-42 属性模块

没什么新意，就是针对属性的增删改查
