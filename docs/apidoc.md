# 健康管理系统API definition


**接口路径**:/v3/api-docs/default


[TOC]






# 分析数据模块


## 获取分析数据


**接口地址**:`/analysis/stream`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>流式获取当前用户的健康数据分析结果，支持实时生成或使用缓存</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|refresh|是否刷新缓存，true表示强制重新生成分析|query|false|boolean||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功返回流式分析数据||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 服药提醒管理


## 获取服药提醒列表


**接口地址**:`/medication-reminder`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|startDate||query|false|string(date-time)||
|endDate||query|false|string(date-time)||
|pageNum||query|false|integer(int32)||
|pageSize||query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultPageMedicationReminder|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||PageMedicationReminder|PageMedicationReminder|
|&emsp;&emsp;records||array|MedicationReminder|
|&emsp;&emsp;&emsp;&emsp;reminderId||integer||
|&emsp;&emsp;&emsp;&emsp;accountId||integer||
|&emsp;&emsp;&emsp;&emsp;medicationName||string||
|&emsp;&emsp;&emsp;&emsp;medicationDosage||string||
|&emsp;&emsp;&emsp;&emsp;medicationFrequency||integer||
|&emsp;&emsp;&emsp;&emsp;medicationDuration||integer||
|&emsp;&emsp;&emsp;&emsp;startTime||string||
|&emsp;&emsp;&emsp;&emsp;completionStatus||integer||
|&emsp;&emsp;&emsp;&emsp;nextReminderTime||string||
|&emsp;&emsp;&emsp;&emsp;reminderTime||string||
|&emsp;&emsp;&emsp;&emsp;reminderCount||integer||
|&emsp;&emsp;&emsp;&emsp;createdAt||string||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;optimizeCountSql||PageMedicationReminder|PageMedicationReminder|
|&emsp;&emsp;searchCount||PageMedicationReminder|PageMedicationReminder|
|&emsp;&emsp;optimizeJoinOfCountSql||boolean||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;countId||string||
|&emsp;&emsp;pages||integer(int64)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"records": [
			{
				"reminderId": 0,
				"accountId": 0,
				"medicationName": "",
				"medicationDosage": "",
				"medicationFrequency": 0,
				"medicationDuration": 0,
				"startTime": "",
				"completionStatus": 0,
				"nextReminderTime": "",
				"reminderTime": "",
				"reminderCount": 0,
				"createdAt": ""
			}
		],
		"total": 0,
		"size": 0,
		"current": 0,
		"orders": [
			{
				"column": "",
				"asc": true
			}
		],
		"optimizeCountSql": {},
		"searchCount": {},
		"optimizeJoinOfCountSql": true,
		"maxLimit": 0,
		"countId": "",
		"pages": 0
	}
}
```


## 创建服药提醒


**接口地址**:`/medication-reminder`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "medicationName": "阿司匹林",
  "medicationDosage": "100mg",
  "medicationFrequency": 3,
  "medicationDuration": 7,
  "reminderContent": "请记得按时服用药物，注意饭后服用",
  "reminderTime": "[08:00, 20:00]",
  "startTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|medicationReminderDto|服药提醒数据传输对象|body|true|MedicationReminderDto|MedicationReminderDto|
|&emsp;&emsp;medicationName|药品名称||true|string||
|&emsp;&emsp;medicationDosage|药品剂量||true|string||
|&emsp;&emsp;medicationFrequency|每日服药次数||true|integer(int32)||
|&emsp;&emsp;medicationDuration|服药持续天数||true|integer(int32)||
|&emsp;&emsp;reminderContent|提醒内容||false|string||
|&emsp;&emsp;reminderTime|提醒时间（格式：[HH:mm]）||true|string||
|&emsp;&emsp;startTime|开始服药时间||true|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultVoid|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 更新服药提醒


**接口地址**:`/medication-reminder/{reminderId}`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "medicationName": "阿司匹林",
  "medicationDosage": "100mg",
  "medicationFrequency": 3,
  "medicationDuration": 7,
  "reminderContent": "请记得按时服用药物，注意饭后服用",
  "reminderTime": "[08:00, 20:00]",
  "startTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|reminderId||path|true|integer(int64)||
|medicationReminderDto|服药提醒数据传输对象|body|true|MedicationReminderDto|MedicationReminderDto|
|&emsp;&emsp;medicationName|药品名称||true|string||
|&emsp;&emsp;medicationDosage|药品剂量||true|string||
|&emsp;&emsp;medicationFrequency|每日服药次数||true|integer(int32)||
|&emsp;&emsp;medicationDuration|服药持续天数||true|integer(int32)||
|&emsp;&emsp;reminderContent|提醒内容||false|string||
|&emsp;&emsp;reminderTime|提醒时间（格式：[HH:mm]）||true|string||
|&emsp;&emsp;startTime|开始服药时间||true|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultVoid|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 删除服药提醒


**接口地址**:`/medication-reminder/{reminderId}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|reminderId||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultVoid|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 批量创建服药提醒


**接口地址**:`/medication-reminder/batch`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
[
  {
    "medicationName": "阿司匹林",
    "medicationDosage": "100mg",
    "medicationFrequency": 3,
    "medicationDuration": 7,
    "reminderContent": "请记得按时服用药物，注意饭后服用",
    "reminderTime": "[08:00, 20:00]",
    "startTime": ""
  }
]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|medicationReminderDtos|服药提醒数据传输对象|body|true|array|MedicationReminderDto|
|&emsp;&emsp;medicationName|药品名称||true|string||
|&emsp;&emsp;medicationDosage|药品剂量||true|string||
|&emsp;&emsp;medicationFrequency|每日服药次数||true|integer(int32)||
|&emsp;&emsp;medicationDuration|服药持续天数||true|integer(int32)||
|&emsp;&emsp;reminderContent|提醒内容||false|string||
|&emsp;&emsp;reminderTime|提醒时间（格式：[HH:mm]）||true|string||
|&emsp;&emsp;startTime|开始服药时间||true|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultVoid|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 获取下次最近服药时间的提醒


