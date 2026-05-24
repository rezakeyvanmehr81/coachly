package org.example.coachly.controller;

import org.example.coachly.model.Item;
import org.example.coachly.model.User;
import org.example.coachly.service.ItemService;
import org.example.coachly.service.RentalService;
import org.example.coachly.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final ItemService itemService;
    private final UserService userService;

    public RentalController(RentalService rentalService, ItemService itemService,
                            UserService userService) {
        this.rentalService = rentalService;
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping
    public String createRental(@RequestParam Long itemId,
                               @RequestParam String startDate,
                               @RequestParam String endDate,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            User borrower = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Item item = itemService.getItemById(itemId)
                    .orElseThrow(() -> new RuntimeException("Coaching service not found"));

            // Prevent booking your own coaching service
            if (item.getOwner().getId().equals(borrower.getId())) {
                redirectAttributes.addFlashAttribute("error", "You cannot book your own coaching service.");
                return "redirect:/items/" + itemId;
            }

            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            // Server-side past date check
            if (start.isBefore(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("error", "Start date cannot be in the past.");
                return "redirect:/items/" + itemId;
            }

            rentalService.createRental(item, borrower, start, end);
            redirectAttributes.addFlashAttribute("message", "Session booked successfully!");
            return "redirect:/dashboard";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/items/" + itemId;
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelRental(@PathVariable Long id,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean cancelled = rentalService.cancelRental(id, user);
        if (cancelled) {
            redirectAttributes.addFlashAttribute("message", "Booking cancelled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Unable to cancel this booking.");
        }

        return "redirect:/dashboard";
    }
}

