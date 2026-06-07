package com.minecalc.stack_calculator.service;

public record CalculatorResult(
		String itemType,
		long totalItems,
		long stacks,
		long remainingItems,
		
		long shulkerBoxes,
		long fullShulkerBoxes,
		long lastShulkerFullStacks,
		long lastShulkerRemainingItems,

		long doubleChests,
		long fullDoubleChests,
		long lastDoubleChestFullStacks,
		long lastDoubleChestRemainingItems
) {
}