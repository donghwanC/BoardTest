package kr.co.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.dao.BoardDAO;
import kr.co.util.FileUtils;
import kr.co.vo.BoardVO;
import kr.co.vo.Criteria;
import kr.co.vo.SearchCriteria;

@Service
public class BoardServiceImpl implements BoardService {

	@Inject
	private BoardDAO dao;
	
	/*
	@Resource로 FileUtils를 사용할수 있게 추가해 줍니다.
	BoardService와 BoardServiceImpl에 
	첨부파일 파라미터를 받을 MultipartHttpServletRequest를 
	추가해줍니다.

	impl에는 Map타입의 List타입으로 list라는 이름에 
	fileUtils.parseInsertFileInfo(boardVO, mpRequest);를 
	받아옵니다.
	*/
	@Resource(name="fileUtils")
	private FileUtils fileUtils;
	
	// 글 작성 + 파일첨부
	@Override
	public void write(BoardVO vo, MultipartHttpServletRequest mpRequest) throws Exception {
		// TODO Auto-generated method stub
		
		// 먼저.. 글을 작성하는 dao 호출 !!
		// parseInsertFileInfo에 증가된 bno값이 담겨져서
		// insertFile에 bno값이 자동으로 담겨짐
		dao.write(vo);
		
		
		List<Map<String, Object>> list = fileUtils.parseInsertFileInfo(vo, mpRequest);
		
		int size = list.size();		
		
		for(int i=0; i<size; i++) {
			dao.insertFile(list.get(i));
		}
		// for문을 써서 list의 size만큼 넣어주는 이유는 
		//나중에 여러개의 첨부파일을 등록하기 위해서 입니다.
	}

	@Override
	public List<BoardVO> list() throws Exception {
		// TODO Auto-generated method stub
		return dao.list();
	}

	// 게시물 조회 + 조회수 기능
	@Override
	public BoardVO read(int bno) throws Exception {
		// TODO Auto-generated method stub
		dao.boardHit(bno);
		
		return dao.read(bno);
	}

	// 게시글 수정 + 파일 수정
	@Override
	public void update(BoardVO vo, String[] files, 
			String[] fileNames, MultipartHttpServletRequest mpRequest) throws Exception {
		// 글 내용 수정
		dao.update(vo);
		
		// 파일 업데이트할 값들을 list에 담습니다.
		List<Map<String, Object>> list = fileUtils.parseUpdateFileInfo(vo, files, fileNames, mpRequest);
		Map<String, Object> tempMap = null;
		
		int size = list.size();
		
		// fileUtils.parseUpdateFileInfo()결과의 
		// size만큼  for문을 돌립니다
		for(int i=0; i<size; i++) {
			tempMap = list.get(i);
			
			// tempMap에 list.get(i)를 담고 
			// if문을 이용하여 tempMap에서 
			// IS_NEW를 꺼내와서 값이Y와 같으면
			// dao.insertFile(tempMap)를 실행합니다. 
			// 같지 않으면 dao.updateFile(tempMap)실행합니다.
			if(tempMap.get("IS_NEW").equals("Y")) {
				dao.insertFile(tempMap);
			} else {
				dao.updateFile(tempMap);
			}
		}
	}

	@Override
	public void delete(int bno) throws Exception {
		// TODO Auto-generated method stub
		dao.delete(bno);
	}

	@Override
	public List<BoardVO> list(Criteria cri) throws Exception {
		// TODO Auto-generated method stub
		return dao.list(cri);
	}

	@Override
	public int listCount() throws Exception {
		// TODO Auto-generated method stub
		return dao.listCount();
	}

	@Override
	public List<BoardVO> list(SearchCriteria scri) throws Exception {
		// TODO Auto-generated method stub
		return dao.list(scri);
	}

	@Override
	public int listCount(SearchCriteria scri) throws Exception {
		// TODO Auto-generated method stub
		return dao.listCount(scri);
	}

	// 파일조회
	@Override
	public List<Map<String, Object>> selectFileList(int bno) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectFileList(bno);
	}

	// 파일다운로드
	@Override
	public Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectFileInfo(map);
	}

}
