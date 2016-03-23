var choseResult;
function choseRorl(argu) {
	switch (argu) {
	case "studentTable":            
		document.getElementById("studentTable").style.display = "block";
		document.getElementById("managerTable").style.display = "none";
		document.getElementById("studentTab").className = "choseRole";
		document.getElementById("managerTab").className = "noChoseRole";
		document.getElementById("studentTabAbove").className = "onlyTopCell";
		document.getElementById("managerTabAbove").className = "noAllCell";
		break;
	case "managerTable":
		document.getElementById("studentTable").style.display = "none";
		document.getElementById("managerTable").style.display = "block";
		document.getElementById("studentTab").className = "noChoseRole";
		document.getElementById("managerTab").className = "choseRole";
		document.getElementById("studentTabAbove").className = "noAllCell";
		document.getElementById("managerTabAbove").className = "onlyTopCell";
		break;
	}
}
function removchoseRorleOptions(selectObj) {
	if (typeof selectObj != 'object') {
		selectObj = document.getElementById(selectObj);
	}
	var len = selectObj.options.length;
	for ( var i = 0; i < len; i++) {
		selectObj.options[0] = null;
	}
}
function setSelectsOption(selectObj, optionList) {
	selectObj = document.getElementById(selectObj);
	removchoseRorleOptions(selectObj);
	var start = 0;
	selectObj.options[0] = new Option('—————请选择—————', '0');
	start++;
	var len = optionList.length;
	for ( var i = 0; i < len; i++) {
		selectObj.options[start] = new Option(optionList[i][0]+':'+optionList[i][1], optionList[i][0]);
		start++;
	}
	selectObj.options[0].selected = true;

}
function login() {
	var loadMarsk = new Ext.LoadMask(document.getElementById("loginTable"), {
		msg : "正在登录......",
		removeMask : true
	});
	// loadMarsk.show();
	if (document.getElementById("studentTable").style.display == "block") {
		if (document.getElementById('studentID').value == "") {
			document.getElementById("msg").innerHTML = "<font color=red>学号不能为空</font>";
			return;
		}
		if (document.getElementById('IDnum').value == "") {
			document.getElementById("msg").innerHTML = "<font color=red>身份证号不能为空</font>";
			return;
		}
		if (document.getElementById('school').value == 0) {
			document.getElementById("msg").innerHTML = "<font color=red>请先选择所在学校</font>";
			return;
		}
		if (!idNumCheck(document.getElementById('IDnum').value)) {
			document.getElementById("msg").innerHTML = "<font color=red>身份证号码不合法</font>";
			return;
		}
		document.getElementById("loadingImage").style.display = "block";
		Ext.Ajax
				.request({
					url : 'StudentLogin.do',
					method : 'post',
					waitMsg : '正在提交，请等待……',
					params : {
						studentID : document.getElementById('studentID').value,
						IDnum : document.getElementById('IDnum').value,
						school : document.getElementById('school').value
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
							Ext.Msg.alert('提示','原因如下：'+ jsonData.errors.info);
						}

					},
					failure : function(response, options) {
						document.getElementById("loadingImage").style.display = "none";
						Ext.Msg.alert('提示','原因如下：'+ Ext.util.JSON.decode(response.responseText).errors.info);
					}
				});
	} else if (document.getElementById("managerTable").style.display == "block") {
		if (document.getElementById('managerName').value == "") {
			document.getElementById("msg").innerHTML = "<font color=red>用户名不能为空</font>";
			return;
		}
		if (document.getElementById('managerPassword').value == "") {
			document.getElementById("msg").innerHTML = "<font color=red>密码不能为空</font>";
			return;
		}
		if (document.getElementById('institution').value == 0) {
			document.getElementById("msg").innerHTML = "<font color=red>请先选择所在机构</font>";
			return;
		}
		document.getElementById("loadingImage").style.display = "block";
		Ext.Ajax.request({
					url : 'ManagerLogin.do',
					method : 'post',
					waitMsg : '正在提交，请等待……',
					params : {
						managerName : document.getElementById('managerName').value,
						managerPassword : hex_md5(document
								.getElementById('managerPassword').value),
						institution : document.getElementById('institution').value
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
							Ext.Msg.alert('提示', '原因如下：' + jsonData.errors.info);
						}

					},
					failure : function(response, options) {
						document.getElementById("loadingImage").style.display = "none";
						Ext.Msg.alert('提示','原因如下：'+ Ext.util.JSON.decode(response.responseText).errors.info);
					}
				});
	} else
		alert("请选择学生或教务标签");
	// loadMarsk.hide();
}
function init() {
	document.getElementById("studentTable").style.display = "block";
	document.getElementById("managerTable").style.display = "none";
	document.getElementById("studentTab").className = "choseRole";
	document.getElementById("managerTab").className = "noChoseRole";
	document.getElementById("studentTabAbove").className = "onlyTopCell";
	document.getElementById("managerTabAbove").className = "noAllCell";
	InstitutionController.getSchoolNumName(function(data) {
		setSelectsOption("school", data);
	});
	InstitutionController.getInstitutionNumName(function(data) {
		setSelectsOption("institution", data);
	});
}
Ext.onReady(function() {
	init();
});