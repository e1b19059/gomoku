package team3.gomoku.model;

public class Matchinfo{
int id;
  int player1, player2;
  boolean start;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPlayer1() {
    return player1;
  }

  public void setPlayer1(int player1) {
    this.player1 = player1;
  }

  public int getPlayer2() {
    return player2;
  }

  public void setPlayer2(int player2) {
    this.player2 = player2;
  }

  public boolean getStart() {
    return start;
  }

  public void setStart(boolean start) {
    this.start = start;
  }

}
