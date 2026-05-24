package org.example.coachly.controller;

import org.example.coachly.model.Item;
import org.example.coachly.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final ItemService itemService;

    public HomeController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Item> items = itemService.getAllItems();
        model.addAttribute("items", items);
        model.addAttribute("itemCount", items.size());
        return "index";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            Model model) {

        List<Item> items = itemService.searchWithFilters(query, category, maxPrice);
        model.addAttribute("items", items);
        model.addAttribute("itemCount", items.size());
        model.addAttribute("searchQuery", query);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedMaxPrice", maxPrice);
        return "index";
    }
}

