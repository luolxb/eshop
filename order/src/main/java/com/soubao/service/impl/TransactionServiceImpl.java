package com.soubao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.ReplyMapper;
import com.soubao.dao.TransactionMapper;
import com.soubao.entity.Reply;
import com.soubao.entity.Transaction;
import com.soubao.service.ReplyService;
import com.soubao.service.TransactionService;
import org.springframework.stereotype.Service;

@Service("transactionService")
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {
}
