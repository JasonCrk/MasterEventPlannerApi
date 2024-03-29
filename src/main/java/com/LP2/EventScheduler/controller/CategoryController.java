package com.LP2.EventScheduler.controller;

import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.category.CategoryResponse;
import com.LP2.EventScheduler.service.category.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category")
@RestController
@RequestMapping(path = "/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Retrieve all categories")
    @GetMapping
    public ResponseEntity<ListResponse<CategoryResponse>> retrieveAllCategories() {
        return ResponseEntity.ok(this.categoryService.retrieveAllCategories());
    }
}
