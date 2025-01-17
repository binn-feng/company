package com.internal.quote.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.internal.quote.dto.EmailUserInfoDTO;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ListTypeHandler extends BaseTypeHandler<List<EmailUserInfoDTO>> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<EmailUserInfoDTO> parameter, JdbcType jdbcType) throws SQLException {
        try {
            // 将 List 转换为 JSON 字符串并设置到 PreparedStatement
            ps.setString(i, OBJECT_MAPPER.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new SQLException("Error converting List to JSON", e);
        }
    }

    @Override
    public List<EmailUserInfoDTO> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return convertJsonToList(json);
    }

    @Override
    public List<EmailUserInfoDTO> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return convertJsonToList(json);
    }

    @Override
    public List<EmailUserInfoDTO> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return convertJsonToList(json);
    }

    private List<EmailUserInfoDTO> convertJsonToList(String json) throws SQLException {
        if (json == null) {
            return null;
        }
        try {
            // 将 JSON 字符串转换为 List
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, EmailUserInfoDTO.class));
        } catch (IOException e) {
            throw new SQLException("Error converting JSON to List", e);
        }
    }
}