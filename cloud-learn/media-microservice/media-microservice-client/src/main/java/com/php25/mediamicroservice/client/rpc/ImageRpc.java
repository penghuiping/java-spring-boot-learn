package com.php25.mediamicroservice.client.rpc;

import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.IdsStringReq;
import com.php25.mediamicroservice.client.bo.Base64ImageBo;
import com.php25.mediamicroservice.client.bo.res.ImgBoListRes;
import com.php25.mediamicroservice.client.bo.res.ImgBoRes;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @Auther: penghuiping
 * @Date: 2018/6/22 11:09
 * @Description:
 */
public interface ImageRpc {

    /**
     * 保存base64Image图片，并返回保存的文件名
     */
    public Mono<String> save(@Valid Mono<Base64ImageBo> base64ImageReqMono);

    /**
     * 通过图片id查询图片信息
     */
    public Mono<ImgBoRes> findOne(@Valid Mono<IdStringReq> idStringReqMono);


    /**
     * 批量查询图片信息
     */
    public Mono<ImgBoListRes> findAll(@Valid Mono<IdsStringReq> idsStringReqMono);
}
