package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final CouponRepository couponRepository;

    public void applyCoupon(Long userId){
        final long count = couponRepository.count();

        if(count > 100) return;

        Coupon coupon = new Coupon(userId);

        couponRepository.save(coupon);
    }

}