**接口地址**:`/medication-reminder/next`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultListMedicationReminder|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|array|MedicationReminder|
|&emsp;&emsp;reminderId||integer(int64)||
|&emsp;&emsp;accountId||integer(int64)||
|&emsp;&emsp;medicationName||string||
|&emsp;&emsp;medicationDosage||string||
|&emsp;&emsp;medicationFrequency||integer(int32)||
|&emsp;&emsp;medicationDuration||integer(int32)||
|&emsp;&emsp;startTime||string(date-time)||
|&emsp;&emsp;completionStatus||integer(int32)||
|&emsp;&emsp;nextReminderTime||string(date-time)||
|&emsp;&emsp;reminderTime||string||
|&emsp;&emsp;reminderCount||integer(int32)||
|&emsp;&emsp;createdAt||string(date-time)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": [
		{
			"reminderId": 0,
			"accountId": 0,
			"medicationName": "",
			"medicationDosage": "",
			"medicationFrequency": 0,
			"medicationDuration": 0,
			"startTime": "",
			"completionStatus": 0,
			"nextReminderTime": "",
			"reminderTime": "",
			"reminderCount": 0,
			"createdAt": ""
		}
	]
}
```


## 通过图片识别服药信息


**接口地址**:`/medication-reminder/png`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|file||query|true|file||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultListMedicationReminderVo|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|array|MedicationReminderVo|
|&emsp;&emsp;medicationName||string||
|&emsp;&emsp;medicationDosage||string||
|&emsp;&emsp;medicationFrequency||integer(int32)||
|&emsp;&emsp;medicationDuration||integer(int32)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": [
		{
			"medicationName": "",
			"medicationDosage": "",
			"medicationFrequency": 0,
			"medicationDuration": 0
		}
	]
}
```


# 健康目标管理


## 创建健康目标


**接口地址**:`/health-goals`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "goalCategory": "",
  "targetValue": 0,
  "targetDate": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|healthGoalDto|健康目标请求参数|body|true|HealthGoalDto|HealthGoalDto|
|&emsp;&emsp;goalCategory|目标类别（WEIGHT_LOSS, BLOOD_SUGAR, BLOOD_LIPID, EXERCISE_CALORIES）||true|string||
|&emsp;&emsp;targetValue|目标值||true|number(double)||
|&emsp;&emsp;targetDate|目标日期（yyyy-MM-dd）||true|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultLong|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


## 删除目标


**接口地址**:`/health-goals/{goalId}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|goalId||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultVoid|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 获取当前目标


**接口地址**:`/health-goals/current`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultHealthGoal|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||HealthGoal|HealthGoal|
|&emsp;&emsp;goalId||integer(int64)||
|&emsp;&emsp;accountId||integer(int64)||
|&emsp;&emsp;goalCategory||string||
|&emsp;&emsp;targetValue||number(double)||
|&emsp;&emsp;currentValue||number(double)||
|&emsp;&emsp;startDate||string(date-time)||
|&emsp;&emsp;targetDate||string(date-time)||
|&emsp;&emsp;goalStatus||integer(int32)||
|&emsp;&emsp;createdAt||string(date-time)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"goalId": 0,
		"accountId": 0,
		"goalCategory": "",
		"targetValue": 0,
		"currentValue": 0,
		"startDate": "",
		"targetDate": "",
		"goalStatus": 0,
		"createdAt": ""
	}
}
```


# 认证模块


## 检查是否登录


**接口地址**:`/api/auth/checkLogin`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>检查当前用户是否处于登录状态</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功返回登录状态||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 用户登录


**接口地址**:`/api/auth/login`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>使用用户名/邮箱登录获取Token</p>



**请求示例**:


