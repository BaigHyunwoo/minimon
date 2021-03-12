package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeSelectorTypeEnum {
    ID("By.id", "ID"),
    CSS("By.cssSelector", "CSS"),
    TEXT("By.linkText", "TEXT"),
    CLASS("By.className", "CLASS"),
    NAME("By.name", "NAME"),
    TAG("By.tagName", "TAG"),
    XPATH("By.xpath", "XPATH"),
    PARTIAL_LINK("By.partialLinkText", "PARTIAL_LINK_TEXT");

    private String code;
    private String value;
}
