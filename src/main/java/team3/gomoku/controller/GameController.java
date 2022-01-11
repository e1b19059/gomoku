package team3.gomoku.controller;

import java.security.Principal;
import java.util.ArrayList;

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
import team3.gomoku.model.Matchinfo;
import team3.gomoku.model.MatchinfoMapper;
import team3.gomoku.service.AsyncGame;
import team3.gomoku.service.AsyncMatching;

@Controller
public class GameController {
  @Autowired
  private AsyncGame ag;

  @Autowired
  private AsyncMatching am;

  @Autowired
  Board gomokuBoard;

  @Autowired
  PlayerMapper playerMapper;

  @Autowired
  MatchMapper matchMapper;

  @Autowired
  MatchinfoMapper matchinfoMapper;

  @GetMapping("/game")
  public String game(ModelMap model, Principal prin) {
    ArrayList<Match> matchList = matchMapper.selectAllMatch();
    ArrayList<Player> playerList = playerMapper.selectAllPlayer();
    int myid = playerMapper.selectByName(prin.getName());

    model.addAttribute("matchList", matchList);
    model.addAttribute("playerList", playerList);
    model.addAttribute("myid", myid);
    model.addAttribute("playername", prin.getName());
    return "game1.html";
  }

  @GetMapping("/game1/cancel")
  public String cancel(ModelMap model, Principal prin) {
    int myid = playerMapper.selectByName(prin.getName());

    int matchid = matchMapper.select1ByActive(myid);
    matchMapper.deleteById(matchid);
    matchinfoMapper.deleteBy1(myid);

    ArrayList<Match> matchList = matchMapper.selectAllMatch();
    ArrayList<Player> playerList = playerMapper.selectAllPlayer();

    model.addAttribute("matchList", matchList);
    model.addAttribute("playerList", playerList);
    model.addAttribute("myid", myid);
    model.addAttribute("playername", prin.getName());
    return "game1.html";
  }

  @GetMapping("gomoku1")
  public String gomoku1(ModelMap model, @RequestParam int id, Principal prin) {
    Match match = new Match();
    int myid = playerMapper.selectByName(prin.getName());
    match.setPlayer1(myid);
    match.setPlayer2(id);
    ArrayList<Integer> player2 = matchMapper.selectByActive(true);
    boolean flag = false;
    for (int player : player2) {
      if (player == myid) {
        flag = true;
        break;
      }
    }
    if (!flag) {
      matchMapper.insertMatch(match);
      // flagがtureでないなら先行なのでボードを初期化
      gomokuBoard.initAll();
    }
    model.addAttribute("board", this.gomokuBoard.getBoard());
    model.addAttribute("board_info", this.gomokuBoard.getBoardinfo());
    ArrayList<Match> activeMatches = matchMapper.selectActiveMatch();
    // 一個目しかとり出さない
    boolean turn = false;
    for (Match acmatch : activeMatches) {
      if (acmatch.getPlayer1() == myid) {
        turn = true;
        break;
      } else {
        turn = false;
        break;
      }
    }
    // playerテーブルのturnを update
    playerMapper.updateById(myid, turn);
    model.addAttribute("turn", turn);
    return "gomoku.html";
  }

  @GetMapping("/rule")
  public String rule() {
    return "rule.html";
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

    // player tableのturnを更新
    int yourid = 0;
    int num = 0;// リストの何番目か数える
    ArrayList<Matchinfo> activeMatches = matchinfoMapper.selectActiveMatchinfo(true);
    for (Matchinfo acmatch : activeMatches) {
      if (acmatch.getPlayer1() == myid) {
        yourid = acmatch.getPlayer2();
        break;
      } else {
        yourid = acmatch.getPlayer1();
        break;
      }
    }
    if (flag == 1 ||(flag==0&&this.gomokuBoard.getCnt()==225)) {
      int result = 0;
      if(flag==0){
        winner="引き分け";
      }else if (this.gomokuBoard.getBoardinfo()[col][row] == 0) {
        winner = "黒の勝利";
        result = myid;
      } else {
        winner = "白の勝利";
        result = myid;
      }
      this.gomokuBoard.setWinner(winner);
      this.gomokuBoard.setWinnerFlag(flag);
      model.addAttribute("flag", flag);
      model.addAttribute("winner", winner);

      playerMapper.updatetonull(myid);
      playerMapper.updatetonull(yourid);
      // matchesテーブルの更新

      matchMapper.updatebyplayers(activeMatches.get(0).getPlayer1(),activeMatches.get(0).getPlayer2(), result);
      ArrayList<Matchinfo> matchinfoList = matchinfoMapper.selectActiveMatchinfo(true);
      int matchinfo_id = matchinfoList.get(0).getId();
      matchinfoMapper.deleteById(matchinfo_id);
    } else {
      // 試合が続いているので、ターンの更新
      playerMapper.updateById(myid, false);
      playerMapper.updateById(yourid, true);
    }
    model.addAttribute("turn", false);
    return "gomoku.html";
  }

