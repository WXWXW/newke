package com.example.newke.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.newke.dao.DiscussPostDao;
import com.example.newke.dao.MessageDao;
import com.example.newke.dao.UserDao;
import com.example.newke.dto.PageDto;
import com.example.newke.entity.DiscussPost;
import com.example.newke.entity.Message;
import com.example.newke.entity.User;
import com.example.newke.service.MessageService;
import com.example.newke.service.UserService;
import com.example.newke.utils.HostHolder;
import com.example.newke.utils.SensitiveUtil;
import com.example.newke.vo.ConversationPageVO;
import com.example.newke.vo.ConversationVO;
import com.example.newke.vo.MessagePageVO;
import com.example.newke.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements MessageService {


    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private SensitiveUtil sensitiveUtil;

    @Autowired
    private UserService userService;





    @Override
    public MessagePageVO getAllMessage(int userId, PageDto pageDto) {

        Page<Message> page = new Page<>(pageDto.getCurrent(), pageDto.getLimit());
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = Wrappers.lambdaQuery();
        messageLambdaQueryWrapper.orderByDesc(Message::getCreateTime);
        Page<Message> messageListPage  = messageDao.selectPage(page, messageLambdaQueryWrapper);
        List<Message> messageList = messageListPage.getRecords();               //所有用户的所有会话

        //当前用户的第一条会话记录
        List<Message> messagesThisUser = messageDao.getAllMessagesThisUser(userId);

        List<ConversationVO> conversationVOS = messagesThisUser.stream().map(message -> {

            ConversationVO conversationVO = new ConversationVO();
            conversationVO.setMessage(message);
            //发消息的对象
            int selectUserId = message.getFromId() == userId ? message.getToId() : message.getFromId();
            User user = userDao.selectById(selectUserId);
            conversationVO.setUser(user);


            Integer countOfAllMessage = messageDao.getCountOfAllMessage(message.getConversationId());

            conversationVO.setCountOfMessage(countOfAllMessage);

            Integer countOfUnread = messageDao.getCountOfUnread(message.getConversationId());

            conversationVO.setCountOfUnread(countOfUnread);

            return conversationVO;
        }).collect(Collectors.toList());

        long totalUnreadMsg = messageDao.getTotalUnreadMessage(userId);


        return new MessagePageVO(messageList,conversationVOS,totalUnreadMsg,pageDto);



    }

    @Override
    public int selectConversationCount(int userId) {

        return 0;
    }

    @Override
    public List<Message> selectLetters(String conversationId, PageDto pageDto) {

        return null;
    }

    @Override
    public int selectLettersCount(String conversationId) {

        return 0;
    }

    @Override
    public int selectLetterUnreadCount(int userId, String conversationId) {

        return 0;
    }

    @Override
    public int getNoticeCount(int userId) {
        return messageDao.getAllUnreadNoticeCount(userId);
    }

    @Override
    public int updateStatus(List<Integer> ids, int status) {
        return 0;
    }

    @Override
    public int sendMessage(String toName, String content) {
        Message message = new Message();
        //信息完整封装
        User user = hostHolder.getUser();
        if (user == null)
            throw new RuntimeException("用户未登录");
        message.setFromId(user.getId());
        //对私信内容进行敏感词处理
        content = HtmlUtils.htmlEscape(content);
        content = sensitiveUtil.filter(content);
        message.setContent(content);
        User toUser = userService.getUserByName(toName);
        if(toUser == null)
            throw new RuntimeException("接收者账号不存在");
        message.setToId(toUser.getId());
        String conversationId = "";
        if(user.getId() < toUser.getId()){
            conversationId = user.getId() +"_"+ toUser.getId();
        }else {
            conversationId = toUser.getId() +"_"+user.getId();
        }
        message.setConversationId(conversationId);
        message.setCreateTime(new Date());
        return messageDao.insert(message);
    }

    @Override
    public ConversationPageVO getAllConversationMsgByPage(String conversationId, PageDto pageDto) {
        if (conversationId == null)
            throw new IllegalArgumentException("会话id不能为空");
        Page<Message> page = new Page<>(pageDto.getCurrent(), pageDto.getLimit());

        List<Message> allMessageThisConversation = messageDao.getAllMessageThisConversation(conversationId);
        List<ConversationVO> messages = allMessageThisConversation.stream().map(message -> {
            ConversationVO conversationVO = new ConversationVO();
            //设置已读
            messageDao.updateMessageStatus(Arrays.asList(message.getId()),1);
            conversationVO.setMessage(message);
            User user = userService.getUserById(message.getFromId());
            System.out.println("user"+"="+user);
            //设置消息发送者
            conversationVO.setUser(user);
            return conversationVO;
        }).collect(Collectors.toList());
        User user = hostHolder.getUser();
        for (ConversationVO message : messages) {
            if (message.getUser().getId() != user.getId()) {
                user = userService.getUserById(message.getUser().getId());
                break;
            }
        }

        //当前页的消息设置为已读

        return new ConversationPageVO(page,messages, user,1);
    }


}
