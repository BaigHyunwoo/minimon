

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
		
		$('#cuid').val("");
		$('#cpwd').val("");
		
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
							console.log(data);
							$("#saveUrlForm [name='loadTime']").val(data.data.totalLoadTime);
							$("#saveUrlForm [name='payLoad']").val(data.data.totalPayLoad);
							$("#saveUrlForm [name='status']").val(data.data.status);

						}
					}
				}
			});
		}
	});
	
	$('body').on('click', '#saveUrl', function(){
		var method = 'POST';
		if($("#saveUrlForm [name='seq']").val() != '') method = 'PUT';
		console.log($("#saveUrlForm").serialize());
		
		/* URL 리스트 출력 */
		$.ajax({
			type : method,
			url : '/url',
			data : $("#saveUrlForm").serialize(),
			dataType : 'json',
			success : function(data) {
				var errorCode = data.errorCode;
				if(typeof errorCode != "undefined"){
					$('#errorCode').val(errorCode);
					$('#errorCreate').submit();
				}else{
					if(data.result == "success") { 

						console.log(data);

					}
				}
			}
		});
	});
}