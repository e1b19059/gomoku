package team3.gomoku.model;

import team3.gomoku.model.Board;
import java.util.ArrayList;

public class Judge{
  int WIDTH = 15;
  int HEIGHT = 15;

  //1方向に確認する
  public int gomoku(int h, int v, int directionH, int directionV, int[][] cBoard,int player){
    int count = 1;  //カウント用の変数を追加

    //確認する座標x, yを宣言
    int x = h + directionH;
    int y = v + directionV;

    //つながっているか確認する
    while (x < WIDTH && x >= 0 && y < HEIGHT && y >= 0){
      //相手の駒または空欄だった場合
      if ((cBoard[x][y] == player) || (cBoard[x][y] == -1)){
        int x2 = h + directionH;
        int y2 = v + directionV;
        while (!(x2 == x && y2 == y)){
          x2 += directionH;
          y2 += directionV;
          count++;
        }
        break;//処理を終える
      }
      //確認座標を次に進める
      x += directionH;
      y += directionV;
    }
    return count;
  }

  //全方向に確認する
  public ArrayList<Integer> gomokuAll(int h, int v,int[][] cBoard,int player){
    ArrayList<Integer> count = new ArrayList<>();
    count.add(gomoku(h, v, 1, 0, cBoard,player));  //右方向
    count.add(gomoku(h, v, -1, 0, cBoard,player)); //左方向
    count.add(gomoku(h, v, 0, -1, cBoard,player)); //上方向
    count.add(gomoku(h, v, 0, 1, cBoard,player));  //下方向
    count.add(gomoku(h, v, 1, -1, cBoard,player)); //右上方向
    count.add(gomoku(h, v, -1, -1, cBoard,player));//左上方向
    count.add(gomoku(h, v, 1, 1, cBoard,player));  //右下方向
    count.add(gomoku(h, v, -1, 1, cBoard,player)); //左下方向
    return count;
  }

  //結果の判定
  public boolean result(ArrayList<Integer> count){
    int max = listMax(count);
    if(max>=5){
      return true;
    }else{
      return false;
    }
  }

  private static int listMax(ArrayList<Integer> list) {
        int max = list.get(0);
        for(int k = 0; k < list.size(); k++) {
            if(max < list.get(k)) max = list.get(k);
        }
        return max;
    }
}
