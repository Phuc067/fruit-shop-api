package com.fruitshop.service;

import com.fruitshop.model.ResponseObject;

import java.util.Optional;

public interface DiscountService {
    ResponseObject getPageDiscountNow(Optional<Integer> page, Optional<Integer> amount);
}
