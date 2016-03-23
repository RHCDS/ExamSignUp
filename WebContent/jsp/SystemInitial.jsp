<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="/ExamSignUp/resources/ext/resources/css/ext-all.css" />
			<script type="text/javascript" src="/ExamSignUp/resources/ext/adapter/ext/ext-base.js"></script>
			<script type="text/javascript" src="/ExamSignUp/resources/ext/ext-all.js"></script>
			<script type='text/javascript' src='/ExamSignUp/dwr/engine.js'></script>
			<script type='text/javascript' src='/ExamSignUp/dwr/util.js'></script>
			<script type='text/javascript' src='/ExamSignUp/dwr/interface/StudentController.js'></script>
			<script type='text/javascript' src='/ExamSignUp/dwr/interface/ClearHistoryDataController.js'></script>
			<script type='text/javascript' src='/ExamSignUp/dwr/interface/CampusController.js'></script>
			<script type='text/javascript' src='/ExamSignUp/dwr/interface/LanguageController.js'></script>
			<script type='text/javascript' src='/ExamSignUp/dwr/interface/ExamNumEditController.js'></script>
			<script type='text/javascript' src='/ExamSignUp/dwr/interface/PhysiceexamroomController.js'></script>
			<script type='text/javascript' src='/ExamSignUp/dwr/interface/InstitutionController.js'></script>
			<script type='text/javascript' src='/ExamSignUp/dwr/interface/SupervisorController.js'></script>
			<script type="text/javascript" src="/ExamSignUp/resources/custom/DwrProxy.js"></script>
			<script type="text/javascript" src="../resources/ItemSelector/DDView.js"></script>
			<script type="text/javascript" src="../resources/ItemSelector/Multiselect.js"></script>
			<script type="text/javascript" src="/ExamSignUp/resources/js/ExamNumEdit.js"></script>
			<script type="text/javascript" src="/ExamSignUp/resources/js/ExamroomManage.js"></script>
			<script type="text/javascript" src="/ExamSignUp/resources/js/LanguageManage.js"></script>
			<script type="text/javascript" src="/ExamSignUp/resources/js/SupervisorManage.js"></script>
			<script type="text/javascript" src="/ExamSignUp/resources/js/SystemInitial.js"></script>
			
	
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

.next {
	background-image: url(../resources/icons/next.png) !important;
}

.forward {
	background-image: url(../resources/icons/forward.png) !important;
}

.ok {
	background-image: url(../resources/icons/ok.png) !important;
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

.import {
	background-image: url(../resources/icons/landDataImport.png) !important;
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
  			<form name ="backStudentsExcel" id = "backStudentsExcel" action = "" target = "export" method ="post" accept-charset="UTF-8"></form>
  			<iframe name="export" id="export"></iframe>
  		</div>
  		 <div style = "display:none">
  			<form name ="exportStudentsExcel" id = "exportStudentsExcel" action = "" target = "export" method ="post" accept-charset="UTF-8"></form>
  			<iframe name="export" id="export"></iframe>
  		</div>
  		<div style = "display:none">
  			<form name ="exportStudentsDbf" id = "exportStudentsDbf" action = "" target = "export" method ="post" accept-charset="UTF-8"></form>
  			<iframe name="export" id="export"></iframe>
  		</div>
	<script language="javascript">
		Ext.onReady(function(){
			systemInitialWindow.show();
		});
  		</script>
	</body>

</html>