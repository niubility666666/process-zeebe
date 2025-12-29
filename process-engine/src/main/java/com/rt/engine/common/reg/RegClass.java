package com.rt.engine.common.reg;

import lombok.Builder;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
public class RegClass {

    /**
     * {}匹配规则
     */
    private static final String BRACE_PATTERN = "\\{([^}]*)\\}";

    /**
     * 找到字符串中{}内容
     * 
     * @param input
     *            待验证字符
     * @return Matcher
     */
    public static Matcher getBracePattern(CharSequence input) {
        Pattern localPattern = Pattern.compile(BRACE_PATTERN);
        return localPattern.matcher(input);
    }
}
