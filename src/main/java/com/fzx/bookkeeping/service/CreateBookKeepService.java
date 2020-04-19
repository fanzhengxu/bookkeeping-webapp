package com.fzx.bookkeeping.service;

import com.fzx.bookkeeping.model.BookKeep;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 23:40 2020/4/13
 */
public interface CreateBookKeepService {

    BookKeep create(BookKeep bookKeep);

    BookKeep get(Integer id);
}
