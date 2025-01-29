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
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.deep.electronic.store.payload.ImageResponseMessage;
import com.deep.electronic.store.payload.PageableResponse;
import com.deep.electronic.store.services.ProductService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@Value("${spring.user.images.path}")
	private String imageUploadPath;

	// create
	@PostMapping("/")
//	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDto) {
		ProductDTO dto = service.createProduct(productDto);
		return new ResponseEntity<ProductDTO>(dto, HttpStatus.CREATED);
	}
	
	// Update
	@PutMapping("/{productId}")
//	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable String productId, @RequestBody ProductDTO productDTO) {
		ProductDTO updateProduct = service.updateProduct(productId, productDTO);
		return new ResponseEntity<ProductDTO>(updateProduct, HttpStatus.OK);
	}
	//delete
	@DeleteMapping("/{productId}")
//	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
		service.deleteProduct(productId);
		return new ResponseEntity<String>("Product deleted successfully", HttpStatus.OK);
	}
	
	// Get
	@GetMapping("/")
//	@PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
	public ResponseEntity<PageableResponse<ProductDTO>> getProduct(
			@RequestParam(name = "pageNumber",defaultValue = "0",required = false) int pageNumber, 
			@RequestParam(name = "pageSize",defaultValue = "10", required = false) int pageSize, 
			@RequestParam(name = "sortBy",defaultValue = "title",required = false) String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "desc",required = false) String sortDir) {
		PageableResponse<ProductDTO> product = service.getProduct(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<ProductDTO>>(product, HttpStatus.OK);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable String productId) {
		ProductDTO productById = service.getProductById(productId);
		return new ResponseEntity<ProductDTO>(productById, HttpStatus.OK);
	}
	
	@GetMapping("/live/")
	public ResponseEntity<PageableResponse<ProductDTO>> getProductByLive(
			@RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "desc", required = false) String sortDir) {
		PageableResponse<ProductDTO> productByLive = service.getProductByLive(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<ProductDTO>>(productByLive, HttpStatus.OK);

	}

	@GetMapping("keyword/{subTitle}")
	public ResponseEntity<PageableResponse<ProductDTO>> getProductByKeyword(@PathVariable("subTitle") String subTitle,
			@RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(name = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(name = "sortDir", defaultValue = "desc", required = false) String sortDir) {
		PageableResponse<ProductDTO> productByKeyword = service.getProductByKeyword(subTitle, pageNumber, pageSize,
				sortBy, sortDir);

		return new ResponseEntity<PageableResponse<ProductDTO>>(productByKeyword, HttpStatus.OK);
	}
	
	
	@PostMapping("/upload/{productId}")
	public ResponseEntity<ImageResponseMessage> uploadCategoryImage(@PathVariable("productId") String productId,
			@RequestParam("coverImage") MultipartFile image) throws IOException {
		String imageName = service.uploadFile(image, imageUploadPath);
		ProductDTO product = service.getProductById(productId);
		product.setProductImage(imageName);
		ProductDTO updateProduct = service.updateProduct(productId, product);
		ImageResponseMessage imageResponseMessage = ImageResponseMessage.builder().imageName(imageName).success(true)
				.satus(HttpStatus.CREATED).message("Image Uploaded Successfully !!").build();
		return new ResponseEntity<ImageResponseMessage>(imageResponseMessage, HttpStatus.CREATED);
	}
	
	
	
	// Serve Image....

	@GetMapping("/Image/{productId}")
	public void getCategoryName(@PathVariable("productId") String productId, HttpServletResponse response)
			throws IOException {
		ProductDTO product = service.getProductById(productId);
		InputStream resource = service.getResource(imageUploadPath, product.getProductImage());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
	

	
}
