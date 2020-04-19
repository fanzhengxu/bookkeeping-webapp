package com.fzx.bookkeeping.service.impl;
import	java.text.SimpleDateFormat;

import com.fzx.bookkeeping.dal.BookKeepMapper;
import com.fzx.bookkeeping.model.BookKeep;
import com.fzx.bookkeeping.service.CreateBookKeepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 23:41 2020/4/13
 */
@Service
public class CreateBookKeepImpl implements CreateBookKeepService {
    @Autowired
    private BookKeepMapper bookKeepMapper;

    @Override
    public BookKeep create(BookKeep bookKeep) {
        Calendar calendar = Calendar.getInstance();
        bookKeep.setCreateddate(calendar.getTime());
        bookKeep.setUpdateddate(calendar.getTime());
        bookKeepMapper.insertSelective(bookKeep);
        return get(bookKeep.getId());
    }

    @Override
    public BookKeep get(Integer id) {
        return bookKeepMapper.selectByPrimaryKey(id);
    }
}
