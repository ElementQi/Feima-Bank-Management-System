# 飞马银行管理系统

这是一个老生常谈的*经典*项目，一个类似于图书管理系统的**银行管理系统**。

基于*JAVA*，接入*MySQL*数据库，客户端是图形化界面。

主要功能如下（项目需求）：

### 普通用户的功能
1. 开户，年满18岁的飞马人可以申请开设个人账号，飞马银行会存入2000飞马币作为星球福利赠送给每个新用户；
2. 销户，对于年满70岁的老人强制销户，或者因故离世的人员，经申请可以销户处理；
3. 查询余额，用户可以自由查询自己账户中的余额；
4. 取钱，用户可以支取账户上的余额，但支取后要保证账户余额必须大于等于0；
5. 存钱，用户可以往账号中存钱；
6. 转账，用户可以给另一个存在账号转钱，同样，要保证账户余额大于等于0的要求；
7. 修改账户信息，但账户id，身份id，账户余额等信息不允许修改。

### 银行管理员功能
1. 可以导入xls文件进行批量开户；
2. 可以将特定查询信息导出为xls文件；
3. 年终可以生成一个pdf版综述报告，报告内容包括但不限于目前账户总数，今年新开户数，总余额等数据。

### 项目文件信息

- 建立数据库.sql
    - 内有创建数据库以及表的代码，还有插入管理员信息的代码。

- 面向对象技术大作业二(基于华为云).docx
    - 项目要求

- Accent.xls
    - 进行批量导入的xls文件。

- Data.xls
    - 根据特定信息查询后导出的xls文件。

- Print.pdf
    - 导出的银行报表的pdf文件。

- feima.exe
    - 通过打包程序(exe4j)将项目转化的可执行文件，其初始界面为项目中的登录界面。

- feima.docx
    - 项目的说明文档，内有对项目的具体介绍和演示

- project2文件夹
    - 代码存放在其中的src文件夹中
    - 主函数为Main.java
    - 在com\project中有4个包，其中有不同功能部分的代码
    - 在out\artifacts中有将项目打包完毕的jar文件，可以直接双击project2.jar来运行程序


### 使用的云服务器相关信息

    IP地址：114.115.251.234
    用户名：root
    密码：623abc!!!
    mysql数据库密码：623abcDEF!!!

(该华为云服务器已经在2022/7/15过期)

用ssh root@114.115.251.234登入服务器

进入数据库mysql -uroot -p