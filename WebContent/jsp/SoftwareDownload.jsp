<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>浙江省高校计算机等级考试管理系统相关下载</title>
<link rel="stylesheet" type="text/css"
	href="/ExamSignUp/resources/ext/resources/css/ext-all.css" />
<style>
body {
	background-image: url(../resources/images/loginbg.png);
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
.topFont {
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
.table {
	position: absolute;
	width: 818px;
	height: 100px;
	left: 50%;
	top: 50%;
	margin-left: -410px;
	margin-top: -240px;
	font-family: Microsoft Simhei, serif;
	font-weight: bold;
	border-collapse: collapse;
	text-align: center;
	vertical-align: middle;
	background-color: #EFF2FD;
	filter: progid:DXImageTransform.Microsoft.Shadow(color=#909090,
		direction=120, strength=4 ); /*ie*/
	-moz-box-shadow: 2px 2px 10px #909090; /*firefox*/
	-webkit-box-shadow: 2px 2px 10px #909090; /*safari或chrome*/
	box-shadow: 2px 2px 10px #909090; /*opera或ie9*/
}
.tr1 {
	background: #EFF2FD;
	font-weight: bold;
	color: black;
	height: 50px;
}
.tr2 {
	background: #8DB5EB;
	font-weight: bold;
	color: black;
	height: 50px;
}

</style>
	<link rel="stylesheet" type="text/css" href="/ExamSignUp/resources/ext/resources/css/ext-all.css" />
	<script type="text/javascript" src="/ExamSignUp/resources/ext/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="/ExamSignUp/resources/ext/ext-all.js"></script>
	<script type='text/javascript' src='/ExamSignUp/dwr/engine.js'></script>
	<script type='text/javascript' src='/ExamSignUp/dwr/util.js'></script>
    <script type='text/javascript' src='/ExamSignUp/dwr/interface/SoftwareDownloadController.js'></script>
	<script type="text/javascript" src="/ExamSignUp/resources/custom/DwrProxy.js"></script>
</head>
<body>
     <script language=javascript>
     function makeTable(basedir,softwareList) {
    	 //命名规则，中文部分为类型名，截取最后的英文部分及后缀最为名称
			var data= new Array();
    	    data.push('<table class="table"><tbody>');
    	    data.push('<tr class="tr1">');
    	    data.push('<td>序号</td>');
    	    data.push('<td>类型</td>');
    	    data.push('<td>版本号</td>');
    	    data.push('<td>名称</td>');
    	    data.push('<td>下载</td>');
    	    data.push('</tr>');
    	    	var count=1;
    	       for (var i = 0; i < softwareList.length; i++,count++) {
    	    	   if(i%2==1){
    	    		   data.push('<tr class="tr1">');
    	    	   }else{
    	    		   data.push('<tr class="tr2">');
    	    	   }
		   	        {
		   	         	data.push('<td>' + count + '</td>');
		   	         	data.push('<td>' +softwareList[i].remark + '</td>');
		   	         	data.push('<td>' +softwareList[i].version + '</td>');
		   	      	   data.push('<td>' +softwareList[i].filename  + '</td>');
		   	      	   data.push('<td>');
		   	      	   var path="/ExamSignUp/"+basedir+"/"+softwareList[i].filename;
		   	      	   data.push(  "<a href='#' onclick=\'window.open(\""+path+"\")\'>下载</a>");
		   	     	    data.push('</td>');
		   	        }
		   	        data.push('</tr>');
    	       }
    	       data.push('</tbody><table>');
    	       document.getElementById('softwareList').innerHTML = data.join('');
   	}
     
     Ext.onReady(function(){
    	 SoftwareDownloadController.getSoftwareList(function(data){
    		 var jsonData=Ext.util.JSON.decode(data);
    		 if(jsonData.success==false){
    			 Ext.MessageBox.alert('提示',jsonData.errors.info);
    			 return null;
    		 }else{
    			 makeTable(jsonData.basedir,jsonData.data);
    		 }
    	 });
	 });
     </script>
</body>
<div id="topBackground" class="ccdiv1">
		<p class="topFont">相关软件下载</p>
</div>
<div id="softwareList">
</div>
</html>