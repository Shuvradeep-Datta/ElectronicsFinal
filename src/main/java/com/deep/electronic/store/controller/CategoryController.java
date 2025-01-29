package com.deep.electronic.store.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.deep.electronic.store.dtos.CategoryDTO;
import com.deep.electronic.store.dtos.ProductDTO;
import com.deep.electronic.store.dtos.UserDTO;
import com.deep.electronic.store.payload.ImageResponseMessage;
import com.deep.electronic.store.payload.PageableResponse;
import com.deep.electronic.store.services.CategoryService;
import com.deep.electronic.store.services.ProductService;
import com.deep.electronic.store.services.impl.ProductServiceImpl;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/categories")
@RestController
@Tag(name = "CategoryController",description = "This is Category based controller")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;

	@Value("${spring.user.images.path}")
	private String imageUploadPath;
	
	// create
	@PostMapping("/")
//	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
		CategoryDTO dto = categoryService.createCategory(categoryDTO);
		return new ResponseEntity<CategoryDTO>(dto, HttpStatus.CREATED);
	}

	// update
	
	@PutMapping("/{categoryId}")
//	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO,
			@PathVariable("categoryId") String categoryId) {
		CategoryDTO updateCategory = categoryService.updateCategory(categoryDTO, categoryId);
		return new ResponseEntity<CategoryDTO>(updateCategory, HttpStatus.OK);

	}

	// delete

	@DeleteMapping("/{categoryId}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") String categoryId) {
		categoryService.deleteCategory(categoryId);
		return new ResponseEntity<String>("Category Deleted Successfully", HttpStatus.OK);
	}

	// get
	@GetMapping("/")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<PageableResponse<CategoryDTO>> getCategory(
			@RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(name = "pageNumber", defaultValue = "10", required = false) int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "desc", required = false) String sortDir) {
		PageableResponse<CategoryDTO> category = categoryService.getCategory(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<CategoryDTO>>(category, HttpStatus.OK);
	}

	// get by Id
	@GetMapping("/{categoryId}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String categoryId) {
		CategoryDTO categoryById = categoryService.getCategoryById(categoryId);
		return new ResponseEntity<CategoryDTO>(categoryById, HttpStatus.OK);
	}

	// Search By Keyword
	@GetMapping("/key/{keyword}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<PageableResponse<CategoryDTO>> searchKeyWord(@PathVariable("keyword") String keyword,
			@RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(name = "pageNumber", defaultValue = "10", required = false) int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "desc", required = false) String sortDir) {
		PageableResponse<CategoryDTO> searchKeyWord = categoryService.searchKeyWord(keyword, keyword, pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<CategoryDTO>>(searchKeyWord, HttpStatus.OK);
	}
	
	
	@PostMapping("/upload/{categoryId}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<ImageResponseMessage> uploadCategoryImage(@PathVariable("categoryId") String categoryId,
			@RequestParam("coverImage") MultipartFile image) throws IOException {
		String imageName = categoryService.uploadFile(image, imageUploadPath);
		CategoryDTO category = categoryService.getCategoryById(categoryId);
		category.setCoverImage(imageName);
		CategoryDTO updateCategory = categoryService.updateCategory(category, categoryId);
		ImageResponseMessage imageResponseMessage = ImageResponseMessage.builder().imageName(imageName).success(true)
				.satus(HttpStatus.CREATED).message("Image Uploaded Successfully !!").build();
		return new ResponseEntity<ImageResponseMessage>(imageResponseMessage, HttpStatus.CREATED);
	}
	
	
	
	// Serve Image....

	@GetMapping("/Image/{categoryId}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public void getCategoryName(@PathVariable("categoryId") String categoryId, HttpServletResponse response)
			throws IOException {
		CategoryDTO category = categoryService.getCategoryById(categoryId);
		InputStream resource = categoryService.getResource(imageUploadPath, category.getCoverImage());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
	
	
	@PostMapping("/{categoryId}/products")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<ProductDTO> createProductWithCategory(@RequestBody ProductDTO productDTO,@PathVariable String categoryId) {
		ProductDTO productWithCategory = productService.createProductWithCategory(productDTO, categoryId);
		return new ResponseEntity<ProductDTO>(productWithCategory, HttpStatus.CREATED);
	}
	
	@PostMapping("/{categoryId}/products/{productId}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<ProductDTO> assignCategoryToProduct(@PathVariable("categoryId") String categoryId,
			@PathVariable("productId") String productId) {
		ProductDTO updateCategory = productService.updateCategory(productId, categoryId);
		return new ResponseEntity<ProductDTO>(updateCategory, HttpStatus.OK);
	}
	
	@GetMapping("/{categoryId}/products")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<PageableResponse<ProductDTO>> getProductOfCategory(@PathVariable String categoryId,
			@RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(name = "pageNumber", defaultValue = "10", required = false) int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "desc", required = false) String sortDir){
		PageableResponse<ProductDTO> response = productService.getAllOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<PageableResponse<ProductDTO>>(response, HttpStatus.OK);
	}
	

	


}
