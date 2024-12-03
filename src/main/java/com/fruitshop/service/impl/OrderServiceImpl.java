package com.fruitshop.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fruitshop.constant.OrderStatus;
import com.fruitshop.constant.PaymentMethod;
import com.fruitshop.dto.request.OrderRequest;
import com.fruitshop.dto.response.OrderReponse;
import com.fruitshop.entity.CartDetail;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderDetail;
import com.fruitshop.entity.OrderLog;
import com.fruitshop.entity.Product;
import com.fruitshop.entity.ShippingInformation;
import com.fruitshop.entity.User;
import com.fruitshop.exception.CustomException;
import com.fruitshop.mapper.OrderMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.CartDetailRepository;
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

	@Override
	@Transactional
	public ResponseObject createOrder(OrderRequest request) {

		Optional<ShippingInformation> shippingInfoDB = shippingInformationRepository
				.findById(request.getShippingInformationId());

		if (shippingInfoDB.isEmpty()) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy địa chỉ này");
		}

		Optional<User> userDB = userRepository.findById(request.getUserId());
		if (userDB.isEmpty())
			throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng");
		User user = userDB.get();

		if (!AuthenticationUtils.isAuthenticate(user.getLogin().getUsername()))
			throw new CustomException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này");

		ShippingInformation shippingInfo = shippingInfoDB.get();

		OrderStatus orderStatus = request.getPaymentMethod().equals(PaymentMethod.VNPAY.getDisplayName())
				? OrderStatus.AWAITING_PAYMENT
				: OrderStatus.PENDING;
		Instant orderTime = TimeUtils.getInstantNow();
		Order order = Order.builder().id(RandomUtils.getUniqueId()).orderDate(orderTime).state(orderStatus)
				.recipientName(shippingInfo.getRecipientName()).recipientAddress(shippingInfo.getShippingAdress())
				.phoneNumber(shippingInfo.getPhone()).paymentMethod(request.getPaymentMethod()).user(user).build();

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
			Product product = cartDetail.getProduct();

			if (product.getQuantity() < cartDetail.getQuantity())
				throw new CustomException(HttpStatus.FORBIDDEN,
						"Sản phẩm " + product.getTitle() + " Không còn đủ số lượng");

			Integer discountPercentage = productRepository.getProductDiscount(product.getId());
			Double price = product.getPrice() * (1 - discountPercentage / 100);

			OrderDetail orderDetail = OrderDetail.builder().Quantity(cartDetail.getQuantity()).price(price).order(order)
					.product(product).build();
			orderDetails.add(orderDetail);

			Integer oldQuantity = product.getQuantity();
			Integer newQuantity = oldQuantity - cartDetail.getQuantity();
			product.setQuantity(newQuantity);
			productRepository.save(product);

			cartDetailRepository.delete(cartDetail);
		}

		orderDetailRepository.saveAll(orderDetails);

		OrderLog orderLog = OrderLog.builder().order(order).PerformedBy(user.getLogin().getUsername()).time(orderTime)
				.log(orderStatus.getLogMessage()).build();
		orderLogRepository.save(orderLog);
		return new ResponseObject(HttpStatus.ACCEPTED, "Tạo đơn hàng thành công", order);
	}

	@Override
	public ResponseObject getPageOrder(Integer userId, Optional<Integer> pageNumber, Optional<Integer> amount,
			String state) {

		try {
			Pageable pageable = PageRequest.of(pageNumber.orElse(0), amount.orElse(10));
			List<OrderStatus> states = new ArrayList<OrderStatus>();
			if (!state.equals("")) {
				 states = OrderStatus.parseStates(state);
			}
			Page<Order> orderPage;
			Boolean isAdminAccess = AuthenticationUtils.isAdminAccess();
			if (isAdminAccess) {
				orderPage = orderRepository.findByState(states, pageable);
			} else {

				Optional<User> userDB = userRepository.findById(userId);
				if (userDB.isEmpty())
					return new ResponseObject(HttpStatus.NOT_FOUND, "Không tìm thấy ngươì dùng", null);
				User user = userDB.get();

				if (!AuthenticationUtils.isAuthenticate(user.getLogin().getUsername()))
					throw new CustomException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này");
				orderPage = state.equals("") ? orderRepository.findByUserId(userId, pageable)
						: orderRepository.findByUserIdAndState(userId, states, pageable);
			}
			List<OrderReponse> orderReponses = OrderMapper.INSTANCE.entitysToResponses(orderPage.getContent());

			for (OrderReponse orderReponse : orderReponses) {
				List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderReponse.getId());
				orderReponse.setOrderDetails(orderDetails);
				OrderLog lastOrderLog = orderLogRepository.findByOrderIdAndState(orderReponse.getId(),
						OrderStatus.fromDisplayName(orderReponse.getState()));
				orderReponse.setOrderLog(lastOrderLog);
			}

			Page<OrderReponse> orderReponsePage = new PageImpl<>(orderReponses, pageable, orderPage.getTotalElements());

			return new ResponseObject(HttpStatus.OK, "Lấy danh sách đơn hàng thành công.", orderReponsePage);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "Lấy danh sách đơn hàng thất bại.", null);
		}
	}

	@Override
	@Transactional
	public ResponseObject updateStatus(String id) {
		Optional<Order> orderDB = orderRepository.findById(id);
		if (orderDB.isEmpty())
			return new ResponseObject(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng", null);

		Order order = orderDB.get();

		OrderStatus orderStatus = order.getState();

		Boolean isAdminAccess = AuthenticationUtils.isAdminAccess();

		if (orderStatus.requiresAdminAccess() && !isAdminAccess)
			return new ResponseObject(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này", null);
		else if (AuthenticationUtils.isAuthenticate(order.getUser().getLogin().getUsername()))
			return new ResponseObject(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này", null);
		OrderStatus nextStatus = orderStatus.getNextStatus();
		order.setState(nextStatus);
		orderRepository.save(order);

		Instant now = TimeUtils.getInstantNow();

		OrderLog orderLog = OrderLog.builder().order(order)
				.PerformedBy(isAdminAccess ? "admin" : order.getUser().getLogin().getUsername()).time(now)
				.log(nextStatus.getLogMessage()).build();
		orderLogRepository.save(orderLog);
		return new ResponseObject(HttpStatus.ACCEPTED, "Cập nhật trạng thái đơn hàng thành công",
				nextStatus.getDisplayName());
	}

	@Override
	@Transactional
	public ResponseObject cancelOrder(String orderId) {
		Optional<Order> orderDB = orderRepository.findById(orderId);
		if (orderDB.isEmpty())
			return new ResponseObject(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng", null);

		Order order = orderDB.get();

		Boolean isAdminAccess = AuthenticationUtils.isAdminAccess();

		if (!isAdminAccess && AuthenticationUtils.isAuthenticate(order.getUser().getLogin().getUsername()))
			return new ResponseObject(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này", null);
		OrderStatus orderStatus = order.getState();

		if (!orderStatus.allowCancel())
			return new ResponseObject(HttpStatus.BAD_REQUEST, "Đơn hàng không thể hủy được nữa", null);

		OrderStatus nextStatus = OrderStatus.CANCELED;
		order.setState(nextStatus);
		orderRepository.save(order);

		Instant now = TimeUtils.getInstantNow();

		OrderLog orderLog = OrderLog.builder().order(order)
				.PerformedBy(isAdminAccess ? "admin" : order.getUser().getLogin().getUsername()).time(now)
				.log(nextStatus.getLogMessage()).build();
		orderLogRepository.save(orderLog);
		return new ResponseObject(HttpStatus.ACCEPTED, "Hủy đơn hàng thành công", nextStatus.getDisplayName());
	}

}
