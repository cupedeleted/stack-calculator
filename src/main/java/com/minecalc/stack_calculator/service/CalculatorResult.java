package com.minecalc.stack_calculator.service;

public record CalculatorResult(
    String itemType,
    long totalItems,
    long stacks,
    long remainingItems,
    long shulkerBoxes,
    long doubleChests
) {}