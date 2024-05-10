package com.example.api.service;

import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    CouponCountRepository couponCountRepository;

    @Test
    public void applyOnlyOnce(){
        applyService.applyCoupon(1L);

        long count = couponRepository.count();

        assertThat(count).isEqualTo(1);
    }

    //해당 테스트 케이스로는 레이스 컨디션이 발생함으로서 테스트 실패함
    //레이스 컨디션 : 두 개 이상의 쓰레드가 공유 데이터에 엑세스를 하고, 동시에 작업을 하려고 할 때 발생하는 문제
    @Test
    public void multiplePeopleApply() throws InterruptedException {
        //동시에 여러 개의 요청을 보내야 되므로 멀티 쓰레드를 사용 (1000개의 요청으로 가정)
        int threadCount = 1000;

        //ExecutorService : 병렬 작업을 간단하게 할 수 있게 도와주는 Java의 API
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        //모든 요청이 끝날 때까지 기다려야 하므로 CountDownLatch를 사용
        //CountDownLatch : 다른 Thread에서 수행하는 작업을 기다리도록 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++){
            long userId = i;

            executorService.submit(() -> {
                try {
                    applyService.applyCoupon(userId);
                }
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long count = couponRepository.count();

        assertThat(count).isEqualTo(100);

        //couponCountRepository.flushAll();
    }

}