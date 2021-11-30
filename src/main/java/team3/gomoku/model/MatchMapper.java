package team3.gomoku.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface MatchMapper {

  @Select("select * from matches;")
  ArrayList<Match> selectAllMatch();

  @Select("select player2 from matches where isActive=#{isActive};")
  ArrayList<Integer> selectByActive(boolean isActive);

  @Insert("INSERT INTO matches (player1,player2,isActive) VALUES (#{player1},#{player2},True)")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatch(Match match);

}
