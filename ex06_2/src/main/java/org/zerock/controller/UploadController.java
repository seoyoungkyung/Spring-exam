package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {

	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload from");
	}
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		
		String uploadFolder = "c:\\upload";
		
		
		
		for(MultipartFile multipartFile: uploadFile) {
			log.info("------------------");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File Size : " + multipartFile.getSize());

			
			//파일 경로 객체 설정
			File savefile = new File(uploadFolder, multipartFile.getOriginalFilename());
			try {
				multipartFile.transferTo(savefile);		//파일저장
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		
	}
	
//	@PostMapping("/uploadAjaxAction")
//	public void uploadAjaxPost(MultipartFile[] uploadFile, Model model) {
//		
//		String uploadFolder = "c:\\upload";
//		
//		//make folder ....
//		File uploadPath = new File(uploadFolder,getFolder());
//		log.info("uploadPath : "+uploadPath);
//		
//		if(uploadPath.exists() == false) {
//			uploadPath.mkdirs();
//		}			//make yyyy/MM/dd folder 생성
//		
//		
//		for(MultipartFile multipartFile: uploadFile) {
//			log.info("------------------");
//			log.info("Upload File Name : " + multipartFile.getOriginalFilename());
//			log.info("Upload File Size : " + multipartFile.getSize());
//			
//			String uploadFileName = multipartFile.getOriginalFilename();
//			
//			uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
//			
//			log.info("only file name : "+uploadFileName);
//			
//			//중복방지
//			UUID uuid = UUID.randomUUID();
//			uploadFileName = uuid.toString()+"_"+uploadFileName;
//			
//			
//			//파일 경로 객체 설정
////			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
////			File saveFile = new File(uploadFolder,uploadFileName);
//			File saveFile = new File(uploadPath,uploadFileName);
//			
//			try {
//				multipartFile.transferTo(saveFile);		//파일저장
//				
//				//check image file
//				if(checkImageType(saveFile)) {
//					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_"  + uploadFileName));
//					
//					Thumbnailator.createThumbnail(multipartFile.getInputStream(),thumbnail,200,200);		//섬네일 이미지 생성
//					thumbnail.close();
//				}
//				
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//		}//end for
//		
//	}//end uploadAjaxPost
	
	
	@PostMapping(value="/uploadAjaxAction", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile, Model model) {
		
		List<AttachFileDTO> list = new ArrayList<>();
		
		String uploadFolder = "c:\\upload";
		
		//make folder .... 2024/11/19
		String uploadFolderPath = getFolder();
		
		//uploadPath --> c:\\upload\2024\11\19
		File uploadPath = new File(uploadFolder,uploadFolderPath);
	
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}			//make yyyy/MM/dd folder 생성
		
		
		for(MultipartFile multipartFile: uploadFile) {
			
			AttachFileDTO attachDTO = new AttachFileDTO();
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
			
			log.info("only file name : "+uploadFileName);
			
			attachDTO.setFileName(uploadFileName);
			attachDTO.setUploadPath(uploadFolderPath);
			
			//중복방지
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString()+"_"+uploadFileName;
			
			attachDTO.setUuid(uuid.toString());
			
			
			//파일 경로 객체 설정
//			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
//			File saveFile = new File(uploadFolder,uploadFileName);
			File saveFile = new File(uploadPath,uploadFileName);
			
			
			try {
				multipartFile.transferTo(saveFile);		//파일저장
				
				//check image file
				if(checkImageType(saveFile)) {
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_"  + uploadFileName));
					
					Thumbnailator.createThumbnail(multipartFile.getInputStream(),thumbnail,200,200);		//섬네일 이미지 생성
					thumbnail.close();
					
					attachDTO.setImage(true);
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			list.add(attachDTO);
		}//end for
		
		return new ResponseEntity<>(list,HttpStatus.OK);
	}//end uploadAjaxPost
	
	
	
	//오늘날짜로 폴더 경로 생성
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		String str = sdf.format(date);
		return str.replace("-", File.separator);
	}
	
	//이미지 파일 여부 판단
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			
			return contentType.startsWith("image");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		
		log.info("fileName : "+fileName);
		
		File file = new File("c:\\upload\\" + fileName);
		ResponseEntity<byte[]> result = null;
		
		try {
			
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@GetMapping(value="/download",produces= {MediaType.APPLICATION_OCTET_STREAM_VALUE})
	@ResponseBody
//	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent,String fileName){
	public ResponseEntity<Resource> downloadFile(String fileName){
		
		log.info("download file : "+fileName);
		
		Resource resource = new FileSystemResource("c:\\upload\\"+fileName);
		
		log.info("resource : "+resource);
		
		if(resource.exists() == false) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		
		
		String resourceName = resource.getFilename();

		//remove UUID
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1);
		
		log.info("resourceName : "+resourceName);
		
		
		log.info("resourceOriginalName : "+resourceOriginalName);
		
		HttpHeaders header = new HttpHeaders();
		
		try {
		
//			header.add("Content-Disposition", "attachment; filename= " + new String(resourceName.getBytes("utf-8"), "ISO-8859-1"));
			header.add("Content-Disposition", "attachment; filename= " + URLEncoder.encode(resourceOriginalName,"utf-8"));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource,header,HttpStatus.OK);
	}
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName,String type){
		log.info("deleteFile : " + fileName);
		
		File file;
		
		try {
			file = new File("c:\\upload\\" + URLDecoder.decode(fileName,"UTF-8"));
			
			file.delete();
			
			if(type.equals("image")) {
				String largeFileName = file.getAbsolutePath().replace("s_","");
				
				log.info("largeFileName : "+largeFileName);
				
				file = new File(largeFileName);
				
				file.delete();
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>("deleted",HttpStatus.OK);
	}
	
}
