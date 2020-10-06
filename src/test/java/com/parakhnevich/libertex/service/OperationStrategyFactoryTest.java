package com.parakhnevich.libertex.service;

import java.util.Set;

import com.parakhnevich.libertex.constant.OperationName;
import com.parakhnevich.libertex.error.exception.NoSuchOperationStrategy;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class OperationStrategyFactoryTest {

    private OperationStrategyFactory operationStrategyFactory;

    @BeforeEach
    void init() {
        Set<OperationStrategy> strategies = Sets.newHashSet();
        strategies.add(new DebitOperation(null));
        operationStrategyFactory = new OperationStrategyFactory(strategies);
    }

    @Test
    void testGetStrategy() {
        //given
        //when
        OperationStrategy strategy = operationStrategyFactory.getStrategy(OperationName.DEBIT);
        //then
        assertThat(strategy.getStrategyName()).isEqualTo(OperationName.DEBIT);
    }

    @Test
    void testGetStrategyNoSuchOperationStrategy() {
        //given
        //when
        //then
        assertThatThrownBy(() -> operationStrategyFactory.getStrategy(OperationName.CREDIT))
                .isInstanceOf(NoSuchOperationStrategy.class);
    }

}
