package com.fruitshop.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fruitshop.model.ProductIdNameProjection;
import com.fruitshop.repository.OrderDetailRepository;
import com.fruitshop.repository.SpRepository;
import com.fruitshop.utils.TimeUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fruitshop.constant.OrderStatus;
import com.fruitshop.dto.request.ProductRequest;
import com.fruitshop.dto.response.ProductDiscount;
import com.fruitshop.dto.response.ProductResponse;
import com.fruitshop.entity.Category;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.Product;
import com.fruitshop.exception.CustomException;
import com.fruitshop.mapper.ProductMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.CategoryRepository;
import com.fruitshop.repository.ProductRepository;
import com.fruitshop.service.ProductService;
import com.fruitshop.utils.AuthenticationUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private OrderDetailRepository orderDetailRepository;

  @Autowired
  private SpRepository spRepository;

  @Override
  public ResponseObject getAll() {
    List<ProductIdNameProjection> products = productRepository.findAllBy();

    return new ResponseObject(HttpStatus.ACCEPTED, "Lấy danh sách sản phẩm thành công", products);
  }



  @Override
  @Transactional
  public ResponseObject getPageProduct(Optional<Integer> pageNumber, Optional<Integer> amount, String keyword,
                                       String sortType) {


    String sanitizedKeyword = keyword.isEmpty() ?
        null : "%" + keyword + "%";

    Integer sanitizedSortType = mapSortTypeToNumber(sortType);

    System.out.println("Keyword: " + sanitizedKeyword);
    System.out.println("SortType: " + sanitizedSortType);
    System.out.println("Page: "+ pageNumber.get());
    System.out.println("Amount: "+ amount.get());
    Pageable pageable = PageRequest.of(pageNumber.orElse(0), amount.orElse(10));
    List<ProductRequest> productRequests = spRepository.getPageProduct(pageNumber.orElse(0), amount.orElse(10), sanitizedKeyword, sanitizedSortType);
//    Page<Product> productPage = productRepository.findProductsWithPaginationAndSorting(sanitizedKeyword,
//        sanitizedSortType, pageable);
    if (productRequests.isEmpty())
      return new ResponseObject(HttpStatus.ACCEPTED, "Danh sách sản phẩm bị trống", null);
    long total = keyword.isEmpty() ? productRepository.count(): productRepository.countByKeyword(sanitizedKeyword);
    List<Product> products = ProductMapper.INSTANT.requestToEntityList(productRequests);
    List<ProductResponse> productResponses = new ArrayList<ProductResponse>();
    for (Product product : products) {
      ProductResponse productResponse = getProductWithDiscount(product);
      Optional<Category> category = categoryRepository.findById(productResponse.getCategory().getId());
      category.ifPresent(productResponse::setCategory);
      productResponses.add(productResponse);
    }
    Page<ProductResponse> productResponsePage = new PageImpl<>(productResponses, pageable,
        total);
    return new ResponseObject(HttpStatus.OK, "Lấy danh sách sản phẩm thành công", productResponsePage);
  }

  @Override
  public ResponseObject getProductByid(Integer id) {
    Optional<Product> productDB = productRepository.findById(id);
    if (productDB.isEmpty()) throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm");
    Product product = productDB.get();
    ProductResponse productResponse = getProductWithDiscount(product);
    return new ResponseObject(HttpStatus.OK, "Lấy thông tin sản phẩm thành công", productResponse);
  }

  @Override
  public ProductDiscount getProductDiscount(Integer productId) {
    Object result = productRepository.getProductDiscount(productId);
    ProductDiscount productDiscount = new ProductDiscount(0f, null);
    if (result != null) {
      Object[] values = (Object[]) result;
      Float value = (Float) values[0];
      Timestamp expiryDate = (Timestamp) values[1];

      productDiscount.setValue(value);
      productDiscount.setExpiredDate(expiryDate);
    }
    return productDiscount;
  }

  @Override
  public ResponseObject deleteProduct(Integer id) {
    Optional<Product> productDB = productRepository.findById(id);
    if(productDB.isEmpty()) throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm");
    Product product = productDB.get();
    if(orderDetailRepository.existsByProduct(product)) throw new CustomException(HttpStatus.BAD_REQUEST, "Sản phẩm này đã có trong đơn hàng của người dùng, không thể xóa");
    productRepository.delete(product);
    return new ResponseObject(HttpStatus.ACCEPTED, "Xoá sản phẩm thành công", null);
  }

  @Override
  public ResponseObject createProduct(ProductRequest request) {
    Category category = validate(null, request);

    Product product = ProductMapper.INSTANT.requestToEnity(request);
    productRepository.save(product);

    ProductResponse productResponse = ProductMapper.INSTANT.entityToResponse(product);
    return new ResponseObject(HttpStatus.ACCEPTED, "Thêm sản phẩm thành công", productResponse);
  }

  @Override
  @Transactional
  public ResponseObject updateProduct(Integer id, ProductRequest request) {
    Category category = validate(id, request);
    Optional<Product> productDB = productRepository.findById(id);
    if (productDB.isEmpty()) throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm");
    Product product = productDB.get();
    product.setTitle(request.getTitle());
    product.setDescription(request.getDescription());
    product.setOrigin(request.getOrigin());
    product.setPrice(request.getPrice());
    product.setQuantity(request.getQuantity());
    product.setImage(request.getImage());
    product.setCategory(category);
    productRepository.save(product);
    return new ResponseObject(HttpStatus.ACCEPTED, "Sửa thông tin sản phẩm thành công", product);
  }

  private Category validate(Integer id, ProductRequest request) {
    if (request.getPrice() <= 0)
      throw  new CustomException(HttpStatus.BAD_REQUEST, "Giá sản phẩm không hợp lệ");
     if (request.getQuantity() <= 0)
       throw  new CustomException(HttpStatus.BAD_REQUEST, "Số lượng sản phẩm không hợp lệ");
    Product product = productRepository.findByTitle(request.getTitle());
    if (product != null && !Objects.equals(product.getId(), id))
       throw  new CustomException(HttpStatus.BAD_REQUEST, "Tên sản phẩm bị trùng");

    Optional<Category> categoryDB = categoryRepository.findById(request.getCategoryId());
    if (categoryDB.isEmpty())
       throw  new CustomException(HttpStatus.NOT_FOUND, "không tìm thấy danh mục sản phẩm");

    return categoryDB.get();
  }

  public Integer mapSortTypeToNumber(String sortType) {

    if (sortType.equals("price_asc"))
      return 1;
    if (sortType.equals("price_desc"))
      return 2;
    if (sortType.equals("discount_asc"))
      return 3;
    if (sortType.equals("discount_desc"))
      return 4;
    return 0;

  }


  @Transactional
  private ProductResponse getProductWithDiscount(Product product) {
    ProductResponse productResponse = ProductMapper.INSTANT.entityToResponse(product);
    ProductDiscount productDiscount = getProductDiscount(product.getId());
    productResponse.setDiscountPercentage(productDiscount.getValue());
    productResponse.setDiscountExpired(TimeUtils.timeStampToInstant(productDiscount.getExpiredDate()));
    return productResponse;
  }


}
