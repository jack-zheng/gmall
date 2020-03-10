# gmall
SpringBoot 商城项目实践 - B站地址[https://www.bilibili.com/video/av55643074]

## 笔记

P7-P14 准备 DB，创建第一个 user module

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
