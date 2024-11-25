package com.fruitshop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.fruitshop.constant.OrderStatus;
import com.fruitshop.dto.response.OrderReponse;
import com.fruitshop.entity.Order;

@Mapper
public interface OrderMapper {
	OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "state", target = "state", qualifiedByName = "mapOrderStatusToString")
    OrderReponse entityToResponse(Order order);

    List<OrderReponse> entitysToResponses(List<Order> orders);

    @Named("mapOrderStatusToString")
    default String mapOrderStatusToString(OrderStatus state) {
        return state != null ? state.getDisplayName() : null;
    }
}
