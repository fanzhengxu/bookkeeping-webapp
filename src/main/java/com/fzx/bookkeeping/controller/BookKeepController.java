package com.fzx.bookkeeping.controller;
import java.security.SecureRandom;
import java.text.ParseException;
import	java.text.SimpleDateFormat;
import	java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.fzx.bookkeeping.common.UserUtils;
import com.fzx.bookkeeping.model.BookKeep;
import com.fzx.bookkeeping.service.CreateBookKeepService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 新增账单.
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 22:53 2020/4/13
 */
@Controller("/bookKeep")
@RequestMapping("/bookKeep")
public class BookKeepController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookKeepController.class);
    @Autowired
    private CreateBookKeepService createBookKeepService;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserUtils userUtils;

    @RequestMapping("/create")
    @ResponseBody
    public JSONObject create(HttpServletRequest request) {
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String userInfo = request.getParameter("userInfo");
        if (StringUtils.isBlank(userInfo)) {
            jsonObject = new JSONObject();
            jsonObject.put("status", 500);
            jsonObject.put("msg", "未传入用户信息");
            return jsonObject;
        }
        Integer id = userUtils.getUserId(userInfo);
        if (StringUtils.isBlank(date)) {
            jsonObject = new JSONObject();
            jsonObject.put("status", 500);
            jsonObject.put("msg", "未选择账单日期");
            return jsonObject;
        }
        if (StringUtils.isBlank(time)) {
            jsonObject = new JSONObject();
            jsonObject.put("status", 500);
            jsonObject.put("msg", "未选择账单时间");
            return jsonObject;
        }
        String msg = "";
        if (jsonObject.get("msg") != null) {
            msg = jsonObject.getString("msg");
        }
        String amountStr = jsonObject.getString("amount");
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
        } catch (Exception e) {
            jsonObject = new JSONObject();
            jsonObject.put("status", 500);
            jsonObject.put("msg", "输入的金额\"" + amountStr +"\"不为数字");
            LOGGER.error("输入的金额 \"{}\" 不为数字", amountStr);
            return jsonObject;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String substring;
        String sdfStr;
        Date bDate;
        try {
            substring = date.substring(0, 10);
            String[] split = substring.split("-");
            if (split.length > 1) {
                bDate = sdf2.parse(substring);
                sdfStr = "yyyy-MM-dd";
            } else {
                bDate = sdf.parse(substring);
                sdfStr = "yyyy/MM/dd";
            }
        } catch (ParseException e) {
            jsonObject = new JSONObject();
            jsonObject.put("status", 500);
            jsonObject.put("msg", "账单日期格式错误: {" + date + "}");
            LOGGER.error("账单日期格式错误: {}, msg: {}, e: {}", date, e.getMessage(), e);
            return jsonObject;
        }
        Date bTimeDate = null;
        String bTime;
        SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if (time.length() > 5) {
            bTimeDate = new Date(time);
            bTime = time;
        } else {
            Random random = new SecureRandom();
            bTime = sdf.format(new Date()) + " " + time + ":" + random.nextInt(59);
            try {
                bTimeDate = sdf5.parse(bTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        SimpleDateFormat sdf4 = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf6 = new SimpleDateFormat("yyyy-MM");
        jdbcTemplate.update(
                "insert into t_bookKeep(user_id, payType, purpose, msg, amount, b_date," +
                        "b_time, month, year, b_dateTime, createdDate, updatedDate)" +
                        "value (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                id, jsonObject.getString("payType"), jsonObject.getString("purpose"),
                msg, amount.doubleValue(), bDate, bTime, sdf6.format(bDate),
                Integer.valueOf(date.substring(0, 4)), substring + " " + sdf4.format(bTimeDate),
                sdf5.format(cal.getTime()), sdf5.format(cal.getTime()));
        jsonObject = new JSONObject();
        jsonObject.put("status", 200);
        jsonObject.put("msg", "新增成功");
        return jsonObject;
    }

    @RequestMapping("/queryMyConfigure")
    @ResponseBody
    public JSONObject queryMyConfigure(HttpServletRequest request) {
        String userInfo = request.getParameter("userInfo");
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isBlank(userInfo)) {
            jsonObject.put("status", 500);
            jsonObject.put("msg", "未传入用户信息");
            return jsonObject;
        }
        Integer id = userUtils.getUserId(userInfo);
        JSONObject data = new JSONObject();
        List<Map<String, Object>> payTypeMaps = jdbcTemplate.queryForList("select id, name from t_payType where enable = true and user_id = ?", id);
        data.put("payTypeMaps", payTypeMaps);
        List<Map<String, Object>> purposeMaps = jdbcTemplate.queryForList("select id, name from t_purpose where enable = true and user_id = ?", id);
        data.put("purposeMaps", purposeMaps);
        jsonObject.put("status", 200);
        jsonObject.put("data", data);
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
        jdbcTemplate.update("delete from t_bookKeep where id = ?", Integer.valueOf(id));
        jsonObject.put("status", 200);
        jsonObject.put("msg", "添加成功");
        return jsonObject;
    }
}
