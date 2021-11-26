package team3.gomoku.service;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team3.gomoku.model.Board;
import org.springframework.beans.factory.annotation.Autowired;




@Service
public class AsyncGame {
  private final Logger logger = LoggerFactory.getLogger(AsyncGame.class);
  @Autowired
  Board board;

  @Async
  public void putStone(SseEmitter emitter) {
    logger.info("start");
    while (true) {// 無限ループ
      try {
        logger.info("send:"+this.board.getBoardinfo()[0][0]);
        TimeUnit.SECONDS.sleep(1);// 1秒STOP
        emitter.send(board);// ここでsendすると引数をブラウザにpushする
      } catch (Exception e) {
        // 例外の名前とメッセージだけ表示する
        logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
        // 例外が発生したらカウントとsendを終了する
        emitter.complete();// emitterの後始末．明示的にブラウザとの接続を一度切る．
        break;
      }
    }

  }
}
