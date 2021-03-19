# minimon
사이트 접근, Http 요청, 사이트 접속 후 어떠한 행위들이 정상적으로 동작하는지 검사하는 모니터링 시스템입니다.

  
## 사용 환경  
호환 OS : Windows  
호환 브라우저 : ChromeBrowser  
필수 설치 :   
  [Selenium plugin](https://chrome.google.com/webstore/detail/selenium-ide/mooikfkahbdckldjjndioackbalphokd?hl=en)  
  [ChromeDriver](http://chromedriver.chromium.org/downloads?tmpl=%2Fsystem%2Fapp%2Ftemplates%2Fprint%2F&showPrintDialog=1)
  

## 사용 예제
스웨거 이용  
경로 : http://localhost:8080/swagger-ui.html#  


## 개발 환경 설정  
설정 파일에 chromeDriver 경로와 결과를 전송 받을 API 경로를 작성해주세요.  
driverPath: chromeDriver 경로  
location: 응답 API 경로  
  
ex)  
application.yml  
common:  
  driverPath: C:\\Users\\user\\Downloads\\chromedriver  
  location: C:\\Users\\user\\Downloads\\chromedriver\\location.properties
  
  