```javascript
{
  "identifier": "health_user",
  "password": "securePwd123"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|loginDto|登录请求参数|body|true|LoginDto|LoginDto|
|&emsp;&emsp;identifier|登录标识（用户名或邮箱）||true|string||
|&emsp;&emsp;password|登录密码||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|登录成功|ResponseResultMapStringString|
|505|用户名或密码错误|ResponseResultMapStringString|
|511|账户被禁用|ResponseResultMapStringString|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-505**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-511**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 退出登录


**接口地址**:`/api/auth/logout`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>退出当前用户的登录状态</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功退出登录|ResponseResultVoid|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 用户注册


**接口地址**:`/api/auth/register`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>使用邮箱验证码完成注册</p>



**请求示例**:


```javascript
{
  "username": "health_user",
  "email": "user@example.com",
  "password": "securePwd123",
  "code": "123456",
  "birthday": "2004-01-01"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|registerDto|注册请求参数|body|true|RegisterDto|RegisterDto|
|&emsp;&emsp;username|用户名||true|string||
|&emsp;&emsp;email|邮箱地址||true|string||
|&emsp;&emsp;password|密码（6-20位字符）||true|string||
|&emsp;&emsp;code|邮箱验证码||true|string||
|&emsp;&emsp;birthday|生日（字符串形式）||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|注册成功|ResponseResultVoid|
|400|参数校验失败/验证码错误|ResponseResultVoid|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 发送验证码


**接口地址**:`/api/auth/send-code`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>向指定邮箱发送6位数字验证码</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|email|注册邮箱|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|验证码发送成功|ResponseResultVoid|
|400|邮箱格式错误|ResponseResultVoid|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


# 数据导出


## 导出健康数据


**接口地址**:`/export/biometric`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>导出当前用户的生物特征数据到Excel文件</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功导出数据||
|500|导出失败||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 导出饮食数据


**接口地址**:`/export/diet`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>导出当前用户的饮食日志数据到Excel文件</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功导出数据||
|500|导出失败||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 导出运动数据


**接口地址**:`/export/exercise`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>导出当前用户的运动日志数据到Excel文件</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功导出数据||
|500|导出失败||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 导出睡眠数据


**接口地址**:`/export/sleep`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>导出当前用户的睡眠日志数据到Excel文件</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功导出数据||
|500|导出失败||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 睡眠记录管理


## 新增睡眠记录


**接口地址**:`/sleep`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>需要登录，参数要求：<br>1. 入睡/醒来时间不能为空且需早于当前时间<br>2. 醒来时间需晚于入睡时间<br>3. 睡眠质量等级1-5</p>



**请求示例**:


```javascript
{
  "sleepStart": "",
  "sleepEnd": "",
  "sleepQuality": 4
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sleepLogDto|睡眠记录请求参数|body|true|SleepLogDto|SleepLogDto|
|&emsp;&emsp;sleepStart|入睡时间（时间戳格式）||true|string(date-time)||
|&emsp;&emsp;sleepEnd|醒来时间（时间戳格式）||true|string(date-time)||
|&emsp;&emsp;sleepQuality|睡眠质量（1-5级）||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|记录创建成功|ResponseResultLong|
|400|参数校验失败|ResponseResultLong|
|401|未登录访问|ResponseResultLong|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


## 删除睡眠记录


**接口地址**:`/sleep/{logId}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据记录ID删除指定记录</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|logId|记录ID|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|删除成功|ResponseResultVoid|
|400|记录不存在/无权限|ResponseResultVoid|
|401|未登录访问|ResponseResultVoid|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 获取每日睡眠时长数据


**接口地址**:`/sleep/duration`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json,*/*`


**接口描述**:<p>返回指定时间范围内每天的睡眠时长数据，用于生成柱状图，所对应的地址：<a href="https://echarts.apache.org/examples/zh/editor.html?c=bar-tick-align">https://echarts.apache.org/examples/zh/editor.html?c=bar-tick-align</a></p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|range|时间范围,可用值:week,month,3months,6months|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|查询成功||
|400|参数无效|ResponseResultMapStringObject|
|401|未登录访问|ResponseResultMapStringObject|


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 获取最新一次睡眠数据


**接口地址**:`/sleep/latest`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultSleepLog|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||SleepLog|SleepLog|
|&emsp;&emsp;logId||integer(int64)||
|&emsp;&emsp;accountId||integer(int64)||
|&emsp;&emsp;sleepStart||string(date-time)||
|&emsp;&emsp;sleepEnd||string(date-time)||
|&emsp;&emsp;sleepQuality||integer(int32)||
|&emsp;&emsp;createdAt||string(date-time)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"logId": 0,
		"accountId": 0,
		"sleepStart": "",
		"sleepEnd": "",
		"sleepQuality": 0,
		"createdAt": ""
	}
}
```


## 分页查询睡眠记录


**接口地址**:`/sleep/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>支持按入睡时间范围筛选</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|startDate|开始日期(yyyy-MM-dd)|query|false|string(date-time)||
|endDate|结束日期(yyyy-MM-dd)|query|false|string(date-time)||
|pageNum|页码（默认1）|query|false|integer(int32)||
|pageSize|每页数量（默认10）|query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|查询成功|ResponseResultPageSleepLog|
|401|未登录访问|ResponseResultPageSleepLog|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||PageSleepLog|PageSleepLog|
|&emsp;&emsp;records||array|SleepLog|
|&emsp;&emsp;&emsp;&emsp;logId||integer||
|&emsp;&emsp;&emsp;&emsp;accountId||integer||
|&emsp;&emsp;&emsp;&emsp;sleepStart||string||
|&emsp;&emsp;&emsp;&emsp;sleepEnd||string||
|&emsp;&emsp;&emsp;&emsp;sleepQuality||integer||
|&emsp;&emsp;&emsp;&emsp;createdAt||string||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;optimizeCountSql||PageSleepLog|PageSleepLog|
|&emsp;&emsp;searchCount||PageSleepLog|PageSleepLog|
|&emsp;&emsp;optimizeJoinOfCountSql||boolean||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;countId||string||
|&emsp;&emsp;pages||integer(int64)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"records": [
			{
				"logId": 0,
				"accountId": 0,
				"sleepStart": "",
				"sleepEnd": "",
				"sleepQuality": 0,
				"createdAt": ""
			}
		],
		"total": 0,
		"size": 0,
		"current": 0,
		"orders": [
			{
				"column": "",
				"asc": true
			}
		],
		"optimizeCountSql": {},
		"searchCount": {},
		"optimizeJoinOfCountSql": true,
		"maxLimit": 0,
		"countId": "",
		"pages": 0
	}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||PageSleepLog|PageSleepLog|
|&emsp;&emsp;records||array|SleepLog|
|&emsp;&emsp;&emsp;&emsp;logId||integer||
|&emsp;&emsp;&emsp;&emsp;accountId||integer||
|&emsp;&emsp;&emsp;&emsp;sleepStart||string||
|&emsp;&emsp;&emsp;&emsp;sleepEnd||string||
|&emsp;&emsp;&emsp;&emsp;sleepQuality||integer||
|&emsp;&emsp;&emsp;&emsp;createdAt||string||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;optimizeCountSql||PageSleepLog|PageSleepLog|
|&emsp;&emsp;searchCount||PageSleepLog|PageSleepLog|
|&emsp;&emsp;optimizeJoinOfCountSql||boolean||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;countId||string||
|&emsp;&emsp;pages||integer(int64)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"records": [
			{
				"logId": 0,
				"accountId": 0,
				"sleepStart": "",
				"sleepEnd": "",
				"sleepQuality": 0,
				"createdAt": ""
			}
		],
		"total": 0,
		"size": 0,
		"current": 0,
		"orders": [
			{
				"column": "",
				"asc": true
			}
		],
		"optimizeCountSql": {},
		"searchCount": {},
		"optimizeJoinOfCountSql": true,
		"maxLimit": 0,
		"countId": "",
		"pages": 0
	}
}
```


# 体检提醒管理


## 获取最近一次体检提醒信息


**接口地址**:`/health-check-reminder`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取当前用户最近一次未完成的体检提醒</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|查询成功|ResponseResultHealthCheckReminderDto|
|401|未登录访问|ResponseResultHealthCheckReminderDto|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||HealthCheckReminderDto|HealthCheckReminderDto|
|&emsp;&emsp;checkFrequencyDays|体检频率（天）|integer(int32)||
|&emsp;&emsp;scheduledTime|体检时间|string(date-time)||
|&emsp;&emsp;reminderContent|提醒内容|string||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"checkFrequencyDays": 180,
		"scheduledTime": "",
		"reminderContent": "请记得进行半年一次的常规体检"
	}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||HealthCheckReminderDto|HealthCheckReminderDto|
|&emsp;&emsp;checkFrequencyDays|体检频率（天）|integer(int32)||
|&emsp;&emsp;scheduledTime|体检时间|string(date-time)||
|&emsp;&emsp;reminderContent|提醒内容|string||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"checkFrequencyDays": 180,
		"scheduledTime": "",
		"reminderContent": "请记得进行半年一次的常规体检"
	}
}
```


## 创建体检提醒


**接口地址**:`/health-check-reminder`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>为当前登录用户创建新的体检提醒</p>



**请求示例**:


```javascript
{
  "checkFrequencyDays": 180,
  "scheduledTime": "",
  "reminderContent": "请记得进行半年一次的常规体检"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|healthCheckReminderDto|体检提醒信息|body|true|HealthCheckReminderDto|HealthCheckReminderDto|
|&emsp;&emsp;checkFrequencyDays|体检频率（天）||false|integer(int32)||
|&emsp;&emsp;scheduledTime|体检时间||false|string(date-time)||
|&emsp;&emsp;reminderContent|提醒内容||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|创建成功|ResponseResultVoid|
|401|未登录访问|ResponseResultVoid|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 更新体检提醒


**接口地址**:`/health-check-reminder/{id}`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>更新指定ID的体检提醒信息</p>



**请求示例**:


```javascript
{
  "checkFrequencyDays": 180,
  "scheduledTime": "",
  "reminderContent": "请记得进行半年一次的常规体检"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|提醒ID|path|true|integer(int64)||
|healthCheckReminderDto|体检提醒信息|body|true|HealthCheckReminderDto|HealthCheckReminderDto|
|&emsp;&emsp;checkFrequencyDays|体检频率（天）||false|integer(int32)||
|&emsp;&emsp;scheduledTime|体检时间||false|string(date-time)||
|&emsp;&emsp;reminderContent|提醒内容||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|更新成功|ResponseResultVoid|
|400|提醒不存在或无权限|ResponseResultVoid|
|401|未登录访问|ResponseResultVoid|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 删除体检提醒


**接口地址**:`/health-check-reminder/{reminderId}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除指定ID的体检提醒</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|reminderId|提醒ID|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|删除成功|ResponseResultVoid|
|400|提醒不存在或无权限|ResponseResultVoid|
|401|未登录访问|ResponseResultVoid|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 确认体检提醒


**接口地址**:`/health-check-reminder/confirm`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>通过令牌确认完成体检</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|token|确认令牌|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|确认成功|ResponseResultVoid|
|400|令牌无效或已过期|ResponseResultVoid|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 获取体检提醒列表


**接口地址**:`/health-check-reminder/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>分页获取当前用户的体检提醒列表，可按日期范围筛选</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|startDate|开始日期(yyyy-MM-dd)|query|false|string(date-time)||
|endDate|结束日期(yyyy-MM-dd)|query|false|string(date-time)||
|pageNum|页码，默认1|query|false|integer(int32)||
|pageSize|每页数量，默认10|query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|查询成功|ResponseResultPageHealthCheckReminder|
|401|未登录访问|ResponseResultPageHealthCheckReminder|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||PageHealthCheckReminder|PageHealthCheckReminder|
|&emsp;&emsp;records||array|HealthCheckReminder|
|&emsp;&emsp;&emsp;&emsp;reminderId||integer||
|&emsp;&emsp;&emsp;&emsp;accountId||integer||
|&emsp;&emsp;&emsp;&emsp;reminderContent||string||
|&emsp;&emsp;&emsp;&emsp;scheduledTime||string||
|&emsp;&emsp;&emsp;&emsp;completionStatus||integer||
|&emsp;&emsp;&emsp;&emsp;checkFrequencyDays||integer||
|&emsp;&emsp;&emsp;&emsp;lastReminderSent||string||
|&emsp;&emsp;&emsp;&emsp;nextReminderTime||string||
|&emsp;&emsp;&emsp;&emsp;reminderCount||integer||
|&emsp;&emsp;&emsp;&emsp;createdAt||string||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;optimizeCountSql||PageHealthCheckReminder|PageHealthCheckReminder|
|&emsp;&emsp;searchCount||PageHealthCheckReminder|PageHealthCheckReminder|
|&emsp;&emsp;optimizeJoinOfCountSql||boolean||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;countId||string||
|&emsp;&emsp;pages||integer(int64)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"records": [
			{
				"reminderId": 0,
				"accountId": 0,
				"reminderContent": "",
				"scheduledTime": "",
				"completionStatus": 0,
				"checkFrequencyDays": 0,
				"lastReminderSent": "",
				"nextReminderTime": "",
				"reminderCount": 0,
				"createdAt": ""
			}
		],
		"total": 0,
		"size": 0,
		"current": 0,
		"orders": [
			{
				"column": "",
				"asc": true
			}
		],
		"optimizeCountSql": {},
		"searchCount": {},
		"optimizeJoinOfCountSql": true,
		"maxLimit": 0,
		"countId": "",
		"pages": 0
	}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||PageHealthCheckReminder|PageHealthCheckReminder|
|&emsp;&emsp;records||array|HealthCheckReminder|
|&emsp;&emsp;&emsp;&emsp;reminderId||integer||
|&emsp;&emsp;&emsp;&emsp;accountId||integer||
|&emsp;&emsp;&emsp;&emsp;reminderContent||string||
|&emsp;&emsp;&emsp;&emsp;scheduledTime||string||
|&emsp;&emsp;&emsp;&emsp;completionStatus||integer||
|&emsp;&emsp;&emsp;&emsp;checkFrequencyDays||integer||
|&emsp;&emsp;&emsp;&emsp;lastReminderSent||string||
|&emsp;&emsp;&emsp;&emsp;nextReminderTime||string||
|&emsp;&emsp;&emsp;&emsp;reminderCount||integer||
|&emsp;&emsp;&emsp;&emsp;createdAt||string||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;optimizeCountSql||PageHealthCheckReminder|PageHealthCheckReminder|
|&emsp;&emsp;searchCount||PageHealthCheckReminder|PageHealthCheckReminder|
|&emsp;&emsp;optimizeJoinOfCountSql||boolean||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;countId||string||
|&emsp;&emsp;pages||integer(int64)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"records": [
			{
				"reminderId": 0,
				"accountId": 0,
				"reminderContent": "",
				"scheduledTime": "",
				"completionStatus": 0,
				"checkFrequencyDays": 0,
				"lastReminderSent": "",
				"nextReminderTime": "",
				"reminderCount": 0,
				"createdAt": ""
			}
		],
		"total": 0,
		"size": 0,
		"current": 0,
		"orders": [
			{
				"column": "",
				"asc": true
			}
		],
		"optimizeCountSql": {},
		"searchCount": {},
		"optimizeJoinOfCountSql": true,
		"maxLimit": 0,
		"countId": "",
		"pages": 0
	}
}
```


## 测试处理提醒


**接口地址**:`/health-check-reminder/test`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>触发体检提醒处理逻辑（仅用于测试）</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|处理成功|ResponseResultVoid|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


# 体征数据管理


## 创建生物特征记录


**接口地址**:`/health/biometric`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>需要登录，参数需符合验证规则</p>



**请求示例**:


```javascript
{
  "heightCm": 175.5,
  "weightKg": 65.3,
  "systolicPressure": 120,
  "diastolicPressure": 80,
  "bloodGlucose": 5.6,
  "bloodLipid": 1.8,
  "measurementTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|biometricRecordDto|BiometricRecordDto|body|true|BiometricRecordDto|BiometricRecordDto|
|&emsp;&emsp;heightCm|身高(cm)||false|number(double)||
|&emsp;&emsp;weightKg|体重(kg)||false|number(double)||
|&emsp;&emsp;systolicPressure|收缩压(mmHg)||false|integer(int32)||
|&emsp;&emsp;diastolicPressure|舒张压(mmHg)||false|integer(int32)||
|&emsp;&emsp;bloodGlucose|血糖(mmol/L)||false|number(double)||
|&emsp;&emsp;bloodLipid|血脂(mmol/L)||false|number(double)||
|&emsp;&emsp;measurementTime|测量时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|记录创建成功|ResponseResultLong|
|400|参数校验失败|ResponseResultLong|
|401|需要登录后操作|ResponseResultLong|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


## 删除生物特征记录


**接口地址**:`/health/biometric/{id}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>需要登录，id为对应的记录id</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultVoid|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 获取生物特征图表数据


**接口地址**:`/health/chart`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json,*/*`


**接口描述**:<p>获取指定时间范围内的生物特征数据用于图表展示。支持七天、一个月、三个月和半年的数据范围。返回的数据包括体重、血压、血糖、血脂和BMI等指标的时间序列数据，可直接用于前端图表展示。</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|timeRange|时间范围: 7d-七天, 1m-一个月, 3m-三个月, 6m-半年|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|查询成功|ResponseResult|
|400|无效的时间范围参数|ResponseResultBiometricChartDataDto|
|401|未登录访问|ResponseResultBiometricChartDataDto|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||BiometricChartDataDto|BiometricChartDataDto|
|&emsp;&emsp;dates|日期列表|array|string|
|&emsp;&emsp;weights|体重列表 (kg)|array|number(double)|
|&emsp;&emsp;systolicPressures|收缩压列表 (mmHg)|array|integer(int32)|
|&emsp;&emsp;diastolicPressures|舒张压列表 (mmHg)|array|integer(int32)|
|&emsp;&emsp;bloodGlucoses|血糖列表 (mmol/L)|array|number(double)|
|&emsp;&emsp;bloodLipids|血脂列表 (mmol/L)|array|number(double)|
|&emsp;&emsp;bmis|BMI列表|array|number(double)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"dates": "['2023-01-01', '2023-01-02', ...]",
		"weights": "[70.5, 71.0, ...]",
		"systolicPressures": "[120, 118, ...]",
		"diastolicPressures": "[80, 78, ...]",
		"bloodGlucoses": "[5.5, 5.6, ...]",
		"bloodLipids": "[4.5, 4.6, ...]",
		"bmis": "[22.5, 22.6, ...]"
	}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||BiometricChartDataDto|BiometricChartDataDto|
|&emsp;&emsp;dates|日期列表|array|string|
|&emsp;&emsp;weights|体重列表 (kg)|array|number(double)|
|&emsp;&emsp;systolicPressures|收缩压列表 (mmHg)|array|integer(int32)|
|&emsp;&emsp;diastolicPressures|舒张压列表 (mmHg)|array|integer(int32)|
|&emsp;&emsp;bloodGlucoses|血糖列表 (mmol/L)|array|number(double)|
|&emsp;&emsp;bloodLipids|血脂列表 (mmol/L)|array|number(double)|
|&emsp;&emsp;bmis|BMI列表|array|number(double)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"dates": "['2023-01-01', '2023-01-02', ...]",
		"weights": "[70.5, 71.0, ...]",
		"systolicPressures": "[120, 118, ...]",
		"diastolicPressures": "[80, 78, ...]",
		"bloodGlucoses": "[5.5, 5.6, ...]",
		"bloodLipids": "[4.5, 4.6, ...]",
		"bmis": "[22.5, 22.6, ...]"
	}
}
```


## 获取最新一次生物特征记录


**接口地址**:`/health/latest`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取当前用户最新的一条生物特征记录</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|查询成功|ResponseResultBiometricRecordVo|
|401|未登录访问|ResponseResultBiometricRecordVo|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||BiometricRecordVo|BiometricRecordVo|
|&emsp;&emsp;recordId||integer(int64)||
|&emsp;&emsp;accountId||integer(int64)||
|&emsp;&emsp;heightCm||number(double)||
|&emsp;&emsp;weightKg||number(double)||
|&emsp;&emsp;systolicPressure||integer(int32)||
|&emsp;&emsp;diastolicPressure||integer(int32)||
|&emsp;&emsp;bloodGlucose||number(double)||
|&emsp;&emsp;bloodLipid||number(double)||
|&emsp;&emsp;measurementTime||string(date-time)||
|&emsp;&emsp;createdAt||string(date-time)||
|&emsp;&emsp;bmi|BMI值|number(double)||
|&emsp;&emsp;bmiLevel|BMI分级,可用值:偏瘦,正常,超重,肥胖|string||
|&emsp;&emsp;bloodPressureLevel|血压分级,可用值:正常,正常偏高,轻度高血压前期,1级高血压,2级高血压,3级高血压|string||
|&emsp;&emsp;bloodGlucoseLevel|血糖状态,可用值:低血糖,正常,糖耐量受损,血糖偏高|string||
|&emsp;&emsp;bloodLipidLevel|血脂状态,可用值:正常,边缘升高,升高|string||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"recordId": 0,
		"accountId": 0,
		"heightCm": 0,
		"weightKg": 0,
		"systolicPressure": 0,
		"diastolicPressure": 0,
		"bloodGlucose": 0,
		"bloodLipid": 0,
		"measurementTime": "",
		"createdAt": "",
		"bmi": 22.5,
		"bmiLevel": "正常",
		"bloodPressureLevel": "正常",
		"bloodGlucoseLevel": "正常",
		"bloodLipidLevel": "正常"
	}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||BiometricRecordVo|BiometricRecordVo|
