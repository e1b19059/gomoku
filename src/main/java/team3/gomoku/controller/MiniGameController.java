package team3.gomoku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MiniGameController {

  @GetMapping("/minigame")
  public String minigame() {
    return "minigame.html";
  }
}
