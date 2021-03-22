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
            let body = {url: url, timeout: $("#saveUrlForm [name='timeout']").val()};
            $.ajax({
                type: 'POST',
                url: '/monUrl/check',
                data: JSON.stringify(body),
                contentType: "application/json",
                dataType: 'json',
                success: function (result) {
                    if (result.meta.code == 200) {
                        $("#saveUrlForm [name='loadTime']").val(result.data.totalLoadTime);
                        $("#saveUrlForm [name='status']").val(result.data.statusCode);
                        $("#urlCheck").attr('cd', url);
                        alert('검사 완료');
                    }
                },
                error: function(e){
                    console.log(e);
                    alert(e.message);
                }
            });
        }
    });

    $('body').on('click', '#deleteUrl', function () {
        $.ajax({
            type: 'DELETE',
            url: '/url/' + $("#saveUrlForm [name='seq']").val(),
            dataType: 'json',
            success: function (data) {
                let errorCode = data.errorCode;
                if (typeof errorCode != "undefined") {
                    $('#errorCode').val(errorCode);
                    $('#errorCreate').submit();
                } else {
                    if (data.result == "success") {
                        alert('삭제 완료');
                        window.location.reload();
                    }
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
        let url = '/url';
        if ($("#saveUrlForm [name='seq']").val() != '') {
            method = 'PUT';
            url = '/url/' + $("#saveUrlForm [name='seq']").val();
        }

        $.ajax({
            type: method,
            url: url,
            data: $("#saveUrlForm").serialize(),
            dataType: 'json',
            success: function (data) {
                let errorCode = data.errorCode;
                if (typeof errorCode != "undefined") {
                    $('#errorCode').val(errorCode);
                    $('#errorCreate').submit();
                } else {
                    if (data.result == "success") {

                        $("#saveUrlModal").hide();
                        alert("저장 완료");
                        window.location.reload();

                    }
                }
            }
        });
    });


    $('body').on('click', '.urlEditBtn', function () {

        /* URL 출력 */
        $.ajax({
            type: 'GET',
            url: '/url/' + $(this).attr('cd'),
            dataType: 'json',
            success: function (data) {
                let errorCode = data.errorCode;
                if (typeof errorCode != "undefined") {
                    $('#errorCode').val(errorCode);
                    $('#errorCreate').submit();
                } else {
                    if (data.result == "success") {


                        $("#saveUrlForm [name='seq']").val(data.data.seq);
                        $("#saveUrlForm [name='url']").val(data.data.url);
                        $("#saveUrlForm [name='title']").val(data.data.title);
                        $("#saveUrlForm [name='timeout']").val(data.data.timeout);
                        $("#saveUrlForm [name='timer']").val(data.data.timer);
                        $("#saveUrlForm [name='url_start_date']").val(getFormatDate(new Date(data.data.startDate)));
                        $("#saveUrlForm [name='url_end_date']").val(getFormatDate(new Date(data.data.endDate)));
                        $("#saveUrlForm [name='url_start_hour']").val(data.data.startHour);
                        $("#saveUrlForm [name='url_end_hour']").val(data.data.endHour);
                        $("#saveUrlForm [name='errLoadTime']").val(data.data.errLoadTime);
                        $("#saveUrlForm [name='payLoadPer']").val(data.data.payLoadPer);
                        $("#saveUrlForm [name='useable']").each(function () {
                            if ($(this).val() == data.data.useable) $(this).attr('checked', 'true');
                            else $(this).removeAttr('checked');
                        });
                        $("#saveUrlForm [name='status']").val(data.data.status);
                        $("#saveUrlForm [name='loadTime']").val(data.data.loadTime);
                        $("#saveUrlForm [name='url_loadTimeCheck']").each(function () {
                            if ($(this).val() == data.data.loadTimeCheck) $(this).attr('checked', 'true');
                            else $(this).removeAttr('checked');
                        });
                        $("#saveUrlForm [name='textCheckValue']").val(data.data.textCheckValue);
                        $("#saveUrlForm [name='textCheck']").each(function () {
                            if ($(this).val() == data.data.textCheck) $(this).attr('checked', 'true');
                            else $(this).removeAttr('checked');
                        });
                        $("#saveUrlForm [name='payLoad']").val(data.data.payLoad);
                        $("#saveUrlForm [name='url_payLoadCheck']").each(function () {
                            if ($(this).val() == data.data.payLoadCheck) $(this).attr('checked', 'true');
                            else $(this).removeAttr('checked');
                        });
                        $("#urlCheck").attr('cd', data.data.url);
                        $('#saveUrlModal').modal('show');

                    }
                }
            }
        });
    });


    $('body').on('click', '.urlExecuteBtn', function () {

        $.ajax({
            type: 'GET',
            url: '/urlExecute/' + $(this).attr('cd'),
            dataType: 'json',
            success: function (data) {
                let errorCode = data.errorCode;
                if (typeof errorCode != "undefined") {
                    $('#errorCode').val(errorCode);
                    $('#errorCreate').submit();
                } else {
                    if (data.result == "success") {
                        alert("실행 완료");
                    }
                }
            }
        });
    });
}