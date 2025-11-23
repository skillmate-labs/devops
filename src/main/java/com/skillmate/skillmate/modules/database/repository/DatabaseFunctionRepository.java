package com.skillmate.skillmate.modules.database.repository;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import oracle.jdbc.OracleTypes;

@Repository
@RequiredArgsConstructor
public class DatabaseFunctionRepository {

  private final JdbcTemplate jdbcTemplate;

  public String convertGoalToJson(String goalId) {
    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("PKG_FUNCTIONS")
        .withFunctionName("CONVERT_GOAL_TO_JSON")
        .declareParameters(
            new SqlParameter("p_goal_id", OracleTypes.CHAR),
            new SqlOutParameter("return", OracleTypes.CLOB));

    Map<String, Object> result = jdbcCall.execute(goalId);
    Object clobValue = result.get("return");
    return clobValue == null ? null : convertClobToString(clobValue);
  }

  public String calculateGoalCompatibility(String userId, String goalId) {
    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("PKG_FUNCTIONS")
        .withFunctionName("CALCULATE_GOAL_COMPATIBILITY")
        .declareParameters(
            new SqlParameter("p_user_id", OracleTypes.CHAR),
            new SqlParameter("p_goal_id", OracleTypes.CHAR),
            new SqlOutParameter("return", OracleTypes.CLOB));

    Map<String, Object> result = jdbcCall.execute(userId, goalId);
    Object clobValue = result.get("return");
    return clobValue == null ? null : convertClobToString(clobValue);
  }

  public String exportDatasetToJson() {
    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
        .withCatalogName("PKG_EXPORT")
        .withProcedureName("EXPORT_DATASET_TO_JSON")
        .declareParameters(
            new SqlOutParameter("p_output", OracleTypes.CLOB));

    Map<String, Object> result = jdbcCall.execute();
    Object clobValue = result.get("p_output");
    return clobValue == null ? null : convertClobToString(clobValue);
  }

  @SuppressWarnings("deprecation")
  private String convertClobToString(Object clobValue) {
    if (clobValue == null) {
      return null;
    }

    try {
      if (clobValue instanceof oracle.sql.CLOB) {
        oracle.sql.CLOB oracleClob = (oracle.sql.CLOB) clobValue;
        long length = oracleClob.length();
        if (length > Integer.MAX_VALUE) {
          return readClobWithReader(oracleClob);
        }
        return oracleClob.getSubString(1, (int) length);
      }

      if (clobValue instanceof Clob) {
        return readClobWithReader((Clob) clobValue);
      }

      return clobValue.toString();
    } catch (Exception e) {
      throw new RuntimeException("Erro ao converter CLOB para String", e);
    }
  }

  private String readClobWithReader(Clob clob) throws Exception {
    Reader reader = clob.getCharacterStream();
    BufferedReader br = new BufferedReader(reader);
    StringBuilder sb = new StringBuilder();
    char[] buffer = new char[8192];
    int charsRead;
    while ((charsRead = br.read(buffer)) != -1) {
      sb.append(buffer, 0, charsRead);
    }
    br.close();
    reader.close();
    return sb.toString();
  }
}
