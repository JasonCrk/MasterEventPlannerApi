package com.LP2.EventScheduler.service.category;

import com.LP2.EventScheduler.model.Category;
import com.LP2.EventScheduler.repository.CategoryRepository;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.category.CategoryMapper;
import com.LP2.EventScheduler.response.category.CategoryResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public ListResponse<CategoryResponse> retrieveAllCategories() {
        List<Category> categories = this.categoryRepository.findAll();
        return new ListResponse<>(CategoryMapper.INSTANCE.toList(categories));
    }
}
