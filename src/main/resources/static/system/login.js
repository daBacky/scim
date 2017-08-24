function login(system) {
			try {	
				var email = $( "#email" ).val();
				var password = $( "#password" ).val();
				
				$.ajax({
					type : 'GET',
					contentType : 'application/json',
					url : "/admin/user/login?email=" + email + "&password=" + password + "&system=" + system,
					success : function(data) {
						if (data.errMsg == null || data.errMsg == 'undefined') {
							var role = lookupSystem(data, system);
							$('#loginMsg').html("Logged in with role: " + role);
							$('#loginMsg').css('visibility', 'visible');
							$('#errMsg').css('visibility', 'hidden');
						} else {
							$('#errMsg').html(data.errMsg);
							$('#errMsg').css('visibility', 'visible');
							$('#loginMsg').css('visibility', 'hidden');
						}
					},
					error : function(jqXHR, textStatus) {
						alert('Please make sure all fields are filled out. Error: ' + textStatus);
					}
				});
			
			} catch(err) {
				console.log(err);
			}	
		}

function lookupSystem(user, systemName) {
	for (var i=0; i < user.userAuthorizations.length; i++) {
		if (user.userAuthorizations[i].system.name == systemName) {
			return user.userAuthorizations[i].role;
		}
	}
	return null;
}
