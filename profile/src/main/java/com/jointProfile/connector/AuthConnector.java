package com.jointProfile.connector;

import com.jointProfile.bom.profile.ProfileBom;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-connector", url = "${connector.auth.url}")
public interface AuthConnector {

    @GetMapping(path = "/getCurrentProfile") // подключение к методу в авторизации
    ProfileBom getCurrentProfile(@RequestHeader(name = "Authorization") String token);
}
