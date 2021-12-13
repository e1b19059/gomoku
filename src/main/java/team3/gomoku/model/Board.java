package team3.gomoku.model;

import org.springframework.stereotype.Component;

@Component
public class Board {
  int W = 15;
  int H = 15;
  String board[][] = new String[H][W];
  int board_info[][] = new int[H][W];
  int flag = 0;// 石の色
  String winner = "";
  int winner_flag = -1;

  public Board() {
    this.board = initBoard();
    this.board_info = initBoardinfo();
  }

  public String[][] getBoard() {
    return board;
  }
  public int[][] getBoardinfo(){
    return this.board_info;
  }

  public String[][] initBoard() {
    int i, j;
    String gBoard[][] = new String[H][W];
    for (i = 1; i < H-1; i++) {
      for (j = 1; j < W-1; j++) {
        gBoard[i][j] = " ┼ ";
      }
    }
    for (i = 1; i < H-1; i++) {
      gBoard[i][0] = "├ ";
    }
    for (i = 1; i < H-1; i++) {
      gBoard[i][W-1] = " ┤";
    }
    for (j = 1; j < W-1; j++) {
      gBoard[0][j] = " ┬ ";
    }
    for (j = 1; j < W-1; j++) {
      gBoard[H-1][j] = " ┴ ";
    }
    gBoard[0][0] = "┌ ";
    gBoard[0][W-1] = " ┐";
    gBoard[H-1][0] = "└ ";
    gBoard[H-1][W-1] = " ┘";
    gBoard[3][3] = " ╋ ";
    gBoard[3][11] = " ╋ ";
    gBoard[11][3] = " ╋ ";
    gBoard[11][11] = " ╋ ";
    gBoard[7][7] = " ╋ ";
    return gBoard;
  }

  public int[][] initBoardinfo(){
    int info[][] = new int[H][W];
    int i, j;
    for(i=0;i<H;i++){
      for(j=0;j<W;j++){
          info[i][j] = -1;
      }
    }
    return info;
  }

  public void putStone(int col, int row) {
    if (flag == 0) {
      this.board[col][row] = " ● ";
      this.board_info[col][row] = flag;
      flag = 1;
    } else {
      this.board[col][row] = " ○ ";
      this.board_info[col][row] = flag;
      flag = 0;
    }
    // 並んでいるか確認するメソッド
  }

  public void initAll() {
    flag = 0;
    this.board = initBoard();
    this.board_info = initBoardinfo();
    this.winner = "";
    this.winner_flag = -1;
  }
  public String getWinner() {
    return winner;
  }
  public void setWinner(String winner) {
    this.winner = winner;
  }
  public void setWinnerFlag(int flag){
    this.winner_flag = flag;
  }

  public int getWinnerFlag() {
    return winner_flag;
  }

}
