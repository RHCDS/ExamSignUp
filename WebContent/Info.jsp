<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>浙江省高校计算机等级考试考务管理系统</title>
<link rel="stylesheet" type="text/css"
	href="/ExamSignUp/resources/ext/resources/css/ext-all.css" />
<style>
body {
	background-image: url(/ExamSignUp/resources/images/loginbg.png);
	background-repeat: repeat;
}
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
}
</style>
<script type="text/javascript"
	src="/ExamSignUp/resources/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript"
	src="/ExamSignUp/resources/ext/ext-all.js"></script>
</head>
<body>
	<div id="topBackground" class="ccdiv1">
		<p class="topFont">浙江省高校计算机等级考试考务管理系统</p>
	</div>
	<div id="redirect" class="ccdiv2">
		<table  class="loginTable">
			<tr>
				<td colspan="2" align="center">${msg}</td>
			</tr>
			<tr>
				<td align="center">系统将在<span id="totalSecond">5</span>后自动跳转到主页！</td>
			</tr>
		</table>
	</div>
	<div id="topBackground" class="ccdiv3">
		<p class="copyright">杭州电子科技大学研制&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2013年10月</p>
	</div>
	<script type="text/javascript">
	var second = document.getElementById('totalSecond').textContent;

	if (navigator.appName.indexOf("Explorer") > -1) {
		second = document.getElementById('totalSecond').innerText;
	} else {
		second = document.getElementById('totalSecond').textContent;
	}

	setInterval("redirect()", 1000);
	function redirect() {
		if (second < 0) {
			top.location.href='/ExamSignUp';
		} else {
			if (navigator.appName.indexOf("Explorer") > -1) {
				document.getElementById('totalSecond').innerText = second--;
			} else {
				document.getElementById('totalSecond').textContent = second--;
			}
		}
	} 
	</script>
</body>
</html>