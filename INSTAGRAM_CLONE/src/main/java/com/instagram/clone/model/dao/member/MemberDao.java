package com.instagram.clone.model.dao.member;

import java.util.List;

import com.instagram.clone.model.vo.MemberJoinProfileSimpleVo;
import com.instagram.clone.model.vo.MemberJoinProfileVo;
import com.instagram.clone.model.vo.MemberProfileVo;
import com.instagram.clone.model.vo.MemberVo;
import com.instagram.clone.model.vo.SearchProfileVo;

public interface MemberDao {

   String NAMESPACE = "member.";

   /*
    * 
    */
   public int emailCheck(MemberVo vo);

   /*
    * 
    */
   public int insertProfile(MemberProfileVo memberProfileVo);

   public MemberProfileVo selectMemberProfile(int member_code);

   public int updateMemberProfileImage(MemberProfileVo member_profile);

   public List<MemberJoinProfileVo> nameSearchAutoComplete(int my_member_code, String id_name);

   public List<MemberJoinProfileSimpleVo> selectMemberList(List<Integer> codeList);

   public int updateMemberProfile(MemberProfileVo memberProfileVo);

   public int updateMember(MemberVo memberVo);

   public SearchProfileVo SearchProfile(String keyword);

}