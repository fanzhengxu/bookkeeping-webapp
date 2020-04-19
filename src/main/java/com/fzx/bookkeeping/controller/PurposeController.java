package com.fzx.bookkeeping.controller;

import com.alibaba.fastjson.JSONObject;
import com.fzx.bookkeeping.common.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Date: Create in 22:30 2020/4/16
 */
@Controller("/purpose")
@RequestMapping("/purpose")
public class PurposeController {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserUtils userUtils;

    @RequestMapping("/selectAll")
    @ResponseBody
    public JSONObject selectAll(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String userInfo = request.getParameter("userInfo");
        if (StringUtils.isBlank(userInfo)) {
            jsonObject = new JSONObject();
            jsonObject.put("status", 500);
            jsonObject.put("msg", "未传入用户信息");
            return jsonObject;
        }
        Integer id = userUtils.getUserId(userInfo);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select id, name, enable from t_purpose where user_id = ?", id);
        jsonObject.put("status", 200);
        jsonObject.put("data", maps);
        return jsonObject;
    }

    @RequestMapping("/create")
    @ResponseBody
    public JSONObject create(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String data = request.getParameter("data");
        if (StringUtils.isBlank(data)) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "传入参数为空");
            return jsonObject;
        }
        jsonObject = JSONObject.parseObject(data);
        String name = jsonObject.getString("name");
        if (StringUtils.isBlank(name)) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "未传入用途名称");
            return jsonObject;
        }
        Boolean enable = jsonObject.getBoolean("enable");
        if (enable == null) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "缺少是否启用参数");
            return jsonObject;
        }
        String userInfo = request.getParameter("userInfo");
        if (StringUtils.isBlank(userInfo)) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "未传入用户信息");
            return jsonObject;
        }
        Integer id = userUtils.getUserId(userInfo);
        jdbcTemplate.update("insert into t_purpose(name, enable, createdDate, updatedDate, user_id) " +
                "value (?, ?, ?, ?, ?)", name, enable, new Date(), new Date(), id);
        jsonObject.put("status", 200);
        jsonObject.put("msg", "添加成功");
        return jsonObject;
    }

    @RequestMapping("/enable")
    @ResponseBody
    public JSONObject enable(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String data = request.getParameter("data");
        if (StringUtils.isBlank(data)) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "传入参数为空");
            return jsonObject;
        }
        jsonObject = JSONObject.parseObject(data);
        Integer id = jsonObject.getInteger("id");
        if (id == null) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "未传入支付方式id");
            return jsonObject;
        }
        Boolean enable = jsonObject.getBoolean("enable");
        if (enable == null) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "缺少是否启用参数");
            return jsonObject;
        }
        jdbcTemplate.update("update t_purpose set enable = ? where id = ?", enable, id);
        jsonObject.put("status", 200);
        jsonObject.put("msg", "添加成功");
        return jsonObject;
    }

    @RequestMapping("/del")
    @ResponseBody
    public JSONObject del(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id");
        if (StringUtils.isBlank(id)) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "未传入id");
            return jsonObject;
        }
        jdbcTemplate.update("delete from t_purpose where id = ?", Integer.valueOf(id));
        jsonObject.put("status", 200);
        jsonObject.put("msg", "添加成功");
        return jsonObject;
    }
}
