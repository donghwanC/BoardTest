package kr.co.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.vo.BoardVO;

@Component("fileUtils")
public class FileUtils {

	/*
	 * pom.xml >> context-common.xml >> web.xml
	 * FileUtils.java 작성 >>
	 * root-context.xml에 패키지추가
	 * 
	첨부파일의 정보를 이용하여 여러가지 조작을 할 클래스

	Iterator.. 데이터들의 집합체? 에서 컬렉션으로부터 
	정보를 얻어올 수 있는 인터페이스입니다.
	
	List나 배열은 순차적으로 데이터의 접근이 가능하지만, 
	Map등의 클래스들은 순차적으로 접근할 수가 없습니다.
	
	Iterator을 이용하여 Map에 있는 데이터들을 
	while문을 이용하여 순차적으로 접근합니다.
	
	getRandomString() 메서드는 
	32글자의 랜덤한 문자열(숫자포함)을 
	만들어서 반환해주는 기능을 합니다.
	*/
	
	private static final String filePath = "C:\\mp\\file\\"; // 파일저장 경로
	
	public List<Map<String, Object>> parseInsertFileInfo(BoardVO vo, MultipartHttpServletRequest mpRequest) throws Exception{
		
		// 컬렉션으로부터 정보를 얻어올 수 있는 인터페이스
		Iterator<String> iterator = mpRequest.getFileNames();
		
		
		MultipartFile multipartFile = null;
		String originalFileName = null; // 원본파일 이름
		String originalFileExtension = null;
		String storedFileName = null; // 변경된파일 이름
		
		Map<String, Object> listMap = null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				
		int bno = vo.getBno(); // 작성 된 글의 bno 값을 가져 옴
		
		File file = new File(filePath);
		
		if(file.exists() == false) { // 경로에 폴더생성
			file.mkdirs();
		}
		
		
		while(iterator.hasNext()) {
			multipartFile = mpRequest.getFile(iterator.next());
			
			if(multipartFile.isEmpty() == false) {
				originalFileName = multipartFile.getOriginalFilename();
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
				storedFileName = getRandomString() + originalFileExtension;
								
				System.out.println("originalFileName:"+originalFileName);
				System.out.println("originalFileExtension:"+originalFileExtension);
				System.out.println("storedFileName:"+storedFileName);
				
				file = new File(filePath + storedFileName);
				multipartFile.transferTo(file);
				
				listMap = new HashMap<String, Object>();
				listMap.put("BNO", bno);
				listMap.put("ORG_FILE_NAME", originalFileName);
				listMap.put("STORED_FILE_NAME", storedFileName);
				listMap.put("FILE_SIZE", multipartFile.getSize());
				
				list.add(listMap);
			}
		}
		return list;
	}
	
	
	public List<Map<String, Object>> parseUpdateFileInfo(BoardVO vo, String[] files, 
			String[] fileNames, MultipartHttpServletRequest mpRequest) throws Exception{
		
		Iterator<String> iterator = mpRequest.getFileNames();
		MultipartFile multipartFile = null;
		String originalFileName = null;
		String originalFileExtension = null;
		String storedFileName = null;
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> listMap = null;
		
		int bno = vo.getBno();
		
		while(iterator.hasNext()) {
			multipartFile = mpRequest.getFile(iterator.next());
			
			// 새로운 새로운 첨부파일이 등록되었을때 타게됩니다.
			if(multipartFile.isEmpty() == false) {
				originalFileName = multipartFile.getOriginalFilename();
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
				storedFileName = getRandomString()+originalFileExtension;
				multipartFile.transferTo(new File(filePath+storedFileName));
				
				listMap = new HashMap<String, Object>();
				listMap.put("IS_NEW", "Y");
				listMap.put("BNO", bno);
				listMap.put("ORG_FILE_NAME", originalFileName);
				listMap.put("STORED_FILE_NAME", storedFileName);
				listMap.put("FILE_SIZE", multipartFile.getSize());
				
				list.add(listMap);
			}
		}
		
		if(files != null && fileNames != null) {
			for(int i=0; i<fileNames.length; i++) {
				listMap = new HashMap<String, Object>();
				
				listMap.put("IS_NEW", "N");
				listMap.put("FILE_NO", files[i]); 
				list.add(listMap);
			}
		}
		
		return list;
	}
	

	private String getRandomString() {
		// 범용 고유 식별자(Universally Unique Identifiers)
		// 각 개체를 고유하게 식별 가능한 값
		// replaceAll.. "-"를 ""로 바꿈
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
