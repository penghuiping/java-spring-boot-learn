package com.php25.userservice.client.rpc;

import com.php25.userservice.client.dto.CustomerDto;

import java.util.List;

/**
 * 用户服务类
 *
 * @author penghuiping
 * @Time 16/9/2.
 */
public interface CustomerRpc {

    CustomerDto findOne(Long id);

    /**
     * 根据用户名与密码，查询出用户信息
     *
     * @param username
     * @param password
     * @return
     * @author penghuiping
     * @Time 16/9/2.
     */
    CustomerDto findOneByUsernameAndPassword(String username, String password);

    /**
     * 根据联系方式和密码查询
     *
     * @param phone
     * @param password
     * @return
     */
    CustomerDto findOneByPhoneAndPassword(String phone, String password);

    /**
     * 根据联系方式查询
     *
     * @param phone
     * @return
     */
    CustomerDto findOneByPhone(String phone);


    /**
     * 分页查询
     *
     * @param searchParams
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<CustomerDto> query(String searchParams, Integer pageNum, Integer pageSize);


    /**
     * 根据姓名模糊查询
     *
     * @param name
     * @return
     */
    List<CustomerDto> findByName(String name);

}
