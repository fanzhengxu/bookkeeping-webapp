package com.fzx.bookkeeping.service.impl;

import com.fzx.bookkeeping.dal.PayTypeMapper;
import com.fzx.bookkeeping.model.PayType;
import com.fzx.bookkeeping.service.PayTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 23:24 2020/4/14
 */
@Service
public class PayTypeServiceImpl implements PayTypeService {
    @Autowired
    private PayTypeMapper payTypeMapper;

    @Override
    public int create(PayType payType) {
        return payTypeMapper.insert(payType);
    }

    @Override
    public List<PayType> selectAll() {
        return null;
    }
}
