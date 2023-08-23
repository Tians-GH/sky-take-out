package com.sky.dto;

import com.fasterxml.jackson.databind.util.BeanUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 员工dto
 */
@Data
@ApiModel
public class EmployeeDTO implements Serializable {
    @ApiModelProperty(value = "员工id")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("电话号码")
    private String phone;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("身份证号")
    private String idNumber;

}
