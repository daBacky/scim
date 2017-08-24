var userTable = null;
var authTable = null;
var systemsTable = null;
var users = null;
var selectedUser = null;
var selectedAuth = null;
var selectedSystem = null;

$(document).ready(function() {
	userTable = $('#userTable').DataTable({
        "columnDefs": [
            { "width": "5%", "targets": 0 },
            { "width": "12%", "targets": 1 },
            { "width": "12%", "targets": 2 },
            { "width": "20%", "targets": 3 },
            { "width": "10%", "targets": 4 },
            { "width": "10%", "targets": 5 },
            { "width": "10%", "targets": 6 },
            { "width": "20%", "targets": 7 },
        	{
            "targets": -1,
            "data": null,
            "defaultContent": "<button class='btn btn-default' id='btnEdit'>Edit</button>&nbsp; <button class='btn btn-default' id='btnDelete'>Delete</button>"
        } ]
	});

    $('#userTable tbody').on( 'click', 'button', function () {
        var content = userTable.row( $(this).parents('tr') ).data();
        var selectedId = content[0];
        if(this.id === "btnEdit"){
            localStorage.setItem('selectedUserId', content[0]);
            location.href = "user-detail.html";
        }
        if(this.id === "btnDelete"){
        	var user = getUser(selectedId);
            $('#confirmDelete')
                .modal({ backdrop: 'static', keyboard: false })
                .one('click', '#delete', function () {
                    //delete function
                    $.ajax({
                        type : 'DELETE',
                        url : "/admin/scim/v2/" + user.id,
                        data : '',
                        success : function() {
                            refreshData();
                        },
                        error : function(jqXHR, textStatus) {
                            alert('Could not delete user. Error: ' + textStatus);
                        }
                    });
                });
		}
    } );

	systemsTable = $('#systemsTable').DataTable();
	authTable = $('#authTable').DataTable();
	refreshData();

});


function refreshData() {
	$.get("/admin/scim/v2", function(data) {
		userTable.clear().draw(false);
		authTable.clear().draw(false);
		$("#remove").attr("disabled", true);
		$("#edit").attr("disabled", true);
		$("#addAuth").attr("disabled", true);
		setAuthButtonsState(true);		
		
		users = data;
		
		$.each(data, function(i, item) {
			var active = '';
			if (item.active == 1) {
				active = 'Yes';
			} else {
				active = 'No';
			}
			
			var email = '';			
			if (item.emails != null) {
				email = item.emails[0].value;
			}
			
			var role = '';			
			if (item.roles != null) {
				role = item.roles[0].value;
			}
			
			var created = '';			
			if (item.meta.created != null) {
				created = new Date(item.meta.created).toLocaleString();
			}
			
			userTable.row.add([ item.internal[0].id, item.name.givenName, item.name.familyName, email, role, created, active]).draw(false);
		});
	});
}

function saveUser() {

	var email = $( "#email" ).val();
	var validEmail = validateEmail(email);
	var password = $( "#password" ).val();
	var validPassword = checkPassword(password);

    var user = null;
	var scimId = '';
	var httpMethod;

	httpMethod = "POST";
		
		user = '{"schemas": ["urn:ch.fhnw.scim.model.User"],'
		    +'"userName": "' + $( "#email" ).val() + '",'
		    +'"name": {'
		    +	'"familyName": "' + $( "#lastname" ).val() + '",'
		    +	'"givenName": "' + $( "#firstname" ).val() + '"'
		    +'},'
		    +'"active": ' + $( "#active" ).val() + ','
		    +'"password": "' + $( "#password" ).val() + '",'
		    +'"emails": ['
		    +	'{'
		    +		'"value": "' + $( "#email" ).val() + '",'
		    +		'"display": null,'
		    +		'"type": "work",'
		    +		'"primary": true'
		    +	'}'
		    +'],'
		    +'"roles": ['
		    +	'{'
		    +		'"value": "' + $( "#role" ).val() + '",'
		    +		'"display": null,'
		    +		'"type": null,'
		    +		'"primary": true'
		    +	'}'
		    +'],'
		    +'"internal": ['
		    +	'{'
		    +		'"id": 0,'
		    +		'"gender": "' + $( "#gender" ).val() + '",'
		    +		'"birthday": ' + new Date($( "#birthday" ).val()).getTime()
		    +	'}'
		    +']}';			

	if(validPassword && validEmail){

	$.ajax({
		type : httpMethod,
		contentType : 'application/json',
		url : "/admin/scim/v2/" + scimId,
		dataType : "json",
		data : user,
		success : function() {
			refreshData();
		},
		error : function(jqXHR, textStatus) {
			alert('Please make sure all fields are filled out. Error: ' + textStatus);
		}
	});
    }else {
		alert("Please check email or password. Password must at least contains six character and have one number, one lowercase and one uppercase letter.")
	}

}

function saveAuth() {
	$.ajax({
		type : 'GET',
		url : "/admin/system/get?name=" + $( "#authSystem" ).val(),
		success : function(data) {
			if(selectedAuth != null && selectedAuth[0] != undefined) {
				for (var i=0; i < selectedUser.authorizations.length; i++) {
					if (selectedUser.authorizations[i].systemName == selectedAuth[1]) {
						selectedUser.authorizations[i].systemId = data.id;
						selectedUser.authorizations[i].systemName = data.name;
						selectedUser.authorizations[i].role = $( "#authRole" ).val();									
					}
				}
			} else {
				var auth = new Object();
				auth.id = 0;
				auth.systemId = data.id;
				auth.systemName = data.name;
				auth.role = $( "#authRole" ).val();
				auth.user_id = selectedUser.internal[0].id;
				
				if (selectedUser.authorizations == null) {
					selectedUser.authorizations = [];
				}
				
				selectedUser.authorizations.push(auth);
				user = JSON.stringify(selectedUser);				
			}
						
			$.ajax({
				type : 'PATCH',
				contentType : 'application/json',
				url : "/admin/scim/v2/" + selectedUser.id,
				dataType : "json",
				data : JSON.stringify(selectedUser),
				success : function(data) {
					selectedUser = data;
					updateUserAuth(data);
					refreshAuthData();
				},
				error : function(jqXHR, textStatus) {
					alert('Please make sure all fields are filled out. Error: ' + textStatus);
				}
			});						
					
		}			
	});
}	


function checkPassword(str)
{
    // at least one number, one lowercase and one uppercase letter
    // at least six characters that are letters, numbers or the underscore
    var re = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])\w{6,}$/;
    return re.test(str);
}

function validateEmail(email) {
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}