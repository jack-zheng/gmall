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

## P43-50 SPU 模块开发

没啥新意，唯一有价值的就是学习了一下怎么处理上传文件

## P51-66 整合文件服务器 FastDFS, 完成信息保存

安装讲的太罗嗦了直接参考我找到的这两篇文章，半小时肯定 OK 了

* [官方文档](https://github.com/happyfish100/fastdfs/wiki)
* [视频教程，不过要翻墙](https://www.youtube.com/watch?v=6Y2NihvPijQ)

16年开始淘宝官方就已经提供了官方的 fdfs java 客户端了，就是用法和视频的不一样，参考下文档，还是可以用的

## P67-70 Sku 先跳过，没什么新意，而且介绍的不是很清楚

还是躲不过，后面要用到的，躲不过，还得把他写完。不过重新看了一遍他的 db 设计文档，比之前要清楚多了

Idea 自带的 database 最多只能显示 500 条数据还以为自己代码写错了，查了半天，干。不过顺便查到了 mybaties 的显示sql配置

## 71-84 thymeleave 实现 item 模块

gmall-item-web 前台商品详情, 端口 8082

前台访问压力更大，可以使用限流，缓存来提高效率

系统小直接用 session 管理会话，系统大了直接用 redis 代替 session 进行对话

session： browser 和 jvm 交互式存储临时数据的对象

Ctrl + Shift + F9 刷新提交页面

马马虎虎，基本是抄完的。 这个项目的 db 设计的太次了，不是很感兴趣，还是其他两个项目的好

## P85-90 item hash 实现，没什么意思

## P91-108 Redis

整合步骤

1. redis 整合到 spring
1. 设计数据存储策略，redis 存储策略：[数据对象：数据对象id: 对象属性]

controller -> redies -> db redis 起到一个缓冲垫的作用

### 安装

```cmd
sudo apt-get update

sudo apt-get install redis-server

# 启动服务
redis-server

# 查看服务, 默认端口 6379
redis-cli

# 设置值
set k1 hello-redis

# 取值
get k1

# 配置文件路径
/etc/redis/redis.conf

/etc/init.d/redis-server stop 
/etc/init.d/redis-server start 
/etc/init.d/redis-server restart

# 删除数据
flushdb

del *
```

### 整合

1. 引入pom依赖
1. 写 redis 工具类，redis pool 初始化
1. 写 spring 整合 redis 配置类，将 redis 初始化 到 spring 容器中
1. 每个应用工程引入 service-util 后需要单独配置 redis 信息

* redis 由于是装在树莓派里面的，所以需要再 conf 里面开放 ip， pc 才能访问到
* 注意添加了配置之后，application 启动文件的位置，不然可能加载不到

### Redis 常见问题

这些问题其实本质都是一样的，就是缓存失效，压力重新回到 db 层

* redis 缓存穿透：利用缓存漏洞，比如访问一个不存在的 id, 绕过 redis 直接给 db 造成压力。
    - 可在实现层直接写业务逻辑实现阻挡，给这种query 设置返回值
* redis 缓存击穿：某个 key 高并发下突然失效（比如过期）导致大量访问直接给到 db 层
    - 加锁
* redis 缓存雪崩：key 过期时间设置在同一时间，导致缓存同时失效，db 瞬间压力过大，崩溃
    - 设置不同的过期时间
    
### redis 分布式锁

1. redisson 框架, 自带 JUC 实现
1. redis 自带的分布式锁， set ex nx， 这个redis 一般要单独配

> set sku:111:lock 1 px 10000 nx  # 设置锁，过期时间 10s   
> del sku:111:lock  ## 删除锁 

* 线程删除锁前一刻，锁过期了，这个线程会继续去删除锁，这就但是下一个线程的锁被删了，怎么避免
    - 通过 set lock 的时候设置 token 来避免
* 在加了 token 的情况下，在查询的时候还没有过期，但是执行时过期了，怎么办
    - 可以用 LUA 语言减小执行语句的时间差，基本上达到 查询和删除同时执行
    
Idea run config, allow parallel run 可以为同一个service 起多个 instance

可以通过 apache 的 ab 工具做压力测试，类似的工具还有一个叫 jmeter 的

### P110-128 ES search

ES search 要求设备内存 >2G, 树莓派只有 1G，装不了。选择在 windows 上安装

去到 elastic search 的官方网站，下载 ES 和 kibana 的安装包，下载解压后可直接运行，步骤相当直接，文档也很详细

elastic search 访问地址 ip:9200, java 访问端口 9300

kibana 地址：ip: 5601

```query_sample
GET movie_index/_search
{
  "query": {
    "match": {
      "name": "red"
    }
  }
}
```

中文分词器，安装 IK 分词器，支持中英文分词，放到 ES search 下面的 plugins 文件夹下

插件地址： [Git](https://github.com/medcl/elasticsearch-analysis-ik), 可以离线安装，5.5.1 版本后也支持通过 cmd 安装了

```search
GET _analyze
{
  "analyzer": "ik_smart", 
  "text": "红海行动"
}
```
集群配置只是听了一下，不准备实践，单节点就够完成项目了。

主节点负责集群管理，任务分发，不负责文档增删改查

片时 es 的实际物理存储单元

索引时 es 的逻辑单元，一个所以一般建立在多个不同机器的分片上

复制片，每个机器的分片一般在其他机器上都有2-3个复制片

一旦集群的耨写机器发生故障，那么剩余的机器会在主节点的管理下，重新分配资源

## P129-152 search 功能实现

ES7 里mapping 已经**被移除**了，为了省事，我还是重新安装一个 ES6吧

ES6.x 不支持 Java version above 11...

```es
PUT /gmall
{
  "mappings": {
    "PmsSkuInfo": {
      "properties": {
        "id": {
          "type": "keyword",
          "index": true
        },
        "skuName": {
          "type": "text",
          "analyzer": "ik_max_word"
        },
        "skuDesc": {
          "type": "text",
          "analyzer": "ik_smart"
        },
        "catalog3Id": {
          "type": "keyword"
        },
        "price": {
          "type": "double"
        },
        "skuDefaultImage": {
          "type": "keyword",
          "index": false
        },
        "hotScore": {
          "type": "double"
        },
        "productId": {
          "type": "text"
        },
        "skuAttrValueList": {
          "properties": {
            "attrId": {
              "type": "keyword"
            },
            "valueId": {
              "type": "keyword"
            }
          }
        }
      }
    }
  }
}
```

gmall-search-web: 8083
gmall-search-service: 8073

取类名的i时候用业务关键字而不是技术， 比如 redis 什么的用 cache 代替

先过滤后搜索效率高 query {bool + must}

测试 query

```query
GET gmall/PmsSkuInfo/_search
{
  "query": {
    "bool": {
      "filter": [
        {"term": {"skuAttrValueList.valueId": "49"}},
        {"term": {"skuAttrValueList.valueId": "43"}}
        ],
      "must": [
        {
          "match": {
            "skuName": "小米"
          }
        }
      ]
    }
  }
}
```
ES 的聚合函数执行效率比较低， aggs 统计重复的字段的数量


P141, 使用 sql 实现查询去重效果，后面的url 和 面包屑 url 的实现挺有意思，不过没什么新意，没什么意外的话，打算看看视频过了

## P153-P166 购物车

gmall-cart-web: 8084
gmall-cart-service: 8074

为了完成这个实验你必须去 hosts 文件中建立 ip-host 的映射，不然 cookie 设置会出问题

实现时需要考虑的点：

1. 在不登陆的情况下，也可以使用 - cookie
1. 登录的情况下，使用 mysql, redis 存储数据， redis 作为购物车缓存
1. 在缓存的情况下，或者用户已经添加购物车后，允许购物车中的数据和原始商品数据的不一致。 - 对应商品过期之后，购物车里是否需要保留该产品的情况
1. 购物车同步问题 - 什么时候同步，登录+结算；同步后是否删除 cookie中数据，需要。
1. 用户在不同客户端同时登录，如何处理购物车数据

cookie 操作时副本操作，server 端改了需要覆盖浏览器端的值，session 时值引用，getSessionId 可见一斑

只需要启动 manage service 和 item web 就行了

ssh pi@192.168.1.106 - raspberry 链接树莓派， redis-cli 打开 redis 客户端

```redis
# 显示所有可用 key
keys *

# 拿到 id 为 116 的map 信息
hget user:1:cart 116
```

BigDecimal 用字符串初始化靠谱， 浮点，双精度会有精度丧失

## P167- 用户认证

gmall-passport-web 用户认证
gmall-user-service 用户服务 8070

知识点： 拦截器,跨域，单点登录

JSession + session + redis pool: 性能不高，安全性差 -> 发展为 -> Token + redis pool, 去除中间层，也更安全， 不过也过时了？！！

最新的解决方案， JWT 通过加密/解密 实现用户验证，省去了 redis 访问链接，更高效，但是对验证的安全性要求也更高

```exception
跑JWT 测试的时候跑错
Exception in thread "main" java.lang.NoClassDefFoundError: javax/xml/bind/DatatypeConverter

是因为 java 9 之后一些包 deprecate 了，你可以降到 java8 或者添加一些额外的引用到 pom 中

<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
</dependency>
<dependency>
    <groupId>com.sun.xml.bind</groupId>
    <artifactId>jaxb-impl</artifactId>
    <version>2.3.0</version>
</dependency>
<dependency>
    <groupId>com.sun.xml.bind</groupId>
    <artifactId>jaxb-core</artifactId>
    <version>2.3.0</version>
</dependency>
<dependency>
    <groupId>javax.activation</groupId>
    <artifactId>activation</artifactId>
    <version>1.1.1</version>
</dependency>
```

## P190-202 社交帐号登录-新浪

授权流程：

1. 本站提供链接导向到授权站点
1. 授权方提供授权成功的 token
1. 通过 token 去资源站点提取信息

微博开放平台 -> 微链接 -> 网站接入 -> 立即接入 -> 输入信息，通过邮箱验证 -> 在进入立即接入，创建应用，拿到 app key 和 app secret

在我的应用，应用信息 的高级信息里面可以设置 OAuth2 的授权信息


