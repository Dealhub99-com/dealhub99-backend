package com.dealhub99.backend.service.impl;

import com.dealhub99.backend.entity.SellerProfile;
import com.dealhub99.backend.entity.User;
import com.dealhub99.backend.repository.SellerProfileRepository;
import com.dealhub99.backend.repository.UserRepository;
import com.dealhub99.backend.service.SellerProfileService;
import com.dealhub99.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerProfileServiceImpl implements SellerProfileService {

    private final SellerProfileRepository sellerProfileRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<SellerProfile> getProfileByUserId(Long userId) {
        return sellerProfileRepository.findByUserId(userId);
    }

    @Override
    public List<SellerProfile> searchSellers(String businessName, String city) {
        if (businessName == null) businessName = "";
        if (city == null) city = "";
        return sellerProfileRepository.findByBusinessNameContainingIgnoreCaseAndCityContainingIgnoreCaseAndActiveTrue(businessName, city);
    }

    @Override
    public List<SellerProfile> getAllActiveSellers() {
        return sellerProfileRepository.findByActiveTrue();
    }

    @Override
    public SellerProfile saveOrUpdateProfile(Long userId, SellerProfile profileData) {
        Optional<SellerProfile> existingProfile = sellerProfileRepository.findByUserId(userId);
        
        if (existingProfile.isPresent()) {
            SellerProfile profile = existingProfile.get();
            profile.setBusinessName(profileData.getBusinessName());
            profile.setBusinessAddress(profileData.getBusinessAddress());
            profile.setCity(profileData.getCity());
            profile.setNumberOfLocations(profileData.getNumberOfLocations());
            profile.setGstNumber(profileData.getGstNumber());
            profile.setAadhaarNumber(profileData.getAadhaarNumber());
            return sellerProfileRepository.save(profile);
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for profile creation."));
            profileData.setUser(user);
            return sellerProfileRepository.save(profileData);
        }
    }

    @Override
    public void requestPromotion(Long userId, String message) {
        SellerProfile profile = sellerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller profile not found."));
        profile.setPromotionRequested(true);
        profile.setSupportRequestMessage(message);
        sellerProfileRepository.save(profile);
    }

    @Override
    public void togglePromotion(Long userId, boolean status) {
        SellerProfile profile = sellerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller profile not found."));
        profile.setPromoted(status);
        profile.setPromotionRequested(false);
        sellerProfileRepository.save(profile);
    }
}