|&emsp;&emsp;recordId||integer(int64)||
|&emsp;&emsp;accountId||integer(int64)||
|&emsp;&emsp;heightCm||number(double)||
|&emsp;&emsp;weightKg||number(double)||
|&emsp;&emsp;systolicPressure||integer(int32)||
|&emsp;&emsp;diastolicPressure||integer(int32)||
|&emsp;&emsp;bloodGlucose||number(double)||
|&emsp;&emsp;bloodLipid||number(double)||
|&emsp;&emsp;measurementTime||string(date-time)||
|&emsp;&emsp;createdAt||string(date-time)||
|&emsp;&emsp;bmi|BMI值|number(double)||
|&emsp;&emsp;bmiLevel|BMI分级,可用值:偏瘦,正常,超重,肥胖|string||
|&emsp;&emsp;bloodPressureLevel|血压分级,可用值:正常,正常偏高,轻度高血压前期,1级高血压,2级高血压,3级高血压|string||
|&emsp;&emsp;bloodGlucoseLevel|血糖状态,可用值:低血糖,正常,糖耐量受损,血糖偏高|string||
|&emsp;&emsp;bloodLipidLevel|血脂状态,可用值:正常,边缘升高,升高|string||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"recordId": 0,
		"accountId": 0,
		"heightCm": 0,
		"weightKg": 0,
		"systolicPressure": 0,
		"diastolicPressure": 0,
		"bloodGlucose": 0,
		"bloodLipid": 0,
		"measurementTime": "",
		"createdAt": "",
		"bmi": 22.5,
		"bmiLevel": "正常",
		"bloodPressureLevel": "正常",
		"bloodGlucoseLevel": "正常",
		"bloodLipidLevel": "正常"
	}
}
```


## 分页获取生物特征记录


**接口地址**:`/health/records`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取当前用户的生物特征记录，支持日期范围筛选</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|startTime|开始日期(yyyy-MM-dd)|query|false|string(date-time)||
|endTime|结束日期(yyyy-MM-dd)|query|false|string(date-time)||
|pageNum|页码，默认1|query|false|integer(int32)||
|pageSize|每页数量，默认10|query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|查询成功|ResponseResultPageBiometricRecordVo|
|401|未登录访问|ResponseResultPageBiometricRecordVo|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||PageBiometricRecordVo|PageBiometricRecordVo|
|&emsp;&emsp;records|生物特征记录视图对象，包含计算的BMI和各项指标评估|array|BiometricRecordVo|
|&emsp;&emsp;&emsp;&emsp;recordId||integer||
|&emsp;&emsp;&emsp;&emsp;accountId||integer||
|&emsp;&emsp;&emsp;&emsp;heightCm||number||
|&emsp;&emsp;&emsp;&emsp;weightKg||number||
|&emsp;&emsp;&emsp;&emsp;systolicPressure||integer||
|&emsp;&emsp;&emsp;&emsp;diastolicPressure||integer||
|&emsp;&emsp;&emsp;&emsp;bloodGlucose||number||
|&emsp;&emsp;&emsp;&emsp;bloodLipid||number||
|&emsp;&emsp;&emsp;&emsp;measurementTime||string||
|&emsp;&emsp;&emsp;&emsp;createdAt||string||
|&emsp;&emsp;&emsp;&emsp;bmi|BMI值|number||
|&emsp;&emsp;&emsp;&emsp;bmiLevel|BMI分级,可用值:偏瘦,正常,超重,肥胖|string||
|&emsp;&emsp;&emsp;&emsp;bloodPressureLevel|血压分级,可用值:正常,正常偏高,轻度高血压前期,1级高血压,2级高血压,3级高血压|string||
|&emsp;&emsp;&emsp;&emsp;bloodGlucoseLevel|血糖状态,可用值:低血糖,正常,糖耐量受损,血糖偏高|string||
|&emsp;&emsp;&emsp;&emsp;bloodLipidLevel|血脂状态,可用值:正常,边缘升高,升高|string||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;optimizeCountSql||PageBiometricRecordVo|PageBiometricRecordVo|
|&emsp;&emsp;searchCount||PageBiometricRecordVo|PageBiometricRecordVo|
|&emsp;&emsp;optimizeJoinOfCountSql||boolean||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;countId||string||
|&emsp;&emsp;pages||integer(int64)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"records": [
			{
				"recordId": 0,
				"accountId": 0,
				"heightCm": 0,
				"weightKg": 0,
				"systolicPressure": 0,
				"diastolicPressure": 0,
				"bloodGlucose": 0,
				"bloodLipid": 0,
				"measurementTime": "",
				"createdAt": "",
				"bmi": 22.5,
				"bmiLevel": "正常",
				"bloodPressureLevel": "正常",
				"bloodGlucoseLevel": "正常",
				"bloodLipidLevel": "正常"
			}
		],
		"total": 0,
		"size": 0,
		"current": 0,
		"orders": [
			{
				"column": "",
				"asc": true
			}
		],
		"optimizeCountSql": {},
		"searchCount": {},
		"optimizeJoinOfCountSql": true,
		"maxLimit": 0,
		"countId": "",
		"pages": 0
	}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||PageBiometricRecordVo|PageBiometricRecordVo|
|&emsp;&emsp;records|生物特征记录视图对象，包含计算的BMI和各项指标评估|array|BiometricRecordVo|
|&emsp;&emsp;&emsp;&emsp;recordId||integer||
|&emsp;&emsp;&emsp;&emsp;accountId||integer||
|&emsp;&emsp;&emsp;&emsp;heightCm||number||
|&emsp;&emsp;&emsp;&emsp;weightKg||number||
|&emsp;&emsp;&emsp;&emsp;systolicPressure||integer||
|&emsp;&emsp;&emsp;&emsp;diastolicPressure||integer||
|&emsp;&emsp;&emsp;&emsp;bloodGlucose||number||
|&emsp;&emsp;&emsp;&emsp;bloodLipid||number||
|&emsp;&emsp;&emsp;&emsp;measurementTime||string||
|&emsp;&emsp;&emsp;&emsp;createdAt||string||
|&emsp;&emsp;&emsp;&emsp;bmi|BMI值|number||
|&emsp;&emsp;&emsp;&emsp;bmiLevel|BMI分级,可用值:偏瘦,正常,超重,肥胖|string||
|&emsp;&emsp;&emsp;&emsp;bloodPressureLevel|血压分级,可用值:正常,正常偏高,轻度高血压前期,1级高血压,2级高血压,3级高血压|string||
|&emsp;&emsp;&emsp;&emsp;bloodGlucoseLevel|血糖状态,可用值:低血糖,正常,糖耐量受损,血糖偏高|string||
|&emsp;&emsp;&emsp;&emsp;bloodLipidLevel|血脂状态,可用值:正常,边缘升高,升高|string||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;optimizeCountSql||PageBiometricRecordVo|PageBiometricRecordVo|
|&emsp;&emsp;searchCount||PageBiometricRecordVo|PageBiometricRecordVo|
|&emsp;&emsp;optimizeJoinOfCountSql||boolean||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;countId||string||
|&emsp;&emsp;pages||integer(int64)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"records": [
			{
				"recordId": 0,
				"accountId": 0,
				"heightCm": 0,
				"weightKg": 0,
				"systolicPressure": 0,
				"diastolicPressure": 0,
				"bloodGlucose": 0,
				"bloodLipid": 0,
				"measurementTime": "",
				"createdAt": "",
				"bmi": 22.5,
				"bmiLevel": "正常",
				"bloodPressureLevel": "正常",
				"bloodGlucoseLevel": "正常",
				"bloodLipidLevel": "正常"
			}
		],
		"total": 0,
		"size": 0,
		"current": 0,
		"orders": [
			{
				"column": "",
				"asc": true
			}
		],
		"optimizeCountSql": {},
		"searchCount": {},
		"optimizeJoinOfCountSql": true,
		"maxLimit": 0,
		"countId": "",
		"pages": 0
	}
}
```


