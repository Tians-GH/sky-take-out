package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 密码编辑dto
 */
@Data
public class PasswordEditDTO implements Serializable {

    //员工id
    private Long empId;

    //旧密码
    private String oldPassword;

    //新密码
    private String newPassword;

}
