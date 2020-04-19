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
import java.util.List;
import java.util.Map;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 14:53 2020/4/18
 */
@Controller("/report")
@RequestMapping("/report")
public class ReportController {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserUtils userUtils;

    @RequestMapping("/reportOfDay")
    @ResponseBody
    public JSONObject reportOfDay(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String userInfo = request.getParameter("userInfo");
        if (StringUtils.isBlank(userInfo)) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "未传入用户信息");
            return jsonObject;
        }
        Integer id = userUtils.getUserId(userInfo);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select id, b_date, amount from t_bookKeep where user_id = ? order by b_dateTime desc", id);
        jsonObject.put("status", 200);
        jsonObject.put("data", maps);
        return jsonObject;
    }

    @RequestMapping("/reportOfMonth")
    @ResponseBody
    public JSONObject reportOfMonth(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String userInfo = request.getParameter("userInfo");
        if (StringUtils.isBlank(userInfo)) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "未传入用户信息");
            return jsonObject;
        }
        Integer id = userUtils.getUserId(userInfo);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(
                "SELECT MONTH, SUM(amount) SUM FROM t_bookKeep WHERE user_id = ? GROUP BY month order by month desc", id);
        jsonObject.put("status", 200);
        jsonObject.put("data", maps);
        return jsonObject;
    }
}
