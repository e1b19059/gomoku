package team3.gomoku.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PlayerMapper {

  @Select("select * from players;")
  ArrayList<Player> selectAllPlayer();

  @Select("select * from players where id = #{id};")
  Player selectbyId(int id);

  @Select("select id from players where name = #{name};")
  int selectByName(String name);

  @Update("UPDATE players SET turn = #{turn} WHERE ID = #{id}")
  void updateById(int id, boolean turn);
}
