package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeActionEnum {
    GET("driver.get", "창 이동"),
    SIZE("setSize", "사이즈 변경"),
    WINDOW_HANDLES("getWindowHandles", "윈도우 이동"),
    WAIT("waitForWindow", "SUCCESS"),
    SWITCH("switchTo", "화면 이동"),
    CLICK("click", "클릭"),
    SUBMIT("submit", "전송"),
    SEND("sendKeys", "키 입력");


    private String code;
    private String value;
}