# 饮食记录管理


## 新增饮食记录


**接口地址**:`/diet`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>需要登录，记录饮食信息</p>



**请求示例**:


```javascript
{
  "foodItem": "苹果",
  "quantityGrams": 150,
  "totalCalories": 80,
  "consumptionTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dietLogDto|饮食记录请求参数|body|true|DietLogDto|DietLogDto|
|&emsp;&emsp;foodItem|食物名称||true|string||
|&emsp;&emsp;quantityGrams|食用量（克）||false|number(double)||
|&emsp;&emsp;totalCalories|总热量（大卡）||true|integer(int32)||
|&emsp;&emsp;consumptionTime|时间戳格式||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|创建成功||
|400|参数校验失败|ResponseResultLong|
|401|未登录访问|ResponseResultLong|


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


## 删除饮食记录


**接口地址**:`/diet/{logId}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据记录ID删除指定记录</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|logId|记录ID|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|删除成功|ResponseResultVoid|
|400|记录不存在/无权限|ResponseResultVoid|
|401|未登录访问|ResponseResultVoid|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 获取每日饮食热量统计


**接口地址**:`/diet/daily-calories`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>按时间范围获取每日饮食热量统计数据</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|range|统计范围类型：WEEK(一周)、MONTH(一月)、THREE_MONTHS(三个月)、HALF_YEAR(半年)|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功获取统计数据|ResponseResultMapStringObject|
|401|未登录访问|ResponseResultMapStringObject|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 获取今天的饮食热量


