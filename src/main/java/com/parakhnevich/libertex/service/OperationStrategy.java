package com.parakhnevich.libertex.service;

import com.parakhnevich.libertex.constant.OperationName;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;

public interface OperationStrategy {

    OperationName getStrategyName();

    TransactionEntity execute(TransactionEntity transactionEntity);
}
