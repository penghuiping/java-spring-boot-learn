package com.php25.usermicroservice.web.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author: penghuiping
 * @date: 2019/8/11 23:25
 * @description:
 */
@Setter
@Getter
@EqualsAndHashCode
@Table("t_group")
public class Group {

    @Id
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column("create_date")
    private LocalDateTime createDate;

    @Column("create_user_id")
    private Long createUserId;

    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column("last_modified_user_id")
    private Long lastModifiedUserId;

    @Column("app_id")
    private String appId;

    @Column
    private Integer enable;
}
