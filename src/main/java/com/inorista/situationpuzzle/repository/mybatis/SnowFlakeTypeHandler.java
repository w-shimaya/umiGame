package com.inorista.situationpuzzle.repository.mybatis;

import discord4j.common.util.Snowflake;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * Type handler.
 */
public class SnowFlakeTypeHandler extends BaseTypeHandler<Snowflake> {

  @Override
  public void setNonNullParameter(PreparedStatement preparedStatement, int i, Snowflake snowflake,
      JdbcType jdbcType)
      throws SQLException {
    preparedStatement.setString(i, snowflake.toString());
  }

  @Override
  public Snowflake getNullableResult(ResultSet resultSet, String s) throws SQLException {
    return Snowflake.of(resultSet.getString(s));
  }

  @Override
  public Snowflake getNullableResult(ResultSet resultSet, int i) throws SQLException {
    return Snowflake.of(resultSet.getString(i));
  }

  @Override
  public Snowflake getNullableResult(CallableStatement callableStatement, int i)
      throws SQLException {
    return Snowflake.of(callableStatement.getString(i));
  }
}