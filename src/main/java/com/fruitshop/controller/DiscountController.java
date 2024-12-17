package com.fruitshop.controller;

import com.fruitshop.constant.ApiPath;
import com.fruitshop.dto.request.DiscountRequest;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.service.DiscountService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(ApiPath.DISCOUNT)
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getDiscountNow(@RequestParam("page") Optional<Integer> pageNumber,
			@RequestParam("amount") Optional<Integer> amount)
    {
        ResponseObject responseObject = discountService.getPageDiscountNow(pageNumber, amount);
        return ResponseEntity.ok(responseObject);
    }

    @PostMapping("")
  public ResponseEntity<ResponseObject> createDiscount(@RequestBody DiscountRequest request){
      ResponseObject responseObject = discountService.createDiscount(request);
        return ResponseEntity.ok(responseObject);
    }
}
