package com.php25.mediamicroservice.server;

import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.JSONResponse;
import com.php25.mediamicroservice.MediaServiceApplicationTest;
import com.php25.mediamicroservice.server.vo.ReqImageVo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

/**
 * @author: penghuiping
 * @date: 2018/10/12 10:04
 * @description:
 */
@Slf4j
@Component
public class ImageServiceTest {

    private static final String imageBase64 = "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iOohZmVkDUHs8bkAtrWV6j9FlGRrMxeLehkZbWWnpz8imxSaAHh7fHp66tD09HQ+8lQbgOcTc3P67ByDTJwQkh2y6946kAv+Rf4BjloH9ksQi34AAAAJcEhZcwAAFiUAABYlAUlSJPAAABjISURBVHgB7dZBDQAhEATB46TiAWf4gwQR/ao1MEllHz3W3OdzBAgQIECAAAECBAgkAn+yYoQAAQIECBAgQIAAgScgwD0CAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAgAD3AwQIECBAgAABAgRCAQEeYpsiQIAAAQIECBAgIMD9AAECBAgQIECAAIFQQICH2KYIECBAgAABAgQICHA/QIAAAQIECBAgQCAUEOAhtikCBAgQIECAAAECAtwPECBAgAABAgQIEAgFBHiIbYoAAQIECBAgQICAAPcDBAgQIECAAAECBEIBAR5imyJAgAABAgQIECAgwP0AAQIECBAgQIAAgVBAgIfYpggQIECAAAECBAgIcD9AgAABAgQIECBAIBQQ4CG2KQIECBAgQIAAAQIC3A8QIECAAAECBAgQCAUEeIhtigABAgQIECBAgIAA9wMECBAgQIAAAQIEQgEBHmKbIkCAAAECBAgQICDA/QABAgQIECBAgACBUECAh9imCBAgQIAAAQIECAhwP0CAAAECBAgQIEAgFBDgIbYpAgQIECBAgAABAgLcDxAgQIAAAQIECBAIBQR4iG2KAAECBAgQIECAwAUBkAXahm+bWAAAAABJRU5ErkJggg==";

    public void save(MediaServiceApplicationTest mediaServiceApplicationTest) {
        ReqImageVo reqImageVo = new ReqImageVo(imageBase64);
        String result = mediaServiceApplicationTest.webTestClient.post().uri("/image/save")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(reqImageVo)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(String.class).consumeWith(document("mediaservice_save", requestFields(
                        fieldWithPath("content").description("base64形式的图片内容")
                        ), responseFields(
                        fieldWithPath("errorCode").description("错误码:0为正常;0以外都是非正常"),
                        fieldWithPath("returnObject").description("true:成功,false:失败"),
                        fieldWithPath("message").description("错误描述").type("String"))
                )).returnResult().getResponseBody();

        JSONResponse jsonResponse = JsonUtil.fromJson(result, JSONResponse.class);
        Assertions.assertThat(jsonResponse.getErrorCode()).isEqualTo(ApiErrorCode.ok.value);
        Assertions.assertThat(jsonResponse.getReturnObject()).isNotNull();
        log.info("/image/save:{}", result);
    }

//    @Test
//    public void findOne(MediaServiceApplicationTest mediaServiceApplicationTest) {
//        ReqIdString idStringReq = new ReqIdString();
//        idStringReq.setId("1");
//
//        WebTestClient.BodySpec result = mediaServiceApplicationTest.webTestClient.post().uri("/img/findOne")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaType.APPLICATION_JSON_UTF8)
//                .syncBody(idStringReq)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .expectBody(String.class);
//
//        log.info("/img/findOne:{}", JsonUtil.toJson(result.returnResult().getResponseBody()));
//    }


}
