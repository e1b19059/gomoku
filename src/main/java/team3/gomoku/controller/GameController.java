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
      // flagがtureでないなら先行なのでボードを初期化
      gomokuBoard.initBoard();
      gomokuBoard.initBoardInfo();
    }
    model.addAttribute("board", this.gomokuBoard.getBoard());
    model.addAttribute("board_info", this.gomokuBoard.getBoardinfo());
    ArrayList<Match> activeMatches = matchMapper.selectActiveMatch();
    //一個目しかとり出さない
    boolean turn = false;
    for(Match acmatch : activeMatches){
      if(acmatch.getPlayer1()==myid){
        turn = true;
        break;
      }
      else{
        turn = false;
        break;
      }
    }
     //playerテーブルのturnを update
      playerMapper.updateById(myid, turn);
      model.addAttribute("turn", turn);
    return "gomoku.html";
  }

  @GetMapping("gomoku2")
  public String gomoku2(@RequestParam int col, @RequestParam int row, ModelMap model, Principal prin) {
    Game game = new Game();
    gomokuBoard.putStone(col, row);
    model.addAttribute("board", this.gomokuBoard.getBoard());
    model.addAttribute("board_info", this.gomokuBoard.getBoardinfo());
    int flag = game.check(col, row, this.gomokuBoard.getBoardinfo(), -1, -1);
    int myid = playerMapper.selectByName(prin.getName());
    String winner = " ";


    //player tableのturnを更新
    int yourid = 0;
    int num = 0;// リストの何番目か数える
    ArrayList<Match> activeMatches = matchMapper.selectActiveMatch();
    for (Match acmatch : activeMatches) {
      num++;
      if (acmatch.getPlayer1() == myid) {
        yourid = acmatch.getPlayer2();
        break;
      } else {
        yourid = acmatch.getPlayer1();
        break;
      }
    }

    if (flag == 1) {
      if (this.gomokuBoard.getBoardinfo()[col][row] == 0) {
        winner = "黒の勝利";
      } else {
        winner = "白の勝利";
      }
      model.addAttribute("flag", flag);
      model.addAttribute("winner", winner);
      // 試合が終わったので、ターンの更新
      playerMapper.updateById(myid, false);
      playerMapper.updateById(yourid, false);
      // matchesテーブルの更新
      matchMapper.updateEndById(activeMatches.get(num - 1).getId());// リストの要素は0からなのでnumから1引く
    } else {
      // 試合が続いているので、ターンの更新
      playerMapper.updateById(myid, false);
      playerMapper.updateById(yourid, true);
    }
    model.addAttribute("turn", false);
    return "gomoku.html";
  }

  @GetMapping("gomoku2/load")
  public SseEmitter Load() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.ag.putStone(sseEmitter);
    return sseEmitter;
  }

   @GetMapping("gomoku2/turn")
  public SseEmitter Turn(Principal prin) {
    int myid = playerMapper.selectByName(prin.getName());
    final SseEmitter sseEmitter = new SseEmitter();
    this.ag.turn(sseEmitter, myid);
    return sseEmitter;
  }
}
