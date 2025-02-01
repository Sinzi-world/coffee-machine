package com.example.coffemachine.mapper;

import com.example.coffemachine.model.entity.Orders;
import com.example.coffemachine.model.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "drink.id", target = "drink.id")
    @Mapping(source = "drink.name", target = "drink.name")
    @Mapping(source = "createdAt", target = "createdAt")
    OrderDTO toDto(Orders order);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "drink.id", target = "drink.id")
    @Mapping(source = "drink.name", target = "drink.name")
    @Mapping(source = "createdAt", target = "createdAt")
    Orders toEntity(OrderDTO orderDTO);
}
