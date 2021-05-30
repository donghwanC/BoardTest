package kr.co.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.vo.BoardVO;
import kr.co.vo.Criteria;
import kr.co.vo.SearchCriteria;

public interface BoardService {

	// 게시글 작성
	public void write(BoardVO vo, MultipartHttpServletRequest mpRequest) throws Exception;

	// 게시물 목록 조회
	public List<BoardVO> list() throws Exception;

	// 게시물 조회 + 조회수
	public BoardVO read(int bno) throws Exception;

	// 게시물 수정
	// + 파일삭제
	public void update(BoardVO vo, String[] files, 
			String[] fileNames, MultipartHttpServletRequest mpRequest) throws Exception;

	// 게시물 삭제
	public void delete(int bno) throws Exception;

	// 페이징처리 + 게시글 목록
	public List<BoardVO> list(Criteria cri) throws Exception;

	// 페이징처리 + 게시글 총 갯수
	public int listCount() throws Exception;
	
	// 페이징처리 + 검색기능 + 게시글 목록
	public List<BoardVO> list(SearchCriteria scri) throws Exception;

	// 페이징처리 + 검색기능 + 게시글 총 갯수
	public int listCount(SearchCriteria scri) throws Exception;
	
	// 파일 조회
	public List<Map<String, Object>> selectFileList(int bno) throws Exception;
	
	// 파일 다운로드
	public Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception;

}
