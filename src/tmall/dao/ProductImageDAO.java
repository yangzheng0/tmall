package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;

public class ProductImageDAO {
	public static final String type_single = "type_single";
	public static final String type_detail = "type_detail";

	// 获取总数
	public int getTotal() {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

			String sql = "select count(*) from ProductImage";

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
	public void add(ProductImage productImage) {
		String sql = "insert into ProductImage values (null,?,?)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, productImage.getId());
			ps.setString(2, productImage.getType());
			ps.execute();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				productImage.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 更新
	public void update(ProductImage productImage) {

	}

	// 删除
	public void delete(int id) {
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "delete from ProductImage where id = " + id;
			s.equals(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 根据id查询属性
	public ProductImage get(int id) {
		ProductImage productImage = new ProductImage();

		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "select * from ProductImage where id = " + id;

			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				int pid = rs.getInt("pid");
				String type = rs.getString("type");
				Product product = new ProductDAO().get(pid);
				productImage.setProduct(product);
				productImage.setType(type);
				productImage.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productImage;

	}

	// 查找所有产品图片列表
	public List<ProductImage> list(Product p, String type) {
		return list(p, type, 0, Short.MAX_VALUE);

	}

	// 查找产品图片一段列表
	public List<ProductImage> list(Product p, String type, int start, int count) {
		List<ProductImage> productImages = new ArrayList<ProductImage>();

		String sql = "select * from ProductImage where pid =? and type =? order by id desc limit ?,? ";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, p.getId());
			ps.setString(2, type);
			
			ps.setInt(3, start);
			ps.setInt(4, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ProductImage productImage = new ProductImage();
				int id = rs.getInt(1);
				
				productImage.setProduct(p);
				productImage.setType(type);
				productImage.setId(id);
				
				productImages.add(productImage);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productImages;

	}
}
