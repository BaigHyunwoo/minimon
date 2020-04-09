

/**
 *  date 변환 
 *  
 */
function getFormatDate(date){ 
	var year = date.getFullYear();
	var month = (1 + date.getMonth());
	month = getTwoLengthDate(month);
	var day = date.getDate();
	day = getTwoLengthDate(day);
	var hour = date.getHours();
	hour = getTwoLengthDate(hour);
	var minute = date.getMinutes();
	minute = getTwoLengthDate(minute);
	var second = date.getSeconds();
	second = getTwoLengthDate(second);
	return year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second; 
}

/**
 *  
 *  자릿수 변환 
 */
function getTwoLengthDate(input){
	input = input >= 10 ? input : '0' + input;
	return input;
}

/**
 * ERROR CODE 출력
 *
 */
function errorSubmitAction(){
	var pe_errorCode = $('#errorCode').val();
	alert("ERROR : "+pe_errorCode," 관리자에게 문의하십시오.");
	return false;
}

function monInit(){

	/* URL 모달  on */
	$('body').on('click' , '#createUrlBtn' , function(){
		
		$('#saveUrlModal').modal('show');
		
	});

	/* URL 모달  off */
	$('body').on('hide.bs.modal','#saveUrlModal', function (e) {
		
		$("#saveUrlForm").trigger('reset');
		
	});
	
	$('body').on('click', '#urlCheck', function(){
		
		var url = $("#saveUrlForm [name='url']").val();
		if(url == '') {
			alert('URL을 입력해주세요');
		}
		else{
			$.ajax({
				type : 'POST',
				url : '/urlCheck',
				data : { url : url},
				dataType : 'json',
				success : function(data) {
					var errorCode = data.errorCode;
					if(typeof errorCode != "undefined"){
						$('#errorCode').val(errorCode);
						$('#errorCreate').submit();
					}else{
						if(data.result == "success") { 
							$("#saveUrlForm [name='loadTime']").val(data.data.totalLoadTime);
							$("#saveUrlForm [name='payLoad']").val(data.data.totalPayLoad);
							$("#saveUrlForm [name='status']").val(data.data.status);
							$("#urlCheck").attr('cd', url);
							alert('검사 완료');

						}
					}
				}
			});
		}
	});

	$('body').on('click', '#deleteUrl', function(){
		$.ajax({
			type : 'DELETE',
			url : '/url/'+$("#saveUrlForm [name='seq']").val(),
			dataType : 'json',
			success : function(data) {
				var errorCode = data.errorCode;
				if(typeof errorCode != "undefined"){
					$('#errorCode').val(errorCode);
					$('#errorCreate').submit();
				}else{
					if(data.result == "success") { 
						alert('삭제 완료');
						window.location.reload();
					}
				}
			}
		});
	});
	
	$('body').on('click', '#saveUrl', function(){

		if($("#urlCheck").attr('cd') != $("#saveUrlForm [name='url']").val()) {
			alert('URL 검사를 진행해주세요.');
			return;
		}
		
		var method = 'POST';
		var url = '/url';
		if($("#saveUrlForm [name='seq']").val() != '') {
			method = 'PUT';
			url = '/url/'+$("#saveUrlForm [name='seq']").val();
		}
		
		$.ajax({
			type : method,
			url : url,
			data : $("#saveUrlForm").serialize(),
			dataType : 'json',
			success : function(data) {
				var errorCode = data.errorCode;
				if(typeof errorCode != "undefined"){
					$('#errorCode').val(errorCode);
					$('#errorCreate').submit();
				}else{
					if(data.result == "success") { 

						$("#saveUrlModal").hide();
						alert("저장 완료");
						window.location.reload();

					}
				}
			}
		});
	});

	
	$('body').on('click', '.urlEditBtn', function(){
		
		/* URL 출력 */
		$.ajax({
			type : 'GET',
			url : '/url/'+$(this).attr('cd'),
			dataType : 'json',
			success : function(data) {
				var errorCode = data.errorCode;
				if(typeof errorCode != "undefined"){
					$('#errorCode').val(errorCode);
					$('#errorCreate').submit();
				}else{
					if(data.result == "success") { 

						$("#saveUrlForm [name='seq']").val(data.data.seq);
						$("#saveUrlForm [name='url']").val(data.data.url);
						$("#saveUrlForm [name='title']").val(data.data.title);
						$("#saveUrlForm [name='timeout']").val(data.data.timeout);
						$("#saveUrlForm [name='timer']").val(data.data.timer);
						$("#saveUrlForm [name='loadTimePer']").val(data.data.loadTimePer);
						$("#saveUrlForm [name='payLoadPer']").val(data.data.payLoadPer);
						$("#saveUrlForm [name='useable']").each(function(){
							if($(this).val() == data.data.useable) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});
						$("#saveUrlForm [name='status']").val(data.data.status);
						$("#saveUrlForm [name='loadTime']").val(data.data.loadTime);
						$("#saveUrlForm [name='payLoad']").val(data.data.payLoad);
						$("#urlCheck").attr('cd', data.data.url);
						$('#saveUrlModal').modal('show');
						
					}
				}
			}
		});
	});
	

	
	$('body').on('click', '.urlExecuteBtn', function(){
		
		$.ajax({
			type : 'GET',
			url : '/urlExecute/'+$(this).attr('cd'),
			dataType : 'json',
			success : function(data) {
				var errorCode = data.errorCode;
				if(typeof errorCode != "undefined"){
					$('#errorCode').val(errorCode);
					$('#errorCreate').submit();
				}else{
					if(data.result == "success") { 

						alert("실행 완료");
						
					}
				}
			}
		});
	});

	
	
}