package com.messmate.controller;

import com.messmate.model.*;
import com.messmate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/redeem")
public class RedeemController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;

    @PostMapping("/{userId}/coupon/{couponId}")
    public ResponseEntity<?> redeemCoupon(@PathVariable Long userId, @PathVariable Long couponId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Coupon> couponOpt = couponRepository.findById(couponId);
        if (userOpt.isEmpty() || couponOpt.isEmpty()) return ResponseEntity.notFound().build();
        User user = userOpt.get();
        Coupon coupon = couponOpt.get();
        int cost = coupon.getValue(); // You may want to store cost separately
        if (user.getCoins() < cost) {
            return ResponseEntity.badRequest().body("Insufficient coins");
        }
        user.setCoins(user.getCoins() - cost);
        userRepository.save(user);
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUser(user);
        userCoupon.setCoupon(coupon);
        userCoupon.setUsed(false);
        userCouponRepository.save(userCoupon);
        return ResponseEntity.ok("Coupon redeemed successfully");
    }
} 