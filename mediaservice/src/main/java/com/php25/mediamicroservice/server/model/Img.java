package com.php25.mediamicroservice.server.model;

import com.php25.common.db.cnd.annotation.Column;
import com.php25.common.db.cnd.annotation.Table;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import java.util.Date;

/**
 * 图片表,系统图片留痕
 *
 * @author penghuiping
 * @date 2019-01-04
 */
@Data
@Table("mediaservice_img")
public class Img implements Persistable<String> {

    @Id
    private String id;

    /**
     * 图片地址 http访问地址
     */
    @Column("img_url")
    private String imgUrl;

    /**
     * 图片名
     */
    @Column
    private String name;

    /**
     * 创建时间
     */
    @Column("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column("update_time")
    private Date updateTime;

    /**
     * 0:无效 1:有效 2:软删除
     */
    @Column
    private Integer enable;


    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }


}
