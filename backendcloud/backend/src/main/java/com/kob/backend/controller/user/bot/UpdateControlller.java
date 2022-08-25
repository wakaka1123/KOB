package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.UpdateService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateControlller {
  @Autowired
  private UpdateService updateService;

  @PostMapping("/user/bots/update")
  public Map<String,String> update(@RequestParam Map<String, String> data) {
    return updateService.update(data);
  }
}
