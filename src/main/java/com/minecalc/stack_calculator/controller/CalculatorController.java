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

    @GetMapping("/calculator")
    public String showCalculator(Model model) {
        model.addAttribute("totalItems", 0);
        model.addAttribute("shulkerCount", 0);
        model.addAttribute("chestCount", 0);
        
        return "index";
    }

    @PostMapping("/result")
    public String calculateStacks(@RequestParam int blocks, @RequestParam String itemType, Model model) {
        model.addAttribute("totalItems", blocks);
        model.addAttribute("itemType", itemType);

        try {
            CalculatorResult result = calculatorService.calculate(blocks, itemType);
        
            model.addAttribute("result", result);
            model.addAttribute("shulkerCount", result.shulkerBoxes());
            model.addAttribute("chestCount", result.doubleChests());
        
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        
            model.addAttribute("shulkerCount", 0);
            model.addAttribute("chestCount", 0);
        }

        return "index";
    }
}