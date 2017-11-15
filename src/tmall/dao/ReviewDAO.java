package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tmall.bean.Product;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class ReviewDAO {

	public int getTotal() {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

			String sql = "select count(*) from Review";

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	public int getTotal(int pid) {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

			String sql = "select count(*) from Review where pid = " + pid;

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return total;
	}

	public void update(Review review) {
		String sql = "pdate Review set content= ?, uid=?, pid=? , createDate = ? where id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, review.getContent());
			ps.setInt(2, review.getUser().getId());
			ps.setInt(3, review.getProduct().getId());
			ps.setTimestamp(4, DateUtil.d2t(review.getCreateDate()));
			ps.setInt(5, review.getId());

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {

			String sql = "delete from Review where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Review get(int id) {
		Review review = new Review();
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "select * from Review where id = " + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				int pid = rs.getInt("pid");
				int uid = rs.getInt("uid");

				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

				String content = rs.getString("content");

				Product product = new ProductDAO().get(pid);
				User user = new UserDAO().get(uid);

				review.setContent(content);
				review.setCreateDate(createDate);
				review.setProduct(product);
				review.setUser(user);
				review.setId(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return review;
	}

	public List<Review> list(int pid) {
		return list(pid, 0, Short.MAX_VALUE);
	}

	public int getCount(int pid) {
		String sql = "select count(*) from Review where pid = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, pid);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private List<Review> list(int pid, int start, int count) {
		List<Review> reviews = new ArrayList<Review>();
		String sql = "select * from Review where pid = ? order by id desc limit ? , ? ";
		try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, pid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Review review = new Review();
				int id = rs.getInt(1);
				int uid = rs.getInt("uid");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				Product product = new ProductDAO().get(pid);
				User user = new UserDAO().get(uid);
				String content = rs.getString("content");
				
				review.setContent(content);
				review.setCreateDate(createDate);
				review.setProduct(product);
				review.setUser(user);
				review.setId(id);
				
				reviews.add(review);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviews;
	}
}
