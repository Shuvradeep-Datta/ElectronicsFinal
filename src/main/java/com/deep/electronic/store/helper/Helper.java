package com.deep.electronic.store.helper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.deep.electronic.store.payload.PageableResponse;

public class Helper {
	
	
	public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V>types) {
		List<U> entity = page.getContent();

		List<V> getAllDtos =entity.stream().map(object->new ModelMapper().map(object, types)).toList();
		System.out.println(getAllDtos);

		PageableResponse<V> response = new PageableResponse<>();
		response.setContent(getAllDtos);
		response.setPageNumber(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setLastPage(page.isLast());
		response.setFirstPage(page.isFirst());
		return response;
		
		
	}

}
