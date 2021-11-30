package team3.gomoku.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import team3.gomoku.model.Board;
import team3.gomoku.model.Game;
import team3.gomoku.model.Match;
import team3.gomoku.model.MatchMapper;
import team3.gomoku.model.Player;
import team3.gomoku.model.PlayerMapper;
import team3.gomoku.service.AsyncGame;

@Controller
public class GameController {
  private final Logger logger = LoggerFactory.getLogger(GameController.class);

  @Autowired
  private AsyncGame ag;

  @Autowired
  Board gomokuBoard;

  @Autowired
  PlayerMapper playerMapper;

  @Autowired
  MatchMapper matchMapper;

  @GetMapping("/game")
  public String game(ModelMap model) {
    ArrayList<Match> matchList = matchMapper.selectAllMatch();
    ArrayList<Player> playerList = playerMapper.selectAllPlayer();

    model.addAttribute("matchList", matchList);
    model.addAttribute("playerList", playerList);
    return "game1.html";
  }

  @GetMapping("gomoku1")
  public String gomoku1(ModelMap model, @RequestParam int id, Principal prin) {
    // gomokuBoard = new Board();// 今はここにあるけどマッチが成立したときに一方だけ行う
    Match match = new Match();
    int myid = playerMapper.selectByName(prin.getName());
    match.setPlayer1(myid);
    match.setPlayer2(id);
    ArrayList<Integer> player2 = matchMapper.selectByActive(true);
    boolean flag = false;
    for(int player : player2){
      if(player==myid){
        flag = true;
        break;
      }

    }
    if (!flag){
      matchMapper.insertMatch(match);
    }



    model.addAttribute("board", this.gomokuBoard.getBoard());
    model.addAttribute("board_info", this.gomokuBoard.getBoardinfo());
    model.addAttribute("turn", true);// 非同期にするときに変更する
    return "gomoku.html";
  }

  @GetMapping("gomoku2")
  public String gomoku2(@RequestParam int col, @RequestParam int row, ModelMap model) {
    // 非同期に変える
    // まだ交代交代にはなっていない
    Game game = new Game();
    gomokuBoard.putStone(col, row);
    model.addAttribute("board", this.gomokuBoard.getBoard());
    model.addAttribute("board_info", this.gomokuBoard.getBoardinfo());
    int flag = game.check(col, row, this.gomokuBoard.getBoardinfo(), -1, -1);
    String winner = " ";
    if (flag == 1) {
      if (this.gomokuBoard.getBoardinfo()[col][row] == 0) {
        winner = "黒の勝利";
      } else {
        winner = "白の勝利";
      }
    }
    model.addAttribute("flag", flag);
    model.addAttribute("winner", winner);
    model.addAttribute("turn", true);// 非同期にするときに変更する
    return "gomoku.html";
  }

  @GetMapping("gomoku2/load")
  public SseEmitter Load() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.ag.putStone(sseEmitter);
    return sseEmitter;
  }
}