  @GetMapping("gomoku2/surrender")
  public String gomoku2(ModelMap model, Principal prin) {
    model.addAttribute("board", this.gomokuBoard.getBoard());
    model.addAttribute("board_info", this.gomokuBoard.getBoardinfo());
    int flag = 1; // 投了した側が負けなので
    int myid = playerMapper.selectByName(prin.getName());
    String winner = " ";
    int mystonecolor = 0;

    // player tableのturnを更新
    int yourid = 0;
    ArrayList<Matchinfo> activeMatches = matchinfoMapper.selectActiveMatchinfo(true);
    for (Matchinfo acmatch : activeMatches) {
      if (acmatch.getPlayer1() == myid) {
        yourid = acmatch.getPlayer2();
        mystonecolor = 0;
        break;
      } else {
        yourid = acmatch.getPlayer1();
        mystonecolor = 1;
        break;
      }
    }

    if (mystonecolor == 1) {
      winner = "黒の勝利";
    } else {
      winner = "白の勝利";
    }
    this.gomokuBoard.setWinner(winner);
    this.gomokuBoard.setWinnerFlag(flag);
    model.addAttribute("flag", flag);
    model.addAttribute("winner", winner);
    // 試合が終わったので、ターンの更新
    playerMapper.updatetonull(myid);
    playerMapper.updatetonull(yourid);
    // matchesテーブルの更新
    matchMapper.updatebyplayers(activeMatches.get(0).getPlayer1(),activeMatches.get(0).getPlayer2(), yourid);// リストの要素は0からなのでnumから1引く
    ArrayList<Matchinfo> matchinfoList = matchinfoMapper.selectActiveMatchinfo(true);
    int matchinfo_id = matchinfoList.get(0).getId();
    matchinfoMapper.deleteById(matchinfo_id);
    model.addAttribute("turn", false);
    return "gomoku.html";
  }

  @GetMapping("wait")
  public String wait1(ModelMap model, @RequestParam int id, Principal prin) {
    Match match = new Match();
    Matchinfo matchinfo = new Matchinfo();
    int myid = playerMapper.selectByName(prin.getName());
    match.setPlayer1(myid);
    match.setPlayer2(id);
    matchinfo.setPlayer1(myid);
    matchinfo.setPlayer2(id);
    matchinfo.setStart(false);

    ArrayList<Match> matches = matchMapper.selectActiveMatch();
    boolean flag = false;
    for (Match cmatch : matches) {
      if (cmatch.getPlayer2() == myid && cmatch.getPlayer1()==id) {
        flag = true;
        break;
      }
    }

    if (flag) {
      int matchinfo_id = matchinfoMapper.selectByplayersMatchinfo(myid,id);
      matchinfoMapper.updateById(matchinfo_id);
    } else {
      matchMapper.insertMatch(match);
      matchinfoMapper.insertMatchinfo(matchinfo);
    }

    return "wait.html";
  }

  @GetMapping("wait/tomatch")
  public String tomatch(ModelMap model, Principal prin) {
    Matchinfo matchinfo = new Matchinfo();
    int myid = playerMapper.selectByName(prin.getName());
    ArrayList<Matchinfo> matchinfoList = matchinfoMapper.selectActiveMatchinfo(true);

    gomokuBoard.initAll();
    model.addAttribute("board", this.gomokuBoard.getBoard());
    model.addAttribute("board_info", this.gomokuBoard.getBoardinfo());
    // 一個目しかとり出さない
    matchinfo = matchinfoList.get(0);
    boolean turn = false;
    if (matchinfo.getPlayer1() == myid) {
      turn = true;
    } else {
      turn = false;
    }
    playerMapper.updateById(myid, turn);
    model.addAttribute("turn", turn);
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

  @GetMapping("game/matching")
  public SseEmitter Matching(Principal prin) {
    final SseEmitter sseEmitter = new SseEmitter();
    this.am.matching(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("wait/matchinfo")
  public SseEmitter sendmatchinfo(Principal prin) {
    int myid = playerMapper.selectByName(prin.getName());
    final SseEmitter sseEmitter = new SseEmitter();
    this.ag.tomatch(sseEmitter, myid);
    return sseEmitter;
  }
}
