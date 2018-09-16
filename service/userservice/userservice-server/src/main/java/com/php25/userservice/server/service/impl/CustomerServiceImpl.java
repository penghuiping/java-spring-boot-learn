package com.php25.userservice.server.service.impl;

import com.php25.common.core.dto.DataGridPageDto;
import com.php25.common.jdbc.service.BaseServiceImpl;
import com.php25.userservice.client.constant.CustomerUuidType;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.server.model.Customer;
import com.php25.userservice.server.repository.CustomerRepository;
import com.php25.userservice.server.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by penghuiping on 16/9/2.
 */
@Slf4j
@Transactional
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerDto, Customer, Long> implements CustomerService {


    private CustomerRepository customerRepository;

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public Optional<CustomerDto> findOneByUsernameAndPassword(String username, String password) {
        Assert.hasText(username, "用户名不能为空");
        Assert.hasText(password, "密码不能为空");
        Customer customer = customerRepository.findByUsernameAndPassword(username, password);
        return transOptional(customer);
    }

    @Override
    public Optional<CustomerDto> findByUuidAndType(String uuid, CustomerUuidType type) {
        Assert.hasText(uuid, "uuid表示wx,weibo,qq账号,不能为空");
        Assert.notNull(type, "type不能为null");
        Customer customer = null;
        switch (type) {
            case qq:
                customer = customerRepository.findOneByQQ(uuid);
                break;
            case weibo:
                customer = customerRepository.findOneBySina(uuid);
                break;
            case weixin:
                customer = customerRepository.findOneByWx(uuid);
                break;
            default:
                customer = customerRepository.findOneByWx(uuid);
                break;
        }
        return transOptional(customer);

    }

    @Override
    public Optional<CustomerDto> findOneByPhoneAndPassword(String phone, String password) {
        Assert.hasText(phone, "手机不能为空");
        Assert.hasText(password, "密码不能为空");
        Customer customer = customerRepository.findOneByPhoneAndPassword(phone, password);
        return transOptional(customer);
    }


    @Override
    public Optional<CustomerDto> findOneByPhone(String phone) {
        Assert.hasText(phone, "手机不能为空");
        Customer customer = customerRepository.findOneByPhone(phone);
        return transOptional(customer);
    }

    @Override
    public Optional<CustomerDto> save(CustomerDto obj) {
        Assert.notNull(obj, "CustomerDto不能为null");
        if (null == obj.getId()) {
            obj.setCreateTime(new Date());
        }
        obj.setUpdateTime(new Date());
        return super.save(obj);
    }


    @Override
    public Optional<DataGridPageDto<CustomerDto>> query(Integer pageNum, Integer pageSize, String searchParams) {
        return this.query(pageNum, pageSize, searchParams, BeanUtils::copyProperties, Sort.Direction.DESC, "id");
    }

    @Override
    public Optional<List<CustomerDto>> query(String searchParams, Integer pageNum, Integer pageSize) {
        Optional<DataGridPageDto<CustomerDto>> customerDtoDataGridPageDto = query(pageNum, pageSize, searchParams);
        if (customerDtoDataGridPageDto.isPresent()) {
            return Optional.ofNullable(customerDtoDataGridPageDto.get().getData());
        }
        return Optional.empty();
    }

    /**
     * 此方法用来转化对象
     *
     * @param customer
     * @return
     */
    private CustomerDto trans(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    /**
     * model to dto
     *
     * @param customer
     * @return
     */
    private Optional<CustomerDto> transOptional(Customer customer) {
        if (null != customer) {
            CustomerDto customerDto = new CustomerDto();
            BeanUtils.copyProperties(customer, customerDto);
            return Optional.ofNullable(customerDto);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<CustomerDto>> findByName(String name) {
        Assert.hasText(name, "name不能为空");
        List<Customer> customers = customerRepository.findByName(name);
        return Optional.ofNullable(customers.stream().map(customer -> trans(customer)).collect(Collectors.toList()));
    }
}
