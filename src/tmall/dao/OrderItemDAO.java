package tmall.dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
 
import tmall.bean.Category;
import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.User;
import tmall.util.DBUtil;
  
public class OrderItemDAO {
  
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "select count(*) from OrderItem";
  
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return total;
    }
  
    public void add(OrderItem orderItem) {
 
        String sql = "insert into OrderItem values(null,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
  
            ps.setInt(1, orderItem.getProduct().getId());
             
            //订单项在创建的时候，是没有蒂订单信息的
            if(null==orderItem.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, orderItem.getOrder().getId());  
             
            ps.setInt(3, orderItem.getUser().getId());
            ps.setInt(4, orderItem.getNumber());
            ps.execute();
  
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                orderItem.setId(id);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
    public void update(OrderItem orderItem) {
 
        String sql = "update OrderItem set pid= ?, oid=?, uid=?,number=?  where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
 
            ps.setInt(1, orderItem.getProduct().getId());
            if(null==orderItem.getOrder())
                ps.setInt(2, -1);
            else
                ps.setInt(2, orderItem.getOrder().getId());              
            ps.setInt(3, orderItem.getUser().getId());
            ps.setInt(4, orderItem.getNumber());
             
            ps.setInt(5, orderItem.getId());
            ps.execute();
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
  
    }
  
    public void delete(int id) {
  
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "delete from OrderItem where id = " + id;
  
            s.execute(sql);
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
    public OrderItem get(int id) {
        OrderItem orderItem = new OrderItem();
  
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "select * from OrderItem where id = " + id;
  
            ResultSet rs = s.executeQuery(sql);
  
            if (rs.next()) {
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);
                orderItem.setProduct(product);
                orderItem.setUser(user);
                orderItem.setNumber(number);
                 
                if(-1!=oid){
                    Order order= new OrderDAO().get(oid);
                    orderItem.setOrder(order);                   
                }
                 
                orderItem.setId(id);
            }
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return orderItem;
    }
  
    public List<OrderItem> listByUser(int uid) {
        return listByUser(uid, 0, Short.MAX_VALUE);
    }
  
    public List<OrderItem> listByUser(int uid, int start, int count) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
  
        String sql = "select * from OrderItem where uid = ? and oid=-1 order by id desc limit ?,? ";
  
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
  
            ps.setInt(1, uid);
            ps.setInt(2, start);
            ps.setInt(3, count);
  
            ResultSet rs = ps.executeQuery();
  
            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                int id = rs.getInt(1);
 
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");
                 
                Product product = new ProductDAO().get(pid);
                if(-1!=oid){
                    Order order= new OrderDAO().get(oid);
                    orderItem.setOrder(order);                   
                }
 
                User user = new UserDAO().get(uid);
                orderItem.setProduct(product);
 
                orderItem.setUser(user);
                orderItem.setNumber(number);
                orderItem.setId(id);                
                orderItems.add(orderItem);
            }
        } catch (Exception e) {
  
            e.printStackTrace();
        }
        return orderItems;
    }
    public List<OrderItem> listByOrder(int oid) {
        return listByOrder(oid, 0, Short.MAX_VALUE);
    }
     
    public List<OrderItem> listByOrder(int oid, int start, int count) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
         
        String sql = "select * from OrderItem where oid = ? order by id desc limit ?,? ";
         
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
             
            ps.setInt(1, oid);
            ps.setInt(2, start);
            ps.setInt(3, count);
             
            ResultSet rs = ps.executeQuery();
             
            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                int id = rs.getInt(1);
                 
                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                 
                Product product = new ProductDAO().get(pid);
                if(-1!=oid){
                    Order order= new OrderDAO().get(oid);
                    orderItem.setOrder(order);                   
                }
                 
                User user = new UserDAO().get(uid);
                orderItem.setProduct(product);
                 
                orderItem.setUser(user);
                orderItem.setNumber(number);
                orderItem.setId(id);                
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
             
            e.printStackTrace();
        }
        return orderItems;
    }
 
    public void fill(List<Order> os) {
        for (Order o : os) {
            List<OrderItem> ois=listByOrder(o.getId());
            float total = 0;
            int totalNumber = 0;
            for (OrderItem oi : ois) {
                 total+=oi.getNumber()*oi.getProduct().getPromotePrice();
                 totalNumber+=oi.getNumber();
            }
            o.setTotal(total);
            o.setOrderItems(ois);
            o.setTotalNumber(totalNumber);
        }
         
    }
 
    public void fill(Order o) {
        List<OrderItem> ois=listByOrder(o.getId());
        float total = 0;
        for (OrderItem oi : ois) {
             total+=oi.getNumber()*oi.getProduct().getPromotePrice();
        }
        o.setTotal(total);
        o.setOrderItems(ois);
    }
 
    public List<OrderItem> listByProduct(int pid) {
        return listByProduct(pid, 0, Short.MAX_VALUE);
    }
  
    public List<OrderItem> listByProduct(int pid, int start, int count) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
  
        String sql = "select * from OrderItem where pid = ? order by id desc limit ?,? ";
  
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
  
            ps.setInt(1, pid);
            ps.setInt(2, start);
            ps.setInt(3, count);
  
            ResultSet rs = ps.executeQuery();
  
            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                int id = rs.getInt(1);
 
                int uid = rs.getInt("uid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");
                 
                Product product = new ProductDAO().get(pid);
                if(-1!=oid){
                    Order order= new OrderDAO().get(oid);
                    orderItem.setOrder(order);                   
                }
 
                User user = new UserDAO().get(uid);
                orderItem.setProduct(product);
 
                orderItem.setUser(user);
                orderItem.setNumber(number);
                orderItem.setId(id);                
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return orderItems;
    }
 
    public int getSaleCount(int pid) {
         int total = 0;
            try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
      
                String sql = "select sum(number) from OrderItem where pid = " + pid;
      
                ResultSet rs = s.executeQuery(sql);
                while (rs.next()) {
                    total = rs.getInt(1);
                }
            } catch (SQLException e) {
      
                e.printStackTrace();
            }
            return total;
    }
     
}