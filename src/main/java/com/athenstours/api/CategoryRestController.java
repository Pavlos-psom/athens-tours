package com.athenstours.api;

import com.athenstours.dto.CategoryReadOnlyDTO;
import com.athenstours.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final ICategoryService categoryService;

    /** Public - the frontend needs this list before a customer even logs in (filters, dropdowns). */
    @GetMapping
    public List<CategoryReadOnlyDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
