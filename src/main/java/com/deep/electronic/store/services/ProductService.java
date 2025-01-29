package com.deep.electronic.store.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.deep.electronic.store.dtos.ProductDTO;
import com.deep.electronic.store.payload.PageableResponse;

@Component
public interface ProductService {
	
	//create
	
	ProductDTO createProduct(ProductDTO productDTO);
	
	//upadate 
	ProductDTO updateProduct(String productId,ProductDTO productDTO);
	
	//delete
	void deleteProduct(String productId);
	
	//Get
	PageableResponse<ProductDTO> getProduct(int pageNumber, int pageSize,String sortBy,String sortDir);
	
	//Single Get
	
	ProductDTO getProductById(String productId);
	
	// Get All::Live
	
	PageableResponse<ProductDTO> getProductByLive(int pageNumber, int pageSize, String sortBy, String sortDir);
	
	
	//Search Keyword by title
	
	PageableResponse<ProductDTO> getProductByKeyword(String subTitle,int pageNumber, int pageSize, String sortBy, String sortDir);

	String uploadFile(MultipartFile file, String path) throws IOException;

	InputStream getResource(String path, String name) throws FileNotFoundException;
	
	ProductDTO createProductWithCategory(ProductDTO productDTO, String productId);
	
	//Update Category of Product
	
	ProductDTO updateCategory(String productId,String categoryId);
	
	
	PageableResponse<ProductDTO> getAllOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);
	
	
	
}
