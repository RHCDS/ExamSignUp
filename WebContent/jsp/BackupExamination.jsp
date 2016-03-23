<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/ExamSignUp/resources/ext/resources/css/ext-all.css" />
        <script type="text/javascript" src="/ExamSignUp/resources/ext/adapter/ext/ext-base.js"></script>
        <script type="text/javascript" src="/ExamSignUp/resources/ext/ext-all.js"></script>
        <script type='text/javascript' src='/ExamSignUp/dwr/engine.js'></script>
        <script type='text/javascript' src='/ExamSignUp/dwr/util.js'></script>
        <script type="text/javascript" src="/ExamSignUp/resources/custom/DwrProxy.js"></script>
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
    <script> 
    function closeTab() {
        var temp = window.top.tabPanel.getActiveTab( );
        window.top.tabPanel.remove(temp);
    }

    function exportExaminationDataXls(confirm){
        if(confirm == "yes") {
            var f = document.getElementById('exportExamination');
            f.action = '../exportExaminationExcel.do';
            f.submit();
        } else {
            closeTab();
        }   
    }
    function backUpExaminationInfo() {
        Ext.MessageBox.confirm('提示',' 将会导出教室信息，监考人员信息，考试场次信息，理论/上级考场编排信息和理论/上级老师编排信息，是否执行?',exportExaminationDataXls);
    }
    </script>
    </head>
    <body>
        <div style = "display:none">
        <form name ="exportExamination" id = "exportExamination" action = "" target = "export" method ="post" accept-charset="UTF-8"></form>
        <iframe name="export" id="export"></iframe>
        </div>
        <script type="text/javascript">
            Ext.onReady(function(){
                this.backUpExaminationInfo();
            });
        </script>
    </body>

</html>
