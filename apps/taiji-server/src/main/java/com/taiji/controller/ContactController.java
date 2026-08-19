/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/controller/ContactController.java
 * @Description: 留资/投递接口（见 docs/13 §2.6）
 */
package com.taiji.controller;

import com.taiji.common.Result;
import com.taiji.entity.ContactLead;
import com.taiji.service.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/submit")
    public Result<Void> submit(@Valid @RequestBody ContactLead lead, HttpServletRequest request) {
        // TODO: 接入风控/限频(60s/次，见 docs/13)；当前直接落库
        contactService.save(lead);
        return Result.success(null);
    }
}
