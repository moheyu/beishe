package com.wit.travel.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * String到List<String>的类型转换器
 * 用于处理数据库中JSON格式字符串到Java List<String>的转换
 */
@MappedTypes(List.class)
public class StringToListTypeHandler extends BaseTypeHandler<List<String>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        try {
            if (parameter != null && !parameter.isEmpty()) {
                // 将List序列化为JSON数组字符串
                String json = objectMapper.writeValueAsString(parameter);
                ps.setString(i, json);
            } else {
                ps.setString(i, "[]");
            }
        } catch (Exception e) {
            throw new SQLException("序列化List失败", e);
        }
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return convertToList(value);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return convertToList(value);
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return convertToList(value);
    }

    private List<String> convertToList(String value) {
        if (value != null && !value.trim().isEmpty() && !value.equals("[]")) {
            try {
                // 将JSON字符串反序列化为List<String>
                return objectMapper.readValue(value, new TypeReference<List<String>>(){});
            } catch (Exception e) {
                throw new RuntimeException("解析images字段失败：" + value, e);
            }
        }
        return new ArrayList<>();
    }
}
