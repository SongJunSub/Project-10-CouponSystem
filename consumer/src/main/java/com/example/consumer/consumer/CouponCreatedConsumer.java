package com.example.consumer.consumer;

import com.example.consumer.domain.Coupon;
import com.example.consumer.domain.FailedEvent;
import com.example.consumer.repository.CouponRepository;
import com.example.consumer.repository.FailedEventRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;
    private final FailedEventRepository failedEventRepository;
    private final Logger logger = LoggerFactory.getLogger(CouponCreatedConsumer.class);

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId){
        try {
            Coupon coupon = new Coupon(userId);

            couponRepository.save(coupon);
        }
        catch (Exception e){
            logger.error("Failed to create coupon::" + userId);

            FailedEvent failedEvent = new FailedEvent(userId);

            failedEventRepository.save(failedEvent);
        }
    }

}