var PASSWORD_LENGTH = 6;

$(document).ready(function() {
	$.ajax({
		url : "user/my-profile",
		type : "GET",
		cache:false,
		contentType: "application/json;charset=UTF-8",
		success : function(data) {
			$('#userId').val(data.id);
			$('#userName').val(data.userName);
			$('#realName').val(data.realName);
			$('#mobile').val(data.mobile);
			$('#email').val(data.email);
		},
		error : function(data) {
			infoTip({
				content : data.message,
				color : "#FF0000"
			});
		}
	});
	$('#modifyUserButton').on('click', function (event) {
		var button = $(event.relatedTarget); // Button that triggered the modal
		var user = {};
		var tel=/^(13[0-9]|15[0|3|6|7|8|9]|18[0-9])\d{8}$/;
		var reg=/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
		user.id = $('#userId').val();
		user.realName = $('#realName').val();
		user.mobile = $('#mobile').val();
		user.email = $('#email').val();
		if(user.mobile!=""&&!tel.test(user.mobile)){
			$("#mobile").focus();
			infoTip({
				content : "手机号码格式不正确",
				color : "#D58512"
			});
		}else if(user.email!=""&&!reg.test(user.email)){
			$("#email").focus();
			infoTip({
				content : "电子邮箱格式不正确",
				color : "#D58512"
			});
		}else{
			$.ajax({
				url : "user/save-my-profile",
				type : "PUT",
				dataType : "json",
				contentType: "application/json;charset=UTF-8",
				data:JSON.stringify(user),
				success : function(data) {
					infoTip({
						content : data.message,
						color : "#D58512"
					});
				},
				error : function(data) {
					infoTip({
						content : data.message,
						color : "#FF0000"
					});
				}
			});
		}
	});
	$('#setPasswordModal').on('show.bs.modal', function (event) {
		  var button = $(event.relatedTarget); // Button that triggered the modal
		  var modal = $(this);
		  modal.find('.modal-title').text("修改密码");
	});
	
	$('#setPasswordButton').on('click', function (event) {
		var button = $(event.relatedTarget); // Button that triggered the modal
		var dataChangePassword = {};
		dataChangePassword.oldPassword = $('#inputOldPassword').val();
		dataChangePassword.newPassword = $('#inputNewPassword').val();
		dataChangePassword.newPasswordAgain = $('#inputNewPasswordAgain').val();
		var newPassword = dataChangePassword.newPassword;
		var newPasswordAgain = dataChangePassword.newPasswordAgain;
		
		if(!dataChangePassword.oldPassword){
			infoTip({
				content : "没有输入旧密码",
				color : "#FF0000"
			});
			return;
		}

		if(!newPassword){
			infoTip({
				content : "没有输入新密码",
				color : "#FF0000"
			});
			return;
		}
		
		if(!newPasswordAgain){
			infoTip({
				content : "没有输入确认密码",
				color : "#FF0000"
			});
			return;
		}


		if(newPassword!==newPasswordAgain){
			infoTip({
				content : "确认密码与新密码不一致",
				color : "#FF0000"
			});
			return;
		}

		if(newPassword.length < PASSWORD_LENGTH){
			infoTip({
				content : "新密码长度小于 "+PASSWORD_LENGTH,
				color : "#FF0000"
			});
			return;
		}
		
		if(newPassword==dataChangePassword.oldPassword){
			infoTip({
				content : "新密码与旧密码不能相同",
				color : "#FF0000"
			});
			return;
		}
		
		$.ajax({
			cache:false,
			url : "user/reset-my-password",
			type : "POST",
			dataType : "json",
			contentType: "application/json;charset=UTF-8",
			data:JSON.stringify(dataChangePassword),
			success : function(data) {
				if(data.resultCode == 0){
					infoTip({
						content : data.message,
						color : "#D58512"
					});
					$("#setPasswordModal").modal("hide");//隐藏模态框
					window.location.href = "logout";
				}else if(data.resultCode == -1){
					infoTip({
						content : data.message,
						color : "#FF0000"
					});
				}
				
				
			},
			error : function(data) {
				infoTip({
					content : data.message,
					color : "#FF0000"
				});
			}
		});
	});
});