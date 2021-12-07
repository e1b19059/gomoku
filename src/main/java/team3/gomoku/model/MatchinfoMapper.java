package team3.gomoku.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface MatchinfoMapper{
  @Select("select * from matchinfo where start=#{start};")
  ArrayList<Matchinfo> selectActiveMatchinfo(boolean start);

  @Select("select * from matchinfo;")
  ArrayList<Matchinfo> selectAllMatchinfo();

  @Insert("INSERT INTO matchinfo (player1,player2,start) VALUES (#{player1},#{player2},false)")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatchinfo(Matchinfo matchinfo);

  @Update("UPDATE matchinfo SET start = true WHERE ID = #{id}")
  void updateById(int id);

  @Delete("DELETE FROM matchinfo WHERE ID =#{id}")
  boolean deleteById(int id);
}
