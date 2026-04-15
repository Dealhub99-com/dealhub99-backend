package com.dealhub99.backend.service;

import com.dealhub99.backend.entity.SellerProfile;
import java.util.List;
import java.util.Optional;

public interface SellerProfileService {
    Optional<SellerProfile> getProfileByUserId(Long userId);
    List<SellerProfile> searchSellers(String businessName, String city);
    List<SellerProfile> getAllActiveSellers();
    SellerProfile saveOrUpdateProfile(Long userId, SellerProfile profileData);
    void requestPromotion(Long userId, String message);
    void togglePromotion(Long userId, boolean status);
}
