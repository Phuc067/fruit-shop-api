package com.fruitshop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.fruitshop.entity.Product;
import com.fruitshop.entity.ShippingInformation;
import com.fruitshop.entity.User;
import com.fruitshop.exception.CustomException;
import com.fruitshop.mapper.OrderMapper;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.CartDetailRepository;
import com.fruitshop.repository.OrderDetailRepository;
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
	private UserRepository usererRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Override
	@Transactional
	public ResponseObject createOrder(OrderRequest request) {

		Optional<ShippingInformation> shippingInfoDB = shippingInformationRepository
				.findById(request.getShippingInformationId());

		if (shippingInfoDB.isEmpty()) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy địa chỉ này");
		}

		Optional<User> userDB = usererRepository.findById(request.getUserId());
		if (userDB.isEmpty())
			throw new CustomException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng");
		User user = userDB.get();

		if (!AuthenticationUtils.isAuthenticate(user.getLogin().getUsername()))
			throw new CustomException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này");

		ShippingInformation shippingInfo = shippingInfoDB.get();

		Order order = Order.builder().id(RandomUtils.getUniqueId()).orderDate(TimeUtils.getInstantNow())
				.state(request.getPaymentMethod().equals(PaymentMethod.VNPAY.getDisplayName())
						? OrderStatus.AWAITING_PAYMENT
						: OrderStatus.PENDING)
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

		return new ResponseObject(HttpStatus.ACCEPTED, "Tạo đơn hàng thành công", order);
	}

	@Override
	public ResponseObject getAllOrderOfUser(Integer userId) {
		Optional<User> userDB = usererRepository.findById(userId);
		if (userDB.isEmpty())
			return new ResponseObject(HttpStatus.NOT_FOUND, "Không tìm thấy ngươì dùng", null);
		User user = userDB.get();

		if (!AuthenticationUtils.isAuthenticate(user.getLogin().getUsername()))
			throw new CustomException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này");

		List<Order> orders = orderRepository.findByUserId(userId);

		List<OrderReponse> orderReponses = OrderMapper.INSTANCE.entitysToResponses(orders);

		for (OrderReponse orderReponse : orderReponses) {
			List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderReponse.getId());
			orderReponse.setOrderDetails(orderDetails);
		}

		return new ResponseObject(HttpStatus.OK, "Lấy danh sách đơn hàng thành công", orderReponses);
	}

	@Override
	public ResponseObject getListOrderByUserIdAndState(Integer userId, String state) {
		Optional<User> userDB = usererRepository.findById(userId);
		if (userDB.isEmpty())
			return new ResponseObject(HttpStatus.NOT_FOUND, "Không tìm thấy ngươì dùng", null);
		User user = userDB.get();

		if (!AuthenticationUtils.isAuthenticate(user.getLogin().getUsername()))
			throw new CustomException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này");

		List<Order> orders = orderRepository.findByUserIdAndState(userId, OrderStatus.fromDisplayName(state));

		List<OrderReponse> orderReponses = OrderMapper.INSTANCE.entitysToResponses(orders);

		for (OrderReponse orderReponse : orderReponses) {
			List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderReponse.getId());
			orderReponse.setOrderDetails(orderDetails);
		}

		return new ResponseObject(HttpStatus.OK, "Lấy danh sách đơn hàng thành công", orderReponses);
	}

}
