package com.ssafy.back.sale.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:5173")
public class ImageController {

	@Value("${file.path}")
	private String uploadPath;

	@GetMapping("/image/{datePath}/{fileName}")
	public ResponseEntity<Resource> image(@PathVariable String datePath, @PathVariable String fileName)
			throws FileNotFoundException {
		String filePath = uploadPath + File.separator + datePath + File.separator + fileName;
		InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(filePath));
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(inputStreamResource);
	}
}
