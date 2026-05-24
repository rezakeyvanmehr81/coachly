package org.example.coachly.config;

import org.example.coachly.model.Item;
import org.example.coachly.model.User;
import org.example.coachly.repository.ItemRepository;
import org.example.coachly.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds the database with the admin account and sample data on startup.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, ItemRepository itemRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String adminEmail = "admin@coachly.com";
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setFullName("Coachly Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
            System.out.println("Default Admin account created: " + adminEmail);
        }

        if (userRepository.count() > 1) {
            return;
        }

        User maya = userRepository.save(new User("Maya Carter", "maya@example.com",
                passwordEncoder.encode("password123")));
        User daniel = userRepository.save(new User("Daniel Brooks", "daniel@example.com",
                passwordEncoder.encode("password123")));
        User elena = userRepository.save(new User("Elena Rossi", "elena@example.com",
                passwordEncoder.encode("password123")));

        Item strength = new Item();
        strength.setName("Beginner Strength Coaching");
        strength.setDescription("A focused one-on-one strength session for clients who want safer form, a clear gym routine, and confidence with basic lifts. Includes movement assessment, coaching cues, and a simple follow-up plan.");
        strength.setCategory("Strength");
        strength.setPricePerDay(45.0);
        strength.setSecurityDeposit(20.0);
        strength.setLocation("Downtown Gym or Online");
        strength.setImageUrl("https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=1080");
        strength.setOwner(maya);
        itemRepository.save(strength);

        Item boxing = new Item();
        boxing.setName("1-on-1 Boxing Fundamentals");
        boxing.setDescription("Private boxing coaching for beginners and returning athletes. Learn stance, footwork, jab-cross mechanics, defensive movement, and conditioning drills in a structured session.");
        boxing.setCategory("Boxing");
        boxing.setPricePerDay(55.0);
        boxing.setSecurityDeposit(25.0);
        boxing.setLocation("Brooklyn Training Studio");
        boxing.setImageUrl("https://images.unsplash.com/photo-1549719386-74dfcbf7dbed?w=1080");
        boxing.setOwner(daniel);
        itemRepository.save(boxing);

        Item nutrition = new Item();
        nutrition.setName("Online Nutrition Coaching");
        nutrition.setDescription("A practical nutrition coaching session built around your goals, schedule, and food preferences. Includes habit review, meal structure guidance, and realistic weekly actions.");
        nutrition.setCategory("Nutrition");
        nutrition.setPricePerDay(40.0);
        nutrition.setSecurityDeposit(15.0);
        nutrition.setLocation("Online");
        nutrition.setImageUrl("https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=1080");
        nutrition.setOwner(elena);
        itemRepository.save(nutrition);

        Item yoga = new Item();
        yoga.setName("Yoga Mobility Session");
        yoga.setDescription("A guided mobility and yoga session for better posture, flexibility, and recovery. Ideal for desk workers, runners, and anyone who wants to move with less tension.");
        yoga.setCategory("Yoga");
        yoga.setPricePerDay(35.0);
        yoga.setSecurityDeposit(15.0);
        yoga.setLocation("Queens Wellness Studio");
        yoga.setImageUrl("https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=1080");
        yoga.setOwner(maya);
        itemRepository.save(yoga);

        System.out.println("=== Coachly: Sample data initialized ===");
        System.out.println("Demo accounts: maya@example.com / daniel@example.com / elena@example.com");
        System.out.println("Password for all: password123");
    }
}

