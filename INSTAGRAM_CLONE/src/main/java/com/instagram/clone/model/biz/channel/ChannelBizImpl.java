package com.instagram.clone.model.biz.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instagram.clone.model.dao.channel.ChannelDao;
import com.instagram.clone.model.dao.member.MemberDao;
import com.instagram.clone.model.vo.ChannelVo;

@Service
public class ChannelBizImpl implements ChannelBiz {


	@Autowired
	private ChannelDao dao;
	

	@Override
	public int myChannelCode(int member_code) {
		System.out.println(dao);
		
		return dao.myChannelCode(member_code);
	}

}
