## 个人健康数据管理系统

### 选用技术栈

- springboot3构建，SSM架构
- MySQL+Redis进行数据存储，MyBatisPlus增强封装
- SA-Token登录校验
- more？

### 功能概述

#### 数据记录

- 个人可以记录自己的体征数据，包括身高、体重、血压、血糖、血脂等。

- 个人可以记录自己的运动数据，包括运动类型、运动时间、运动距离、运动消耗等。

- 个人可以记录自己的饮食数据，包括食物名称、食物数量、食物热量等。

- 个人可以记录自己的睡眠数据，包括睡眠时间、睡眠质量等。

#### 数据查询

- 个人可以查询自己的历史健康数据。

- 个人可以按时间、类型等条件查询健康数据。

- 个人可以导出自己的健康数据。

#### 数据分析

- 系统可以提供个人健康趋势分析，如体重变化趋势、血压变化趋势等。

- 系统可以提供疾病风险评估，如高血压风险、糖尿病风险等。

- 系统可以提供健康建议，如减肥建议、运动建议等。

#### 健康目标设定

- 个人可以设定自己的健康目标，如减肥、增肌等。

- 系统可以提供目标达成进度跟踪。

#### 健康提醒

- 系统可以提供健康体检提醒、服药提醒等功能。（通过邮件提醒）

### 部署服务端

#### 运行项目

1. Star本项目

2. Clone本项目，安装maven依赖

   ```shell
   git clone https://github.com/ZPolister/SQL_sys_backend.git
   cd SQL_sys_backend
   mvn install
   ```

3. 在MySQL中运行根目录下`init.sql`文件
4. 填写`resource`下`application-github.yaml`信息

5. 运行

#### 部署流水线

- 已写好`dockerfile`文件，构造docker镜像推送即可

### 相关链接

- **前端**：[ZPolister/SQL-sys-frontend](https://github.com/ZPolister/SQL-sys-frontend)

- **API文档**：[API doc](https://github.com/ZPolister/SQL_sys_backend/tree/release/docs/apidoc.md)

