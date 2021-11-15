package team3.gomoku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import team3.gomoku.model.Board;

@Controller
public class GameController {

  @Autowired
  Board gomokuBoard;

  @GetMapping("/game")
  public String game() {

    return "game1.html";
  }

  @GetMapping("gomoku1")
  public String gomoku1(ModelMap model) {
    gomokuBoard = new Board();// 今はここにあるけどマッチが成立したときに一方だけ行う
    model.addAttribute("board", this.gomokuBoard.getBoard());
    model.addAttribute("turn", true);// 非同期にするときに変更する
    return "gomoku.html";
  }

  @GetMapping("gomoku2")
  public String gomoku2(@RequestParam int col, @RequestParam int row, ModelMap model) {
    // 非同期に変える
    // まだ交代交代にはなっていない
    gomokuBoard.putStone(col, row);
    model.addAttribute("board", this.gomokuBoard.getBoard());
    model.addAttribute("turn", false);// 非同期にするときに変更する
    return "gomoku.html";
  }
}
