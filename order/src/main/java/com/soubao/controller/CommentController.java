package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.*;
import com.soubao.service.CommentService;
import com.soubao.service.ReplyService;
import com.soubao.service.UserService;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.utils.ShopStringUtil;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * 评论表 前端控制器
 *
 * @author dyr
 * @since 2020-02-28
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private UserService userService;

    @ApiOperation("商品评价概况")
    @GetMapping("/statistics")
    public GoodsCommentStatistic statistics(@RequestParam(value = "goods_id") Integer goodsId) {
        Goods goods = new Goods();
        goods.setGoodsId(goodsId);
        return commentService.getGoodsCommentStatistic(goods);
    }

    @ApiOperation("商品评价分页")
    @GetMapping("/page")
    public Page<Comment> getPage(
            @RequestParam(value = "goods_id") Integer goodsId,
            @RequestParam(value = "type", defaultValue = "1") Integer type,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "2") Integer size) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.select("comment_id, content, goods_rank, img, impression, is_anonymous, reply_num, spec_key_name, zan_num, add_time");
        wrapper.eq("is_show", 1).eq("parent_id", 0).eq("deleted", 0);
        if (goodsId != null) {
            wrapper.eq("goods_id", goodsId);
        }
        if (type != null) {
            if (type == 1) {
                wrapper.in("goods_rank", 0, 1, 2, 3, 4, 5);
            }
            if (type == 2) {
                wrapper.in("goods_rank", 4, 5);
            }
            if (type == 3) {
                wrapper.eq("goods_rank", 3);
            }
            if (type == 4) {
                wrapper.in("goods_rank", 0, 1, 2);
            }
            if (type == 5) {
                wrapper.apply("img != ''");
            }
        }
        wrapper.orderByDesc("comment_id");
        IPage<Comment> commentIPage = commentService.page(new Page<>(page, size), wrapper);
        commentService.withUser(commentIPage.getRecords());
        return (Page<Comment>) commentIPage;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @ApiOperation("添加评价")
    @PostMapping
    public SBApi add(@RequestBody Comment comment, HttpServletRequest request) {
        User user = authenticationFacade.getPrincipal(User.class);
        comment.setUserId(user.getUserId());
        comment.setIpAddress(ShopStringUtil.getIpAddr(request));
        commentService.orderGoodsAddComment(comment);
        return new SBApi();
    }

    @GetMapping("/page/admin")
    public IPage<Comment> pageAdmin(
            @RequestParam(value = "store_id", required = false) Integer storeId,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        IPage<Comment> commentIPage = new Page<>();
        if (storeId != null) {
            wrapper.eq("store_id", storeId);
        }
        if (StringUtils.isNotEmpty(content)) {
            wrapper.like("content", content);
        }
        if (null != username) {
            Set<Integer> uIds = userService.userIdsByName(username);
            if (!uIds.isEmpty()) {
                wrapper.in("user_id", uIds);
            } else {
                return commentIPage;
            }
        }
        wrapper.orderByDesc("add_time");
        commentIPage = commentService.page(new Page<>(page, size), wrapper);
        commentService.withUser(commentIPage.getRecords());
        commentService.withGoodsName(commentIPage.getRecords());
        return commentIPage;
    }

    @DeleteMapping
    public SBApi remove(@RequestParam("ids") Set<Integer> ids) {
        commentService.removeByIds(ids);
        return new SBApi();
    }

    @PutMapping("/is_show")
    public SBApi updateComment(
            @RequestParam("comment_ids") Set<Integer> commentIds,
            @RequestParam("is_show") Integer isShow) {
        UpdateWrapper<Comment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_show", isShow).in("comment_id", commentIds);
        commentService.update(updateWrapper);
        return new SBApi();
    }

    @GetMapping("/record")
    public List<Comment> getCommentRecord(@RequestParam("parent_id") Integer parentId) {
        return commentService.list(new QueryWrapper<Comment>().eq("parent_id", parentId));
    }

    @GetMapping("/count")
    public Integer getCount(@RequestParam(value = "is_show", required = false) Integer isShow) {
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        if (isShow != null) {
            commentQueryWrapper.eq("is_show", isShow);
        }
        return commentService.count(commentQueryWrapper);
    }

    @PostMapping("/reply")
    public SBApi saveComment(
            @RequestParam("comment_id") Integer commentId,
            @RequestParam("content") String content) {
        Comment comment = new Comment();
        Comment cm = commentService.getById(commentId);
        Integer goodsId = cm.getGoodsId();
        comment.setAddTime(System.currentTimeMillis() / 1000);
        comment.setContent(content);
        comment.setGoodsId(goodsId);
        comment.setParentId(commentId);
        comment.setStoreId(cm.getStoreId());
        comment.setImg("");
        commentService.save(comment);
        return new SBApi();
    }

    @GetMapping("/{comment_id}")
    public Comment comment(@PathVariable("comment_id") Integer commentId) {
        return commentService.getComment(commentId);
    }

    @GetMapping("/reply/page")
    public IPage<Reply> replies(
            @RequestParam("comment_id") Integer commentId,
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId).eq("deleted", 0);
        return replyService.page(new Page<>(page, size), queryWrapper);
    }
}
