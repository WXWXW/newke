package com.example.newke.controller;

import com.example.newke.dto.PageDto;
import com.example.newke.entity.DiscussPost;
import com.example.newke.entity.User;
import com.example.newke.service.DiscussPostService;
import com.example.newke.service.UserService;
import com.example.newke.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {


    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;


    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String  getIndexPage(Model model, PageDto pageDto){

        pageDto.setPath("/index");

        pageDto.setRows(discussPostService.getTotal());




        PageResult<DiscussPost> discussPostPageResult = discussPostService.listDiscussPost(pageDto);
        List<DiscussPost> recordList = discussPostPageResult.getRecordList();

        List<Map<String, Object>> discussPosts = new ArrayList<>();

        for(DiscussPost post :recordList){

            Map<String, Object> map = new HashMap<>();

            map.put("post", post);

            User user = userService.findUserById(post.getUserId());
            map.put("user", user);

            map.put("likeCount",11);

            discussPosts.add(map);
        }


        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("pageDto",pageDto);



        return "/index";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "/error/500";
    }
}
