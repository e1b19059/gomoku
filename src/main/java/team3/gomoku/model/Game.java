package team3.gomoku.model;

public class Game{
  public int check(int col, int row, int[][] board, int precol, int prerow){

    if(precol==-1||prerow==-1){
      int flag=0;
      int cnt;
      //↑
      if(col!=0 && board[col][row]==board[col-1][row]){
        cnt = check(col-1,row,board,col,row);
        if(cnt==4){
          return flag = 1;
        }
      }
      //↗
      if(col!=0 && row!=14 && board[col][row]==board[col-1][row+1]){
        cnt = check(col-1,row+1,board,col,row);
        if(cnt==4){
          return flag = 1;
        }
      }
      //→
      if(row!=14 && board[col][row]==board[col][row+1]){
        cnt = check(col,row+1,board,col,row);
        if(cnt==4){
          return flag = 1;
        }
      }
      //↘
      if(col!=14 && row!=14 &&board[col][row]==board[col+1][row+1]){
        cnt = check(col+1,row+1,board,col,row);
        if(cnt==4){
          return flag = 1;
        }
      }
      //↓
      if(col!=14 && board[col][row]==board[col+1][row]){
        cnt = check(col+1,row,board,col,row);
        if(cnt==4){
          return flag = 1;
        }
      }
      //↙
      if(col!=14 && row!=0 && board[col][row]==board[col+1][row-1]){
        cnt = check(col+1,row-1,board,col,row);
        if(cnt==4){
          return flag = 1;
        }
      }
      //←
      if(row!=0 && board[col][row]==board[col][row-1]){
        cnt = check(col,row-1,board,col,row);
        if(cnt==4){
          return flag = 1;
        }
      }
      //↖
      if(row!=0 && col!=0 && board[col][row]==board[col-1][row-1]){
        cnt = check(col-1,row-1,board,col,row);
        if(cnt==4){
          return flag = 1;
        }
      }
      return flag;
    }
    else{
      int discol = col - precol;
      int disrow = row - prerow;
      int nextcol = col + discol;
      int nextrow = row + disrow;

      if(board[col][row]!=board[precol][prerow]){
        return 0;
      }
      if(nextcol<0||nextcol>14||nextrow<0||nextrow>14){
        return 1;
      }
      return check(nextcol,nextrow,board,col,row) + 1;
    }

  }



}
