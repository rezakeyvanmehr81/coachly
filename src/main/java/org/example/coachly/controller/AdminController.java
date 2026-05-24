package org.example.coachly.controller;

import org.example.coachly.model.Item;
import org.example.coachly.model.Rental;
import org.example.coachly.model.User;
import org.example.coachly.service.ItemService;
import org.example.coachly.service.RentalService;
import org.example.coachly.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ItemService itemService;
    private final RentalService rentalService;

    public AdminController(UserService userService, ItemService itemService, RentalService rentalService) {
        this.userService = userService;
        this.itemService = itemService;
        this.rentalService = rentalService;
    }

    @GetMapping
    public String adminDashboard(Model model, Principal principal) {
        if (principal != null) {
            Optional<User> currentUser = userService.findByEmail(principal.getName());
            currentUser.ifPresent(user -> model.addAttribute("user", user));
        }

        List<User> users = userService.getAllUsers();
        List<Item> items = itemService.getAllItems();
        List<Rental> rentals = rentalService.getAllRentals();

        model.addAttribute("users", users);
        model.addAttribute("items", items);
        model.addAttribute("rentals", rentals);

        return "admin-dashboard";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUserAdmin(id);
            redirectAttributes.addFlashAttribute("successMessage", "User successfully deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user. They might have active dependencies.");
        }
        return "redirect:/admin";
    }

    @PostMapping("/items/{id}/delete")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            itemService.deleteItemAdmin(id);
            redirectAttributes.addFlashAttribute("successMessage", "Coaching service successfully deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete coaching service.");
        }
        return "redirect:/admin";
    }

    @PostMapping("/rentals/{id}/delete")
    public String deleteRental(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rentalService.deleteRentalAdmin(id);
            redirectAttributes.addFlashAttribute("successMessage", "Booking successfully deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete booking.");
        }
        return "redirect:/admin";
    }
}

