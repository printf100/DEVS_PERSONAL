package com.instagram.clone.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.instagram.clone.model.biz.channel.ChannelBiz;
import com.instagram.clone.model.biz.feed.FeedBiz;
import com.instagram.clone.model.biz.member.MemberBiz;
import com.instagram.clone.model.vo.MemberProfileVo;
import com.instagram.clone.model.vo.MemberVo;
import com.instagram.clone.model.vo.SearchProfileVo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberBiz biz;

	@Autowired
	private FeedBiz fbiz;

	@Autowired
	private ChannelBiz cbiz;

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	// email 존재여부 체크
	@PostMapping(value = "/ajaxemailcheck")
	public Map<String, Boolean> emailCheck(@RequestBody MemberVo vo) {
		logger.info("MEMBER/AJAX ID CHECK");
		int res = biz.emailCheck(vo);
		boolean check = false;

		if (res > 0) {
			check = true;
		}

		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("check", check);

		return map;
	}

	/*
	 * profile 부분
	 */

	// profileUpdate
	@PostMapping(value = "/profileUpdate")
	public ModelAndView profileUpdate(HttpSession session, MemberVo vo, MemberProfileVo pvo) {

		int ress = biz.updateMember(vo);
		int res = biz.updateMemberProfile(pvo);

		if (res > 0 && ress > 0) {
			return new ModelAndView("member/profile");
		} else {
			return new ModelAndView("member/profileEdit");
		}
	}

	// profile.jsp 로 이동
	@GetMapping(value = "/profile")
	public ModelAndView profilePage(Model model, HttpSession session) {
		logger.info("MEMBER/PROFILE.GET");

		int member_code = ((MemberVo) session.getAttribute("user")).getMember_code();
		int channel_code = cbiz.myChannelCode(member_code);
		model.addAttribute("feedList", fbiz.myFeedList(channel_code));
		return new ModelAndView("member/profile");
	}

	@RequestMapping(value = "/profileEdit")
	public ModelAndView insert() {
		return new ModelAndView("member/profileEdit");
	}

	// 계정관리(정보: 이미지) 수정처리
	@PostMapping(value = "/updateprofileimage")
	private Map<String, Object> updateProfileImage(HttpServletRequest request, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();

		// 업로드될 경로
		String filePath = "/resources/images/profileupload/";

		// 업로드될 실제 경로 (이클립스 상의 절대경로)
		String FILE_PATH = request.getSession().getServletContext().getRealPath(filePath);
		System.out.println("절대경로 : " + FILE_PATH);

		// 디렉토리 없을 시 자동 생성!
		File file;
		if (!(file = new File(FILE_PATH)).isDirectory()) {
			file.mkdirs();
		}

		MultipartRequest mr = null;

		try {

			mr = new MultipartRequest(request, // request 객체
					FILE_PATH, // 파일이 저장될 폴더
					1024 * 1024 * 3, // 최대 업로드크기 (3MB)
					"UTF-8", // 인코딩 방식
					new DefaultFileRenamePolicy() // 동일한 파일명이 존재하면 파일명 뒤에 일련번호를 부여
			);

		} catch (IOException e) {
			System.out.println("[ ERROR ] : BoardController - MultipartRequest 객체 생성 오류");
			e.printStackTrace();
		}

		MemberProfileVo member_profile = new MemberProfileVo();

		// 파라미터 받기
		int member_code = Integer.parseInt(mr.getParameter("member_code"));
		// 파일 첨부
		String MEMBER_IMG_SERVER_NAME = null;
		String MEMBER_IMG_ORIGINAL_NAME = null;
		String imgExtend = null;

		// 실제 저장된 파일명
		MEMBER_IMG_SERVER_NAME = mr.getFilesystemName("member_img_original_name");

		if (MEMBER_IMG_SERVER_NAME != null) {
			// 원래 이미지 이름
			MEMBER_IMG_ORIGINAL_NAME = mr.getOriginalFileName("member_img_original_name");

			// 이미지 확장자
			imgExtend = MEMBER_IMG_SERVER_NAME.substring(MEMBER_IMG_SERVER_NAME.lastIndexOf(".") + 1);
			System.out.println("이미지 확장자명 : " + imgExtend);
		}

		member_profile.setMember_code(member_code);
		member_profile.setMember_img_original_name(MEMBER_IMG_ORIGINAL_NAME);
		member_profile.setMember_img_server_name(MEMBER_IMG_SERVER_NAME);
		member_profile.setMember_img_path(FILE_PATH);

		int res = biz.updateMemberProfileImage(member_profile);

		if (res > 0) {
			// 프로필 정보를 session에 리셋
			session = request.getSession();
			MemberProfileVo new_member_profile = biz.selectMemberProfile(member_code);
			session.removeAttribute("profile");
			session.setAttribute("profile", new_member_profile);
			System.out.println(new_member_profile);
		}

		map.put("res", res);
		map.put("img", biz.selectMemberProfile(member_code).getMember_img_server_name());
		return map;
	}

	// 검색한 프로필로 이동
	@RequestMapping(value = "/headerSearch", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView SearchProfile(Model model, @RequestParam("search") String keyword) {

		SearchProfileVo profile = biz.SearchProfile(keyword);

		if (profile != null) {
			System.out.println("************" + profile.getMember_id());
			model.addAttribute("profile", profile);

			return new ModelAndView("member/searchProfile");
		} else {
			return new ModelAndView("redirect:/feed/");
		}

	}

}
