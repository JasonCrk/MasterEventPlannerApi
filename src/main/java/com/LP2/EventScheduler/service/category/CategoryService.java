package com.LP2.EventScheduler.service.category;

import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.category.CategoryResponse;

public interface CategoryService {
    ListResponse<CategoryResponse> retrieveAllCategories();
}
