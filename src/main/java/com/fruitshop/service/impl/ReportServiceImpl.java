package com.fruitshop.service.impl;

import java.time.ZoneOffset;
import java.time.LocalDate;  
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fruitshop.constant.OrderStatus;
import com.fruitshop.dto.response.OrderDateCount;
import com.fruitshop.dto.response.OrderReportResponse;
import com.fruitshop.entity.Invoice;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderDetail;
import com.fruitshop.model.ResponseObject;
import com.fruitshop.repository.InvoiceRepository;
import com.fruitshop.repository.OrderDetailRepository;
import com.fruitshop.repository.OrderRepository;
import com.fruitshop.service.ReportService;


@Service
public class ReportServiceImpl implements ReportService{

	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Override
	public ResponseObject reportOrderByMonth(Integer month, Integer year) {
		
		ResponseObject validationResponse = validateMonthYear(month, year);
	    if (validationResponse != null) {
	        return validationResponse;
	    }
        List<Order> orders = orderRepository.findOrdersByMonthAndYear(month, year);


        Map<Integer, Long> ordersCountByDay = orders.stream()
                .map(order -> order.getOrderDate())  
                .map(instant -> instant.atZone(ZoneOffset.UTC).toLocalDate())  
                .map(LocalDate::getDayOfMonth) 
                .collect(Collectors.groupingBy(day -> day, Collectors.counting()));


        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
        List<Integer> allDaysInMonth = firstDayOfMonth.datesUntil(lastDayOfMonth.plusDays(1))
            .map(LocalDate::getDayOfMonth)  
            .collect(Collectors.toList());


        List<OrderDateCount> orderDateCounts = allDaysInMonth.stream()
            .map(day -> new OrderDateCount(day, ordersCountByDay.getOrDefault(day, 0L)))  
            .collect(Collectors.toList());

        Integer total = orders.size();
        Integer countOfCancel = (int) orders.stream().filter(order -> order.getState().isCanceledOrRefunded()).count();
        
        Double actualRevenue = orders.stream()
                .filter(order -> !order.getState().equals(OrderStatus.REFUNDED))
                .mapToDouble(order -> {
                    Invoice invoice = invoiceRepository.findByOrder(order);  
                    return (invoice != null) ? invoice.getTotalAmount() : 0.0;
                })
                .sum();


        Double projectedRevenue = orders.stream()
                .filter(order -> !order.getState().isCanceledOrRefunded())
                .mapToDouble(order -> getOrderAmount(order))
                .sum();

        OrderReportResponse reportResponse = new OrderReportResponse(orderDateCounts, total, countOfCancel, actualRevenue, projectedRevenue);

        
		return new ResponseObject(HttpStatus.OK, "Lấy dữ liệu thành công", reportResponse);
	}

	private ResponseObject validateMonthYear(Integer month, Integer year) {
	    if (month == null || month < 1 || month > 12) {
	        return new ResponseObject(HttpStatus.BAD_REQUEST, "Tháng không hợp lệ.", null);
	    }

	    if (year == null || year < 1900 || year > LocalDate.now().getYear()) {
	        return new ResponseObject(HttpStatus.BAD_REQUEST, "Năm không hợp lệ.", null);
	    }

	    return null; 
	}
	
	private Double getOrderAmount(Order order) {
		List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
		return orderDetails.stream()
		        .mapToDouble(orderDetail -> orderDetail.getSalePrice() * orderDetail.getQuantity()) 
		        .sum(); 
	}
	
}
