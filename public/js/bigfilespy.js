

function btnGoClick() {
    var request = {
        "path": $("#txtPath").val()
    }
    $.ajax("/svc/bigfilespy/status/start", {
        "async": true,
        "crossDomain": true,
        method : 'post',
        data : JSON.stringify(request),
        headers: {"Content-Type" : "application/json;charset=UTF-8"},
        success: function (data, status) {
            if (status != "success") {
                alert(status);
                return;
            }
            if (data.code != 0) {
                alert(data.message);
                return;
            }
            var status = data.data;
            $("#btnGo").prop('disabled', status.started);

            $("#navigater li").remove();
            $("#navigater").activeFileInfo = null;
            $("#tableFiles tbody tr").remove();
            // appendNavigateItem(status);
        }
    });

}
function appendNavigateItem(fileInfo, readableName) {
    var li = document.createElement("li");
    li.custom_value=fileInfo;
    li.innerHTML = readableName == null ? fileInfo.fname : readableName;
    $(li).click(navigateClick);
    $('#navigater').append(li);
}
function navigateToChildren(event) {
    var fileInfo = event.toElement.parentNode.value;
    appendNavigateItem(fileInfo, getFileName(fileInfo.fname));
    $("#navigater").activeFileInfo = fileInfo;
    refreshDirectory(fileInfo);
}

function navigateClick() {
    var btn = event.toElement;
    $(btn).nextAll().remove();
    var fileInfo = btn.custom_value;
    $("#navigater").activeFileInfo = fileInfo;
    refreshDirectory(fileInfo);
}
function btnRefreshClick() {
    var activeFileInfo = $("#navigater").activeFileInfo;
    if (activeFileInfo == null) {
        $.get("/svc/bigfilespy",
            function(response,status){
                if (status != "success") {
                    alert(status);
                    return;
                }

                if (response.code != 0) {
                    alert(response.message);
                    return;
                }

                var rootFileInfo = response.data;
                $("#navigater li").remove();
                $("#navigater").activeFileInfo = rootFileInfo;
                appendNavigateItem(rootFileInfo);
                refreshDirectory(rootFileInfo);
            }
        );
    } else {
        refreshDirectory(activeFileInfo);
    }
}

function refreshDirectory(fileInfo) {

    var request = {
        "fid": fileInfo.fid
    }
    $.ajax("/svc/bigfilespy", {
        "async": true,
        "crossDomain": true,
        method : 'post',
        data : JSON.stringify(request),
        headers: {"Content-Type" : "application/json;charset=UTF-8"},
        success: function (response, status) {
            if (status != "success") {
                alert(status);
                return;
            }
            if (response.code != 0) {
                alert(response.message);
                return;
            }

            $("#tableFiles tbody tr").remove();
            var tblFiles = $("#tableFiles");
            var fileInformations = response.data;
            for (var index = 0, len = fileInformations.length; index < len; ++index) {
                var fileItem = createFileRecord(fileInformations[index]);
                tblFiles.append(fileItem);
            }
        }
    });
}

function createTD(value) {
    var td = document.createElement("td");
    td.innerHTML = value;
    return td;
}
function getFileName(fullFileName) {
    var pos = fullFileName.lastIndexOf("/");
    return fullFileName.substring(pos + 1, fullFileName.length);
}
function getReadableSize(size) {
    var unit = ['B', 'K', 'M', 'G', 'T'];
    for (var curValue = size, index = 0; curValue > 1024; ++index, curValue/=1024);
    return curValue.toFixed(2) + unit[index];
}
function createFileTypeTD(value) {
    var icons = ["glyphicon glyphicon-file", "glyphicon glyphicon-folder-open"];
    var td = document.createElement("td");
    td.setAttribute('class', icons[value]);
    return td;
}
function createFileRecord(jsonData) {
    var tr = document.createElement("tr");
    // tr.setAttribute('class', 'tblRow');
    tr.appendChild(createFileTypeTD(jsonData.ftype));
    var tdName = createTD(getFileName(jsonData.fname));
    $(tdName).click(navigateToChildren);
    tr.appendChild(tdName);
    tr.appendChild(createTD(getReadableSize(jsonData.fsize)));
    tr.appendChild(createActionTD(jsonData.fid));
    tr.value = jsonData;
    return tr;
}
function createActionTD(id) {
    var td = document.createElement("td");
    td.innerHTML = "<td><div class='btnDel'>Del</div></td>";
    $(td).find('.btnDel').click(deleteRow);
    return td;
}

function deleteRow() {
    
}
function refreshStatus() {
    $.get("/svc/bigfilespy/status",
        function(response,status){
            if (status != "success") {
                alert(status);
                return;
            }

            if (response.code != 0) {
                alert(response.message);
                return;
            }

            var status = response.data;
            $("#btnGo").prop('disabled', status.started);
        }
    );
}

function initUI() {
    $("#progressBar").hide();
    $("#btnGo").click(btnGoClick);
    $("#btnRefresh").click(btnRefreshClick);

    refreshStatus();
  }
