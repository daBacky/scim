var selectedUserId = -1;
var authTable = null;
var selectedAuth = null;
var selectedUser = undefined;
var counter = null;

$(document).ready(function() {

    authTable = $('#authTable').DataTable({
        "columnDefs": [ {
            "targets": -1,
            "data": null,
            "defaultContent": "<button class='btn btn-default' id='btnEdit'>Edit</button>&nbsp; &nbsp; <button class='btn btn-default' id='btnDelete'>Delete</button>"
        } ]
    });

    $('#authTable tbody').on( 'click', 'button', function () {

        var content = authTable.row( $(this).parents('tr') ).data();

        if(this.id === "btnDelete"){
            $('#confirmDelete')
                .modal({ backdrop: 'static', keyboard: false })
                .one('click', '#delete', function (e) {
                    //delete function
                    removeAuth(content);
                });


        }

        if(this.id === "btnEdit") {
            selectedAuth = content;
            $("#authSystem").val(content[1]);
            $("#authRole").val(content[2]);
            $("#addAuthModal").modal();
        }

    });

	selectedUserId  = parseInt(localStorage.getItem("selectedUserId"));
    if(selectedUserId < 0)
    	return;
    else {
    	$.get("/admin/scim/v2", function(data) {

            $.each(data, function(i, item) {

                if(item){
                    if(item.internal){
                        for(var i=0; i<item.internal.length;i++){
                            var p = item.internal[i];
                            if(p){
                                if(p.id === selectedUserId){
                                    selectedUser = item;
                                }
                            }
                        }
                    }
                }
            });

            $("#firstname").val(selectedUser.name.givenName);
            $("#lastname").val(selectedUser.name.familyName);
            $("#password").val(selectedUser.password);
            $("#active").val(selectedUser.active.toString());
            $("#gender").val(selectedUser.internal[0].gender);


            if (selectedUser.internal != null && selectedUser.internal[0].birthday != null) {
                var birthday = new Date(selectedUser.internal[0].birthday).toISOString().slice(0, 10);
                $("#birthday").val(birthday);
            } else {
                $("#birthday").val('');
            }

            if (selectedUser.emails != null) {
                $("#email").val(selectedUser.emails[0].value);
            } else {
                $("#email").val('');
            }


            for (var i=0; i < selectedUser.authorizations.length; i++) {
                var item =  selectedUser.authorizations[i];
                if(item)
                    authTable.row.add([item.systemId, item.systemName, item.role ]).draw(false);
            }

        });
	}


    populateSystemDropdown();


});


function saveUser() {

    var email = $( "#email" ).val();
    var validEmail = validateEmail(email);
    var password = $( "#password" ).val();
    var validPassword = checkPassword(password);

    var scimId = '';
    var httpMethod;
    var user = null;

    scimId = selectedUser.id;
    httpMethod = "PATCH";

    selectedUser.name.givenName = $( "#firstname" ).val();
    selectedUser.name.familyName = $( "#lastname" ).val();

    var email = new Object();
    email.display = null;
    email.primary = true;
    email.work = "work";
    email.value = $( "#email" ).val();

    selectedUser.emails = [];
    selectedUser.emails.push(email);

    selectedUser.password = $( "#password" ).val();
    selectedUser.active = $( "#active" ).val();

    selectedUser.internal[0].gender = $( "#gender" ).val();
    selectedUser.internal[0].birthday = new Date($( "#birthday" ).val()).getTime();

    user = JSON.stringify(selectedUser);


    if(validPassword && validEmail){
    $.ajax({
        type : httpMethod,
        contentType : 'application/json',
        url : "/admin/scim/v2/" + scimId,
        dataType : "json",
        data : user,
        success : function() {
            window.location.href='/admin/dashboard.html';
        },
        error : function(jqXHR, textStatus) {
            alert('Please make sure all fields are filled out. Error: ' + textStatus);
        }
    });

    }
    else {
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
                    refreshAuthData();
                },
                error : function(jqXHR, textStatus) {
                    alert('Please make sure all fields are filled out. Error: ' + textStatus);
                }
            });

        }
    });
}


function removeAuth(systemId) {
    if (selectedUser.authorizations != null) {
        for (var i=0; i < selectedUser.authorizations.length; i++) {
            if (selectedUser.authorizations[i].systemId == systemId[0]) {
                selectedUser.authorizations.splice(i, 1);
            }
        }
    }

    $.ajax({
        type : 'PATCH',
        contentType : 'application/json',
        url : "/admin/scim/v2/" + selectedUser.id,
        dataType : "json",
        data : JSON.stringify(selectedUser),
        success : function() {
            refreshAuthData();
        },
        error : function(jqXHR, textStatus) {
            alert('Could not delete user. Error: ' + textStatus);
        }
    });
}


function populateSystemDropdown() {
    $.get("/admin/system/all", function(data) {
        $("#authSystem").empty();
        for(var i = 0; i < data.length; i++) {
            $("#authSystem").append(
                $('<option></option>')
                    .val(data[i].name)
                    .html(data[i].name));
        }
    });
}


function refreshAuthData(){
    authTable.clear().draw(false);


            for (var i=0; i < selectedUser.authorizations.length; i++) {
                var auth = selectedUser.authorizations[i];
                authTable.row.add([auth.systemId, auth.systemName, auth.role ]).draw(false);
            }

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
