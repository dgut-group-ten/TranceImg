package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DBDao {
	public static String drv="com.mysql.jdbc.Driver";// 数据库类型
	public static String url="jdbc:mysql://localhost:3306/ai?serverTimezone=Asia/Shanghai&characterEncoding=utf-8";// 数据库网址
	public static String usr="root";// 用户名
	public static String pwd="123456";// 密码

	Connection connect = null;
	Statement stmt = null;
	ResultSet rs = null;

	public DBDao() throws Exception {
		Class.forName(drv);
		this.connect = DriverManager.getConnection(url, usr, pwd);
		this.stmt = this.connect.createStatement();
	}

	// 查询数据库
	public void query(String sql) throws SQLException {
		this.rs = this.stmt.executeQuery(sql);
	}

	// 更新数据库
	public int update(String sql) throws SQLException {
		return this.stmt.executeUpdate(sql);
	}

	// rs的下一条记录是否存在
	public boolean next() throws SQLException {
		return this.rs.next();
	}

	// 获取字符串类型字段的值，字段值为null型的，按照空字符串处理
	public String getString(String field) throws SQLException {
		return this.rs.getString(field);
	}

	// 获取整数类型字段的值
	public Integer getInt(String field) throws SQLException {
		return this.rs.getInt(field);
	}

	// 获取Timestamp类型字段的值
	public Timestamp getTimestamp(String field) throws SQLException {
		return this.rs.getTimestamp(field);
	}

	// 获取实数类型字段的值
	public Float getFloat(String field) throws SQLException {
		return this.rs.getFloat(field);
	}

	// 根据表、字段，获取该字段上所有非重复值
	public ArrayList<String> getStringFieldValueByTableAndField(String tableName, String fieldName)
			throws SQLException {
		this.query("select distinct " + fieldName + " from " + tableName);
		ArrayList<String> fieldList = new ArrayList<String>();
		while (this.next()) {
			fieldList.add(this.getString(fieldName));
		}

		return fieldList;
	}

	// 查询符合条件的记录的数目
	public int getCount(String sql) throws SQLException {
		this.query(sql);
		while (next()) {
			return this.getRs().getInt("count1");
		}
		return 0;
	}

	// 获取表的字段名称，并保存到数组中
	public ArrayList<String> FieldsList(String tableName) throws SQLException {
		ArrayList<String> fieldList = new ArrayList<String>();
		String sql = "select * from " + tableName + " limit 1";// limit
																// 1表示查询结果只包含一条记录
		query(sql);
		ResultSetMetaData fields = rs.getMetaData();// ResultSetMetaData记录了表的元数据，如字段名称，字段类型等

		for (int i = 1; i < fields.getColumnCount() + 1; i++) {// getColumnCount（）获取字段数据
			fieldList.add(fields.getColumnName(i));// getColumnName(i)获取字段名称
		}
		return fieldList;
	}

	// 修改某个表中的id值对应的记录的某一字符型属性的值
	public int updataAStringFieldById(String tableName, String fieldName, Integer id, String fieldValue)
			throws SQLException {
		String sql = "update " + tableName + " set " + fieldName + "='" + fieldValue + "' where "
				+ tableName.toLowerCase() + "Id=" + id.toString();
		return this.update(sql);
	}

	public void setAutoCommit(boolean f) throws SQLException {
		connect.setAutoCommit(f);
	}

	public void commit() throws SQLException {
		connect.commit();
	}

	public boolean hasId(String tableName, Integer id) throws SQLException {
		tableName = tableName.toLowerCase();
		String sql = "select * from " + tableName + " where " + tableName + "Id=" + id.toString();
		query(sql);
		while (next()) {
			return true;
		}
		return false;
	}

	// 通过某张表的id查找，并返回
	public void getById(String tableName, Integer id) throws SQLException {
		String sql = "select * from " + tableName.toLowerCase() + "where " + tableName.toLowerCase() + "Id =" + id.toString();
		this.query(sql);
	}

	public Connection getConnect() {
		return connect;
	}

	public void setConnect(Connection connect) {
		this.connect = connect;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
}
