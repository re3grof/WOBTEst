package com.worldofbuzz.dao;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.Logger;

import com.worldofbuzz.order.OrderLine;

//import com.mysql.jdbc.Connection;


public class Data
	{
	private Connection connect;

	public Data(String user,String passwd,String host,String port, Logger log)
		{
		try
			{
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + host	+ ":" + port + "/wob_data";
			
			log.info(url);			
			
			connect =  DriverManager.getConnection(url,user,passwd);
			} 
		catch (SQLException  | ClassNotFoundException e)
			{
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			}
		}
	
	public void disconnect(Logger log)
		{
		try
			{
			connect.close();
			} 
		catch (SQLException e)
			{
			log.error(e.getMessage());
			}
	
		}
	
	public String validate(Integer iOrderId, Integer iOrderItemId, Logger log)
		{
		String ret = "";
		try
			{
			Statement stat = connect.createStatement();
			ResultSet rs = stat.executeQuery("SELECT count(*) as db FROM `order` WHERE `OrderId` = " + iOrderId.toString());
			rs.first();
			if (rs.getInt("db") > 0)
				ret += "OrderId alredy exist! ";

			rs = stat.executeQuery("SELECT count(*) as db FROM `order_item` WHERE `OrderItemId` = " + iOrderItemId.toString());
			rs.first();
			if (rs.getInt("db") > 0)
				ret += "OrderItemId alredy exist! ";
			stat.close();
			} 
		catch (SQLException e)
			{
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			}
		return ret;
		}
	
	public String insertOrder(OrderLine line, Logger log)
		{
		String ret = "";
		Statement stat;
		String sql;
		ResultSet rs;
		
		try
			{
			stat = connect.createStatement();
			
			sql = "INSERT INTO `order`(`OrderId`, `BuyerName`, `BuyerEmail`, `OrderDate`, `OrderTotalValue`, `Address`, `PostCode`) "
					+ "VALUES ("+ line.getsOrderId() +",'"+ line.getBuyerName() +"','"+ line.getBuyerEmail() +"','"+ line.getsOrderDate() 
					+ "',0 ,'"+ line.getAddress() +"',"+ line.getsPostcode() +")";

			log.info(sql);	

			stat.execute(sql);
			rs = stat.getResultSet();
			
			sql = "INSERT INTO `order_item`(`OrderItemId`, `OrderId`, `SalePrice`, `ShippingPrice`, `TotalItemPrice`, `SKU`, `Status`)"
					+ " VALUES ("+ line.getsOrderItemId() +","+ line.getsOrderId() +","+ line.getsSalePrice() +","+ line.getsShippingPrice() 
					+",0,'"+ line.getSku() +"','"+ line.getsStatus().toUpperCase() +"')";

			log.info(sql);				

			stat.execute(sql);
			rs = stat.getResultSet();
			
			stat.close();
			} 
		catch (SQLException e)
			{
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			} 

		return ret;
		}
	
	
	}
