package com.php25.notifyservice.server.service;

import com.php25.notifyservice.server.dto.PairDto;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 10:02
 * @Description:
 */
public interface MailService {

    /**
     * 发送简单邮件
     *
     * @param sendTo  收件人地址
     * @param title   邮件标题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String sendTo, String title, String content);

    /**
     * 发送简单邮件
     *
     * @param sendTo              收件人地址
     * @param title               邮件标题
     * @param content             邮件内容
     * @param attachments<文件名，附件> 附件列表
     */
    public void sendAttachmentsMail(String sendTo, String title, String content, List<PairDto<String, File>> attachments);

    /**
     * 发送模板邮件
     *
     * @param sendTo              收件人地址
     * @param title               邮件标题
     * @param content<key,        内容> 邮件内容
     * @param attachments<文件名，附件> 附件列表
     */
    public void sendTemplateMail(String sendTo, String title, Map<String, Object> content, List<PairDto<String, File>> attachments);

}
