package com.deep.electronic.store.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.deep.electronic.store.dtos.CategoryDTO;
import com.deep.electronic.store.payload.PageableResponse;

@Component
public interface CategoryService {

	// create

	CategoryDTO createCategory(CategoryDTO categoryDto);

	// update
	CategoryDTO updateCategory(CategoryDTO categoryDTO, String categoryId);

	// Get
	PageableResponse<CategoryDTO> getCategory(int pageNumber, int pageSize, String sortBy, String sortDir);

	// Get Single
	CategoryDTO getCategoryById(String categoryId);

	// Delete

	void deleteCategory(String categoryId);

	// Search
	PageableResponse<CategoryDTO> searchKeyWord(String keywords,String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir);

	String uploadFile(MultipartFile file, String path) throws IOException;

	InputStream getResource(String path, String name) throws FileNotFoundException;

}
