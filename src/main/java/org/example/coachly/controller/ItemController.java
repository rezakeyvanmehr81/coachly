package org.example.coachly.controller;

import org.example.coachly.model.Item;
import org.example.coachly.model.User;
import org.example.coachly.service.ItemService;
import org.example.coachly.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String showCreateForm() {
        return "lend-item";
    }

    @PostMapping("/new")
    public String createItem(@RequestParam String name,
                             @RequestParam String description,
                             @RequestParam String category,
                             @RequestParam Double pricePerDay,
                             @RequestParam Double securityDeposit,
                             @RequestParam String location,
                             @RequestParam(required = false) String imageUrl,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        User owner = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Default image if none provided
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            imageUrl = "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=400";
        }

        itemService.createItem(name, description, category, pricePerDay,
                securityDeposit, location, imageUrl, owner);

        redirectAttributes.addFlashAttribute("message", "Coaching service published successfully!");
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}")
    public String showItemDetail(@PathVariable Long id, Model model) {
        Optional<Item> itemOpt = itemService.getItemById(id);
        if (itemOpt.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("item", itemOpt.get());
        return "product-detail";
    }

    @PostMapping("/{id}/delete")
    public String deleteItem(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean deleted = itemService.deleteItem(id, user);
        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "Coaching service deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this coaching service.");
        }

        return "redirect:/dashboard";
    }
}

