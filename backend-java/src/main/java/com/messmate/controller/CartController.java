package com.messmate.controller;

import com.messmate.model.*;
import com.messmate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();
        List<CartItem> cart = cartItemRepository.findByUser(userOpt.get());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addToCart(@PathVariable Long userId, @RequestBody CartItem item) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();
        item.setUser(userOpt.get());
        cartItemRepository.save(item);
        return ResponseEntity.ok("Item added to cart");
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<?> checkout(@PathVariable Long userId, @RequestParam(required = false) Long userCouponId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();
        User user = userOpt.get();
        List<CartItem> cart = cartItemRepository.findByUser(user);
        double subtotal = cart.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        double tax = subtotal * 0.085;
        double delivery = subtotal > 500 ? 0 : (subtotal > 0 ? 49 : 0);
        double discount = 0;
        if (userCouponId != null) {
            Optional<UserCoupon> userCouponOpt = userCouponRepository.findById(userCouponId);
            if (userCouponOpt.isPresent() && !userCouponOpt.get().isUsed()) {
                Coupon coupon = userCouponOpt.get().getCoupon();
                if (coupon.getType().equals("percentage")) {
                    discount = subtotal * (coupon.getValue() / 100.0);
                } else if (coupon.getType().equals("fixed")) {
                    discount = coupon.getValue();
                } else if (coupon.getType().equals("free_delivery")) {
                    discount = delivery;
                }
                userCouponOpt.get().setUsed(true);
                userCouponRepository.save(userCouponOpt.get());
            }
        }
        double total = Math.max(0, subtotal + tax + delivery - discount);
        int coinsEarned = (int) Math.floor(total * 2);
        user.setCoins(user.getCoins() + coinsEarned);
        userRepository.save(user);
        cartItemRepository.deleteAll(cart);
        return ResponseEntity.ok("Order placed. Coins earned: " + coinsEarned);
    }
} 