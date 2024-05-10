package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;

    public void applyCoupon(Long userId){
        //final long count = couponRepository.count();
        final Long count = couponCountRepository.increment();

        if(count > 100) return;

        //Coupon coupon = new Coupon(userId);

        //couponRepository.save(coupon);
        couponCreateProducer.create(userId);
    }

}