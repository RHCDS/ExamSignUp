<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="/ExamSignUp/resources/ext/resources/css/ext-all.css" />
		<link rel='stylesheet' type='text/css' href='../resources/ItemSelector/Multiselect.css'>
		<script type="text/javascript" src="/ExamSignUp/resources/ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="/ExamSignUp/resources/ext/ext-all.js"></script>
		<script type="text/javascript">
		if(Ext.isChrome===true){ 
			var chromeDatePickerCSS = ".x-date-picker {border-color: #1b376c;background-color:#fff;position: relative;width: 185px;}";
			Ext.util.CSS.createStyleSheet(chromeDatePickerCSS,'chromeDatePickerStyle');
			}
		</script>
		<script type="text/javascript" src="../resources/ItemSelector/DDView.js"></script>
		<script type="text/javascript" src="../resources/ItemSelector/Multiselect.js"></script>
		
  		<script type='text/javascript' src='/ExamSignUp/dwr/engine.js'></script>
  		<script type='text/javascript' src='/ExamSignUp/dwr/util.js'></script>
		<script type="text/javascript" src="/ExamSignUp/resources/custom/DwrProxy.js"></script>
		<script type='text/javascript' src='/ExamSignUp/dwr/interface/ExamNumEditController.js'></script>
		<script type='text/javascript' src='/ExamSignUp/resources/js/ExamNumEdit.js'></script>		
		<style type="text/css">
.icon-grid {
	background-image: url(../resources/icons/grid.gif) !important;
}

.add {
	background-image: url(../resources/icons/add.gif) !important;
}

.edit {
	background-image: url(../resources/icons/edit.gif) !important;
}

.remove {
	background-image: url(../resources/icons/delete.gif) !important;
}

.save {
	background-image: url(../resources/icons/save.gif) !important;
}

.back {
	background-image: url(../resources/icons/back.gif) !important;
}

.refresh {
	background-image: url(../resources/icons/refresh.gif) !important;
}

.upload-icon {
	background: url(../resources/icons/image_add.png) no-repeat 0 0
		!important;
}

.dIcon {
	background-image: url(../resources/icons/data.gif) !important;
}

.lIcon {
	background-image: url(../images/database_table.png) !important;
}

.pIcon {
	background-image: url(../images/house.png) !important;
}

</style>
	</head>
	<body>
	
		<div style = "display:none">
  			<form name ="exportStudentsExcel" id = "exportStudentsExcel" action = "" target = "export" method ="post" accept-charset="UTF-8"></form>
  			<iframe name="export" id="export"></iframe>
  		</div>
  		<div style = "display:none">
  			<form name ="exportStudentsDbf" id = "exportStudentsDbf" action = "" target = "export" method ="post" accept-charset="UTF-8"></form>
  			<iframe name="export" id="export"></iframe>
  		</div>
		<script type="text/javascript">
			Ext.onReady(function(){
				this.pageInit();
			});
  		</script>
	</body>

</html>
