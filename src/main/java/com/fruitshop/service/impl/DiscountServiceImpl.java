package com.fruitshop.service.impl;

import com.fruitshop.entity.Discount;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.DiscountRepository;
import com.fruitshop.service.DiscountService;
import com.fruitshop.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountRepository discountRepository;
    @Override
    public ResponseObject getPageDiscountNow(Optional<Integer> page, Optional<Integer> amount) {
        Pageable pageable = PageRequest.of(page.orElse(0), amount.orElse(10));
        Instant now = TimeUtils.getInstantNow();
        Page<Discount> discounts =  discountRepository.findByExpiryDateAfter(now, pageable);
        return new ResponseObject(HttpStatus.OK, "Lấy danh sách discount thành công", discounts);
    }
}
