package com.zz.common.menus;

import com.zz.common.error.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 主体类型
 *
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/16
 * Time：15:21
 * ================================
 */
@Getter
@AllArgsConstructor
public enum SubjectType {

    /**
     * 个人
     */
    PERSON("person", "个人"),

    /**
     * 企业
     */
    ENTERPRISE("enterprise", "企业");

    private final String value;

    private final String label;

    public static SubjectType getByValue(String value) {
        return Arrays.stream(SubjectType.values()).filter(subjectType -> subjectType.value.equals(value))
                .findAny().orElseThrow(() -> new ServiceException("主体类型不存在"));
    }
}
