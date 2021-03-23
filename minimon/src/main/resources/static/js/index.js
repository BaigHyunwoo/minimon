function monInit() {

    $('body').on('click', '#createUrlBtn', function () {

        $('#saveUrlModal').modal('show');

    });

    $('body').on('hide.bs.modal', '#saveUrlModal', function (e) {
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
}