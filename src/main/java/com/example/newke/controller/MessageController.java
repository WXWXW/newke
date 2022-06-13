package com.example.newke.controller;

import com.example.newke.annotation.LoginRequired;
import com.example.newke.dto.PageDto;
import com.example.newke.entity.User;
import com.example.newke.service.MessageService;
import com.example.newke.utils.CommunityUtil;
import com.example.newke.utils.HostHolder;
import com.example.newke.vo.ConversationPageVO;
import com.example.newke.vo.MessagePageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/message")

public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private CommunityUtil communityUtil;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @LoginRequired
    public String getMessageList(PageDto pageDto,
                                 Model model,
                                 HttpSession session){
        User user = hostHolder.getUser();
        if(user == null)
            throw new RuntimeException("用户未登录");
        // 分页信息
        pageDto.setLimit(5);
        pageDto.setPath("/message/list");

        MessagePageVO  page = messageService.getAllMessage(user.getId(),pageDto);

        model.addAttribute("page",page);



        int noticeCount = messageService.getNoticeCount(user.getId());
        model.addAttribute("noticeUnreadCount",noticeCount);



        return "/site/letter";
    }

    @RequestMapping("/conversation/{conversionId}")
    @LoginRequired
    public String getConversationList(@PathVariable("conversionId") String conversionId,
                                      PageDto pageDto,
                                      Model model){

        ConversationPageVO page = messageService.getAllConversationMsgByPage(conversionId, pageDto);
        model.addAttribute("page",page);
        model.addAttribute("requestPath","/message/conversation/"+conversionId);
        return "/site/letter-detail";
    }


    @PostMapping("/send")
    @ResponseBody
    public String sendMessage(String toName,String content){
        //构建信息，前端传来发送对象和内容
        int i = messageService.sendMessage(toName, content);
        if (i == 0)
            return communityUtil.getJsonString(1,"信息发送失败");
        else
            return communityUtil.getJsonString(0,"信息发送成功");
    }
}
