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
            alert('URL 검사를 진행해주세요.');
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
        alert("준비중인 기능입니다.");
        return;

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
                        $("#saveApiForm [name='loadTime']").val(result.data.loadTime);
                        $("#saveApiForm [name='status']").val(result.data.status);
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
            alert('API 검사를 진행해주세요.');
            return;
        }

        let method = 'POST';
        if ($("#saveApiForm [name='seq']").val() != '') {
            method = 'PUT';
        }

        $.ajax({
            type: method,
            url: '/monApi',
            data: getApiSaveData(),
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
            alert('actFile를 첨부해주세요');
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

                    $("#actCheck").attr('cd', actFile);
                    $("#saveActForm [name=status]").val(monAct.statusCode);
                    $("#saveActForm [name=loadTime]").val(monAct.totalLoadTime);
                    $("#saveActForm [name=codeDataList]").val(JSON.stringify(codeDataList));
                    $("#saveActForm [name=actCode]").val(actFile);
                    $("#actFile").attr('cd', actFile);
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
        alert("준비중인 기능입니다.");
        return;

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
        alert("준비중인 기능입니다.");
        return;

        if ($("#actCheck").attr('cd') != $("#actFile").attr('cd')) {
            alert('Act 검사를 진행해주세요.');
            return;
        }

        let method = 'POST';
        if ($("#saveActForm [name='seq']").val() != '') {
            method = 'PUT';
        }

        $.ajax({
            type: method,
            url: '/monAct',
            data: $("#saveActForm").serialize(),
            dataType: 'json',
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
        alert("준비중인 기능입니다.");
        return;

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
                $('#saveActModal').modal('show');
            }
        });
    });
}