**接口地址**:`/diet/hot_today`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>返回当前用户今天的总饮食热量</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功获取今天的饮食热量||
|401|未登录访问|ResponseResultDouble|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|number(double)|number(double)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


## 分页查询饮食记录


**接口地址**:`/diet/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>支持按日期范围筛选</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|startDate|开始日期(yyyy-MM-dd)|query|true|string(date-time)||
|endDate|结束日期(yyyy-MM-dd)|query|true|string(date-time)||
|pageNum|页码，默认1|query|false|integer(int32)||
|pageSize|每页数量，默认10|query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|查询成功|ResponseResultPageDietLog|
|401|未登录访问|ResponseResultPageDietLog|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||PageDietLog|PageDietLog|
|&emsp;&emsp;records||array|DietLog|
|&emsp;&emsp;&emsp;&emsp;logId||integer||
|&emsp;&emsp;&emsp;&emsp;accountId||integer||
|&emsp;&emsp;&emsp;&emsp;foodItem||string||
|&emsp;&emsp;&emsp;&emsp;quantityGrams||number||
|&emsp;&emsp;&emsp;&emsp;totalCalories||integer||
|&emsp;&emsp;&emsp;&emsp;consumptionTime||string||
|&emsp;&emsp;&emsp;&emsp;createdAt||string||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;optimizeCountSql||PageDietLog|PageDietLog|
|&emsp;&emsp;searchCount||PageDietLog|PageDietLog|
|&emsp;&emsp;optimizeJoinOfCountSql||boolean||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;countId||string||
|&emsp;&emsp;pages||integer(int64)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"records": [
			{
				"logId": 0,
				"accountId": 0,
				"foodItem": "",
				"quantityGrams": 0,
				"totalCalories": 0,
				"consumptionTime": "",
				"createdAt": ""
			}
		],
		"total": 0,
		"size": 0,
		"current": 0,
		"orders": [
			{
				"column": "",
				"asc": true
			}
		],
		"optimizeCountSql": {},
		"searchCount": {},
		"optimizeJoinOfCountSql": true,
		"maxLimit": 0,
		"countId": "",
		"pages": 0
	}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||PageDietLog|PageDietLog|
|&emsp;&emsp;records||array|DietLog|
|&emsp;&emsp;&emsp;&emsp;logId||integer||
|&emsp;&emsp;&emsp;&emsp;accountId||integer||
|&emsp;&emsp;&emsp;&emsp;foodItem||string||
|&emsp;&emsp;&emsp;&emsp;quantityGrams||number||
|&emsp;&emsp;&emsp;&emsp;totalCalories||integer||
|&emsp;&emsp;&emsp;&emsp;consumptionTime||string||
|&emsp;&emsp;&emsp;&emsp;createdAt||string||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;optimizeCountSql||PageDietLog|PageDietLog|
|&emsp;&emsp;searchCount||PageDietLog|PageDietLog|
|&emsp;&emsp;optimizeJoinOfCountSql||boolean||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;countId||string||
|&emsp;&emsp;pages||integer(int64)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"records": [
			{
				"logId": 0,
				"accountId": 0,
				"foodItem": "",
				"quantityGrams": 0,
				"totalCalories": 0,
				"consumptionTime": "",
				"createdAt": ""
			}
		],
		"total": 0,
		"size": 0,
		"current": 0,
		"orders": [
			{
				"column": "",
				"asc": true
			}
		],
		"optimizeCountSql": {},
		"searchCount": {},
		"optimizeJoinOfCountSql": true,
		"maxLimit": 0,
		"countId": "",
		"pages": 0
	}
}
```


