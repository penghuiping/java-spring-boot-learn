package com.php25.auditlogservice.server.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author penghuiping
 * @date 2019/11/6 11:11
 */
@Document(value = "userservice_audit_log")
public class UserServiceAuditLog {

    @Id
    private ObjectId id;

    private String name;

    private Integer age;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }
}
