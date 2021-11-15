package team3.gomoku.model;

import org.springframework.stereotype.Component;

@Component
public class Board {
  String board[][] = new String[15][15];
  int flag = 0;// 石の色

  public Board() {
    this.board = initBoard();
  }

  public String[][] getBoard() {
    return board;
  }

  public String[][] initBoard() {
    int i, j;
    String gBoard[][] = new String[15][15];
    for (i = 1; i < 14; i++) {
      for (j = 1; j < 14; j++) {
        gBoard[i][j] = "┼";
      }
    }
    for (i = 1; i < 14; i++) {
      gBoard[i][0] = "├";
    }
    for (i = 1; i < 14; i++) {
      gBoard[i][14] = "┤";
    }
    for (j = 1; j < 14; j++) {
      gBoard[0][j] = "┬";
    }
    for (j = 1; j < 14; j++) {
      gBoard[14][j] = "┴";
    }
    gBoard[0][0] = "┌";
    gBoard[0][14] = "┐";
    gBoard[14][0] = "└";
    gBoard[14][14] = "┘";
    gBoard[3][3] = "╋";
    gBoard[3][11] = "╋";
    gBoard[11][3] = "╋";
    gBoard[11][11] = "╋";
    gBoard[7][7] = "╋";
    return gBoard;
  }

  public void putStone(int col, int row) {
    if (flag == 0) {
      this.board[col][row] = "●";
      flag = 1;
    } else {
      this.board[col][row] = "○";
      flag = 0;
    }
    // 並んでいるか確認するメソッド
  }

  // 同じ色の石が5個そろっているか確認するメソッド作る
}
