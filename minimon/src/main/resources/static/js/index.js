

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

	$('body').on('click' , '#createUrlBtn' , function(){
		
		$('#saveUrlModal').modal('show');
		
	});

	$('body').on('hide.bs.modal','#saveUrlModal', function (e) {
		
		$("#saveUrlForm").trigger('reset');
		
	});


	$('body').on('click' , '#createApiBtn' , function(){
		
		$('#saveApiModal').modal('show');
		
	});

	$('body').on('hide.bs.modal','#saveApiModal', function (e) {
		
		$("#saveApiForm").trigger('reset');
		
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
				data : { url : url, timeout : $("#saveUrlForm [name='timeout']").val()},
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
	


	
	$('body').on('click', '.apiEditBtn', function(){
		
		/* API 출력 */
		$.ajax({
			type : 'GET',
			url : '/api/'+$(this).attr('cd'),
			dataType : 'json',
			success : function(data) {
				var errorCode = data.errorCode;
				if(typeof errorCode != "undefined"){
					$('#errorCode').val(errorCode);
					$('#errorCreate').submit();
				}else{
					if(data.result == "success") { 

						$("#saveApiForm [name='seq']").val(data.data.seq);
						$("#saveApiForm [name='url']").val(data.data.url);
						$("#saveApiForm [name='title']").val(data.data.title);
						$("#saveApiForm [name='timeout']").val(data.data.timeout);
						$("#saveApiForm [name='timer']").val(data.data.timer);
						$("#saveApiForm [name='loadTimePer']").val(data.data.loadTimePer);
						$("#saveApiForm [name='payLoadPer']").val(data.data.payLoadPer);
						$("#saveApiForm [name='useable']").each(function(){
							if($(this).val() == data.data.useable) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});
						$("#saveApiForm [name='status']").val(data.data.status);
						$("#saveApiForm [name='loadTime']").val(data.data.loadTime);
						$("#saveApiForm [name='payLoad']").val(data.data.payLoad);
						$("#apiCheck").attr('cd', data.data.url);
						$('#saveApiModal').modal('show');
						
					}
				}
			}
		});
	});
	
	
	$('body').on('click', '#saveApi', function(){

		if($("#apiCheck").attr('cd') != $("#saveapiForm [name='url']").val()) {
			alert('API 검사를 진행해주세요.');
			return;
		}
		
		var method = 'POST';
		var url = '/api';
		if($("#saveApiForm [name='seq']").val() != '') {
			method = 'PUT';
			url = '/api/'+$("#saveApiForm [name='seq']").val();
		}
		
		$.ajax({
			type : method,
			url : url,
			data : $("#saveApiForm").serialize(),
			dataType : 'json',
			success : function(data) {
				var errorCode = data.errorCode;
				if(typeof errorCode != "undefined"){
					$('#errorCode').val(errorCode);
					$('#errorCreate').submit();
				}else{
					if(data.result == "success") { 

						$("#saveApiModal").hide();
						alert("저장 완료");
						window.location.reload();

					}
				}
			}
		});
	});

	$('body').on('click', '#deleteApi', function(){
		$.ajax({
			type : 'DELETE',
			url : '/api/'+$("#saveApiForm [name='seq']").val(),
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
	

	$('body').on('click', '#apiCheck', function(){
		
		var url = $("#saveApiForm [name='url']").val();
		if(url == '') {
			alert('URL을 입력해주세요');
		}
		else{
			$.ajax({
				type : 'POST',
				url : '/apiCheck',
				data : { url : url, timeout : $("#saveApiForm [name='timeout']").val()},
				dataType : 'json',
				success : function(data) {
					var errorCode = data.errorCode;
					if(typeof errorCode != "undefined"){
						$('#errorCode').val(errorCode);
						$('#errorCreate').submit();
					}else{
						if(data.result == "success") { 
							$("#saveApiForm [name='loadTime']").val(data.data.totalLoadTime);
							$("#saveApiForm [name='payLoad']").val(data.data.totalPayLoad);
							$("#saveApiForm [name='status']").val(data.data.status);
							alert('검사 완료');

						}
					}
				}
			});
		}
	});
	
	$('body').on('click', '.apiExecuteBtn', function(){
		
		$.ajax({
			type : 'GET',
			url : '/apiExecute/'+$(this).attr('cd'),
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

	
	$('body').on('click', '.add_api_param', function(){
		var api_param = 
			'<li class="list-group-item d-flex justify-content-between align-items-center">'
			+'Key <input type="text" class="form-control" name="param_key" value="">Value <input type="text" class="form-control" name="param_value" value="">'
			+'<a href="#" class="del_api_param badge badge-primary badge-pill">X</a></li>';
		$(".api_params").append(api_param);
	});

	
	$('body').on('click', '.del_api_param', function(){
		$(this).parent().remove();
	});
	
	
}