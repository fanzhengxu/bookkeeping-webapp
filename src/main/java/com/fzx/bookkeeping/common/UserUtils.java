package com.fzx.bookkeeping.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 22:31 2020/4/17
 */
@Component
public class UserUtils {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public Integer getUserId(String userInfo) {
        JSONObject userInfoObject = JSONObject.parseObject(userInfo);
        String nickName = userInfoObject.getString("nickName");
        Map<String, Object> stringObjectMap = jdbcTemplate.queryForMap("select id from t_user where nickName = ?", nickName);
        return Integer.valueOf(String.valueOf(stringObjectMap.get("id")));
    }
}
