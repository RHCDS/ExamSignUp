<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="/ExamSignUp/resources/ext/resources/css/ext-all.css" />
<style>
.ccdiv1 {
	position: absolute;
	width: 820px;
	height: 100px;
	left: 50%;
	top: 50%;
	margin-left: -410px;
	margin-top: -340px;
	background-color: #205983;
	filter: progid:DXImageTransform.Microsoft.Shadow(color=#909090,
		direction=120, strength=4 ); /*ie*/
	-moz-box-shadow: 2px 2px 10px #909090; /*firefox*/
	-webkit-box-shadow: 2px 2px 10px #909090; /*safari或chrome*/
	box-shadow: 2px 2px 10px #909090; /*opera或ie9*/
}

.ccdiv2 {
	position: absolute;
	width: 820px;
	height: 470px;
	left: 50%;
	top: 50%;
	margin-left: -410px;
	margin-top: -240px;
	/* border-style: solid;
  	border-color: black;
  	border-width: 2px; */
	background-image: url(/ExamSignUp/resources/images/bottomBackground.jpg);
	filter: progid:DXImageTransform.Microsoft.Shadow(color=#909090,
		direction=120, strength=4 ); /*ie*/
	-moz-box-shadow: 2px 2px 10px #909090; /*firefox*/
	-webkit-box-shadow: 2px 2px 10px #909090; /*safari或chrome*/
	box-shadow: 2px 2px 10px #909090; /*opera或ie9*/
}

.ccdiv3 {
	position: absolute;
	width:400px;
	height: 20px;
	left: 50%;
	top: 50%;
	margin-left: -170px;
	margin-top:  250px;
}
.ccdiv4 {
	position: absolute;
	width:400px;
	height: 20px;
	left: 50%;
	top: 50%;
	margin-left: -100px;
	margin-top:  300px;
}
.copyright {
	font-family: Microsoft Simhei, serif;
	font-size: 17px;
	color: #000000;
	vertical-align: middle;
}

.ftpAddress {
	font-family: Microsoft Simhei, serif;
	font-size: 15px;
	color: #000000;
	vertical-align: middle;
}

body {
	background-image: url(/ExamSignUp/resources/images/loginbg.png);
	background-repeat: repeat;
}
input,select{vertical-align:middle;}
p.topFont {
	position: relative;
	left: 5%;
	top: 37%;
	font-family: Microsoft Simhei, serif;
	font-weight: bold;
	font-size: 30px;
	color: #FFFFFF;
	text-shadow: black 2px 2px 2px;
	vertical-align: middle;
}

table.loginTable {
	position: relative;
	left: 50%;
	top: 15%;
	width: 370px;
	height: 320px;
	font-family: Microsoft Simhei, serif;
	font-weight: bold;
	border-collapse: collapse;
	background-color: #EFF2FD;
	filter: progid:DXImageTransform.Microsoft.Shadow(color=#909090,
		direction=120, strength=4 ); /*ie*/
	-moz-box-shadow: 2px 2px 10px #909090; /*firefox*/
	-webkit-box-shadow: 2px 2px 10px #909090; /*safari或chrome*/
	box-shadow: 2px 2px 10px #909090; /*opera或ie9*/
}

.loginTableCell {
	position: relative;
	left: 5%;
	top: 0%;
	width: 100%;
	height: 15%;
	font-family: Microsoft Simhei, serif;
	font-weight: bold;
	font-size: 20px;
	border-collapse: collapse;
	vertical-align: middle;
}

.login-tab {
	position: relative;
	left: 0%;
	top: 0%;
	height: 20%;
	width: 100%;
/* 	border-bottom: 4px solid #8DB5EB; */
}

.login-tab-item {
	position: relative;
	width: 50%;
	height:15%;
	text-align: center;
	vertical-align: middle;
	cursor: pointer;
	background: #EFF2FD;
	font-family: Microsoft Simhei, serif;
	font-weight: bold;
	font-size: 20px;
	color: black;
	border-top-left-radius: 3px;
	border-top-right-radius: 3px;
}

.loginButtonCell {
	position: relative;
	width: 50%;
	heigth: 15%;
	text-align: center;
	vertical-align: middle;
	cursor: pointer;
	font-weight: normal;
	color: black;
	font-size: 20px;
	border-top-left-radius: 3px;
	border-top-right-radius: 3px;
}

.uncurrent {
	background: #8DB5EB;
	font-weight: bold;
	color: white;
}
</style>
<!-- 不要随意调整整个文件的顺序，特别是ext-base.js必须在ext-all.js文件之前 -->
<script type="text/javascript"
	src="resources/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="resources/ext/ext-all.js"></script>
