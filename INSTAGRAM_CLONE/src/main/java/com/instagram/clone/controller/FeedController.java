package com.instagram.clone.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.instagram.clone.common.properties.ApplicationProperties;
import com.instagram.clone.model.biz.channel.ChannelBiz;
import com.instagram.clone.model.biz.feed.FeedBiz;
import com.instagram.clone.model.biz.member.MemberBiz;
import com.instagram.clone.model.vo.FeedVo;
import com.instagram.clone.model.vo.MemberVo;
import com.instagram.clone.ssohandler.domain.entity.Member;
import com.instagram.clone.ssohandler.service.MemberService;

@RestController
@RequestMapping("/feed/*")
public class FeedController implements ApplicationProperties {

	private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberBiz memberBiz;

	@Autowired
	private FeedBiz biz;

	@Autowired
	private ChannelBiz cbiz;

	// feed.jsp 로 이동
	@GetMapping(value = "")
	public ModelAndView mainPage(HttpServletRequest request, ModelMap map) {
		logger.info("FEED/FEED.GET");

		MemberVo memberVo = (MemberVo) request.getSession().getAttribute("user");
		System.out.println("\n## user in session : " + memberVo);

		if (memberVo == null) {
			return new ModelAndView("redirect:/");
		}

		Member member = memberService.getUser(memberVo.getMember_id());
		System.out.println("\n## user : " + member);

		if (member.getTokenId() == null) {
			request.getSession().removeAttribute("user");
			return new ModelAndView("redirect:/");
		} else {
			map.put("user", member);
			// 프로필 session 주입
			request.getSession().setAttribute("profile", memberBiz.selectMemberProfile(member.getMembercode()));
			// 서버 포트를 session에 셋팅하여 jsp 페이지에서 사용한다.
			request.getSession().setAttribute("SERVER_PORT", SERVER_PORT);
		}

		return new ModelAndView("feed/feed");
	}

	@PostMapping(value = "/feedlist")
	public Map<String, String> feedList(int startNo) {
		logger.info("FEED/FEEDLIST");

		Map<String, String> map = new HashMap<String, String>();

		return map;
	}

	@GetMapping(value = "/insertpage")
	public ModelAndView insertPage(HttpSession session, int member_code) {
		logger.info("FEED/INSERTPAGE");

		// session.setAttribute("", value);
		return new ModelAndView("feed/insertboard");
	}

	@PostMapping(value = "/insertFeed")
	public void fileUpload(@RequestParam("mpfile") MultipartFile multi, HttpServletRequest request, FeedVo vo,
			Model model) throws IOException {

		// 저장 경로
		String filePath = "/resources/images/feedupload";
		String savePath = request.getSession().getServletContext().getRealPath(filePath);

		File file;
		if (!(file = new File(savePath)).isDirectory()) {
			file.mkdirs();
		}

		String board_file_original_name = multi.getOriginalFilename();
		String uuid = UUID.randomUUID().toString();
		String board_file_server_name = uuid + board_file_original_name;
		File targetFile = new File(savePath, board_file_server_name);
		String board_file_ext = board_file_server_name.substring(board_file_server_name.lastIndexOf(".") + 1);
		try {
			InputStream fileStream = multi.getInputStream();
			 FileUtils.copyInputStreamToFile(fileStream, targetFile);
		} catch (IOException e) {
			 FileUtils.deleteQuietly(targetFile);
			e.printStackTrace();
		}
		int member_code = vo.getMember_code();
		System.out.println("*******멤버코드 들어왔다 : " + member_code);

		int channel_code = (cbiz.myChannelCode(member_code));

		FeedVo voObj = new FeedVo();
		voObj.setMember_code(vo.getMember_code());
		voObj.setBoard_content(vo.getBoard_content());
		voObj.setChannel_code(channel_code);
		voObj.setBoard_file_original_name(board_file_original_name);
		voObj.setBoard_file_server_name(board_file_server_name);
		voObj.setBoard_file_path(savePath);
		voObj.setBoard_file_ext(board_file_ext);

		System.out.println(voObj.getBoard_code());
		System.out.println(voObj.getBoard_content());
		System.out.println(voObj.getBoard_file_original_name());
		System.out.println(voObj.getBoard_file_server_name());
		System.out.println(voObj.getBoard_file_ext());
		int res = biz.insertFeed(voObj);

		// 동영상 업로드시 썸네일 이미지 생성
		String str = null;
		int i = board_file_server_name.indexOf(".");
		String noExtension = board_file_server_name.substring(0, i);
		String[] cmd = new String[] {
				"C:\\Workspace_final\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\INSTAGRAM_CLONE\\resources\\images\\feedupload\\ffmpeg",
				"-i",
				"C:\\Workspace_final\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\INSTAGRAM_CLONE\\resources\\images\\feedupload\\"
						+ board_file_server_name,
				"-an", "-ss", "00:00:01", "-r", "1", "-vframes", "1", "-y",
				"C:\\Workspace_final\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\INSTAGRAM_CLONE\\resources\\images\\feedupload\\"
						+ noExtension + ".jpg" };
		Process process = null;
		try {
			if (board_file_ext.equals("mp4") || board_file_ext.equals("avi")) {
				// 프로세스 빌더를 통하여 외부 프로그램 실행
				process = new ProcessBuilder(cmd).start();
				// 외부 프로그램의 표준출력 상태 버퍼에 저장
				BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
				// 표준출력 상태를 출력
				while ((str = stdOut.readLine()) != null) {
					System.out.println(str);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@GetMapping(value = "/selectContent")
	public Map<String, String> selectContent(@RequestParam(value = "board_code") String board_num) {
		logger.info("FEED/SELECTCONTENT(AJAX)");
		int board_code = Integer.parseInt(board_num);
		System.out.println("**********board_code 넘어옴!" + board_code);
//		String board_file_server_name = biz.selectFeed(board_code);

		return null;
	}

}
