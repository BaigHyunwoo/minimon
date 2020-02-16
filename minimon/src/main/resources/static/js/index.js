

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