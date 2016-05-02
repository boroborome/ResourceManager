
function createTD(value) {
    var td = document.createElement("td");
    td.innerHTML = value;
    return td;
}

function deleteRow(event) {
    var tr = (event.toElement.parentNode.parentNode);
    var idea = tr.idea;
    var ideaID = idea.id;

    if (confirm("Are you sure to delete :" + idea.name +"(" + idea.id + ")?")!=true){
        return;
    }

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
function navigateToChildren(event) {
    var idea = event.toElement.parentNode.idea;
    var btn = document.createElement("input");
    btn.idea = idea;
    $(btn).attr('type', 'button')
        .attr('value', idea.name)
        .click(btnNavigaterClick);
    $('#navigater').append(btn);
    performNavigate(btn);
}
function createIdeaRecord(jsonData) {
    var tr = document.createElement("tr");
    tr.setAttribute('class', 'tblRow');
    tr.appendChild(createTD(jsonData.id));
    var tdName = createTD(jsonData.name);
    $(tdName).click(navigateToChildren);
    tr.appendChild(tdName);
    tr.appendChild(createTD(jsonData.remark));
    tr.appendChild(createTD(jsonData.important));
    tr.appendChild(createTD(jsonData.urgency));
    tr.appendChild(createActionTD(jsonData.id));
    tr.idea = jsonData;
    return tr;
}

function doAdd() {
    var idea = {};
    idea.parentId=$("#navigater")[0].currentParentId;
    if (idea.parentId == null) {
        idea.parentId = 0;
    }
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
function btnNavigaterClick(event) {
    var btn = event.toElement;
    performNavigate(btn);
}
function performNavigate(btn) {
    $(btn).nextAll().remove();

    var parentID=btn.idea != null ? btn.idea.id : 0;
    if (parentID == null) {
        parentID = 0;
    }
    $("#navigater")[0].currentParentId=parentID;

    $.get("/svc/idea/" + parentID + "?withChild=true",function(response,status){
        if (status != "success") {
            alert(status);
            return;
        }

        if (response.code != 0) {
            alert(response.message);
            $("#lblStatus").text(response.message);
            return;
        }

        var tblIdea = $("#tblIdea");
        $("#tblIdea tr.tblRow").remove();
        var parentIdea = response.data;
        for (var index = 0, len = parentIdea.children.length; index < len; ++index) {
            var ideaItem = createIdeaRecord(parentIdea.children[index]);
            tblIdea.append(ideaItem);
        }

        $("#lblStatus").text("Total Row:" + parentIdea.children.length);
    });
}
function initUI() {
    $("#devContent").hide();
    $("#btnRoot").click(btnNavigaterClick);
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

    performNavigate($("#btnRoot")[0]);
}

