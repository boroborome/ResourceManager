
function createTD(value) {
    var td = document.createElement("td");
    td.innerHTML = value;
    return td;
}

function deleteRow(event) {
    var tr = (event.toElement.parentNode.parentNode);
    var tdID = tr.childNodes[0];
    var ideaID = tdID.innerHTML;

    $.ajax("/svc/idea/" + ideaID, {
        "async": true,
        "crossDomain": true,
        method : 'delete',
        success: function (data, status) {
            if (status != "success") {
                alert(status);
                return;
            }
            if (data.code != 0) {
                alert(data.message);
                $("#lblStatus").text(data.message);
                return;
            }

            $(tr).remove();
        }
    });

}
function updateRow() {
    var tr = (event.toElement.parentNode.parentNode);
    var idea = {};
    idea.id = tr.childNodes[0].innerHTML;
    idea.name = tr.childNodes[1].innerHTML;
    idea.remark = tr.childNodes[2].innerHTML;
    idea.important = tr.childNodes[3].innerHTML;
    idea.urgency = tr.childNodes[4].innerHTML;

    $('#txtID').val(idea.id);
    $('#txtName').val(idea.name);
    $('#txtRemark').val(idea.remark);
    $('#txtImportant').val(idea.important);
    $('#txtUrgency').val(idea.urgency);

    var $devContent = $('#devContent');
    $devContent[0].idea = idea;
    $devContent[0].tr = tr;
    $devContent.show();
}
function createActionTD(id) {
    var td = document.createElement("td");
    td.innerHTML = "<td><div class='btnDel'>Del</div> <div class='btnUpdate'>Update</div></td>";
    $(td).find('.btnDel').click(deleteRow);
    $(td).find('.btnUpdate').click(updateRow);
    return td;
}

function createIdeaRecord(jsonData) {
    var tr = document.createElement("tr");
    tr.setAttribute('class', 'tblRow');
    tr.appendChild(createTD(jsonData.id));
    tr.appendChild(createTD(jsonData.name));
    tr.appendChild(createTD(jsonData.remark));
    tr.appendChild(createTD(jsonData.important));
    tr.appendChild(createTD(jsonData.urgency));
    tr.appendChild(createActionTD(jsonData.id));
    return tr;
}

function doAdd() {
    var idea = {};
    idea.name = $("#txtName").val();
    idea.remark = $("#txtRemark").val();
    idea.important = $("#txtImportant").val();
    idea.urgency = $("#txtUrgency").val();
    $.ajax("/svc/idea", {
        "async": true,
        "crossDomain": true,
        method : 'post',
        data : JSON.stringify(idea),
        headers: {"Content-Type" : "application/json;charset=UTF-8"},
        success: function (data, status) {
            if (status != "success") {
                alert(status);
                return;
            }
            if (data.code != 0) {
                alert(data.message);
                $("#lblStatus").text(data.message);
                return;
            }

            var ideaItem = createIdeaRecord(data.data);
            $("#tblIdea").append(ideaItem);
            $("#lblStatus").text("Success in adding record:" + data.data.name);
            $("#devContent").hide();
        }
    });
}
function doUpdate() {
    var $devContent = $('#devContent');
    var idea = $devContent[0].idea;
    idea.name = $("#txtName").val();
    idea.remark = $("#txtRemark").val();
    idea.important = $("#txtImportant").val();
    idea.urgency = $("#txtUrgency").val();
    $.ajax("/svc/idea/" + idea.id, {
        "async": true,
        "crossDomain": true,
        method : 'put',
        data : JSON.stringify(idea),
        headers: {"Content-Type" : "application/json;charset=UTF-8"},
        success: function (data, status) {
            if (status != "success") {
                alert(status);
                return;
            }
            if (data.code != 0) {
                alert(data.message);
                $("#lblStatus").text(data.message);
                return;
            }

            var $devContent = $('#devContent');
            var tr = $devContent[0].tr;
            var idea = data.data;
            tr.childNodes[1].innerHTML = idea.name;
            tr.childNodes[2].innerHTML = idea.remark;
            tr.childNodes[3].innerHTML = idea.important;
            tr.childNodes[4].innerHTML = idea.urgency;
            $("#lblStatus").text("Success in updating record:" + data.data.name);
            $devContent[0].idea = null;
            $devContent[0].tr = null;
            $("#devContent").hide();
        }
    });
}
function initUI() {
    $("#devContent").hide();
    $("#btnRefresh").click(function(){
        $.get("/svc/idea",function(data,status){
            if (status != "success") {
                alert(status);
                return;
            }

            if (data.code != 0) {
                alert(data.message);
                $("#lblStatus").text(data.message);
                return;
            }

            var tblIdea = $("#tblIdea");
            $("#tblIdea tr.tblRow").remove();
            for (var index = 0, len = data.data.length; index < len; ++index) {
                var ideaItem = createIdeaRecord(data.data[index]);
                tblIdea.append(ideaItem);
            }

            $("#lblStatus").text("Total Row:" + data.data.length);
        });
    });
    $("#btnAdd").click(function () {
        $("#txtID").val('');
        $("#txtName").val('');
        $("#txtRemark").val('');
        $("#txtImportant").val('');
        $("#txtUrgency").val('');
        $("#devContent").show();
    });
    $('#btnCancel').click(function () {
        $("#devContent").hide();
    })
    $("#btnOK").click(function () {
        if ($("#txtID").val() == '') {
            doAdd();
        } else {
            doUpdate();
        }

    });
}

