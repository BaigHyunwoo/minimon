function monInit() {

    $('body').on('click', '#createUrlBtn', function () {

        $('#saveUrlModal').modal('show');

    });

    $('body').on('hide.bs.modal', '#saveUrlModal', function (e) {
        window.location.reload();
    });

    $('body').on('click', '#createApiBtn', function () {
        $('#saveApiModal').modal('show');
    });

    $('body').on('hide.bs.modal', '#saveApiModal', function (e) {
        window.location.reload();
    });

    $('body').on('click', '#createActBtn', function () {
        $('#saveActModal').modal('show');
    });

    $('body').on('hide.bs.modal', '#saveActModal', function (e) {
        window.location.reload();
    });

    $('body').on('click', '#resultReceivePathEditBtn', function () {
        $('#editResultReceivePathModal').modal('show');
    });

    $('body').on('hide.bs.modal', '#resultReceivePathEditBtn', function (e) {
        window.location.reload();
    });

    $('body').on('click', '#urlCheck', function () {
        let url = $("#saveUrlForm [name='url']").val();
        if (url == '') {
            alert('URL을 입력해주세요');
        } else {
            let body = {
                url: url,
                timeout: $("#saveUrlForm [name='timeout']").val()
            };

            $.ajax({
                type: 'POST',
                url: '/monUrl/check',
                data: JSON.stringify(body),
                contentType: "application/json",
                dataType: 'json',
                error: function(e){
                    alert(e.responseJSON.meta.message);
                },
                success: function (result) {
                    if (result.meta.code == 200) {
                        $("#saveUrlForm [name='loadTime']").val(result.data.totalLoadTime);
                        $("#saveUrlForm [name='status']").val(result.data.statusCode);
                        $("#urlCheck").attr('cd', url);
                        alert('검사 완료');
                    }
                }
            });
        }
    });

    $('body').on('click', '#deleteUrl', function () {
        if(!confirm("정말 삭제 하시겠습니까?")) {
            return false;
        }
        $.ajax({
            type: 'DELETE',
            url: '/monUrl/' + $("#saveUrlForm [name='seq']").val(),
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                if (result.meta.code == 200) {
                    alert('삭제 완료');
                    window.location.reload();
                }
            }
        });
    });

    $('body').on('click', '#saveUrl', function () {

        if ($("#urlCheck").attr('cd') != $("#saveUrlForm [name='url']").val()) {
            alert('검사를 진행해주세요.');
            return;
        }

        let method = 'POST';
        if ($("#saveUrlForm [name='seq']").val() != '') {
            method = 'PUT';
        }

        let body = {
            seq: $("#saveUrlForm [name='seq']").val(),
            url: $("#saveUrlForm [name='url']").val(),
            title: $("#saveUrlForm [name='title']").val(),
            timeout: $("#saveUrlForm [name='timeout']").val(),
            errorLoadTime: $("#saveUrlForm [name='errorLoadTime']").val(),
            monitoringUseYn: $("#saveUrlForm [name='monitoringUseYn']:checked").val(),
            loadTimeCheckYn: $("#saveUrlForm [name='loadTimeCheckYn']:checked").val(),
            status: $("#saveUrlForm [name='status']").val(),
            loadTime: $("#saveUrlForm [name='loadTime']").val(),
        }

        $.ajax({
            type: method,
            url: '/monUrl',
            data: JSON.stringify(body),
            dataType: 'json',
            contentType: "application/json",
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                if (result.meta.code == 200) {
                    alert("저장 완료");
                    $("#saveUrlModal").hide();
                    window.location.reload();
                }
            }
        });
    });

    $('body').on('click', '.urlExecuteBtn', function () {
        $.ajax({
            type: 'GET',
            url: '/monUrl/' + $(this).attr('cd')+'/execute',
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                if (result.meta.code == 200) {
                    alert("실행 완료");
                }
            }
        });
    });

    $('body').on('click', '.urlEditBtn', function () {
        $.ajax({
            type: 'GET',
            url: '/monUrl/' + $(this).attr('cd'),
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                if (result.meta.code == 200) {
                    let monUrl = result.data;
                    $("#saveUrlForm [name='seq']").val(monUrl.seq);
                    $("#saveUrlForm [name='url']").val(monUrl.url);
                    $("#saveUrlForm [name='title']").val(monUrl.title);
                    $("#saveUrlForm [name='errorLoadTime']").val(monUrl.errorLoadTime);
                    $("#saveUrlForm [name='timeout']").val(monUrl.timeout);
                    $("#saveUrlForm [name='monitoringUseYn']").each(function () {
                        if ($(this).val() == monUrl.monitoringUseYn) $(this).attr('checked', 'true');
                        else $(this).removeAttr('checked');
                    });
                    $("#saveUrlForm [name='status']").val(monUrl.status);
                    $("#saveUrlForm [name='loadTime']").val(monUrl.loadTime);
                    $("#saveUrlForm [name='loadTimeCheckYn']").each(function () {
                        if ($(this).val() == monUrl.loadTimeCheckYn) $(this).attr('checked', 'true');
                        else $(this).removeAttr('checked');
                    });
                    $("#urlCheck").attr('cd', monUrl.url);
                    $('#saveUrlModal').modal('show');
                }
            }
        });
    });

    $('body').on('click', '#apiCheck', function () {
        let url = $("#saveApiForm [name='url']").val();
        if (url == '') {
            alert('URL을 입력해주세요');
        } else {
            let body = {
                url: url,
                method: $("#saveApiForm [name='method']").val(),
                data: $("#saveApiForm [name='data']").val()
            };

            $.ajax({
                type: 'POST',
                url: '/monApi/check',
                data: JSON.stringify(body),
                dataType: 'json',
                contentType: "application/json",
                error: function(e){
                    alert(e.responseJSON.meta.message);
                },
                success: function (result) {
                    if (result.meta.code == 200) {
                        $("#saveApiForm [name='loadTime']").val(result.data.totalLoadTime);
                        $("#saveApiForm [name='status']").val(result.data.statusCode);
                        $("#api_response").text(result.data.response);
                        $("#saveApiForm [name='response']").val(result.data.response);
                        $("#apiCheck").attr('cd', url);
                        alert('검사 완료');
                    }
                }
            });
        }
    });


    $('body').on('click', '.apiEditBtn', function () {
        $.ajax({
            type: 'GET',
            url: '/monApi/' + $(this).attr('cd'),
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                let monApi = result.data;

                $("#apiCheck").attr('cd', monApi.url);
                $("#saveApiForm [name='seq']").val(monApi.seq);
                $("#saveApiForm [name='url']").val(monApi.url);
                $("#saveApiForm [name='title']").val(monApi.title);
                $("#saveApiForm [name='timeout']").val(monApi.timeout);
                $("#saveApiForm [name='errorLoadTime']").val(monApi.errorLoadTime);
                $("#saveApiForm [name='monitoringUseYn']").each(function () {
                    if ($(this).val() == monApi.monitoringUseYn) $(this).attr('checked', 'true');
                    else $(this).removeAttr('checked');
                });
                $("#saveApiForm [name='method']").val(monApi.method);
                $("#saveApiForm [name='status']").val(monApi.status);
                $("#saveApiForm [name='loadTimeCheckYn']").each(function () {
                    if ($(this).val() == monApi.loadTimeCheckYn) $(this).attr('checked', 'true');
                    else $(this).removeAttr('checked');
                });
                $("#saveApiForm [name='loadTime']").val(monApi.loadTime);

                $("#saveApiForm [name='responseCheckYn']").each(function () {
                    if ($(this).val() == monApi.responseCheckYn) $(this).attr('checked', 'true');
                    else $(this).removeAttr('checked');
                });

                $("#saveApiForm [name='data']").val(monApi.data);

                $("#api_response").text(monApi.response);
                $("#saveApiForm [name='response']").val(monApi.response);

                $('#saveApiModal').modal('show');
            }
        });
    });


    $('body').on('click', '#saveApi', function () {

        if ($("#apiCheck").attr('cd') != $("#saveApiForm [name='url']").val()) {
            alert('검사를 진행해주세요.');
            return;
        }

        let method = 'POST';
        if ($("#saveApiForm [name='seq']").val() != '') {
            method = 'PUT';
        }

        let body = {
            seq: $("#saveApiForm [name='seq']").val(),
            method: $("#saveApiForm [name='method']").val(),
            url: $("#saveApiForm [name='url']").val(),
            title: $("#saveApiForm [name='title']").val(),
            timeout: $("#saveApiForm [name='timeout']").val(),
            errorLoadTime: $("#saveApiForm [name='errorLoadTime']").val(),
            monitoringUseYn: $("#saveApiForm [name='monitoringUseYn']:checked").val(),
            loadTimeCheckYn: $("#saveApiForm [name='loadTimeCheckYn']:checked").val(),
            responseCheckYn: $("#saveApiForm [name='responseCheckYn']:checked").val(),
            data: $("#saveApiForm [name='data']").val(),
            status: $("#saveApiForm [name='status']").val(),
            loadTime: $("#saveApiForm [name='loadTime']").val(),
            response: $("#saveApiForm [name='response']").val(),
        }

        $.ajax({
            type: method,
            url: '/monApi',
            data: JSON.stringify(body),
            dataType: 'json',
            contentType: "application/json",
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                $("#saveApiModal").hide();
                alert("저장 완료");
                window.location.reload();
            }
        });
    });

    $('body').on('click', '#deleteApi', function () {
        $.ajax({
            type: 'DELETE',
            url: '/monApi/' + $("#saveApiForm [name='seq']").val(),
            dataType: 'json',
            contentType: "application/json",
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert('삭제 완료');
                window.location.reload();
            }
        });
    });

    $('body').on('click', '.apiExecuteBtn', function () {
        $.ajax({
            type: 'GET',
            url: '/monApi/' + $(this).attr('cd')+'/execute',
            dataType: 'json',
            contentType: "application/json",
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert("실행 완료");
            }
        });
    });

    $('body').on('click', '#actCheck', function () {
        let actFile = $("#saveActForm [name='actFile']").val();

        if (actFile == '') {
            alert('검사 파일을 첨부해주세요');
        } else {

            let file = $("#saveActForm [name=actFile]")[0].files[0];
            let formData = new FormData();
            formData.append("actFile", file);

            $.ajax({
                type: 'POST',
                url: '/monAct/check',
                data: formData,
                processData: false,
                contentType: false,
                enctype: 'multipart/form-data',
                error: function(e){
                    alert(e.responseJSON.meta.message);
                },
                success: function (result) {
                    let monAct = result.data;
                    let codeDataList = new Array();
                    $.each(monAct.response, function(key, value){
                        codeDataList[key] = value.monCodeData;
                    });

                    $("#saveActForm [name=status]").val(monAct.statusCode);
                    $("#saveActForm [name=loadTime]").val(monAct.totalLoadTime);
                    $("#saveActForm [name=codeDataList]").val(JSON.stringify(codeDataList));
                    $("#saveActForm [name=codeFileName]").val(actFile);
                    $("#actFile").attr('cd', actFile);
                    $("#actCheck").attr('cd', actFile);
                    alert('검사 완료');
                }
            });
        }
    });

    $('body').on('click', '.actExecuteBtn', function () {
        $.ajax({
            type: 'GET',
            url: '/monAct/' + $(this).attr('cd')+'/execute',
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert("실행 완료");
            }
        });
    });


    $('body').on('click', '#deleteAct', function () {
        $.ajax({
            type: 'DELETE',
            url: '/monAct/'+$("#saveActForm [name='seq']").val() ,
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert('삭제 완료');
                window.location.reload();
            }
        });
    });


    $('body').on('click', '#saveAct', function () {
        let method = 'POST';
        if ($("#saveActForm [name='seq']").val() != '') {
            method = 'PUT';
            if(!confirm("수정 시 파일을 새로 등록해야합니다. 변경 하시겠습니까?")) {
                return false;
            }
        }

        if ($("#actCheck").attr('cd') != $("#actFile").attr('cd')) {
            alert('검사 파일을 등록 후 검사를 진행해주세요.');
            return;
        }

        let body = {
            seq: $("#saveActForm [name='seq']").val(),
            method: $("#saveActForm [name='method']").val(),
            url: $("#saveActForm [name='url']").val(),
            title: $("#saveActForm [name='title']").val(),
            timeout: $("#saveActForm [name='timeout']").val(),
            errorLoadTime: $("#saveActForm [name='errorLoadTime']").val(),
            monitoringUseYn: $("#saveActForm [name='monitoringUseYn']:checked").val(),
            loadTimeCheckYn: $("#saveActForm [name='loadTimeCheckYn']:checked").val(),
            responseCheckYn: $("#saveActForm [name='responseCheckYn']:checked").val(),
            data: $("#saveActForm [name='data']").val(),
            status: $("#saveActForm [name='status']").val(),
            loadTime: $("#saveActForm [name='loadTime']").val(),
            response: $("#saveActForm [name='response']").val(),
            codeFileName: $("#saveActForm [name='codeFileName']").val(),
            codeDataList: $("#saveActForm [name='codeDataList']").val(),
        }

        $.ajax({
            type: method,
            url: '/monAct',
            data: JSON.stringify(body),
            dataType: 'json',
            contentType: "application/json",
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert("저장 완료");
                $("#saveActModal").hide();
                window.location.reload();
            }
        });
    });


    $('body').on('click', '.actEditBtn', function () {
        $.ajax({
            type: 'GET',
            url: '/monAct/' + $(this).attr('cd'),
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                let monAct = result.data;
                $("#saveActForm [name='seq']").val(monAct.seq);
                $("#saveActForm [name='title']").val(monAct.title);
                $("#saveActForm [name='timeout']").val(monAct.timeout);
                $("#saveActForm [name='errorLoadTime']").val(monAct.errorLoadTime);
                $("#saveActForm [name='monitoringUseYn']").each(function () {
                    if ($(this).val() == monAct.monitoringUseYn) $(this).attr('checked', 'true');
                    else $(this).removeAttr('checked');
                });

                $("#saveActForm [name='loadTimeCheckYn']").each(function () {
                    if ($(this).val() == monAct.loadTimeCheckYn) $(this).attr('checked', 'true');
                    else $(this).removeAttr('checked');
                });
                $("#saveActForm [name='status']").val(monAct.status);
                $("#saveActForm [name='loadTime']").val(monAct.loadTime);
                $("#saveActForm [name='codeDataList']").val(JSON.stringify(monAct.codeDataList));
                $("#saveActForm [name='codeFileName']").val(monAct.codeFileName);
                $('#saveActModal').modal('show');
            }
        });
    });


    $('body').on('click', '#runAllTasks', function () {
        $.ajax({
            type: 'POST',
            url: '/scheduler/run/all',
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert('전체 동작 처리 완료');
                window.location.reload();
            }
        });
    });


    $('body').on('click', '#stopAllTasks', function () {
        $.ajax({
            type: 'POST',
            url: '/scheduler/stop/all',
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert('전체 동작 중지 완료');
                window.location.reload();
            }
        });
    });


    $('body').on('click', '.executeTask', function () {
        $.ajax({
            type: 'POST',
            url: '/scheduler/execute?schedulerType='+$(this).attr('cd'),
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert('동작 실행 완료');
                window.location.reload();
            }
        });
    });


    $('body').on('click', '.runTask', function () {
        $.ajax({
            type: 'POST',
            url: '/scheduler/run?schedulerType='+$(this).attr('cd'),
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert('동작 실행 완료');
                window.location.reload();
            }
        });
    });


    $('body').on('click', '.stopTask', function () {
        $.ajax({
            type: 'POST',
            url: '/scheduler/stop?schedulerType='+$(this).attr('cd'),
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert('동작 중지 완료');
                window.location.reload();
            }
        });
    });


    $('body').on('click', '#resultReceivePathEdit', function () {
        let body = {
            "resultReceivePath" : $("#editResultReceivePathForm [name='resultReceivePath']").val()
        }

        $.ajax({
            type: 'PUT',
            url: '/commonConfig/path/result',
            data: JSON.stringify(body),
            dataType: 'json',
            contentType: "application/json",
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert('수정 완료');
                window.location.reload();
            }
        });
    });


    $('body').on('click', '#driverFileSave', function () {
        let driver = $("#driverUploadForm [name=driver]")[0].files[0];
        let formData = new FormData();
        formData.append("driver", driver);

        $.ajax({
            type: 'PUT',
            url: '/commonConfig/set/driver',
            data: formData,
            processData: false,
            contentType: false,
            enctype: 'multipart/form-data',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                alert('저장 완료');
                window.location.reload();
            }
        });
    });


    function getMonResultList(){
        $.ajax({
            type: 'GET',
            url: '/result?sortKey=regDate&sortType=DESC',
            dataType: 'json',
            error: function(e){
                alert(e.responseJSON.meta.message);
            },
            success: function (result) {
                $(".resultList > tr").hide();
                $(".resultList > tr").remove();
                $.each(result.data.content, function(i, monResult){
                    let resultHtml = $('<tr>\n' +
                        '                    <td>'+monResult.monitoringType+'</td>\n' +
                        '                    <td>'+monResult.title+'</td>\n' +
                        '                    <td>'+monResult.resultCode+'</td>\n' +
                        '                    <td>'+monResult.statusCode+'</td>\n' +
                        '                    <td>'+monResult.regDate+'</td>\n' +
                        '                    <td>\n' +
                        '                        <input cd="'+monResult.seq+'" class="btn btn-success .resultDetail default-btn" type="button" value="View"/>\n' +
                        '                    </td>\n' +
                        '                </tr>').hide();
                    $('.resultList').append($(resultHtml));
                    $(resultHtml).fadeIn("slow");
                });
            }
        });
    }

    function loadingNewResult() {
        getMonResultList();
        setTimeout(loadingNewResult, 5000);
    }

    loadingNewResult();
}