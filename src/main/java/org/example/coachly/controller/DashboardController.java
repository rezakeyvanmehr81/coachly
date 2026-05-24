package org.example.coachly.controller;

import org.example.coachly.model.Item;
import org.example.coachly.model.Rental;
import org.example.coachly.model.User;
import org.example.coachly.service.ItemService;
import org.example.coachly.service.RentalService;
import org.example.coachly.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    private final ItemService itemService;
    private final RentalService rentalService;

    public DashboardController(UserService userService, ItemService itemService,
                               RentalService rentalService) {
        this.userService = userService;
        this.itemService = itemService;
        this.rentalService = rentalService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Sessions the user booked as a client
        List<Rental> myRentals = rentalService.getRentalsByBorrower(user);

        // Coaching services the user offers
        List<Item> myListings = itemService.getItemsByOwner(user);

        // Bookings for the user's coaching services (income tracking)
        List<Rental> incomingRentals = rentalService.getRentalsByItemOwner(user);

        // Stats
        long activeRentals = myRentals.stream()
                .filter(r -> r.getStatus().name().equals("ACTIVE") || r.getStatus().name().equals("PENDING"))
                .count();
        double totalEarned = incomingRentals.stream()
                .mapToDouble(Rental::getTotalCost)
                .sum();

        model.addAttribute("user", user);
        model.addAttribute("myRentals", myRentals);
        model.addAttribute("myListings", myListings);
        model.addAttribute("activeRentals", activeRentals);
        model.addAttribute("activeListings", myListings.size());
        model.addAttribute("totalEarned", totalEarned);

        return "dashboard";
    }
}