# 用户信息管理模块


## 获取用户信息


**接口地址**:`/account`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取当前登录用户的详细信息</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功获取用户信息|UserInfoVo|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|accountId||integer(int64)|integer(int64)|
|username||string||
|accountType||integer(int32)|integer(int32)|
|birthday||string||
|email||string||


**响应示例**:
```javascript
{
	"accountId": 0,
	"username": "",
	"accountType": 0,
	"birthday": "",
	"email": ""
}
```


## 更新用户信息


**接口地址**:`/account`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>更新当前登录用户的信息</p>



**请求示例**:


```javascript
{
  "birthday": "1990-01-01"
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userInfoDto|用户信息|body|true|UserInfoDto|UserInfoDto|
|&emsp;&emsp;birthday|生日（格式：yyyy-MM-dd）||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功更新用户信息|ResponseResultVoid|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


# 运动记录管理


## 分页查询运动记录


**接口地址**:`/exercise`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|startDate|开始时间(yyyy-MM-dd)|query|true|string(date-time)||
|endDate|结束时间(yyyy-MM-dd)|query|true|string(date-time)||
|pageNum|页码|query|false|integer(int32)||
|pageSize|每页数量|query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultPageExerciseLog|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||PageExerciseLog|PageExerciseLog|
|&emsp;&emsp;records||array|ExerciseLog|
|&emsp;&emsp;&emsp;&emsp;logId||integer||
|&emsp;&emsp;&emsp;&emsp;accountId||integer||
|&emsp;&emsp;&emsp;&emsp;exerciseType||string||
|&emsp;&emsp;&emsp;&emsp;startTimestamp||string||
|&emsp;&emsp;&emsp;&emsp;durationMinutes||integer||
|&emsp;&emsp;&emsp;&emsp;distanceKm||number||
|&emsp;&emsp;&emsp;&emsp;caloriesBurned||integer||
|&emsp;&emsp;&emsp;&emsp;createdAt||string||
|&emsp;&emsp;total||integer(int64)||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;orders||array|OrderItem|
|&emsp;&emsp;&emsp;&emsp;column||string||
|&emsp;&emsp;&emsp;&emsp;asc||boolean||
|&emsp;&emsp;optimizeCountSql||PageExerciseLog|PageExerciseLog|
|&emsp;&emsp;searchCount||PageExerciseLog|PageExerciseLog|
|&emsp;&emsp;optimizeJoinOfCountSql||boolean||
|&emsp;&emsp;maxLimit||integer(int64)||
|&emsp;&emsp;countId||string||
|&emsp;&emsp;pages||integer(int64)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"records": [
			{
				"logId": 0,
				"accountId": 0,
				"exerciseType": "",
				"startTimestamp": "",
				"durationMinutes": 0,
				"distanceKm": 0,
				"caloriesBurned": 0,
				"createdAt": ""
			}
		],
		"total": 0,
		"size": 0,
		"current": 0,
		"orders": [
			{
				"column": "",
				"asc": true
			}
		],
		"optimizeCountSql": {},
		"searchCount": {},
		"optimizeJoinOfCountSql": true,
		"maxLimit": 0,
		"countId": "",
		"pages": 0
	}
}
```


