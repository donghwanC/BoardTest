package kr.co.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.vo.BoardVO;
import kr.co.vo.Criteria;
import kr.co.vo.SearchCriteria;

@Repository
public class BoardDAOImpl implements BoardDAO {

	@Inject
	private SqlSession sqlSession;
	
	private String nameSpace = "boardMapper";
	
	
	@Override
	public void write(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.insert(nameSpace+".insert", boardVO);
	}
	@Override
	public List<BoardVO> list() throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList(nameSpace+".list");
	}
	@Override
	public BoardVO read(int bno) throws Exception {
		// TODO Auto-generated method stub

		return sqlSession.selectOne(nameSpace+".read", bno);
	}
	@Override
	public void update(BoardVO boardVO) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.update("boardMapper.update", boardVO);
	}
	@Override
	public void delete(int bno) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.delete("boardMapper.delete", bno);
	}
	@Override
	public List<BoardVO> list(Criteria cri) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList("boardMapper.listPage", cri);
	}
	@Override
	public int listCount() throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("boardMapper.listCount");
	}
	@Override
	public List<BoardVO> list(SearchCriteria scri) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList(nameSpace+".listPage", scri);
	}
	@Override
	public int listCount(SearchCriteria scri) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(nameSpace+".listCount", scri);
	}
	
	// 파일첨부
	@Override
	public void insertFile(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.insert(nameSpace+".insertFile", map);
	}
	
	// 파일조회
	@Override
	public List<Map<String, Object>> selectFileList(int bno) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList(nameSpace+".selectFileList", bno);
	}
	
	// 파일다운
	@Override
	public Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(nameSpace+".selectFileInfo", map);
	}
	
	// 파일삭제
	@Override
	public void updateFile(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.update(nameSpace+".updateFile", map);
	}
	
	// 조회수
	@Override
	public void boardHit(int bno) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.update(nameSpace+".boardHit", bno);
	}

}
