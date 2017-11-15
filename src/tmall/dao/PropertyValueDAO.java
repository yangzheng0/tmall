package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.util.DBUtil;

public class PropertyValueDAO {

	// 获取总数
	public int getTotal() {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

			String sql = "select count(*) from PropertyValue";

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return total;
	}

	// 增加
	public void add(PropertyValue propertyValue) {
		String sql = "insert into PropertyValue values (null,?,?,?)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, propertyValue.getId());
			ps.setInt(2, propertyValue.getId());
			ps.setString(3, propertyValue.getValue());
			ps.execute();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				propertyValue.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 更新
	public void update(PropertyValue propertyValue) {
		String sql = "update Property set pid = ?, ptid = ? value = ?, where id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, propertyValue.getProduct().getId());
			ps.setInt(2, propertyValue.getProperty().getId());
			ps.setString(3, propertyValue.getValue());
			ps.setInt(4, propertyValue.getId());

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 删除
	public void delete(int id) {
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "delete from Property PropertyValue id =" + id;
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 根据自身id查询
	public PropertyValue get(int id) {
		PropertyValue propertyValue = new PropertyValue();
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "select * from Property where id = " + id;

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				int pid = rs.getInt("p");
				int ptid = rs.getInt("ptid");
				String value = rs.getString("value");

				Product product = new Product();
				Property property = new Property();

				propertyValue.setProduct(product);
				propertyValue.setProperty(property);
				propertyValue.setValue(value);
				propertyValue.setId(id);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return propertyValue;

	}

	// 根据自身id查询
	public PropertyValue get(int ptid, int pid) {
		PropertyValue propertyValue = null;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "select * from Property where ptid = " + ptid + " and pid = " + pid;

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				propertyValue = new PropertyValue();
				int id = rs.getInt("id");
				String value = rs.getString("value");

				Product product = new ProductDAO().get(pid);
				Property property = new PropertyDAO().get(ptid);

				propertyValue.setProduct(product);
				propertyValue.setProperty(property);
				propertyValue.setValue(value);
				propertyValue.setId(id);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return propertyValue;
	}

	// 查询所有
	public List<PropertyValue> list() {
		return list(0, Short.MAX_VALUE);
	}

	private List<PropertyValue> list(int start, int count) {
		List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();

		String sql = "select * from PropertyValue order by id desc limit ?,?";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, start);
			ps.setInt(2, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PropertyValue propertyValue = new PropertyValue();
				int id = rs.getInt(1);

				int pid = rs.getInt("pid");
				int ptid = rs.getInt("ptid");
				String value = rs.getString("value");

				Product product = new ProductDAO().get(pid);
				Property property = new PropertyDAO().get(ptid);

				propertyValue.setProduct(product);
				propertyValue.setProperty(property);
				propertyValue.setValue(value);
				propertyValue.setId(id);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return propertyValues;
	}

	public void init(Product p) {
		List<Property> pts = new PropertyDAO().list(p.getCategory().getId());

		for (Property pt : pts) {
			PropertyValue pv = get(pt.getId(), p.getId());
			if (null != pv) {
				pv = new PropertyValue();
				pv.setProduct(p);
				pv.setProperty(pt);
				this.add(pv);
			}
		}
	}

	public List<PropertyValue> list(int pid) {
		List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();

		String sql = "select * from PropertyValue where pid = ? order by ptid desc";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setInt(1, pid);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PropertyValue propertyValue = new PropertyValue();
				int id = rs.getInt(1);

				int ptid = rs.getInt("ptid");
				String value = rs.getString("value");

				Product product = new ProductDAO().get(pid);
				Property property = new PropertyDAO().get(ptid);
				propertyValue.setProduct(product);
				propertyValue.setProperty(property);
				propertyValue.setValue(value);
				propertyValue.setId(id);
				propertyValues.add(propertyValue);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return propertyValues;
	}
}
