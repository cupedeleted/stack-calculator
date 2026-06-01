package com.minecalc.stack_calculator.controller;

import com.minecalc.stack_calculator.service.CalculatorResult;
import com.minecalc.stack_calculator.service.CalculatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalculatorController {

	private final CalculatorService calculatorService;

	public CalculatorController(CalculatorService calculatorService) {
		this.calculatorService = calculatorService;
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/calculate")
	public String calculate(
			@RequestParam("blocks") long blocks,
			@RequestParam(value = "itemType", defaultValue = "default") String itemType,
			Model model
	) {
		CalculatorResult result = calculatorService.calculate(blocks, itemType);
		model.addAttribute("blocks", blocks);
		model.addAttribute("itemType", itemType);
		model.addAttribute("result", result);
		return "index";
	}
}
