package com.deep.electronic.store.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
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

import com.deep.electronic.store.dtos.ProductDTO;
import com.deep.electronic.store.entities.Category;
import com.deep.electronic.store.entities.Product;
import com.deep.electronic.store.exception.BadApiRequestExtension;
import com.deep.electronic.store.exception.ResourceNotFoundException;
import com.deep.electronic.store.helper.Helper;
import com.deep.electronic.store.payload.PageableResponse;
import com.deep.electronic.store.repository.CategoryRepository;
import com.deep.electronic.store.repository.ProductRepository;
import com.deep.electronic.store.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	// Dependency Injection

	@Autowired
	private ProductRepository repos;
	
	@Autowired
	private CategoryRepository catRepos;

	@Autowired
	ModelMapper mapper;

	@Value("${spring.user.images.path}")
	private String imageUploadPath;

	Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public ProductDTO createProduct(ProductDTO productDTO) {

		Product product = mapper.map(productDTO, Product.class);
		String productId = UUID.randomUUID().toString();
		product.setAddedDate(new Date());
		product.setProductId(productId);
		Product saveProduct = repos.save(product);
		ProductDTO productDto = mapper.map(saveProduct, ProductDTO.class);
		return productDto;
	}

	@Override
	public ProductDTO updateProduct(String productId, ProductDTO productDTO) {
		Product product = repos.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("ProductId not found"));
		product.setTitle(productDTO.getTitle());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setQuantity(productDTO.getQuantity());
		product.setAddedDate(productDTO.getAddedDate());
		product.setStock(productDTO.isStock());
		product.setLive(productDTO.isLive());
		product.setProductImage(productDTO.getProductImage());
		Product product1 = repos.save(product);
		ProductDTO productDto = mapper.map(product1, ProductDTO.class);

		return productDto;
	}

	@Override
	public void deleteProduct(String productId) {
		Product product = repos.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("product Id not found !!"));

		String fullPath = imageUploadPath + product.getProductImage();

		try {
			Path path = Path.of(fullPath);
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		repos.delete(product);
	}

	@Override
	public PageableResponse<ProductDTO> getProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> allPages = repos.findAll(pageable);
		PageableResponse<ProductDTO> response = Helper.getPageableResponse(allPages, ProductDTO.class);
		return response;
	}

	@Override
	public ProductDTO getProductById(String productId) {
		Product product = repos.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Id not Found !!"));
		ProductDTO productDto = mapper.map(product, ProductDTO.class);
		return productDto;
	}

	@Override
	public PageableResponse<ProductDTO> getProductByLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> allPages = repos.findByLiveTrue(pageable);
		PageableResponse<ProductDTO> response = Helper.getPageableResponse(allPages, ProductDTO.class);
		return response;
	}

	@Override
	public PageableResponse<ProductDTO> getProductByKeyword(String subTitle, int pageNumber, int pageSize,
			String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> allPages = repos.findByTitleContaining(subTitle, pageable);
		PageableResponse<ProductDTO> response = Helper.getPageableResponse(allPages, ProductDTO.class);
		return response;
	}

	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {
		String originalFileName = file.getOriginalFilename();
		logger.info("FileName : {}", originalFileName);
		String fileName = UUID.randomUUID().toString();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String fileNameWithExtension = fileName + extension;
		String fullFilePathWithFileName = path + File.separator + fileNameWithExtension;

		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")
				|| extension.equalsIgnoreCase(".jpeg")) {
			File folder = new File(path);

			if (!folder.exists()) {
				folder.mkdirs();
			}

			// Upload

			Files.copy(file.getInputStream(), Paths.get(fullFilePathWithFileName));
			return fileNameWithExtension;

		} else {
			throw new BadApiRequestExtension("Give the correct Extension.");
		}
	}

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {
		String fullPath = path + File.separator + name;
		InputStream fileInputStream = new FileInputStream(fullPath);
		return fileInputStream;
	}

	@Override
	public ProductDTO createProductWithCategory(ProductDTO productDTO, String categoryId) {
		Category category = catRepos.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not Found"));
		Product product = mapper.map(productDTO, Product.class);
		
		//productId
		String productId = UUID.randomUUID().toString();
		product.setProductId(productId);
		
		//added
		product.setAddedDate(new Date());
		product.setCategory(category);
		Product saveProduct = repos.save(product);
		return mapper.map(saveProduct,ProductDTO.class);
		
		
		
	}

	@Override
	public ProductDTO updateCategory(String productId, String categoryId) {
		Product product = repos.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("product Id not Found !!"));
		Category category = catRepos.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Id not found !!"));
		product.setCategory(category);
		Product saveProduct = repos.save(product);
		return mapper.map(saveProduct, ProductDTO.class);
	}

	@Override
	public PageableResponse<ProductDTO> getAllOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {
		Category category = catRepos.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Id not found !!"));
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = repos.findByCategory(category,pageable);
		return Helper.getPageableResponse(page, ProductDTO.class);
	}


	
}
