package com.fruitshop.service.impl;

import com.fruitshop.dto.request.DiscountRequest;
import com.fruitshop.dto.response.DiscountResponse;
import com.fruitshop.entity.Discount;
import com.fruitshop.entity.DiscountDetail;
import com.fruitshop.entity.Product;
import com.fruitshop.exception.CustomException;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.DiscountDetailRepository;
import com.fruitshop.repository.DiscountRepository;
import com.fruitshop.repository.ProductRepository;
import com.fruitshop.service.DiscountService;
import com.fruitshop.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

  @Autowired
  private DiscountRepository discountRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private DiscountDetailRepository discountDetailRepository;

  @Override
  public ResponseObject getPageDiscountNow(Optional<Integer> page, Optional<Integer> amount) {
    Pageable pageable = PageRequest.of(page.orElse(0), amount.orElse(10));
    Instant now = TimeUtils.getInstantNow();
    Page<Discount> discountPage = discountRepository.findByExpiryDateAfter(now, pageable);
    if (discountPage.isEmpty())
      return new ResponseObject(HttpStatus.ACCEPTED, "Danh sách khuyến mãi trống", null);
    List<Discount> discounts = discountPage.getContent();
    List<DiscountResponse> discountResponses = new ArrayList<>();
    for (Discount discount : discounts) {
      List<DiscountDetail> discountDetails = discountDetailRepository.findByDiscount(discount);
      List<Product> products = discountDetails.stream()
          .map(DiscountDetail::getProduct)
          .collect(Collectors.toList());
      DiscountResponse discountResponse = DiscountResponse.builder().id(discount.getId()).effectiveDate(discount.getEffectiveDate()).expiryDate(discount.getExpiryDate()).value(discount.getValue()).products(products).build();
      discountResponses.add(discountResponse);
    }
    Page<DiscountResponse> discountResponsePage = new PageImpl<>(discountResponses, pageable, discountPage.getTotalElements());

    return new ResponseObject(HttpStatus.OK, "Lấy danh sách discount thành công", discountResponsePage);
  }

  @Override
  @Transactional
  public ResponseObject createDiscount(DiscountRequest request) {
    validateDiscountRequest(request);
    Instant effectiveDate = TimeUtils.gmt_7(request.getEffectiveDate());
    Instant expiryDate = TimeUtils.gmt_7(request.getExpiryDate());

    Map<String, String> errors = new HashMap<>();
    if(effectiveDate.isBefore(TimeUtils.getInstantNow())) errors.put("effectiveDate","Thời gian bắt đầu khuyến mãi không hợp lệ" );
    if(expiryDate.isBefore(effectiveDate.plusMillis(1000*60))) errors.put("expiryDate", "Thời gian khuyến mãi không được ít hơn 1 phút");
    if (!errors.isEmpty()) {
      return new ResponseObject(HttpStatus.UNPROCESSABLE_ENTITY, "Thông tin không hợp lệ", errors);
    }

    Discount discount = Discount.builder()
        .effectiveDate(effectiveDate)
        .expiryDate(expiryDate)
        .value(request.getValue()).build();
    discountRepository.save(discount);
    for (Integer productId : request.getProducts()) {
      Optional<Product> productOptional = productRepository.findById(productId);
      if (productOptional.isPresent()) {
        Product product = productOptional.get();
        DiscountDetail discountDetail = DiscountDetail.builder()
            .product(product)
            .discount(discount)
            .build();
        discountDetailRepository.save(discountDetail);
      } else {
        throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm");
      }
    }
    return new ResponseObject(HttpStatus.ACCEPTED, "Thêm khuyến mãi thành công", discount);
  }

  @Override
  @Transactional
  public ResponseObject updateDiscount(Integer id, DiscountRequest request) {
    validateDiscountRequest(request);
    Optional<Discount> discountDB = discountRepository.findById(id);
    if (discountDB.isEmpty()) throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy khuyến mãi");
    Discount discount = discountDB.get();
    discount.setValue(request.getValue());
    discountRepository.save(discount);

    List<DiscountDetail> discountDetails = discountDetailRepository.findByDiscount(discount);

    List<Integer> existingProductIds = discountDetails.stream()
        .map(DiscountDetail::getProduct)
        .map(Product::getId)
        .toList();

    for (Integer productId : request.getProducts()) {
      if (!existingProductIds.contains(productId)) {
        DiscountDetail newDetail = new DiscountDetail();
        newDetail.setDiscount(discount);
        Optional<Product> productDB = productRepository.findById(productId);
        if(productDB.isEmpty()) throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm");
        newDetail.setProduct(productDB.get());
        discountDetailRepository.save(newDetail);
      }
    }

    for (DiscountDetail discountDetail : discountDetails) {
      Integer productId = discountDetail.getProduct().getId();
      if (!request.getProducts().contains(productId)) {
        discountDetailRepository.delete(discountDetail);
      }
    }

    return new ResponseObject(HttpStatus.ACCEPTED, "Sửa thông tin khuyến mãi thành công", discount);
  }

  private void validateDiscountRequest(DiscountRequest request) {
    if (request.getEffectiveDate() == null) {
      throw new CustomException(HttpStatus.BAD_REQUEST, "Ngày bắt đầu không được để trống.");
    }

    if (request.getExpiryDate() == null) {
      throw new CustomException(HttpStatus.BAD_REQUEST, "Ngày hết hạn không được để trống.");
    }

    if (request.getExpiryDate().isBefore(request.getEffectiveDate())) {
      throw new CustomException(HttpStatus.BAD_REQUEST, "Thời gian hết hạn phải sau ngày bắt đầu.");
    }

    if (request.getValue() == null || request.getValue() <= 0 ||request.getValue() >100) {
      throw new CustomException(HttpStatus.BAD_REQUEST, "Giá trị giảm từ 0 - 100.");
    }

    if (request.getProducts() == null || request.getProducts().isEmpty()) {
      throw new CustomException(HttpStatus.BAD_REQUEST, "Danh sách sản phẩm không được để trống");
    }
  }

}
