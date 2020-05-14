package com.instagram.clone.model.biz.feed;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instagram.clone.model.dao.feed.FeedDao;
import com.instagram.clone.model.vo.FeedVo;

@Service
public class FeedBizImpl implements FeedBiz {

	@Autowired
	private FeedDao dao;
	
	@Override
	public int insertFeed(FeedVo vo) {
		return dao.insertFeed(vo);
	}

	@Override
	public int updateFeed(FeedVo vo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteFeed(int board_code) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FeedVo selectFeed(int board_code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FeedVo> myFeedList(int channel_code) {
		
		return dao.myFeedList(channel_code);
	}

}
