package com.Statement;

public enum State
{
	Repetitive_UserID, 				 //注册时重复ID，ID是系统自动生成的
	Valid_UserID,						 //有效用户ID
	
	Repetitive_UserName,			 //注册时用户名重复
	Valid_UserName,					 //有效用户名
	
	InValid_EmailAddress,		 //注册时输入无效Email地址
	Valid_EmailAddress,  			 //有效邮箱地址
	
	InValid_PhoneNumber,		 //注册时输入无效电话号码
	Valid_PhoneNumber,			 //有效电话号码
	
	InValid_ShelterID,				//注册时输入的shelterID无效
	Valid_ShelterID,					//有效的shelterID
	
	Connection_Error,				 //与数据库连接错误
	Connection_Success,			 //与数据库连接成功
	
	Insert_Error,						//插入数据错误
	Insert_Success,		 				 //插入数据成功(注册时也可指注册成功)
	
	Login_Error,						//登陆失败
	Login_Success,						 //登陆成功
	
	GetTable_Error,					//获取表失败
	GetTable_Success,				//获取表成功
	
	Operate_Error,					//存储过程执行失败
	Operate_Success,					//存储过程执行成功
}