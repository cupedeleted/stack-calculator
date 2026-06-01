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
			throw new IllegalArgumentException("La cantidad de ítems no puede ser negativa");
		}

		String normalizedItemType = normalizeItemType(itemType);
		int stackSize = resolveStackSize(normalizedItemType);

		long stacks = totalItems / stackSize;
		long remainingItems = totalItems % stackSize;
		long stackSlotsNeeded = stacks + (remainingItems > 0 ? 1 : 0);

		ContainerBreakdown shulkerBreakdown = calculateContainerBreakdown(
				stackSlotsNeeded,
				SHULKER_STACK_SLOTS,
				remainingItems
		);
		ContainerBreakdown doubleChestBreakdown = calculateContainerBreakdown(
				stackSlotsNeeded,
				DOUBLE_CHEST_STACK_SLOTS,
				remainingItems
		);

		return new CalculatorResult(
				normalizedItemType,
				stackSize,
				totalItems,
				stacks,
				remainingItems,
				shulkerBreakdown.totalContainers(),
				shulkerBreakdown.fullContainers(),
				shulkerBreakdown.lastContainerFullStacks(),
				shulkerBreakdown.lastContainerRemainingItems(),
				doubleChestBreakdown.totalContainers(),
				doubleChestBreakdown.fullContainers(),
				doubleChestBreakdown.lastContainerFullStacks(),
				doubleChestBreakdown.lastContainerRemainingItems()
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

	private static ContainerBreakdown calculateContainerBreakdown(
			long stackSlotsNeeded,
			int capacity,
			long remainingItems
	) {
		if (stackSlotsNeeded == 0) {
			return new ContainerBreakdown(0, 0, 0, 0);
		}

		long totalContainers = containersNeeded(stackSlotsNeeded, capacity);
		long slotsInLastContainer = stackSlotsNeeded % capacity;
		if (slotsInLastContainer == 0) {
			slotsInLastContainer = capacity;
		}

		boolean lastContainerCompletelyFull = slotsInLastContainer == capacity && remainingItems == 0;
		long fullContainers = lastContainerCompletelyFull ? totalContainers : totalContainers - 1;
		long lastContainerFullStacks = slotsInLastContainer - (remainingItems > 0 ? 1 : 0);

		return new ContainerBreakdown(
				totalContainers,
				fullContainers,
				lastContainerFullStacks,
				remainingItems
		);
	}

	private static long containersNeeded(long stackSlotsNeeded, int capacity) {
		return (stackSlotsNeeded + capacity - 1) / capacity;
	}

	private record ContainerBreakdown(
			long totalContainers,
			long fullContainers,
			long lastContainerFullStacks,
			long lastContainerRemainingItems
	) {
	}
}
