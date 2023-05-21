package com.financecrm.webportal.controller.admin;

import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.input.user.UpdateUserStatusInput;
import com.financecrm.webportal.services.CustomUserService;
import com.financecrm.webportal.services.admin.AdminCustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/customUser")

public class AdminCustomUserController {

    @Autowired
    private AdminCustomUserService adminCustomUserService;
    @Autowired
    private CustomUserService customUserService;

    @PostMapping("/updateUserStatus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User updateUserStatus(@RequestBody UpdateUserStatusInput updateUserStatusInput){
        return adminCustomUserService.updateUserStatus(updateUserStatusInput);
    }

}
