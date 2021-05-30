package kr.co.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.co.dao.MemberDAO;
import kr.co.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {

	@Inject
	private MemberDAO dao;
	
	
	@Override
	public void register(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		dao.register(vo);
	}


	@Override
	public MemberVO login(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		return dao.login(vo);
	}


	@Override
	public void memberUpdate(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		dao.memberUpdate(vo);
	}


	@Override
	public void memberDelete(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		dao.memberDelete(vo);
	}


	@Override
	public int passChk(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		return dao.passChk(vo);
	}


	@Override
	public int idChk(MemberVO vo) throws Exception {
		// TODO Auto-generated method stub
		return dao.idChk(vo);
	}

}
