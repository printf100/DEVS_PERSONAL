package com.instagram.clone.model.dao.feed;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.instagram.clone.model.vo.FeedVo;

@Repository
public class FeedDaoImpl implements FeedDao {

	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public int insertFeed(FeedVo vo) {
		return sqlSession.insert(NAMESPACE + "insertFeed", vo);
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
		return sqlSession.selectList(NAMESPACE + "myFeedList", channel_code);
	}

}
