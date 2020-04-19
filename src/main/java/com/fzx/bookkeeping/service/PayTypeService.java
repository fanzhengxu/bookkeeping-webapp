package com.fzx.bookkeeping.service;

import com.fzx.bookkeeping.model.PayType;

import java.util.List;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 23:23 2020/4/14
 */
public interface PayTypeService {

    int create(PayType payType);

    List<PayType> selectAll();
}
