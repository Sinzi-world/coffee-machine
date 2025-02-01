package com.example.coffemachine.mapper;

import com.example.coffemachine.model.entity.Orders;
import com.example.coffemachine.model.dto.OrderDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OrderMapper {


    OrderDTO toDto(Orders order);

    Orders toEntity(OrderDTO orderDTO);
}
