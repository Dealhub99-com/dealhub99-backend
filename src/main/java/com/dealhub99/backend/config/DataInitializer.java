package com.dealhub99.backend.config;

import com.dealhub99.backend.entity.*;
import com.dealhub99.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.existsByEmail("admin@dealhub99.com")) {
            return; // Admin already exists, assume initialized
        }

        // 1. Create Special Master User (Admin)
        User admin = User.builder()
                .fullName("System Admin")
                .email("admin@dealhub99.com")
                .mobileNumber("9999988888")
                .password(passwordEncoder.encode("admin123"))
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(admin);

        // 2. Create a Test Seller
        User seller = User.builder()
                .fullName("Manjunath Store")
                .email("seller@test.com")
                .mobileNumber("9876543210")
                .password(passwordEncoder.encode("password123"))
                .role(UserRole.SELLER)
                .build();
        userRepository.save(seller);

        SellerProfile profile = SellerProfile.builder()
                .user(seller)
                .businessName("Manjunath Premium Electronics")
                .businessAddress("123, Tech Street, Indiranagar")
                .city("Bangalore")
                .active(true)
                .promoted(true)
                .build();
        sellerProfileRepository.save(profile);

        // 3. Create Categories
        Category cars = Category.builder().name("Cars").active(true).build();
        Category bikes = Category.builder().name("Bikes").active(true).build();
        Category mobiles = Category.builder().name("Mobiles").active(true).build();
        Category laptops = Category.builder().name("Laptops").active(true).build();
        categoryRepository.saveAll(List.of(cars, bikes, mobiles, laptops));

        // 4. Create Brands
        Brand apple = Brand.builder().name("Apple").active(true).build();
        Brand samsung = Brand.builder().name("Samsung").active(true).build();
        Brand tesla = Brand.builder().name("Tesla").active(true).build();
        Brand bmw = Brand.builder().name("BMW").active(true).build();
        brandRepository.saveAll(List.of(apple, samsung, tesla, bmw));

        // 5. Create Products
        Product p1 = Product.builder()
                .name("iPhone 15 Pro")
                .description("Titanium body, A17 Pro chip. Brand new sealed unit from USA.")
                .price(124900.0)
                .category(mobiles)
                .brand(apple)
                .seller(seller)
                .productType("Brand New")
                .status("Active")
                .approved(true)
                .imageUrl("https://images.unsplash.com/photo-1696446701796-da6122569766?w=800")
                .build();

        Product p2 = Product.builder()
                .name("Tesla Model 3 - 2023")
                .description("Fully Loaded with Autopilot. Single owner, white interior. 5000km driven.")
                .price(4500000.0)
                .category(cars)
                .brand(tesla)
                .seller(seller)
                .productType("Used")
                .status("Active")
                .approved(true)
                .imageUrl("https://images.unsplash.com/photo-1560958089-b8a1929cea89?w=800")
                .build();

        Product p3 = Product.builder()
                .name("MacBook Pro M3 Max")
                .description("14-inch, 128GB RAM, 2TB SSD. For high-end creative work.")
                .price(350000.0)
                .category(laptops)
                .brand(apple)
                .seller(seller)
                .productType("Refurbished")
                .status("Active")
                .approved(true)
                .imageUrl("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800")
                .build();

        productRepository.saveAll(List.of(p1, p2, p3));

        System.out.println("Data initialization completed successfully.");
    }
}
