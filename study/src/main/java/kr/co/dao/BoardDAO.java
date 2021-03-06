package kr.co.dao;

import java.util.List;
import java.util.Map;

import kr.co.vo.BoardVO;
import kr.co.vo.Criteria;
import kr.co.vo.SearchCriteria;

public interface BoardDAO {

	// 게시글 작성
	public void write(BoardVO boardVO) throws Exception;

	// 게시물 목록 조회
	public List<BoardVO> list() throws Exception;

	// 게시물 조회
	public BoardVO read(int bno) throws Exception;

	// 게시물 수정
	public void update(BoardVO boardVO) throws Exception;

	// 게시물 삭제
	public void delete(int bno) throws Exception;

	// 페이징처리 + 게시글 목록
	public List<BoardVO> list(Criteria cri) throws Exception;

	// 페이징처리 + 게시글 총 갯수
	public int listCount() throws Exception;
	
	// 페이징처리 + 검색기능 + 게시물 목록
	public List<BoardVO> list(SearchCriteria scri) throws Exception;
	
	// 페이징처리 + 검색기능 + 게시글 총 갯수
	public int listCount(SearchCriteria scri) throws Exception;
	
	// 첨부파일 업로드
	public void insertFile(Map<String, Object> map) throws Exception;
	
	// 파일 조회
	public List<Map<String, Object>> selectFileList(int bno) throws Exception;
	
	// 파일 다운로드
	public Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception;
	
	// 파일 삭제
	public void updateFile(Map<String, Object> map) throws Exception;
	
	// 조회수
	public void boardHit(int bno) throws Exception;
	
}
