package kr.co.dao;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.vo.MemberVO;

@Repository
public class MemberDAOImpl implements MemberDAO {

	private String nameSpace = "memberMapper";
	
	@Inject
	private SqlSession sql;
	
	@Override
	public void register(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		sql.insert(nameSpace+".register", vo);
	}

	@Override
	public MemberVO login(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		return sql.selectOne(nameSpace+".login", vo);
	}

	@Override
	public void memberUpdate(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		sql.update(nameSpace+".memberUpdate", vo);
	}

	@Override
	public void memberDelete(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		sql.delete(nameSpace+".memberDelete", vo);
	}

	@Override
	public int passChk(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		return sql.selectOne(nameSpace+".passChk", vo);
	}

	@Override
	public int idChk(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		return sql.selectOne(nameSpace+".idChk", vo);
	}

}
