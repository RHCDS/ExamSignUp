function login() {
		if (document.getElementById('studentID').value == "") {
			document.getElementById("msg").innerHTML = "<font color=red>学号不能为空</font>";
			return;
		}
		if (document.getElementById('IDnum').value == "") {
			document.getElementById("msg").innerHTML = "<font color=red>身份证号不能为空</font>";
			return;
		}
		if (!idNumCheck(document.getElementById('IDnum').value)) {
			document.getElementById("msg").innerHTML = "<font color=red>身份证号码不合法</font>";
			return;
		}
		document.getElementById("loadingImage").style.display = "block";
		Ext.Ajax.request({
					url : 'StudentLoginWithoutSchoolNum.do',
					method : 'post',
					waitMsg : '正在提交，请等待……',
					params : {
						studentID : document.getElementById('studentID').value,
						IDnum : document.getElementById('IDnum').value
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
}

function init() {
	document.getElementById('studentID').focus();
}
Ext.onReady(function() {
	init();
});