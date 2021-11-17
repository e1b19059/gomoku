package team3.gomoku.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import team3.gomoku.model.Board;
import team3.gomoku.model.Match;
import team3.gomoku.model.MatchMapper;
import team3.gomoku.model.PlayerMapper;

@Controller
public class GameController {

  @Autowired
  Board gomokuBoard;

  @Autowired
  PlayerMapper playerMapper;

  @Autowired
  MatchMapper matchMapper;

  @GetMapping("/game")
  public String game(ModelMap model) {
    ArrayList<Match> matchList = matchMapper.selectAllMatch();

    model.addAttribute("matchList", matchList);

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
