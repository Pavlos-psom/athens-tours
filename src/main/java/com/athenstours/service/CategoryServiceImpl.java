package com.athenstours.service;

import com.athenstours.dto.CategoryReadOnlyDTO;
import com.athenstours.mapper.TourMapper;
import com.athenstours.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final TourMapper tourMapper;

    @Override
    public List<CategoryReadOnlyDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(tourMapper::mapToCategoryReadOnlyDTO)
                .toList();
    }
}
