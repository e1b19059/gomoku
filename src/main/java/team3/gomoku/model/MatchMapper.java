package team3.gomoku.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchMapper {

  @Select("select * from matches;")
  ArrayList<Match> selectAllMatch();
}