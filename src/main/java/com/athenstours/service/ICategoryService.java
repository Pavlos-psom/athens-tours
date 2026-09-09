package com.athenstours.service;

import com.athenstours.dto.CategoryReadOnlyDTO;

import java.util.List;

public interface ICategoryService {

    List<CategoryReadOnlyDTO> getAllCategories();
}
