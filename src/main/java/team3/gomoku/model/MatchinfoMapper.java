package team3.gomoku.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface MatchinfoMapper {
  @Select("select * from matchinfo where start=#{start};")
  ArrayList<Matchinfo> selectActiveMatchinfo(boolean start);

  @Select("select * from matchinfo;")
  ArrayList<Matchinfo> selectAllMatchinfo();

  @Select("select id from matchinfo where player1 = #{player1} and player2 = #{player2};")
  int selectByplayersMatchinfo(int player2,int player1);

  @Insert("INSERT INTO matchinfo (player1,player2,start) VALUES (#{player1},#{player2},false)")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatchinfo(Matchinfo matchinfo);

  @Update("UPDATE matchinfo SET start = true WHERE ID = #{id}")
  void updateById(int id);

  @Delete("DELETE FROM matchinfo WHERE ID =#{id}")
  boolean deleteById(int id);

  @Delete("DELETE FROM matchinfo WHERE player1=#{player1}")
  boolean deleteBy1(int player1);
}
