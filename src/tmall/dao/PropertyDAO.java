package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.DBUtil;

public class PropertyDAO {

	//获取属性总数
	public int getTotal(int cid) {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {

			String sql = "select count(*) from Property where cid = " + cid;
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				total = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return total;
	}

	//添加属性
	public void add(Property property) {
		String sql = "insert into Property values(null,?,?)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, property.getCategory().getId());// 设置id
			ps.setString(2, property.getName());// 设置名字
			ps.execute();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);// 设置id
				property.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 更新用户
	public void update(Property property) {
		String sql = "update Property set cid = ?,name = ? where id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, property.getCategory().getId());
			ps.setString(2, property.getName());
			ps.setInt(3, property.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 删除用户
	public void delete(int id) {
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "delete from Property where id =" + id;
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 根据名称 id查询属性
	public Property get(String name, int cid) {
		Property property = null;
		String sql = "select * from Property where name = ? and cid = ? ";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setInt(2, cid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				property = new Property();
				property.setName(name);
				int id = rs.getInt("id");
				property.setId(id);
				Category category = new CategoryDAO().get(cid);
				property.setCategory(category);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return property;

	}

	// 根据自身id查询属性
	public Property get(int id) {
		Property property = new Property();

		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "select * from Property where id = " + id;

			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				String name = rs.getString("name");
                int cid = rs.getInt("cid");
                property.setName(name);
                Category category = new CategoryDAO().get(cid);
                property.setCategory(category);
                property.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return property;

	}

	// 查找属性列表
	public List<Property> list(int cid) {
		return list(cid, 0, Short.MAX_VALUE);

	}

	// 查找属性一段列表
	public List<Property> list(int cid, int start, int count) {
		List<Property> properties = new ArrayList<Property>();

		String sql = "select * from Property where cid = ? order by id desc limit ?,?";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, cid);
			ps.setInt(2, start);
			ps.setInt(3, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Property property = new Property();
				int id = rs.getInt(1);
				property.setId(id);
				String name = rs.getString("name");
				property.setName(name);
				Category category = new CategoryDAO().get(cid);
				property.setCategory(category);
				
				properties.add(property);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return properties;

	}

}
