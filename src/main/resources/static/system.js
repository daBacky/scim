var systemsTable = null;
var selectedSystem = null;

$(document).ready(function () {

    systemsTable =  $('#systemsTable').DataTable({
        "columnDefs": [ {
            "targets": -1,
            "data": null,
            "defaultContent": "<button class='btn btn-default' id='btnEdit'>Edit</button>&nbsp; &nbsp; <button class='btn btn-default' id='btnDelete'>Delete</button>"
        } ]
    });

    $('#systemsTable tbody').on( 'click', 'button', function (event) {

        event.preventDefault();

        selectedSystem = systemsTable.row( $(this).parents('tr') ).data();
        var content = systemsTable.row( $(this).parents('tr') ).data();
        if(this.id === "btnDelete"){
            $('#confirmDelete')
                .modal({ backdrop: 'static', keyboard: false })
                .one('click', '#delete', function (e) {
                    //delete function
                    $.ajax({
                        type : 'DELETE',
                        url : "/admin/system/delete?id=" + content[0],
                        data : '',
                        success : function() {
                            refreshData();
                        }
                    });
                });

        }

        if(this.id === "btnEdit"){

            openModal(content);

        }

    });


    $.get("/admin/system/all", function (data) {
        for (var i = 0; i < data.length; i++) {
            systemsTable.row.add([data[i].id, data[i].name, data[i].description]).draw(false);
        }

    });



});

function openModal(item) {
    $("#systemName").val(item[1]);
    $("#systemDescription").val(item[2]);
    $("#addNewSystem").modal();

}


function saveSystem() {

    var system = [];
    if (selectedSystem != null) {
        system.id = selectedSystem[0];
        system.name = $("#systemName").val();
        system.description = $("#systemDescription").val();
    } else {
        system.id = 0;
        system.name = $("#systemName").val();
        system.description = $("systemDescription").val();
    }

    var systemAsJson = JSON.stringify({"id": system.id, "name": system.name, "description": system.description});
    $.ajax({
        type : 'PUT',
        contentType : 'application/json',
        url : "/admin/system/save",
        dataType : "json",
        data : systemAsJson,
        success : function() {
            refreshData();
            populateSystemDropdown();
            selectedSystem = null;
            $("#systemName").val("");
            $("#systemDescription").val("");
        },
        error : function(jqXHR, textStatus) {
            alert('Please make sure all fields are filled out. Error: ' + textStatus);
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

function refreshData() {
    $.get("/admin/system/all", function (data) {
        systemsTable.clear().draw(false);

        for (var i = 0; i < data.length; i++) {
            systemsTable.row.add([data[i].id, data[i].name, data[i].description]).draw(false);
        }
    });
}

