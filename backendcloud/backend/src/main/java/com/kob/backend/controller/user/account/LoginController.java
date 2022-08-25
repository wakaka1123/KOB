package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.LoginService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  @Autowired
  private LoginService loginService;

  @PostMapping("/user/account/token/")
  public Map<String, String> getToken(@RequestParam Map<String, String> map) {//将输入的用户名密码注入到map
    //从map中把用户名密码取出
    String username = map.get("username");
    String password = map.get("password");
    return loginService.getToken(username,password);

  }
}