<script type='text/javascript' src='/ExamSignUp/dwr/engine.js'></script>
<script type='text/javascript' src='/ExamSignUp/dwr/util.js'></script>
<script type='text/javascript' src='/ExamSignUp/resources/js/IdCheck.js'></script>
<script type="text/javascript" src="/ExamSignUp/resources/js/MD5.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>浙江省高校计算机等级考试考务管理系统</title>
</head>
<body>
	<div id="topBackground" class="ccdiv1">
		<p class="topFont">浙江省高校计算机等级考试考务管理系统</p>
	</div>
	<div id="bottomBackground" class="ccdiv2">
		<table id="loginTable" class="loginTable">
			<tr class="login-tab">
				<td id="examCollegeTab" onclick="choseRorl('examCollegeTab')"
					class="login-tab-item">考试院</td>
				<td id="schoolTab" onclick="choseRorl('schoolTab')"
					class="login-tab-item uncurrent">高&nbsp;&nbsp;&nbsp;&nbsp;校</td>
			</tr>
			<tr>
				<td class="loginTableCell" colspan="2" id="showName1">用&nbsp;&nbsp;&nbsp;&nbsp;户:</td>
			</tr>
			<tr>
				<td class="loginTableCell" colspan="2"><input type="text"
					id="field1" style="width: 90%; height: 80%; font-size: 20px;">
				</td>
			</tr>
			<tr>
				<td class="loginTableCell" colspan="2" id="showName2">密&nbsp;&nbsp;&nbsp;&nbsp;码:</td>
			</tr>
			<tr>
				<td class="loginTableCell" colspan="2"><input type="password"
					id="field2"
					style="width: 90%; height: 80%; font-size: 20px;"></td>
			</tr>
			<tr>
				<td id="examCollegeTab" align="center"><img id='loadingImage'
					src='resources/icons/waiting.gif' style="display: none" align="left"></img> <a
					id="msg"></a></td>
				<td id="schoolTab" class="loginButtonCell" align="center"><input
					type="button" value="登录" onclick="login()"
					style="width: 80%; height: 75%; font-size: 20px"></td>
			</tr>
		</table>
	</div>
	<div id="topBackground" class="ccdiv3">
		<p class="copyright">杭州电子科技大学研制&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2013年10月</p>
	</div>
	<script type="text/javascript">
		currentRole = "examCollege";
		function choseRorl(argu) {
			document.getElementById("msg").innerHTML = "<font color=green></font>";
			document.getElementById('field1').value="";
			document.getElementById('field2').value="";
			switch (argu) {
			case "examCollegeTab":
				document.getElementById("examCollegeTab").className = "login-tab-item";
				document.getElementById("schoolTab").className = "login-tab-item uncurrent";
				document.getElementById("showName1").innerHTML = "用&nbsp;&nbsp;&nbsp;&nbsp;户:";
				currentRole = "examCollege";
				break;
			case "schoolTab":
				document.getElementById("examCollegeTab").className = "login-tab-item uncurrent";
				document.getElementById("schoolTab").className = "login-tab-item";
				document.getElementById("showName1").innerHTML = "用&nbsp;&nbsp;&nbsp;&nbsp;户:";
				currentRole = "school";
				break;
			}
		}
		function login() {
			if (document.getElementById('field1').value == "") {
				switch(currentRole){
				case "examCollege":
					document.getElementById("msg").innerHTML = "<font color=red>用户不能为空</font>";
					break;
				case "school":
					document.getElementById("msg").innerHTML = "<font color=red>用户不能为空</font>";
				}
				return;
			}
			if (document.getElementById('field2').value == "") {
				document.getElementById("msg").innerHTML = "<font color=red>密码不能为空</font>";
				return;
			}
			
			document.getElementById("loadingImage").style.display = "block";
			document.getElementById("msg").innerHTML = "<font color=green>正在登陆</font>";
			setTimeout("loginAJAX()", 500);
		}
		function loginAJAX(){
			Ext.Ajax
			.request({
				url : 'ManagerLogin.do',
				method : 'post',
				waitMsg : '正在提交，请等待……',
				params : {
					managerName : document
							.getElementById('field1').value,
					category : currentRole,
					managerPassword : hex_md5(document
							.getElementById('field2').value)
				},
				success : function(response, options) {
					var jsonData = Ext.util.JSON
							.decode(response.responseText);
					document.getElementById("loadingImage").style.display = "none";
					if (jsonData.success == true) {
						document.getElementById("msg").innerHTML = "<font color=green>"
								+ jsonData.errors.info + " 正在跳转</font>";
						window.location.href = 'Home.do';
					} else if (jsonData.success == false) {
						document.getElementById("msg").innerHTML = "<font color=red>"
							+ jsonData.errors.info + "</font>";
					}

				},
				failure : function(response, options) {
					document.getElementById("loadingImage").style.display = "none";
					Ext.Msg
							.alert(
									'提示',
									'原因如下：'
											+ Ext.util.JSON
													.decode(response.responseText).errors.info);
				}
			});
		}
		
		if(document.addEventListener){
			//如果是Firefox   
			     document.addEventListener("keypress",fireFoxHandler, true); 
			     }else{   
			     document.attachEvent("onkeypress",ieHandler);   
			} 
		function fireFoxHandler(evt){
			     if(evt.keyCode==13){
			             login();    //你的代码 
			     }  
		}  
		function ieHandler(evt){  
			        //alert("IE"); 
			        if(evt.keyCode==13){  
			                login();//你的代码  
			        }
		} 
	</script>
</body>
</html>
