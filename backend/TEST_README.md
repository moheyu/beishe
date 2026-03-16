# Maven测试运行说明

## 问题描述
Maven的JAVA_HOME环境变量指向了不存在的JDK 17路径（D:\JDK-17\jdk-17.0.14_windows-x64_bin\jdk-17.0.14），导致无法运行测试。

## 解决方案

### 方法1：修复系统环境变量（推荐）

1. 右键点击"此电脑" -> "属性" -> "高级系统设置"
2. 点击"环境变量"
3. 在"系统变量"中找到"JAVA_HOME"
4. 将其值修改为：`C:\Program Files\Java\jdk1.8.0_401`
5. 点击"确定"保存
6. 重新打开命令行窗口

### 方法2：在IDE中运行测试

#### IntelliJ IDEA
1. 打开测试文件（如：UserServiceTest.java）
2. 点击测试方法左侧的绿色三角形
3. 选择"Run"或"Debug"

#### Eclipse
1. 右键点击测试文件
2. 选择"Run As" -> "JUnit Test"

### 方法3：使用命令行临时设置JAVA_HOME

在PowerShell中运行：
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_401"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
cd backend
mvn clean test
```

在CMD中运行：
```cmd
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_401
set PATH=%JAVA_HOME%\bin;%PATH%
cd backend
mvn clean test
```

## 运行所有测试

```bash
cd backend
mvn clean test
```

## 运行特定测试类

```bash
cd backend
mvn test -Dtest=UserServiceTest
```

## 运行特定测试方法

```bash
cd backend
mvn test -Dtest=UserServiceTest#testSaveUser
```

## 测试文件列表

### Service层测试
- UserServiceTest.java - 用户服务测试（5个测试用例）
- ScenicServiceTest.java - 景点服务测试（7个测试用例）

### Controller层测试
- AuthControllerTest.java - 认证控制器测试（11个测试用例）
- ScenicControllerTest.java - 景点控制器测试（10个测试用例）
- CommentControllerTest.java - 评论控制器测试（14个测试用例）

## 测试统计

总计：47个测试用例
- Service层：12个测试用例
- Controller层：35个测试用例

## 注意事项

1. 所有测试都使用@Transactional注解，测试完成后会自动回滚，不会污染数据库
2. 测试需要MySQL数据库和Redis服务正常运行
3. 测试配置文件：src/test/resources/application.yml

## 常见问题

### Q: 测试失败，提示数据库连接错误
A: 请确保MySQL和Redis服务已启动，并检查application.yml中的配置是否正确。

### Q: 测试失败，提示端口被占用
A: 请检查8080端口是否被其他程序占用。

### Q: 测试失败，提示认证失败
A: 这是正常的，测试中包含了权限验证测试。
