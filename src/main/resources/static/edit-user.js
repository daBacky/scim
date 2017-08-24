var selectedUser = null;

$(document).ready(function() {
	selectedUser = JSON.parse(localStorage.getItem("user"));	
	$("#firstname").val(selectedUser.firstName);
	$("#lastname").val(selectedUser.lastName);
	$("#email").val(selectedUser.email);
	$("#password").val(selectedUser.password);
	$("#role").val(selectedUser.role);
	$("#active").val(selectedUser.active.toString());
	$("#gender").val(selectedUser.gender);
	$("#birthday").val(selectedUser.birthday);	
});

function saveUser() {	
	selectedUser.firstName = $( "#firstname" ).val();
	selectedUser.lastName = $( "#lastname" ).val();
	selectedUser.email = $( "#email" ).val();
    var valid = validateEmail(selectedUser.email);
	selectedUser.password = $( "#password" ).val();
	selectedUser.gender = $( "#gender" ).val();
	selectedUser.birthday = $( "#birthday" ).val();
	var user = JSON.stringify(selectedUser);

    if(valid) {
        $.ajax({
            type: 'PUT',
            contentType: 'application/json',
            url: "/admin/user/save",
            dataType: "json",
            data: user,
            success: function (data) {
                if (data.errMsg == null || data.errMsg == 'undefined') {
                    $('#successMsg').html("Changes saved");
                    $('#successMsg').css('visibility', 'visible');
                    $('#errMsg').css('visibility', 'hidden');
                } else {
                    $('#errMsg').html(data.errMsg);
                    $('#errMsg').css('visibility', 'visible');
                    $('#loginMsg').css('visibility', 'hidden');
                }
            },
            error: function (jqXHR, textStatus) {
                alert('Please make sure all fields are filled out. Error: ' + textStatus);
            }
        });
    }else {
    	console.log("Email address is not valid");
	}

}

function validateEmail(email) {
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

