package com.fruitshop.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fruitshop.entity.*;
import com.fruitshop.service.ProductService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fruitshop.constant.OrderStatus;
import com.fruitshop.constant.PaymentMethod;
import com.fruitshop.dto.request.OrderRequest;
import com.fruitshop.dto.response.OrderReponse;
import com.fruitshop.dto.response.ProductDiscount;
import com.fruitshop.exception.CustomException;
import com.fruitshop.mapper.OrderMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.CartDetailRepository;
import com.fruitshop.repository.InvoiceRepository;
import com.fruitshop.repository.OrderDetailRepository;
import com.fruitshop.repository.OrderLogRepository;
import com.fruitshop.repository.OrderRepository;
import com.fruitshop.repository.ProductRepository;
import com.fruitshop.repository.ShippingInformationRepository;
import com.fruitshop.repository.UserRepository;
import com.fruitshop.service.OrderService;
import com.fruitshop.utils.AuthenticationUtils;
import com.fruitshop.utils.RandomUtils;
import com.fruitshop.utils.TimeUtils;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  private ShippingInformationRepository shippingInformationRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private CartDetailRepository cartDetailRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderDetailRepository orderDetailRepository;

  @Autowired
  private OrderLogRepository orderLogRepository;

  @Autowired
  private InvoiceRepository invoiceRepository;

  @Autowired
  private ProductService productService;

  @Override
  @Transactional
  public ResponseObject createOrder(OrderRequest request) {

    Optional<ShippingInformation> shippingInfoDB = shippingInformationRepository.findById(request.getShippingInformationId());

    if (shippingInfoDB.isEmpty()) {
      throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy địa chỉ này");
    }

    Optional<User> userDB = userRepository.findById(request.getUserId());
    if (userDB.isEmpty()) throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng");
    User user = userDB.get();

    if (!AuthenticationUtils.isAuthenticate(user.getLogin().getUsername()))
      throw new CustomException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này");

    ShippingInformation shippingInfo = shippingInfoDB.get();

    OrderStatus orderStatus = request.getPaymentMethod().equals(PaymentMethod.VNPAY.getDisplayName()) ? OrderStatus.AWAITING_PAYMENT : OrderStatus.PENDING;

    Instant orderTime = TimeUtils.getInstantNow();
    Order order = Order.builder().id(RandomUtils.getUniqueId()).orderDate(orderTime).state(orderStatus).recipientName(shippingInfo.getRecipientName()).recipientAddress(shippingInfo.getShippingAdress()).phoneNumber(shippingInfo.getPhone()).paymentMethod(request.getPaymentMethod()).user(user).build();

    orderRepository.save(order);

    List<CartDetail> cartDetails = new ArrayList<CartDetail>();

    for (Integer cartId : request.getCartList()) {
      Optional<CartDetail> cartDetailDB = cartDetailRepository.findById(cartId);
      if (cartDetailDB.isEmpty())
        throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm trong giỏ hàng");
      CartDetail cartDetail = cartDetailDB.get();
      if (!cartDetail.getUser().equals(user))
        throw new CustomException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này");
      cartDetails.add(cartDetail);
    }

    List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

    for (CartDetail cartDetail : cartDetails) {

      Product product = productRepository.findProductForUpdate(cartDetail.getProduct().getId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại"));

      if (product.getQuantity() < cartDetail.getQuantity())
        throw new CustomException(HttpStatus.FORBIDDEN, "Sản phẩm " + product.getTitle() + " Không còn đủ số lượng");


      ProductDiscount productDiscount = productService.getProductDiscount(product.getId());

      Double salePrice = product.getPrice() * (1 - productDiscount.getValue() / 100);

      OrderDetail orderDetail = OrderDetail.builder().Quantity(cartDetail.getQuantity()).price(product.getPrice()).order(order).salePrice(salePrice).product(product).build();
      orderDetails.add(orderDetail);

      Integer oldQuantity = product.getQuantity();
      Integer newQuantity = oldQuantity - cartDetail.getQuantity();
      product.setQuantity(newQuantity);
      productRepository.save(product);

      cartDetailRepository.delete(cartDetail);
    }

    orderDetailRepository.saveAll(orderDetails);

    OrderLog orderLog = OrderLog.builder().order(order).state(orderStatus).PerformedBy(user.getLogin().getUsername()).time(orderTime).log(orderStatus.getLogMessage()).build();
    orderLogRepository.save(orderLog);
    return new ResponseObject(HttpStatus.ACCEPTED, "Tạo đơn hàng thành công", order);
  }

  @Override
  public ResponseObject getPageOrder(Integer userId, Optional<Integer> pageNumber, Optional<Integer> amount, String state) {
    try {
      Pageable pageable = PageRequest.of(pageNumber.orElse(0), amount.orElse(10));
      Page<Order> orderPage = getPageOrderByRole(userId, pageable, state);

      List<OrderReponse> orderReponses = OrderMapper.INSTANCE.entitysToResponses(orderPage.getContent());

      for (OrderReponse orderReponse : orderReponses) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderReponse.getId());
        orderReponse.setOrderDetails(orderDetails);
        OrderLog lastOrderLog = orderLogRepository.findByOrderIdAndState(orderReponse.getId(), OrderStatus.fromDisplayName(orderReponse.getState()));
        orderReponse.setOrderLog(lastOrderLog);
        Boolean isPaid = invoiceRepository.existsByOrderId(orderReponse.getId());
        orderReponse.setIsPaid(isPaid);
      }

      Page<OrderReponse> orderReponsePage = new PageImpl<>(orderReponses, pageable, orderPage.getTotalElements());

      return new ResponseObject(HttpStatus.OK, "Lấy danh sách đơn hàng thành công.", orderReponsePage);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "Lấy danh sách đơn hàng thất bại.", null);
    }
  }

  private Page<Order> getPageOrderByRole(Integer userId, Pageable pageable, String state) {
    List<OrderStatus> states = new ArrayList<OrderStatus>();

    if (!state.equals("")) {
      states = OrderStatus.parseStates(state);
    }
    Optional<User> userDB = Optional.empty();
    if (ObjectUtils.isNotEmpty(userId)) {
      userDB = userRepository.findById(userId);
      if (userDB.isEmpty()) throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy ngươì dùng");
    }
    Boolean isAdminAccess = AuthenticationUtils.isAdminAccess();
    if (isAdminAccess) {
      System.out.println("userId: " + userId + ",state: " + state);
      if (ObjectUtils.isNotEmpty(userId) && !state.equals(""))
        return orderRepository.findByUserIdAndState(userId, states, pageable);
      if (ObjectUtils.isNotEmpty(userId) && state.equals("")) return orderRepository.findByUserId(userId, pageable);
      if (ObjectUtils.isEmpty(userId) && !state.equals("")) return orderRepository.findByState(states, pageable);
      return orderRepository.findByState(states, pageable);
    } else {
      User user = userDB.get();
      if (!AuthenticationUtils.isAuthenticate(user.getLogin().getUsername()))
        throw new CustomException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này");
      return state.equals("") ? orderRepository.findByUserId(userId, pageable) : orderRepository.findByUserIdAndState(userId, states, pageable);
    }
  }

  @Override
  @Transactional
  public ResponseObject updateStatus(String id) {
    Optional<Order> orderDB = orderRepository.findById(id);
    if (orderDB.isEmpty()) return new ResponseObject(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng", null);

    Order order = orderDB.get();

    OrderStatus orderStatus = order.getState();

    Boolean isAdminAccess = AuthenticationUtils.isAdminAccess();

    if (orderStatus.requiresAdminAccess() && !isAdminAccess)
      return new ResponseObject(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này", null);
    else if (!isAdminAccess && !AuthenticationUtils.isAuthenticate(order.getUser().getLogin().getUsername()) )
      return new ResponseObject(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này", null);

    Instant now = TimeUtils.getInstantNow();
    OrderStatus nextStatus = orderStatus.getNextStatus();

    if (nextStatus.equals(OrderStatus.DELIVERED) && !order.getPaymentMethod().equals(PaymentMethod.VNPAY.getDisplayName())) {
      List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
      Double total = calculateTotalAmount(orderDetails);
      Invoice invoice = Invoice.builder().order(order).date(now).totalAmount(total).build();
      invoiceRepository.save(invoice);
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();
    UpdateOrderStateAndInsertLog(order, nextStatus, now, authUsername);
    return new ResponseObject(HttpStatus.ACCEPTED, "Cập nhật trạng thái đơn hàng thành công", nextStatus.getDisplayName());
  }

  @Override
  @Transactional
  public ResponseObject cancelOrder(String orderId) {
    Optional<Order> orderDB = orderRepository.findById(orderId);
    if (orderDB.isEmpty()) return new ResponseObject(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng", null);

    Order order = orderDB.get();

    Boolean isAdminAccess = AuthenticationUtils.isAdminAccess();

    if (!isAdminAccess && !AuthenticationUtils.isAuthenticate(order.getUser().getLogin().getUsername()))
      return new ResponseObject(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này", null);
    OrderStatus orderStatus = order.getState();

    if (!orderStatus.allowCancel())
      return new ResponseObject(HttpStatus.BAD_REQUEST, "Đơn hàng không thể hủy được nữa", null);

    OrderStatus cancel = OrderStatus.CANCELED;
    Instant now = TimeUtils.getInstantNow();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String authUsername = authentication.getName();
    UpdateOrderStateAndInsertLog(order, cancel, now, authUsername);
    return new ResponseObject(HttpStatus.ACCEPTED, "Hủy đơn hàng thành công", cancel.getDisplayName());
  }


  @Override
  @Transactional
  public void UpdateOrderStateAndInsertLog(Order order, OrderStatus orderStatus, Instant time, String performedBy) {
    order.setState(orderStatus);
    orderRepository.save(order);
    OrderLog orderLog = OrderLog.builder().order(order).state(orderStatus).PerformedBy(performedBy).time(time).log(orderStatus.getLogMessage()).build();
    orderLogRepository.save(orderLog);
  }

  private Double calculateTotalAmount(List<OrderDetail> orderDetails) {
    if (orderDetails == null || orderDetails.isEmpty()) {
      return 0.0;
    }

    return orderDetails.stream().mapToDouble(orderDetail -> {
      double price = orderDetail.getSalePrice() != null ? orderDetail.getSalePrice() : orderDetail.getPrice();
      return price * orderDetail.getQuantity();
    }).sum();
  }
}

