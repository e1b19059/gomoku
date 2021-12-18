package team3.gomoku.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchMapper {

  @Select("select * from matches where isActive=true;")
  ArrayList<Match> selectActiveMatch();

  @Select("select * from matches;")
  ArrayList<Match> selectAllMatch();

  @Select("select id from matches where isActive=true and player1=#{player1};")
  int select1ByActive(int player1);

  @Select("select player2 from matches where isActive=#{isActive};")
  ArrayList<Integer> selectByActive(boolean isActive);

  @Insert("INSERT INTO matches (player1,player2,isActive) VALUES (#{player1},#{player2},True)")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatch(Match match);

  @Update("UPDATE matches SET isActive = false, winner = #{winner} WHERE ID = #{id}")
  void updateEndById(int id, int winner);

  @Update("UPDATE matches SET isActive = false, winner = #{winner} where isActive=true and player1=#{player1} and player2=#{player2};")
  void updatebyplayers(int player1, int player2, int winner);

  @Delete("DELETE FROM matches WHERE ID =#{id}")
  boolean deleteById(int id);
}
