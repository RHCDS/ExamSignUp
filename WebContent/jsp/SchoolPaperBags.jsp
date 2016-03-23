<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="/ExamSignUp/resources/ext/resources/css/ext-all.css" />
		<script type="text/javascript" src="/ExamSignUp/resources/ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="/ExamSignUp/resources/ext/ext-all.js"></script>
  		<script type='text/javascript' src='/ExamSignUp/dwr/engine.js'></script>
  		<script type='text/javascript' src='/ExamSignUp/dwr/util.js'></script>
  		<script type='text/javascript' src='/ExamSignUp/dwr/interface/StudentController.js'></script>
  		<script type='text/javascript' src='/ExamSignUp/dwr/interface/LanguageController.js'></script>
  		<script type='text/javascript' src='/ExamSignUp/dwr/interface/InstitutionController.js'></script>
		<script type="text/javascript" src="/ExamSignUp/resources/custom/DwrProxy.js"></script>
		<script type="text/javascript" src="/ExamSignUp/resources/js/SchoolPaperBags.js"></script>
		<style type="text/css">
.icon-grid {
	background-image: url(../resources/icons/grid.gif) !important;
}
.data{
	background-image: url(../resources/icons/data.gif) !important;
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
  			<form name ="exportSchoolPaperBags" id = "exportSchoolPaperBags" action = "" target = "export" method ="post" accept-charset="UTF-8"></form>
  			<iframe name="export" id="export"></iframe>
  		</div>
		<script language="javascript">
			Ext.onReady(function(){
				var institution ="<%=session.getAttribute("institution")%>";
				this.calcPaperBagsForExamCollege(institution);
			});
  		</script>
	</body>

</html>
