package com.rt.engine.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.rt.engine.common.constants.CodeEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseUtil {

    /**
     * 封装返回报文
     *
     * @param response
     *            返回对象
     */
    public static void returnBaseResponse(HttpServletResponse response, CodeEnum codeEnum) {
        PrintWriter writer = null;
        try {
            Map<String, String> baseResponse = new HashMap<>(10);
            baseResponse.put("code", codeEnum.getCode());
            baseResponse.put("desc", codeEnum.getDesc());

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            writer = response.getWriter();
            writer.print(JSONObject.toJSONString(baseResponse));
        } catch (IOException ex) {
            log.error("returnBaseResponse Fail：", ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
