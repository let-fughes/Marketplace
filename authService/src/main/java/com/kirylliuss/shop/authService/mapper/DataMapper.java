package com.kirylliuss.shop.authService.mapper;

import com.kirylliuss.shop.authService.dto.request.DataRequest;
import com.kirylliuss.shop.authService.dto.response.DataResponse;
import com.kirylliuss.shop.authService.model.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface DataMapper {

    @Mapping(target = "id", ignore = true)
    Data toData(DataRequest request);

    DataResponse toDataResponse(Data data);

    @Mapping(target = "id", ignore = true)
    void updateDataFromRequest(@MappingTarget Data data, DataRequest request);
}
