package com.deep.electronic.store.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.deep.electronic.store.dtos.CategoryDTO;
import com.deep.electronic.store.dtos.ProductDTO;
import com.deep.electronic.store.entities.Category;
import com.deep.electronic.store.entities.Product;
import com.deep.electronic.store.exception.BadApiRequestExtension;
import com.deep.electronic.store.exception.ResourceNotFoundException;
import com.deep.electronic.store.helper.Helper;
import com.deep.electronic.store.payload.PageableResponse;
import com.deep.electronic.store.repository.CategoryRepository;
import com.deep.electronic.store.services.CategoryService;


@Service
public class CategoryServiceImpl implements CategoryService {
	
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	ModelMapper mapper;
	
	@Value("${spring.user.images.path}")
	private String imageUploadPath;
	
	Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	
	//Create Category
	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDto) {
		String categoryId = UUID.randomUUID().toString();
		categoryDto.setCategoryId(categoryId);
		Category category = mapper.map(categoryDto, Category.class);
		Category saveCategory = categoryRepository.save(category);
		CategoryDTO Dto = mapper.map(saveCategory, CategoryDTO.class);
		return Dto;
	}

	// Update Category
	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, String categoryId) {

		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found !!"));
		category.setTitle(categoryDTO.getTitle());
		category.setDescription(categoryDTO.getDescription());
		category.setCoverImage(categoryDTO.getCoverImage());
		Category saveCategory = categoryRepository.save(category);
		CategoryDTO categoryDto = mapper.map(saveCategory, CategoryDTO.class);
		return categoryDto;
	}

	
	//Get Category
	@Override
	public PageableResponse<CategoryDTO> getCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Category> allPages = categoryRepository.findAll(pageable);
		PageableResponse<CategoryDTO> response = Helper.getPageableResponse(allPages, CategoryDTO.class);

		return response;
	}

	@Override
	public CategoryDTO getCategoryById(String categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not founded !!"));

		CategoryDTO categoryDTO = mapper.map(category, CategoryDTO.class);

		return categoryDTO;
	}

	@Override
	public void deleteCategory(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(()->new RuntimeException("CategoryId not found Exception. !!"));
		
		String fullPath = imageUploadPath+category.getCoverImage();
		
		
		try {
			Path path = Path.of(fullPath);
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		categoryRepository.delete(category);
		
	}

	@Override
	public PageableResponse<CategoryDTO> searchKeyWord(String keywords, String subTitle, int pageNumber, int pageSize,
			String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Category> allPages = categoryRepository.findByTitleContaining(subTitle, pageable);
		PageableResponse<CategoryDTO> response = Helper.getPageableResponse(allPages, CategoryDTO.class);
		return response;
	}

	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {
		String originalFileName = file.getOriginalFilename();
		logger.info("FileName : {}",originalFileName);
		String fileName =UUID.randomUUID().toString();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String fileNameWithExtension =fileName+extension;
		String fullFilePathWithFileName =path+File.separator+fileNameWithExtension;
		
		if(extension.equalsIgnoreCase(".png")||extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase(".jpeg")) {
			File folder =new File(path);
			
			if(!folder.exists()) {
				folder.mkdirs();
			}
			
			//Upload 
			
			Files.copy(file.getInputStream(), Paths.get(fullFilePathWithFileName));
			return fileNameWithExtension;
			
			
		}else {
			throw new BadApiRequestExtension("Give the correct Extension.");
		}
	}

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {
		String fullPath = path + File.separator + name;
		InputStream fileInputStream = new FileInputStream(fullPath);
		return fileInputStream;
	}

}
