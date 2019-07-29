/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.php25.gateway.config;

import com.php25.common.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author Rob Winch
 * @since 5.1
 */
@Slf4j
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setSerializationStrategy(new JdkSerializationStrategy());
        return tokenStore;
    }


    @Bean
    ResourceServerTokenServices tokenServices(@Autowired TokenStore tokenStore) {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        return defaultTokenServices;
    }

    @Bean
    @Order(-1)
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ResourceServerTokenServices resourceServerTokenServices) throws Exception {
        return http.csrf().disable().authorizeExchange()
                .pathMatchers("/**/**").access((authenticationMono, context) -> {
                    String token = context.getExchange().getRequest().getHeaders().getFirst("Authorization");
                    if (StringUtil.isBlank(token)) {
                        return Mono.just(new AuthorizationDecision(false));
                    } else {
                        token = token.substring(7);
                        log.info("token:{}", token);
                        try {
                            OAuth2Authentication auth2Authentication = resourceServerTokenServices.loadAuthentication(token);
                            if (null != auth2Authentication && auth2Authentication.isAuthenticated()) {
                                return Mono.just(new AuthorizationDecision(true));
                            } else {
                                return Mono.just(new AuthorizationDecision(false));
                            }
                        }catch (AuthenticationException | InvalidTokenException e) {
                            return Mono.just(new AuthorizationDecision(false));
                        }
                    }
                }).anyExchange().authenticated().and().build();
    }

}
