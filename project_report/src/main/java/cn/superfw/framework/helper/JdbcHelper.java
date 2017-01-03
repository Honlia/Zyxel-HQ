package cn.superfw.framework.helper;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * Jdbc操作帮助类
 * @author chenchao
 * @since 1.0
 */
public abstract class JdbcHelper {
    private static DataSource dataSource = null;

    private static Properties databaseTypeMappings = getDefaultDatabaseTypeMappings();

    private static Properties getDefaultDatabaseTypeMappings() {
        Properties databaseTypeMappings = new Properties();
        databaseTypeMappings.setProperty("H2","h2");
        databaseTypeMappings.setProperty("MySQL","mysql");
        databaseTypeMappings.setProperty("Oracle","oracle");
        databaseTypeMappings.setProperty("PostgreSQL","postgres");
        databaseTypeMappings.setProperty("Microsoft SQL Server","mssql");
        databaseTypeMappings.setProperty("DB2","db2");
        databaseTypeMappings.setProperty("DB2","db2");
        databaseTypeMappings.setProperty("DB2/NT","db2");
        databaseTypeMappings.setProperty("DB2/NT64","db2");
        databaseTypeMappings.setProperty("DB2 UDP","db2");
        databaseTypeMappings.setProperty("DB2/LINUX","db2");
        databaseTypeMappings.setProperty("DB2/LINUX390","db2");
        databaseTypeMappings.setProperty("DB2/LINUXX8664","db2");
        databaseTypeMappings.setProperty("DB2/LINUXZ64","db2");
        databaseTypeMappings.setProperty("DB2/400 SQL","db2");
        databaseTypeMappings.setProperty("DB2/6000","db2");
        databaseTypeMappings.setProperty("DB2 UDB iSeries","db2");
        databaseTypeMappings.setProperty("DB2/AIX64","db2");
        databaseTypeMappings.setProperty("DB2/HPUX","db2");
        databaseTypeMappings.setProperty("DB2/HP64","db2");
        databaseTypeMappings.setProperty("DB2/SUN","db2");
        databaseTypeMappings.setProperty("DB2/SUN64","db2");
        databaseTypeMappings.setProperty("DB2/PTX","db2");
        databaseTypeMappings.setProperty("DB2/2","db2");
        return databaseTypeMappings;
    }



    /**
     * 返回数据源dataSource
     * @return
     */
    public static DataSource getDataSource() {
        AssertHelper.notNull(dataSource);
        return dataSource;
    }

    /**
     * 根据返回的对象集合判断是否为单条记录，并返回
     * 如果返回无记录，或者超过1条记录，则抛出异常
     * @param results
     * @return
     */
    public static <T> T requiredSingleResult(Collection<T> results) {
        int size = (results != null ? results.size() : 0);
        if (size == 0) {
            return null;
        }
        return results.iterator().next();
    }

    /**
     * 根据元数据ResultSetMetaData、列索引columIndex获取列名称
     * @param resultSetMetaData
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
        String name = resultSetMetaData.getColumnLabel(columnIndex);
        if (name == null || name.length() < 1) {
            name = resultSetMetaData.getColumnName(columnIndex);
        }
        return name;
    }

    /**
     * 根据ResultSet结果集、index列索引、字段类型requiredType获取指定类型的对象值
     * @param rs
     * @param index
     * @param requiredType
     * @return
     * @throws SQLException
     */
    public static Object getResultSetValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
        if (requiredType == null) {
            return getResultSetValue(rs, index);
        }

        Object value = null;
        boolean wasNullCheck = false;

        if (String.class.equals(requiredType)) {
            value = rs.getString(index);
        }
        else if (boolean.class.equals(requiredType) || Boolean.class.equals(requiredType)) {
            value = rs.getBoolean(index);
            wasNullCheck = true;
        }
        else if (byte.class.equals(requiredType) || Byte.class.equals(requiredType)) {
            value = rs.getByte(index);
            wasNullCheck = true;
        }
        else if (short.class.equals(requiredType) || Short.class.equals(requiredType)) {
            value = rs.getShort(index);
            wasNullCheck = true;
        }
        else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
            value = rs.getInt(index);
            wasNullCheck = true;
        }
        else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
            value = rs.getLong(index);
            wasNullCheck = true;
        }
        else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
            value = rs.getFloat(index);
            wasNullCheck = true;
        }
        else if (double.class.equals(requiredType) || Double.class.equals(requiredType) ||
                Number.class.equals(requiredType)) {
            value = rs.getDouble(index);
            wasNullCheck = true;
        }
        else if (byte[].class.equals(requiredType)) {
            value = rs.getBytes(index);
        }
        else if (java.sql.Date.class.equals(requiredType)) {
            value = rs.getDate(index);
        }
        else if (java.sql.Time.class.equals(requiredType)) {
            value = rs.getTime(index);
        }
        else if (java.sql.Timestamp.class.equals(requiredType) || java.util.Date.class.equals(requiredType)) {
            value = rs.getTimestamp(index);
        }
        else if (BigDecimal.class.equals(requiredType)) {
            value = rs.getBigDecimal(index);
        }
        else if (Blob.class.equals(requiredType)) {
            value = rs.getBlob(index);
        }
        else if (Clob.class.equals(requiredType)) {
            value = rs.getClob(index);
        }
        else {
            value = getResultSetValue(rs, index);
        }

        if (wasNullCheck && value != null && rs.wasNull()) {
            value = null;
        }
        return value;
    }

    /**
     * 对于特殊字段类型做特殊处理
     * @param rs
     * @param index
     * @return
     * @throws SQLException
     */
    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }
        if (obj instanceof Blob) {
            obj = rs.getBytes(index);
        }
        else if (obj instanceof Clob) {
            obj = rs.getString(index);
        }
        else if (className != null &&
                ("oracle.sql.TIMESTAMP".equals(className) ||
                "oracle.sql.TIMESTAMPTZ".equals(className))) {
            obj = rs.getTimestamp(index);
        }
        else if (className != null && className.startsWith("oracle.sql.DATE")) {
            String metaDataClassName = rs.getMetaData().getColumnClassName(index);
            if ("java.sql.Timestamp".equals(metaDataClassName) ||
                    "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
                obj = rs.getTimestamp(index);
            }
            else {
                obj = rs.getDate(index);
            }
        }
        else if (obj != null && obj instanceof java.sql.Date) {
            if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
                obj = rs.getTimestamp(index);
            }
        }
        return obj;
    }

    /**
     * conn不为空时，关闭conn
     * @param conn
     * @throws SQLException
     */
    public static void close(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * rs不为空时，关闭rs
     * @param rs
     * @throws SQLException
     */
    public static void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    /**
     * stmt不为空时，关闭stmt
     * @param stmt
     * @throws SQLException
     */
    public static void close(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    /**
     * 根据连接对象获取数据库类型
     * @param conn 数据库连接
     * @return 类型
     * @throws Exception
     */
    public static String getDatabaseType(Connection conn) throws Exception {
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        String databaseProductName = databaseMetaData.getDatabaseProductName();
        return databaseTypeMappings.getProperty(databaseProductName);
    }
}
