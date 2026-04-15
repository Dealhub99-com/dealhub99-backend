package com.dealhub99.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seller_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String businessAddress;

    private String city;

    private Integer numberOfLocations;

    private String gstNumber;

    private String aadhaarNumber;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean promoted = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean promotionRequested = false;

    @Column(length = 1000)
    private String supportRequestMessage;

    // Contact info is typically in the User entity, but can be duplicated or extended here if needed.
}
