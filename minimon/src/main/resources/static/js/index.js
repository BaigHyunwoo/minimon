

/**
 *  date 변환 
 *  
 */
function getFormatDateTime(date){
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
function getFormatDate(date){
	var year = date.getFullYear();
	var month = (1 + date.getMonth());
	month = getTwoLengthDate(month);
	var day = date.getDate();
	day = getTwoLengthDate(day);
	return year + '-' + month + '-' + day ;
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
		
		window.location.reload();
	});


	$('body').on('click' , '#createApiBtn' , function(){

		$('#saveApiModal').modal('show');

	});

	$('body').on('hide.bs.modal','#saveApiModal', function (e) {

		window.location.reload();
		
	});


	$('body').on('click' , '#createTransactionBtn' , function(){
		
		$('#saveTransactionModal').modal('show');
		
	});

	$('body').on('hide.bs.modal','#saveTransactionModal', function (e) {

		window.location.reload();
		
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
						$("#saveUrlForm [name='url_start_date']").val(getFormatDate(new Date(data.data.startDate)));
						$("#saveUrlForm [name='url_end_date']").val(getFormatDate(new Date(data.data.endDate)));
						$("#saveUrlForm [name='url_start_hour']").val(data.data.startHour);
						$("#saveUrlForm [name='url_end_hour']").val(data.data.endHour);
						$("#saveUrlForm [name='errLoadTime']").val(data.data.errLoadTime);
						$("#saveUrlForm [name='payLoadPer']").val(data.data.payLoadPer);
						$("#saveUrlForm [name='useable']").each(function(){
							if($(this).val() == data.data.useable) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});
						$("#saveUrlForm [name='status']").val(data.data.status);
						$("#saveUrlForm [name='loadTime']").val(data.data.loadTime);
						$("#saveUrlForm [name='url_loadTimeCheck']").each(function(){
							if($(this).val() == data.data.loadTimeCheck) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});
						$("#saveUrlForm [name='textCheckValue']").val(data.data.textCheckValue);
						$("#saveUrlForm [name='textCheck']").each(function(){
							if($(this).val() == data.data.textCheck) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});
						$("#saveUrlForm [name='payLoad']").val(data.data.payLoad);
						$("#saveUrlForm [name='url_payLoadCheck']").each(function(){
							if($(this).val() == data.data.payLoadCheck) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});
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
						$("#saveApiForm [name='api_start_date']").val(getFormatDate(new Date(data.data.startDate)));
						$("#saveApiForm [name='api_end_date']").val(getFormatDate(new Date(data.data.endDate)));
						$("#saveApiForm [name='api_start_hour']").val(data.data.startHour);
						$("#saveApiForm [name='api_end_hour']").val(data.data.endHour);
						$("#saveApiForm [name='errLoadTime']").val(data.data.errLoadTime);
						$("#saveApiForm [name='payLoadPer']").val(data.data.payLoadPer);
						$("#saveApiForm [name='api_useable']").each(function(){
							if($(this).val() == data.data.useable) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});
						$("#saveApiForm [name='method']").val(data.data.method);
						$("#saveApiForm [name='status']").val(data.data.status);
						$("#saveApiForm [name='api_loadTimeCheck']").each(function(){
							if($(this).val() == data.data.loadTimeCheck) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});
						$("#saveApiForm [name='loadTime']").val(data.data.loadTime);
						$("#saveApiForm [name='api_payLoadCheck']").each(function(){
							if($(this).val() == data.data.payLoadCheck) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});
						$("#saveApiForm [name='payLoad']").val(data.data.payLoad);
						$("#apiCheck").attr('cd', data.data.url);
						$("#saveApiForm [name='api_responseCheck']").each(function(){
							if($(this).val() == data.data.responseCheck) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});
						$("#api_response").text(data.data.response);
						$("#saveApiForm [name='response']").val(data.data.response);
						
						var apiParams = (data.data.apiParams);
						$.each(apiParams, function(i){
							key = apiParams[i]["param_key"];
							value = apiParams[i]["param_value"];
							var api_param = 
								'<li class="list-group-item d-flex justify-content-between align-items-center param">'
								+'Key <input type="text" class="form-control param_key" name="param_key" value="'+key+'">'
								+'Value <input type="text" class="form-control param_value" name="param_value" value="'+value+'">'
								+'<a href="#" class="del_api_param badge badge-pill">X</a></li>';
							$(".api_params").append(api_param);
						});
						
						
						$('#saveApiModal').modal('show');
						
						
					}
				}
			}
		});
	});
	
	
	$('body').on('click', '#saveApi', function(){

		if($("#apiCheck").attr('cd') != $("#saveApiForm [name='url']").val()) {
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
			data : getApiSaveData(),
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
				data : getApiSaveData(),
				dataType : 'json',
				success : function(data) {
					var errorCode = data.errorCode;
					if(typeof errorCode != "undefined"){
						$('#errorCode').val(errorCode);
						$('#errorCreate').submit();
					}else{
						if(data.result == "success") { 
							$("#saveApiForm [name='loadTime']").val(data.data.loadTime);
							$("#saveApiForm [name='payLoad']").val(data.data.payLoad);
							$("#saveApiForm [name='status']").val(data.data.status);
							$("#api_response").text(data.data.response);
							$("#saveApiForm [name='response']").val(data.data.response);
							$("#apiCheck").attr('cd', url);
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
			'<li class="list-group-item d-flex justify-content-between align-items-center param">'
			+'Key <input type="text" class="form-control param_key" name="param_key" value="">Value <input type="text" class="form-control param_value" name="param_value" value="">'
			+'<a href="#" class="del_api_param badge badge-pill">X</a></li>';
		$(".api_params").append(api_param);
	});

	
	$('body').on('click', '.del_api_param', function(){
		$(this).parent().remove();
	});
	
	
	function getApiSaveData(){
		var keys = new Array();
		var values = new Array();

		$(".param").each(function(){
			var key = $(this).find('.param_key').val();
			var value = $(this).find('.param_value').val();
			if(key != ''){
				keys.push(key);
				values.push(value);
			}
		});

		var data = $("#saveApiForm").serializeArray();
		if(keys.length > 0){
			data.push({name: "keys", value: JSON.stringify(keys)});
			data.push({name: "values", value: JSON.stringify(values)});
		}
		
		return data;
	}
	
	


	$('body').on('click', '#transactionCheck', function(){
		var transactionFile = $("#saveTransactionForm [name='transactionFile']").val();
		if(transactionFile == '') {
			alert('transactionFile를 첨부해주세요');
		}
		else{

			var file = $("#saveTransactionForm [name=transactionFile]")[0].files[0];
		    var formData = new FormData();
		    formData.append("transactionFile", file);

			$.ajax({
				type : 'POST',
				url : '/transactionCheck',                 
				data : formData,
				processData: false,
                contentType: false,
	            enctype: 'multipart/form-data',
				success : function(data) {
					var errorCode = data.errorCode;
					if(typeof errorCode != "undefined"){
						$('#errorCode').val(errorCode);
						$('#errorCreate').submit();
					}else{
						if(data.result == "success") { 
							
							
							$("#transactionCheck").attr('cd', transactionFile);
							$("#saveTransactionForm [name=status]").val(data.data.status);
							$("#saveTransactionForm [name=loadTime]").val(data.data.loadTime);
							$("#saveTransactionForm [name=codeDatas]").val(JSON.stringify(data.codeDatas));
							$("#saveTransactionForm [name=transactionCode]").val(transactionFile);
							$("#transactionFile").attr('cd', transactionFile);
							
							alert('검사 완료');
							
						}
					}
				}
			});
			
		}
	});

	
	$('body').on('click', '.transactionExecuteBtn', function(){
		
		$.ajax({
			type : 'GET',
			url : '/transactionExecute/'+$(this).attr('cd'),
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
	

	$('body').on('click', '#deleteTransaction', function(){
		$.ajax({
			type : 'DELETE',
			url : '/transaction/'+$("#saveTransactionForm [name='seq']").val(),
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
	

	$('body').on('click', '#saveTransaction', function(){

		if($("#transactionCheck").attr('cd') != $("#transactionFile").attr('cd')) {
			alert('Transaction 검사를 진행해주세요.');
			return;
		}
		
		var method = 'POST';
		var url = '/transaction';
		if($("#saveTransactionForm [name='seq']").val() != '') {
			method = 'PUT';
			url = '/transaction/'+$("#saveTransactionForm [name='seq']").val();
		}
		
		$.ajax({
			type : method,
			url : url,
			data : $("#saveTransactionForm").serialize(),
			dataType : 'json',
			success : function(data) {
				var errorCode = data.errorCode;
				if(typeof errorCode != "undefined"){
					$('#errorCode').val(errorCode);
					$('#errorCreate').submit();
				}else{
					if(data.result == "success") { 

						$("#saveTransactionModal").hide();
						alert("저장 완료");
						window.location.reload();

					}
				}
			}
		});
	});
	



	
	$('body').on('click', '.transactionEditBtn', function(){
		
		/* transaction 출력 */
		$.ajax({
			type : 'GET',
			url : '/transaction/'+$(this).attr('cd'),
			dataType : 'json',
			success : function(data) {
				var errorCode = data.errorCode;
				if(typeof errorCode != "undefined"){
					$('#errorCode').val(errorCode);
					$('#errorCreate').submit();
				}else{
					if(data.result == "success") { 
						
						$("#saveTransactionForm [name='seq']").val(data.data.seq);
						$("#saveTransactionForm [name='title']").val(data.data.title);
						$("#saveTransactionForm [name='timeout']").val(data.data.timeout);
						$("#saveTransactionForm [name='timer']").val(data.data.timer);
						$("#saveTransactionForm [name='transaction_start_date']").val(getFormatDate(new Date(data.data.startDate)));
						$("#saveTransactionForm [name='transaction_end_date']").val(getFormatDate(new Date(data.data.endDate)));
						$("#saveTransactionForm [name='transaction_start_hour']").val(data.data.startHour);
						$("#saveTransactionForm [name='transaction_end_hour']").val(data.data.endHour);
						$("#saveTransactionForm [name='errLoadTime']").val(data.data.errLoadTime);
						$("#saveTransactionForm [name='useable']").each(function(){
							if($(this).val() == data.data.useable) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});

						$("#saveTransactionForm [name='transaction_loadTimeCheck']").each(function(){
							if($(this).val() == data.data.loadTimeCheck) $(this).attr('checked','true');
							else  $(this).removeAttr('checked');
						});
						$("#saveTransactionForm [name='status']").val(data.data.status);
						$("#saveTransactionForm [name='loadTime']").val(data.data.loadTime);
						$("#saveTransactionForm [name='transactionCode']").val(data.data.transactionCode);
						$("#saveTransactionForm [name=codeDatas]").val(JSON.stringify(data.data.codeDatas));
						$("#transactionFile").attr('cd',data.data.transactionCode);
						$("#transactionCheck").attr('cd', data.data.transactionCode);
						
						
						$('#saveTransactionModal').modal('show');
					}
				}
			}
		});
	});
	
	
	$('body').on('click', '.driverBtn', function(){
		
		$.ajax({
			type : 'POST',
			url : '/main/driver',
			data : {driverPath : $("#driver").val()},
			dataType : 'json',
			success : function(data) {
				var errorCode = data.errorCode;
				if(typeof errorCode != "undefined"){
					$('#errorCode').val(errorCode);
					$('#errorCreate').submit();
				}else{
					if(data.result == "success") { 
						
						alert("저장 완료");
						window.location.reload();

					}
				}
			}
		});
	});
}