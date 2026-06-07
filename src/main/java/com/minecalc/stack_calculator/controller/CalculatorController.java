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
    public String showCalculator(Model model) {
        model.addAttribute("totalItems", 0);
        model.addAttribute("shulkerCount", 0);
        model.addAttribute("chestCount", 0);
        
        return "index";
    }

    @PostMapping("/calculate")
    public String calculateStacks(@RequestParam int quantity, @RequestParam String itemType, Model model) {
        CalculatorResult result = calculatorService.calculate(quantity, itemType);
        
        model.addAttribute("totalItems", quantity);
        model.addAttribute("shulkerCount", result.shulkerBoxes());
        model.addAttribute("chestCount", result.doubleChests());
        
        return "index";
    }
}