package com.instagram.clone.model.dao.feed;

import java.util.List;

import com.instagram.clone.model.vo.FeedVo;

public interface FeedDao {

	String NAMESPACE = "feed.";

	// 게시글 등록
	public int insertFeed(FeedVo vo);

	// 게시글 수정
	public int updateFeed(FeedVo vo);

	// 게시글 삭제
	public int deleteFeed(int board_code);

	// 게시글 조회
	public FeedVo selectFeed(int board_code);

	// 내 피드 전체조회
	public List<FeedVo> myFeedList(int channel_code);
}
