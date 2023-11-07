package com.LP2.EventScheduler.response.category;

import com.LP2.EventScheduler.model.Category;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toList(List<Category> categories);
}
