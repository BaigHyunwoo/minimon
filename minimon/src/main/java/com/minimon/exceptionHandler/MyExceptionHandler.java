package com.minimon.exceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Exception 통합 처리
 *
 * @author 백현우
 */
@ControllerAdvice
public class MyExceptionHandler {

    private static final Logger ErrorLogger = LoggerFactory.getLogger(MyExceptionHandler.class.getName());


    /**
     * Null Check를 한 후 code return
     *
     * @param key       입력 값의 이름
     * @param val       입력 값
     * @param funcName  실행 메소드
     * @param error     에러 코드
     * @param className 실행 클래스
     * @throws MyException
     */
    public static void isNull(String key, Object val, String funcName, int error, String className) throws MyException {
        if (val == null) throw new MyException("CLASS : " + className + " - METHOD : "
                + funcName + " - TYPE = [NULL]/ INPUT [" + key + "] IS Null"
                , className, error);
    }


    /**
     * Controller의 코드를 생성해준다.
     *
     * @param controller
     * @return code
     */
    public int getControllerCode(String controller) {

        if (controller.equals("MainController")) {
            return 1000;
        } else {
            return 2000;
        }

    }


    /**
     * 사용자가 정의한 코드 중 일치하는 코드를 되돌려준다.
     * 사용자 정의 코드는 NULL과 FUNCTION으로 나뉜다.
     *
     * @param msg 메시지
     * @return code
     */
    public int getErrorCheck(String msg) {

        String[] msgArr = msg.split("/");

        int error = 0;

        if (msgArr[0].indexOf("[NULL]") > -1) error = MyErrorCode.PARAM_IS_NULL.getErrorCode();
        else if (msgArr[0].indexOf("[Function]") > -1) error = MyErrorCode.FUNCTION.getErrorCode();

        return error;
    }


    /**
     * ERROR CODE를 log로 출력한다.
     *
     * @param e Exception
     * @return code
     * @param    error        에러 코드
     * @param    request        연결 리퀘스트
     * @param    response    연결 리스폰스
     */
    public void printError(Exception e, int error, HttpServletRequest request, HttpServletResponse response) {

        String[] controllerArr = e.getStackTrace()[0].getClassName().split("\\.");
        int controller = getControllerCode(controllerArr[controllerArr.length - 1]);

        ErrorLogger.debug("MSG : " + e.getStackTrace()[0]);

        if (controller < 5000) controllerFd(error, controller, request, response);
        else ErrorLogger.debug("MSG :  ERRORCODE : " + (error + controller));

    }


    /**
     * 사용자가 발생시킨 MyException 전용 처리
     *
     * @param e Exception
     * @return code
     * @param    request            연결 리퀘스트
     * @param    response        연결 리스폰스
     */
    @ExceptionHandler(value = MyException.class)
    public void handleMyException(MyException e, HttpServletRequest request, HttpServletResponse response) {

        int error = 0;
        int controller = 0;

        try {

            String[] controllerArr = e.getStackTrace()[0].getClassName().split("\\.");
            controller = getControllerCode(controllerArr[controllerArr.length - 1]);

        } catch (Exception ex) {

            String[] controllerArr = e.getClassName().split("\\.");
            controller = getControllerCode(controllerArr[controllerArr.length - 1]);

        }

        if (e.getErrorCode() != 0) error = e.getErrorCode();
        else error = getErrorCheck(e.getMessage());


        if (controller < 5000) controllerFd(error, controller, request, response);

    }


    /**
     * Controller에서 발생한 에러일 경우 화면에 출력한다.
     *
     * @param error errorcode
     * @return code
     * @param    controller        controller code
     * @param    request            연결 리퀘스트
     * @param    response        연결 리스폰스
     */
    public void controllerFd(int error, int controller, HttpServletRequest request, HttpServletResponse response) {

        try {

            int errorCode = (error + controller);
            ErrorLogger.debug("MSG :  ERRORCODE : " + (error + controller));

            request.setAttribute("errorCode", errorCode);
            RequestDispatcher rd = request.getRequestDispatcher("/errorCode");
            rd.forward(request, response);

        } catch (IOException e1) {
            ErrorLogger.debug(e1.getStackTrace()[0] + "");
        } catch (ServletException e1) {
            ErrorLogger.debug(e1.getStackTrace()[0] + "");
        }

    }


    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public void handleMyException(ArrayIndexOutOfBoundsException e, HttpServletRequest request, HttpServletResponse response) {
        int error = MyErrorCode.INDEX_OUT_OF_BOUND.getErrorCode();
        printError(e, error, request, response);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public void handleMyException(NullPointerException e, HttpServletRequest request, HttpServletResponse response) {

        int error = MyErrorCode.NULL_POINTER.getErrorCode();
        printError(e, error, request, response);
    }

    @ExceptionHandler(value = IOException.class)
    public void handleMyException(IOException e, HttpServletRequest request, HttpServletResponse response) {

        int error = MyErrorCode.IO.getErrorCode();
        printError(e, error, request, response);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public void handleMyException(FileNotFoundException e, HttpServletRequest request, HttpServletResponse response) {

        int error = MyErrorCode.FILE_NOT_FOUND.getErrorCode();
        printError(e, error, request, response);
    }

    @ExceptionHandler(value = Exception.class)
    public void handleMyException(HttpSession session, Exception e, HttpServletRequest request, HttpServletResponse response) {
        int error = MyErrorCode.UNKNOWN.getErrorCode();
        printError(e, error, request, response);
    }


}