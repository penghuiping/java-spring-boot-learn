package com.php25.usermicroservice.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * @author: penghuiping
 * @date: 2018/10/17 17:22
 * @description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminAuthorityDto {

    private Long id;

    /**
     * 权限地址
     */
    private String url;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 对应关联的菜单与按钮表id
     */
    private AdminMenuButtonDto adminMenuButton;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminAuthorityDto that = (AdminAuthorityDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
