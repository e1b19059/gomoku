package team3.gomoku.model;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class Parts {
  @Autowired
  Board gomokuBoard;

  int flag=-1;
  int cnt = 0;
  boolean turn1=true;
  boolean turn2=false;
  String winner=" ";

  public void addCnt(){
    this.cnt++;
  }
  public int getCnt(){
    return cnt;
  }

  public String getWinner() {
    return winner;
  }
  public void setWinner(int col, int row) {
    if(this.flag==1){
      if(this.gomokuBoard.getBoardinfo()[col][row]==0){
        winner = "黒の勝利";
    }else{
      winner = "白の勝利";
      }
    }
  }
  public void setFlag(int col, int row){
    Game game = new Game();
    this.flag = game.check(col,row,this.gomokuBoard.getBoardinfo(),-1,-1);
  }
  public int getFlag() {
    return flag;
  }
  public boolean getTurn1(){
    return turn1;
  }
  public boolean getTurn2(){
    return turn2;
  }
  public void setTurn(){
    if (this.turn1==true){
      this.turn1 = false;
      this.turn2 = true;
    }else{
      this.turn1 = true;
      this.turn2 = false;
    }
  }

}
