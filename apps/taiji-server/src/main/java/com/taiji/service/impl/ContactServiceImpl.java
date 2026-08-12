/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/service/impl/ContactServiceImpl.java
 * @Description: 留资业务实现
 */
package com.taiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taiji.entity.ContactLead;
import com.taiji.mapper.ContactLeadMapper;
import com.taiji.service.ContactService;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl extends ServiceImpl<ContactLeadMapper, ContactLead> implements ContactService {
}
