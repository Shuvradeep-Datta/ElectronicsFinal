package com.deep.electronic.store.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.deep.electronic.store.exception.BadApiRequestExtension;
import com.deep.electronic.store.services.FileService;


@Service
public class FileServiceImpl implements FileService {

	Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
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
