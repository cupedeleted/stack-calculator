package com.minecalc.stack_calculator.service;

import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

	private static final int DEFAULT_STACK_SIZE = 64;
	private static final int SMALL_STACK_SIZE = 16;
	private static final int SHULKER_STACK_SLOTS = 27;
	private static final int DOUBLE_CHEST_STACK_SLOTS = 54;

	private static final Set<String> SPECIAL_ITEMS = specialItems();

	private static Set<String> specialItems() {
		return Set.of(
				"ender_pearl",
				"egg",
				"snowball",
				"honey_bottle",
				"armor_stand",
				"written_book",
				"empty_bucket",
				"sign",
				"hanging_sign",
				"banner"
		);
	}

	public CalculatorResult calculate(long totalItems, String itemType) {
		if (totalItems < 0) {
			throw new IllegalArgumentException("The number of items cannot be negative");
		} else if (totalItems >= 10000000) {
			throw new IllegalArgumentException("The number of items cannot exceed 10,000,000.");
		}

		String normalizedItemType = normalizeItemType(itemType);
		int stackSize = resolveStackSize(normalizedItemType);

		long stacks = totalItems / stackSize;
		long remainingItems = totalItems % stackSize;
		
		long stackSlotsNeeded = stacks + (remainingItems > 0 ? 1 : 0);

		long totalShulkers = containersNeeded(stackSlotsNeeded, SHULKER_STACK_SLOTS);
		long totalDoubleChests = containersNeeded(stackSlotsNeeded, DOUBLE_CHEST_STACK_SLOTS);

		return new CalculatorResult(
				normalizedItemType,
				totalItems,
				stacks,
				remainingItems,
				totalShulkers,
				totalDoubleChests
		);
	}

	public Set<String> getSpecialItems() {
		return SPECIAL_ITEMS;
	}

	public boolean isSpecialItem(String itemType) {
		return isSpecialItemType(normalizeItemType(itemType));
	}

	public int resolveStackSize(String itemType) {
		return isSpecialItem(itemType) ? SMALL_STACK_SIZE : DEFAULT_STACK_SIZE;
	}

	private static boolean isSpecialItemType(String normalizedItemType) {
		return "special".equals(normalizedItemType) || SPECIAL_ITEMS.contains(normalizedItemType);
	}

	private static String normalizeItemType(String itemType) {
		if (itemType == null || itemType.isBlank()) {
			return "default";
		}
		return itemType.trim().toLowerCase();
	}

	private static long containersNeeded(long stackSlotsNeeded, int capacity) {
		if (stackSlotsNeeded == 0) return 0;
		return (stackSlotsNeeded + capacity - 1) / capacity;
	}
}