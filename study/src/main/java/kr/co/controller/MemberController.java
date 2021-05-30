package kr.co.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.service.MemberService;
import kr.co.vo.MemberVO;

@Controller
@RequestMapping("/member/*")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Inject
	private MemberService service;
	@Inject
	private BCryptPasswordEncoder pwdEncoder;
	
	
	// 회원가입 GET 회원가입 화면 이동
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String getRegister() throws Exception{
		logger.info("get Register");
		
		return "member/register";
	}
	
	// 회원가입 POST 회원등록 버튼 누르면 진입
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String postRegister(MemberVO vo) throws Exception{
		logger.info("post Register");
		
		// 동일한 id 체크
		// 있다면 1, 없다면 0
		int result = service.idChk(vo);
		
		try {
			if(result == 1) {
				// 동일한 아이디가 있다면
				// 다시 회원가입 페이지로
				return "/member/register";
			} else {
				// 없다면 등록.. 하기전에
				// 받은 비번 값을
				// 암호화 해서 
				// vo객체에 set하기
				String inputPass = vo.getUserPass();
				String pwd = pwdEncoder.encode(inputPass);
				vo.setUserPass(pwd);
				
				service.register(vo);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException();
		}
		
		return "redirect:/";
	}
	
	// 로그아웃 GET
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception{
		logger.info("get Logout");
		
		session.invalidate();
		
		return "redirect:/";
	}
	
	
	// 로그인 POST
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(MemberVO vo, HttpServletRequest req, RedirectAttributes rttr) throws Exception{
		logger.info("post Login");
		
		HttpSession session = req.getSession();
		MemberVO login = service.login(vo);
		
		// 입력받은 비번 값과(vo.pass)
		// DB에 있는 비번 값을 비교(login.pass)
		boolean pwdMatch = pwdEncoder.matches(vo.getUserPass(), login.getUserPass());
		
		if(login != null && pwdMatch == true) {
			session.setAttribute("member", login);
		} else {
			session.setAttribute("member", null);
			rttr.addFlashAttribute("msg", false);
		}
		
		return "redirect:/";
	}
	
	// 유저정보 업데이트 뷰로 이동 GET
	@RequestMapping(value = "/memberUpdateView", method = RequestMethod.GET)
	public String registerUpdateView() throws Exception{
		logger.info("get memberUpdateView");
		
		return "member/memberUpdateView";
	}
	
	// 유저정보 업데이트 액션 POST
	@RequestMapping(value = "/memberUpdate", method = RequestMethod.POST)
	public String registerUpdate(MemberVO vo, HttpSession session) throws Exception{
		logger.info("post memberUpdate");
		
		if(vo.getNewPass().trim().length() == 0) {
			vo.setNewPass(vo.getUserPass());
		}
		
		String inputPass = vo.getNewPass();
		String pwd = pwdEncoder.encode(inputPass);
			
		vo.setNewPass(pwd);
		
		service.memberUpdate(vo);
		
		session.invalidate();
		
		return "redirect:/";
	}
	
	
	// 회원탈퇴 GET
	@RequestMapping(value = "/memberDeleteView", method = RequestMethod.GET)
	public String memberDeleteView() throws Exception{
		logger.info("get memberDeleteView");
		
		return "member/memberDeleteView";
	}
	
	
	// 회원탈퇴 POST
	@RequestMapping(value = "/memberDelete", method = RequestMethod.POST)
	public String memberDelete(MemberVO vo, HttpSession session, RedirectAttributes rttr) throws Exception{
		logger.info("post memberDelete");
		
		/*
		// "member"세션에 있는 정보를 가져옴
		MemberVO member = (MemberVO) session.getAttribute("member");
		
		// 세션에 있는 비밀번호
		String sessionPass = member.getUserPass();
		// vo로 가져온 비밀번호
		String voPass = vo.getUserPass();
		
		if(!sessionPass.equals(voPass)) {
			rttr.addFlashAttribute("msg", false);
			
			return "redirect:/member/memberDeleteView";
		}
		*/
		
		service.memberDelete(vo);
		session.invalidate();
		
		return "redirect:/";
	}
	
	// 패스워드 체크
	@RequestMapping(value = "/passChk", method = RequestMethod.POST)
	@ResponseBody
	public boolean passChk(MemberVO vo) throws Exception{
		logger.info("post passChk");
		
		MemberVO login = service.login(vo);
		boolean pwdChk = pwdEncoder.matches(vo.getUserPass(), login.getUserPass());
		
		return pwdChk;
		/*
		int result = service.passChk(vo);
		
		return result;
		*/
	}
	
	// 아이디 중복체크
	@RequestMapping(value = "/idChk", method = RequestMethod.POST)
	@ResponseBody
	public int idChk(MemberVO vo) throws Exception{
		logger.info("post idChk");
		
		int result = service.idChk(vo);
		
		return result;
	}
}