## 创建运动记录


**接口地址**:`/exercise`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>需要登录，参数需满足以下要求：<br>1. 运动类型必填<br>2. 开始时间不能晚于当前时间<br>3. 持续时间至少1分钟<br>4. 卡路里消耗至少1大卡</p>



**请求示例**:


```javascript
{
  "exerciseType": "跑步",
  "startTimestamp": "",
  "durationMinutes": 30,
  "distanceKm": 5.2,
  "caloriesBurned": 300
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|exerciseLogDto|运动记录请求参数|body|true|ExerciseLogDto|ExerciseLogDto|
|&emsp;&emsp;exerciseType|运动类型||true|string||
|&emsp;&emsp;startTimestamp|开始时间（时间戳格式）||true|string(date-time)||
|&emsp;&emsp;durationMinutes|持续时间（分钟）||true|integer(int32)||
|&emsp;&emsp;distanceKm|运动距离（千米）||false|number(double)||
|&emsp;&emsp;caloriesBurned|消耗卡路里（大卡）||true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|记录创建成功|ResponseResultLong|
|400|参数校验失败|ResponseResult|
|401|未登录访问|ResponseResultLong|


**响应状态码-200**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": 0
}
```


## 删除运动记录


**接口地址**:`/exercise/{logId}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|logId||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultVoid|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 获取每日运动消耗热量统计


**接口地址**:`/exercise/daily-calories-burned`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`application/json,*/*`


**接口描述**:<p>按时间范围获取每日运动消耗热量统计数据，返回格式符合ECharts图表要求</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|range|统计范围类型：WEEK(一周)、MONTH(一月)、THREE_MONTHS(三个月)、HALF_YEAR(半年),可用值:WEEK,MONTH,THREE_MONTHS,HALF_YEAR|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|成功获取统计数据||
|400|无效的时间范围参数|ResponseResult|
|401|未登录访问|ResponseResultMapStringObject|


**响应状态码-400**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


**响应状态码-401**:


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```


## 获取最新的运动记录


**接口地址**:`/exercise/latest`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultExerciseLog|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data||ExerciseLog|ExerciseLog|
|&emsp;&emsp;logId||integer(int64)||
|&emsp;&emsp;accountId||integer(int64)||
|&emsp;&emsp;exerciseType||string||
|&emsp;&emsp;startTimestamp||string(date-time)||
|&emsp;&emsp;durationMinutes||integer(int32)||
|&emsp;&emsp;distanceKm||number(double)||
|&emsp;&emsp;caloriesBurned||integer(int32)||
|&emsp;&emsp;createdAt||string(date-time)||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {
		"logId": 0,
		"accountId": 0,
		"exerciseType": "",
		"startTimestamp": "",
		"durationMinutes": 0,
		"distanceKm": 0,
		"caloriesBurned": 0,
		"createdAt": ""
	}
}
```


# system-controller


## test


**接口地址**:`/system`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResultVoid|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|msg|提示信息|string||
|data|响应数据|object||


**响应示例**:
```javascript
{
	"code": 200,
	"msg": "操作成功",
	"data": {}
}
```