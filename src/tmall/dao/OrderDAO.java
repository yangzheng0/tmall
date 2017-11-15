package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tmall.bean.Order;
import tmall.bean.User;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class OrderDAO {

	public static final String waitPay = "waitPay";// 等待支付
	public static final String waitDelivery = "waitDelivery";// 等待交易
	public static final String waitConfirm = "waitConfirm";// 等待确认
	public static final String waitReview = "waitReview";// 等待评价
	public static final String finish = "finish";// 交易完成
	public static final String delete = "delete";// 删除

	// 获取总数
	public int getTotal() {
		int total = 0;
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
			String sql = "select count(*) from Order_";
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	// 添加
	public void add(Order order) {
		String sql = "select into order_ valus(null,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, order.getOrderCode());
			ps.setString(2, order.getAddress());
			ps.setString(3, order.getPost());
			ps.setString(4, order.getReceiver());
			ps.setString(5, order.getMobile());

			ps.setTimestamp(7, DateUtil.d2t(order.getCreateDate()));
			ps.setTimestamp(8, DateUtil.d2t(order.getPayDate()));
			ps.setTimestamp(9, DateUtil.d2t(order.getDeliveryDate()));
			ps.setTimestamp(10, DateUtil.d2t(order.getConfirmDate()));
			ps.setInt(11, order.getUser().getId());
			ps.setString(12, order.getStatus());

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				order.setId(id);
			}

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 更新
	public void update(Order order) {
		String sql = "update order_ set address= ?, post=?, receiver=?,mobile=?,userMessage=? ,createDate = ? , payDate =? , deliveryDate =?, confirmDate = ? , orderCode =?, uid=?, status=? where id = ?";
		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setString(1, order.getAddress());
			ps.setString(2, order.getPost());
			ps.setString(3, order.getReceiver());
			ps.setString(4, order.getMobile());
			ps.setString(5, order.getUserMessage());
			ps.setTimestamp(6, DateUtil.d2t(order.getCreateDate()));
			;
			ps.setTimestamp(7, DateUtil.d2t(order.getPayDate()));
			;
			ps.setTimestamp(8, DateUtil.d2t(order.getDeliveryDate()));
			;
			ps.setTimestamp(9, DateUtil.d2t(order.getConfirmDate()));
			;
			ps.setString(10, order.getOrderCode());
			ps.setInt(11, order.getUser().getId());
			ps.setString(12, order.getStatus());
			ps.setInt(13, order.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 删除
	public void delete(int id) {
		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

			String sql = "delete from Order_ where id = " + id;

			s.execute(sql);

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	// 根据id获取
	public Order get(int id) {
		Order order = new Order();

		try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

			String sql = "select * from Order_ where id = " + id;

			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				String orderCode = rs.getString("orderCode");
				String address = rs.getString("address");
				String post = rs.getString("post");
				String receiver = rs.getString("receiver");
				String mobile = rs.getString("mobile");
				String userMessage = rs.getString("userMessage");
				String status = rs.getString("status");
				int uid = rs.getInt("uid");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
				Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
				Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));

				order.setOrderCode(orderCode);
				order.setAddress(address);
				order.setPost(post);
				order.setReceiver(receiver);
				order.setMobile(mobile);
				order.setUserMessage(userMessage);
				order.setCreateDate(createDate);
				order.setPayDate(payDate);
				order.setDeliveryDate(deliveryDate);
				order.setConfirmDate(confirmDate);
				User user = new UserDAO().get(uid);
				order.setUser(user);
				order.setStatus(status);

				order.setId(id);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return order;
	}

	// 列出全部
	public List<Order> list() {
		return list(0, Short.MAX_VALUE);
	}

	// 列出部分
	public List<Order> list(int start, int count) {
		List<Order> orders = new ArrayList<Order>();

		String sql = "select * from Order_ order by id desc limit ?,? ";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setInt(1, start);
			ps.setInt(2, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Order order = new Order();
				String orderCode = rs.getString("orderCode");
				String address = rs.getString("address");
				String post = rs.getString("post");
				String receiver = rs.getString("receiver");
				String mobile = rs.getString("mobile");
				String userMessage = rs.getString("userMessage");
				String status = rs.getString("status");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
				Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
				Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
				int uid = rs.getInt("uid");

				int id = rs.getInt("id");
				order.setId(id);
				order.setOrderCode(orderCode);
				order.setAddress(address);
				order.setPost(post);
				order.setReceiver(receiver);
				order.setMobile(mobile);
				order.setUserMessage(userMessage);
				order.setCreateDate(createDate);
				order.setPayDate(payDate);
				order.setDeliveryDate(deliveryDate);
				order.setConfirmDate(confirmDate);
				User user = new UserDAO().get(uid);
				order.setUser(user);
				order.setStatus(status);
				orders.add(order);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return orders;
	}

	// 根据userid列出
	public List<Order> list(int uid, String excludedStatus) {
		return list(uid, excludedStatus, 0, Short.MAX_VALUE);
	}

	public List<Order> list(int uid, String excludedStatus, int start, int count) {
		List<Order> orders = new ArrayList<Order>();

		String sql = "select * from Order_ where uid = ? and status != ? order by id desc limit ?,? ";

		try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setInt(1, uid);
			ps.setString(2, excludedStatus);
			ps.setInt(3, start);
			ps.setInt(4, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Order order = new Order();
				String orderCode = rs.getString("orderCode");
				String address = rs.getString("address");
				String post = rs.getString("post");
				String receiver = rs.getString("receiver");
				String mobile = rs.getString("mobile");
				String userMessage = rs.getString("userMessage");
				String status = rs.getString("status");
				Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
				Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
				Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
				Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));

				int id = rs.getInt("id");
				order.setId(id);
				order.setOrderCode(orderCode);
				order.setAddress(address);
				order.setPost(post);
				order.setReceiver(receiver);
				order.setMobile(mobile);
				order.setUserMessage(userMessage);
				order.setCreateDate(createDate);
				order.setPayDate(payDate);
				order.setDeliveryDate(deliveryDate);
				order.setConfirmDate(confirmDate);
				User user = new UserDAO().get(uid);
				order.setStatus(status);
				order.setUser(user);
				orders.add(order);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return orders;
	}
}
