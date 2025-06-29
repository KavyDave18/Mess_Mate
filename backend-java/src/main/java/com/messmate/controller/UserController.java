package com.messmate.controller;

import com.messmate.model.User;
import com.messmate.model.UserCoupon;
import com.messmate.repository.UserRepository;
import com.messmate.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;

    @GetMapping("/{id}/coins")
    public ResponseEntity<?> getCoinBalance(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(user -> ResponseEntity.ok(user.getCoins()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/coupons")
    public ResponseEntity<?> getUserCoupons(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();
        List<UserCoupon> coupons = userCouponRepository.findByUser(userOpt.get());
        return ResponseEntity.ok(coupons);
    }
} 