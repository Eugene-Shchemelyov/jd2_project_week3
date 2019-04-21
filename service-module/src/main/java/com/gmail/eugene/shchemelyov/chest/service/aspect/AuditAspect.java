package com.gmail.eugene.shchemelyov.chest.service.aspect;

import com.gmail.eugene.shchemelyov.chest.repository.model.AuditItem;
import com.gmail.eugene.shchemelyov.chest.service.AuditItemService;
import com.gmail.eugene.shchemelyov.chest.service.enums.AuditItemStatusEnum;
import com.gmail.eugene.shchemelyov.chest.service.model.ItemDTO;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.gmail.eugene.shchemelyov.chest.service.constant.ItemConstant.DELETED;

@Aspect
@Component
public class AuditAspect {
    private final AuditItemService auditItemService;

    @Autowired
    public AuditAspect(AuditItemService auditItemService) {
        this.auditItemService = auditItemService;
    }

    @Pointcut("execution(* com.gmail.eugene.shchemelyov.chest.service.impl.ItemServiceImpl.add(..))")
    public void saveMethodPointcut() {
    }

    @AfterReturning(
            pointcut = "saveMethodPointcut()",
            returning = "itemDTO"
    )
    public void doAccessCheck(ItemDTO itemDTO) {
        AuditItem auditItem = new AuditItem(
                null,
                AuditItemStatusEnum.CREATED.toString(),
                itemDTO.getId(),
                new Date(),
                DELETED
        );
        auditItemService.save(auditItem);
    }

    @After("execution(* com.gmail.eugene.shchemelyov.chest.service.impl.ItemServiceImpl.update(Long, String)) " +
            "&& args(id, status)")
    public void beforeUpdateStatus(Long id, String status) {
        AuditItem auditItem = new AuditItem(
                null,
                AuditItemStatusEnum.UPDATED.toString(),
                id,
                new Date(),
                DELETED
        );
        auditItemService.save(auditItem);
    }
}
