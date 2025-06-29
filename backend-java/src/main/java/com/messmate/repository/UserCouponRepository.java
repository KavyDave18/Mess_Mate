package com.messmate.repository;

import com.messmate.model.UserCoupon;
import com.messmate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    List<UserCoupon> findByUser(User user);
} 