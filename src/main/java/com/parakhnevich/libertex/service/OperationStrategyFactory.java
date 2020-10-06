package com.parakhnevich.libertex.service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.parakhnevich.libertex.constant.OperationName;
import com.parakhnevich.libertex.error.exception.NoSuchOperationStrategy;
import org.springframework.stereotype.Component;

@Component
public class OperationStrategyFactory {

    private Map<OperationName, OperationStrategy> strategies;

    public OperationStrategyFactory(Set<OperationStrategy> strategies) {
        createStrategy(strategies);
    }

    public OperationStrategy getStrategy(OperationName operation) {
        return Optional.ofNullable(strategies.get(operation)).orElseThrow(() -> new NoSuchOperationStrategy(operation));
    }

    private void createStrategy(Set<OperationStrategy> strategySet) {
        strategies = new EnumMap<>(OperationName.class);
        strategySet.forEach(strategy -> strategies.put(strategy.getStrategyName(), strategy));
    }

}
