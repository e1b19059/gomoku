package team3.gomoku.model;

import org.springframework.stereotype.Component;
import team3.gomoku.model.Judge;
import java.util.ArrayList;

@Component
public class Board {
  int W = 15;//横幅
  int H = 15;//縦幅
  String board[][] = new String[H][W];
  int checkBoard[][] = new int[H][W];
  int flag = 0;// 石の色

  ArrayList<Integer> count = new ArrayList<>();//何個駒がつながっているかを格納
  Judge judge = new Judge();

  public Board() {
    this.board = initBoard();
    this.checkBoard = initCheckBoard();
  }

  public String[][] getBoard() {
    return board;
  }

  public int[][] getCheckBoard() {
    return checkBoard;
  }

  public int getFlag(){
    return flag;
  }

  public ArrayList<Integer> getCount(){
    return count;
  }

  public String[][] initBoard() {
    int i, j;
    String gBoard[][] = new String[15][15];
    for (i = 1; i < H-1; i++) {
      for (j = 1; j < W-1; j++) {
        gBoard[i][j] = "┼";
      }
    }
    for (i = 1; i < H-1; i++) {
      gBoard[i][0] = "├";
    }
    for (i = 1; i < H-1; i++) {
      gBoard[i][W-1] = "┤";
    }
    for (j = 1; j < W-1; j++) {
      gBoard[0][j] = "┬";
    }
    for (j = 1; j < W-1; j++) {
      gBoard[H-1][j] = "┴";
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

  public int[][] initCheckBoard(){
    int cBoard[][] = new int[15][15];
    for(int i = 0 ; i < H ; i++){
      for(int j = 0 ; j < W ;j++ ){
        cBoard[i][j] = -1;
      }
    }
    return cBoard;
  }

  public int putStone(int col, int row) {
    int player = flag;
    if (flag == 0) {
      this.board[col][row] = "●";
      this.checkBoard[col][row] = flag;
      flag = 1;
    } else {
      this.board[col][row] = "○";
      this.checkBoard[col][row] = flag;
      flag = 0;
    }
    // 並んでいるか確認するメソッド
    this.count = judge.gomokuAll(col,row,this.getCheckBoard(),this.getFlag());
    if(judge.result(this.count)){
      return player;
    }else{
      return -1;
    }
  }
}
