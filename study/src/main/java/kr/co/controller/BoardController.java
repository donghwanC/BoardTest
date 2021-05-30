package kr.co.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.service.BoardService;
import kr.co.service.ReplyService;
import kr.co.vo.BoardVO;

import kr.co.vo.PageMaker;
import kr.co.vo.ReplyVO;
import kr.co.vo.SearchCriteria;

@Controller
@RequestMapping("/board/*")
public class BoardController {

	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Inject
	private BoardService service;

	@Inject
	private ReplyService reService;
	
	/*
	HttpServletResponse response는 
	처음에 첨부파일을 업로드 할때는 
	MultipartHttpServletRequest mpRequest을 
	이용하여 서버에 요청을 했었는데요. 
	request는 jsp화면에서 서버로 요청할 때 쓰고,
	response는 서버에서 jsp화면으로 응답할때에 쓰입니다. 
	그래서 파일정보들을 responses에 담아 처리를 합니다.
	*/
	@RequestMapping(value = "/fileDown")
	public void fileDown(HttpServletResponse response, @RequestParam Map<String, Object> map) throws Exception{
		// 첨부 된 파일정보를 map형으로 받아 옴
		Map<String, Object> resultMap = service.selectFileInfo(map);
		
		// 가져온 정보를 각 변수에 저장
		String storedFileName = (String) resultMap.get("STORED_FILE_NAME");
		String originalFileName = (String) resultMap.get("ORG_FILE_NAME");
		
		// 파일을 저장했던 위치에서 첨부파일을 읽어
		// byte[]형식으로 변환 함
		byte fileByte[] = org.apache.commons.io.FileUtils.readFileToByteArray(new File("C:\\mp\\file\\"+storedFileName));
		
		
		// 다운로드를 만드시고 난 후에 개발자모드 (F12)를 
		// 누르고 Network로 가신 후 파일을 다운로드 클릭하면 
		// fileDown이 나옵니다. Headers쪽에 빨간 박스를 보시면 
		// 저희가 response에 set한 값들이 보여지는게 보입니다.
		
		// 이런식으로 속성들에 어떤 값들이 들어갔는지 
		// 확인할 수 있습니다 flush() 메소드는 데이터를 
		// 비워주는? 역할을 하고 close() 닫아주는 역할을 합니다.
		
		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\""+URLEncoder.encode(originalFileName, "UTF-8")+"\";");
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	
	@RequestMapping(value = "writeView", method = RequestMethod.GET)
	public String writeView() throws Exception {
		logger.info("get writeView");

		return "board/writeView";
	}

	// 글 작성 + 파일첨부
	@RequestMapping(value = "write", method = RequestMethod.POST)
	public String write(BoardVO vo, MultipartHttpServletRequest mpRequest) throws Exception {		
		logger.info("post write + File");
		
		// MultipartHttpServletRequest.. 파일첨부
		service.write(vo, mpRequest);
		
		return "redirect: /board/list";
	}


	// 게시판 수정뷰
	// 파일 수정
	@RequestMapping(value = "/updateView", method = RequestMethod.GET)
	public String updateView(Model model, BoardVO vo, @ModelAttribute("scri") SearchCriteria scri) throws Exception {
		logger.info("updateView");

		model.addAttribute("update", service.read(vo.getBno()));
		model.addAttribute("scri", scri);

		// 파일내용을 model에 등록
		List<Map<String, Object>> fileList = service.selectFileList(vo.getBno());
		model.addAttribute("file", fileList);
		
		return "board/updateView";
	}

	// 게시판 수정
	// 파일 수정
	
	// 파라미터에 @RequestParam이 붙은 
	// fileNoDel[]과 fileNameDel[]은 jsp에서 
	// fileNoDel[]과 fileNameDel[]로 지정한 값을 
	// String[] 타입으로 담겠다는 말입니다.
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(BoardVO vo, @ModelAttribute("scri") SearchCriteria scri, RedirectAttributes rttr,
			@RequestParam(value = "fileNoDel[]") String[] files, 
			@RequestParam(value = "fileNameDel[]") String[] fileNames,
			MultipartHttpServletRequest mpRequest) throws Exception {
		
		logger.info("update + file");

		service.update(vo, files, fileNames, mpRequest);
		
		rttr.addAttribute("bno", vo.getBno());
		rttr.addAttribute("page", scri.getPage());
		rttr.addAttribute("perPageNum", scri.getPerPageNum());
		rttr.addAttribute("searchType", scri.getSearchType());
		rttr.addAttribute("keyword", scri.getKeyword());
		
		//return "redirect:/board/list";
		return "redirect:/board/readView";
		
	}

