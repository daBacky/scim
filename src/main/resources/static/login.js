function login() {	
	try {
		var system = "Identity System Manager";
	
		var email = $( "#email" ).val();
		var validEmail = validateEmail(email);
		var password = $( "#password" ).val();
		var validPassword = checkPassword(password);

		if(validEmail && validPassword) {
		$.ajax({
			type : 'GET',
			contentType : 'application/json',
			url : "/admin/user/login?email=" + email + "&password=" + password + "&system=" + system,
			success : function(data) {
				if (data.errMsg == null || data.errMsg == 'undefined') {
					localStorage.setItem("user", JSON.stringify(data));
					var role = lookupSystem(data, system);
					if (role == 'ADMIN') {
						window.location.replace("/admin/dashboard.html");
					} else if (role == 'USER') {
						window.location.replace("/admin/edit-user.html");
					} else {
						$('#errMsg').html("Something went wrong, please contact support");
						$('#errMsg').css('visibility', 'visible');
					}
				} else {
					$('#errMsg').html(data.errMsg);
					$('#errMsg').css('visibility', 'visible');
				}
			},
			error : function(jqXHR, textStatus) {
				alert('Please make sure all fields are filled out. Error: ' + textStatus);
			}
		});
        } else {
			console.log("E-mail is not valid");
		}
		
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

function validateEmail(email) {
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function checkPassword(str)
{
    // at least one number, one lowercase and one uppercase letter
    // at least six characters that are letters, numbers or the underscore
    var re = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])\w{6,}$/;
    return re.test(str);
}