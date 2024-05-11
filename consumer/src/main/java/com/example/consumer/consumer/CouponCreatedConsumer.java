package com.example.consumer.consumer;

import com.example.consumer.domain.Coupon;
import com.example.consumer.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId){
        Coupon coupon = new Coupon(userId);

        couponRepository.save(coupon);
    }

}