	// 게시판 삭제
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(BoardVO vo, @ModelAttribute("scri") SearchCriteria scri, RedirectAttributes rttr) throws Exception {
		logger.info("delete");

		service.delete(vo.getBno());
		
		rttr.addAttribute("page", scri.getPage());
		rttr.addAttribute("perPageNum", scri.getPerPageNum());
		rttr.addAttribute("searchType", scri.getSearchType());
		rttr.addAttribute("keyword", scri.getKeyword());

		return "redirect:/board/list";
	}
	
	
	// 페이징 + 검색 + 게시물 목록
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, @ModelAttribute("scri") SearchCriteria scri) throws Exception{
		logger.info("Paging + Search + list");
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(scri);
		pageMaker.setTotalCount(service.listCount(scri));
		
		model.addAttribute("list", service.list(scri));
		model.addAttribute("pageMaker", pageMaker);
		
		return "board/list";
	}

	// 페이징 + 검색 + 게시판 조회
	// + 파일조회
	@RequestMapping(value = "/readView", method = RequestMethod.GET)
	public String read(BoardVO vo, Model model, @ModelAttribute("scri") SearchCriteria scri) throws Exception {
		logger.info("Paging + Search + read + File");
	
		//System.out.println(vo.getBno());
		
		model.addAttribute("read", service.read(vo.getBno()));
		model.addAttribute("scri", scri);
			
		List<ReplyVO> replyList = reService.readReply(vo.getBno());		
		model.addAttribute("replyList", replyList);
		
		List<Map<String, Object>> fileList = service.selectFileList(vo.getBno());
		model.addAttribute("file", fileList);
		
		return "board/readView";
	}
	
	// 댓글
	@RequestMapping(value = "/replyWrite", method = RequestMethod.POST)
	public String replyWrite(ReplyVO vo, SearchCriteria scri, RedirectAttributes rttr) throws Exception{
		logger.info("reply write");
		
		reService.writeReply(vo);
		
		/*
		 왜 안 되는거지
		rttr.addFlashAttribute("bno", vo.getBno());
		rttr.addFlashAttribute("page", scri.getPage());
		rttr.addFlashAttribute("perPageNum", scri.getPerPageNum());
		rttr.addFlashAttribute("searchType", scri.getSearchType());
		rttr.addFlashAttribute("keyword", scri.getKeyword());
		*/
		
		rttr.addAttribute("bno", vo.getBno());
		rttr.addAttribute("page", scri.getPage());
		rttr.addAttribute("perPageNum", scri.getPerPageNum());
		rttr.addAttribute("searchType", scri.getSearchType());
		rttr.addAttribute("keyword", scri.getKeyword());
		
		return "redirect:/board/readView";
	}
	
	// 댓글수정 GET
	@RequestMapping(value = "/replyUpdateView", method = RequestMethod.GET)
	public String replyUpdateView(ReplyVO vo, SearchCriteria scri, Model model) throws Exception{
		logger.info("reply write GET");
		
		model.addAttribute("replyUpdate", reService.selectReply(vo.getRno()));
		model.addAttribute("scri", scri);
		
		return "board/replyUpdateView";
	}
	
	
	// 댓글수정 POST
	@RequestMapping(value = "/replyUpdate", method = RequestMethod.POST)
	public String replyUpdate(ReplyVO vo, SearchCriteria scri, RedirectAttributes rttr) throws Exception{
		logger.info("reply write POST");
		
		//System.out.println(vo.getBno());
		
		reService.updateReply(vo);
		
		rttr.addAttribute("bno", vo.getBno());
		rttr.addAttribute("page", scri.getPage());
		rttr.addAttribute("perPageNum", scri.getPerPageNum());
		rttr.addAttribute("searchType", scri.getSearchType());
		rttr.addAttribute("keyword", scri.getKeyword());
		
		return "redirect:/board/readView";
	}
	
	
	// 댓글 삭제 GET
	@RequestMapping(value = "/replyDeleteView", method = RequestMethod.GET)
	public String replyDeleteView(ReplyVO vo, SearchCriteria scri, Model model) throws Exception {
		logger.info("reply delete GET");

		model.addAttribute("replyDelete", reService.selectReply(vo.getRno()));
		model.addAttribute("scri", scri);

		return "board/replyDeleteView";
	}
	
	// 댓글 삭제 POST
	@RequestMapping(value = "/replyDelete", method = RequestMethod.POST)
	public String replyDelete(ReplyVO vo, SearchCriteria scri, RedirectAttributes rttr) throws Exception{
		logger.info("reply delete POST");
		
		reService.deleteReply(vo);
		
		rttr.addAttribute("bno", vo.getBno());
		rttr.addAttribute("page", scri.getPage());
		rttr.addAttribute("perPageNum", scri.getPerPageNum());
		rttr.addAttribute("searchType", scri.getSearchType());
		rttr.addAttribute("keyword", scri.getKeyword());
		
		return "redirect:/board/readView";
	}
}
