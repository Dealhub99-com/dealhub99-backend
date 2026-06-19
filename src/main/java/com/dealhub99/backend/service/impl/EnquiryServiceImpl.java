package com.dealhub99.backend.service.impl;

import com.dealhub99.backend.entity.Enquiry;
import com.dealhub99.backend.entity.Product;
import com.dealhub99.backend.entity.User;
import com.dealhub99.backend.repository.EnquiryRepository;
import com.dealhub99.backend.repository.UserRepository;
import com.dealhub99.backend.repository.ProductRepository;
import com.dealhub99.backend.service.EnquiryService;
import com.dealhub99.backend.service.ProductService;
import com.dealhub99.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnquiryServiceImpl implements EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public Enquiry createEnquiry(Long buyerId, Long productId, String message) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found."));
                
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
                
        // Check if user already enquired
        if (enquiryRepository.existsByBuyerIdAndProductId(buyerId, productId)) {
            throw new com.dealhub99.backend.exception.BadRequestException("You have already sent an enquiry for this product.");
        }
        
        User seller = product.getSeller();

        Enquiry enquiry = Enquiry.builder()
                .buyer(buyer)
                .seller(seller)
                .product(product)
                .message(message)
                .status("New")
                .build();
        
        Enquiry saved = enquiryRepository.save(enquiry);
        productService.incrementSales(productId);
        return saved;
    }

    @Override
    public List<Enquiry> getLeadsForSeller(Long sellerId) {
        return enquiryRepository.findBySellerIdOrderByCreatedAtDesc(sellerId);
    }

    @Override
    public List<Enquiry> getInterestsForBuyer(Long buyerId) {
        return enquiryRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId);
    }

    @Override
    public void updateStatus(Long enquiryId, String status) {
        Enquiry e = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new ResourceNotFoundException("Enquiry not found."));
        e.setStatus(status);
        enquiryRepository.save(e);
    }

    @Override
    public void deleteEnquiry(Long id) {
        if (!enquiryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enquiry not found.");
        }
        enquiryRepository.deleteById(id);
    }
}
