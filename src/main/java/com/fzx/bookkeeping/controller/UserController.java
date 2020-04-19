package com.fzx.bookkeeping.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 23:38 2020/4/16
 */
@RequestMapping("/user")
@Controller("/user")
public class UserController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/create")
    @ResponseBody
    public JSONObject create(HttpServletRequest request) {
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String nickName = jsonObject.getString("nickName");
        List<Map<String, Object>> stringObjectMap = jdbcTemplate.queryForList("select id from t_user where nickName = ?", nickName);
        if (stringObjectMap.isEmpty()) {
            jdbcTemplate.update(
                    "insert into t_user(avatarUrl, city, country, gender, language, nickName, province) value " +
                    "(?, ?, ?, ?, ?, ?, ?)",
                    jsonObject.getString("avatarUrl"),
                    jsonObject.getString("city"),
                    jsonObject.getString("country"),
                    jsonObject.getInteger("gender"),
                    jsonObject.getString("language"),
                    jsonObject.getString("nickName"),
                    jsonObject.getString("province")
            );
            List<Map<String, Object>> ids = jdbcTemplate.queryForList("select id from t_user where nickName = ?", nickName);
            String id = String.valueOf(ids.get(0).get("id"));
            jdbcTemplate.update("insert into t_payType(name, enable, createdDate, updatedDate, user_id) " +
                    "value (?, ?, ?, ?, ?)", "支付宝", true, new Date(), new Date(), id);
            jdbcTemplate.update("insert into t_payType(name, enable, createdDate, updatedDate, user_id) " +
                    "value (?, ?, ?, ?, ?)", "微信", true, new Date(), new Date(), id);
            jdbcTemplate.update("insert into t_purpose(name, enable, createdDate, updatedDate, user_id) " +
                    "value (?, ?, ?, ?, ?)", "购物", true, new Date(), new Date(), id);
            jdbcTemplate.update("insert into t_purpose(name, enable, createdDate, updatedDate, user_id) " +
                    "value (?, ?, ?, ?, ?)", "酒店", true, new Date(), new Date(), id);
            jsonObject = new JSONObject();
            jsonObject.put("status", 200);
            jsonObject.put("msg", "新用户 [" + nickName + " ]初始化成功");
            return jsonObject;
        } else {
            jsonObject = new JSONObject();
            jsonObject.put("status", 200);
            jsonObject.put("msg", "用户 [" + nickName + " ]已存在");
            return jsonObject;
        }
    }
}
