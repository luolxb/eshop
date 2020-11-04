package com.soubao.service.impl;

import com.soubao.entity.NewsComment;
import com.soubao.entity.User;
import com.soubao.service.NewsCommentService;
import com.soubao.service.UserService;
import com.soubao.dao.NewsCommentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-03-02
 */
@Service
public class NewsCommentServiceImpl extends ServiceImpl<NewsCommentMapper, NewsComment> implements NewsCommentService {
    @Autowired
    private UserService userService;

    @Override
    public void withUser(List<NewsComment> newsCommentList) {
        Set<Integer> userIds = newsCommentList.stream().map(NewsComment::getUserId).collect(Collectors.toSet());
        if (!userIds.isEmpty()){
            Map<Integer, User> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
            for (NewsComment newsComment:newsCommentList) {
                User user = userMap.get(newsComment.getUserId());
                String commenter = StringUtils.isEmpty(user.getRealname()) ?  user.getMobile() : user.getRealname();
                newsComment.setCommenter(commenter);
            }
        }
    }
}
