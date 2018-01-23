package com.worldofbuzz.order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.Logger;




public class OrderLine
	{
	private String lineNumber;
	private String sOrderItemId;
	private Integer iOrderItemId;
	private String buyerName;
	private String buyerEmail;
	private String address;
	private String sPostcode;
	private Integer iPostcode;
	private String sSalePrice;
	private Double dSalePrice;
	private String sShippingPrice;
	private Double dShippingPrice;
	private String sku;
	private enum  eStatus {IN_STOCK,OUT_OF_STOCK;};
	private String sStatus;
	private String sOrderDate;
	private Date dOrderDate;
	private String sOrderId;
	private Integer iOrderId;
	
	
	
	public Double getdSalePrice()
		{
		return dSalePrice;
		}

	public void setdSalePrice(Double dSalePrice)
		{
		this.dSalePrice = dSalePrice;
		}

	public String getsStatus()
		{
		return sStatus;
		}

	public void setsStatus(String sStatus)
		{
		this.sStatus = sStatus;
		}

	public OrderLine(String lineNumber, String sOrderItemId, String sOrderId, String buyerName, String buyerEmail, String address,
			String sPostcode, String sSalePrice, String sShippingPrice, String sku, String eStatus, String sOrderDate)
		{
		this.lineNumber = lineNumber;
		this.sOrderItemId = sOrderItemId;
		this.buyerName = buyerName;
		this.buyerEmail = buyerEmail;
		this.address = address;
		this.sPostcode = sPostcode;
		this.sSalePrice = sSalePrice;
		this.sShippingPrice = sShippingPrice;
		this.sku = sku;
		this.sOrderDate = sOrderDate;
		this.sStatus = eStatus;
		this.sOrderId = sOrderId;
		}
	
	@Override
	public String toString()
		{
		return "OrderLine [lineNumber=" + lineNumber + ", sOrderItemId=" + sOrderItemId + ", iOrderItemId=" + iOrderItemId
				+ ", buyerName=" + buyerName + ", buyerEmail=" + buyerEmail + ", address=" + address + ", sPostcode="
				+ sPostcode + ", iPostcode=" + iPostcode + ", sSalePrice=" + sSalePrice + ", dSalePrice=" + dSalePrice
				+ ", sShippingPrice=" + sShippingPrice + ", dShippingPrice=" + dShippingPrice + ", sku=" + sku + ", sStatus="
				+ sStatus + ", sOrderDate=" + sOrderDate + ", dOrderDate=" + dOrderDate + ", sOrderId=" + sOrderId
				+ ", iOrderId=" + iOrderId + "]";
		}

	
	public String validate()
		{
		String ret = "";
		//Calendar cal = Calendar.getInstance();
		DateFormat most = new SimpleDateFormat("yyyy-MM-dd");
		
		if (lineNumber.trim().isEmpty())
			ret += "LineNumber is empty! ";
		
		try
			{
			iOrderItemId = Integer.parseInt(sOrderItemId);
			}
		catch (Exception e) 
			{
			ret += "OrderItemId is not valid! ";
		  }
			
		try
			{
			iOrderId = Integer.parseInt(sOrderId);
			}
		catch (Exception e) 
			{
			ret += "OrderId is not valid! ";
			}		
		
		if (buyerName.trim().isEmpty())
			ret += "BuyerName is empty! ";
		
		if (buyerEmail.trim().isEmpty())
			ret += "BuyerEmail is empty! ";
		else
			if (!EmailValidator.getInstance().isValid(buyerEmail))
				ret += "BuyerEmail is not valid! ";
		
		if (address.trim().isEmpty())
			ret += "Address is empty! ";

		try
			{
			iPostcode = Integer.parseInt(sPostcode);
			}
		catch (Exception e) 
			{
			ret += "Postcode is not valid! ";
			}

		try
			{
			dSalePrice = Double.valueOf(sSalePrice);
			}
		catch (Exception e) 
			{
			ret += "SalePrice is not valid or lower than 1.0! ";
			}		

		try
			{
			dShippingPrice = Double.valueOf(sShippingPrice);
			}
		catch (Exception e) 
			{
  		ret += "ShippingPrice is not valid or lower than 0.0! ";
			}
		
		if (sku.trim().isEmpty())
			ret += "SKU is empty! ";

		if (sOrderDate.trim().isEmpty())
			{
			dOrderDate = Calendar.getInstance().getTime();
			sOrderDate = most.format(dOrderDate);
			}
		else
			if (!sOrderDate.matches("20[0-9][0-9]-[0-9][0-9]-[0-9][0-9]"))
				ret += "OrderDate is not valid! ";
				
		if (sStatus.trim().isEmpty())
			ret += "Status is empty! ";	
		else 
			if (!sStatus.toUpperCase().equals("IN_STOCK")  
					&& !sStatus.toUpperCase().equals("OUT_OF_STOCK") )
				ret += "Status is not valid! ";
			else
				eStatus.valueOf(sStatus.toUpperCase());
			
		return ret;
		}
	
	public String getLineNumber()
		{
		return lineNumber;
		}
	public void setLineNumber(String lineNumber)
		{
		this.lineNumber = lineNumber;
		}
	public String getsOrderItemId()
		{
		return sOrderItemId;
		}
	public void setsOrderItemId(String sOrderItemId)
		{
		this.sOrderItemId = sOrderItemId;
		}
	public Integer getiOrderItemId()
		{
		return iOrderItemId;
		}
	public void setiOrderItemId(Integer iOrderItemId)
		{
		this.iOrderItemId = iOrderItemId;
		}
	public String getBuyerName()
		{
		return buyerName;
		}
	public void setBuyerName(String buyerName)
		{
		this.buyerName = buyerName;
		}
	public String getBuyerEmail()
		{
		return buyerEmail;
		}
	public void setBuyerEmail(String buyerEmail)
		{
		this.buyerEmail = buyerEmail;
		}
	public String getAddress()
		{
		return address;
		}
	public void setAddress(String address)
		{
		this.address = address;
		}
	public String getsPostcode()
		{
		return sPostcode;
		}
	public void setsPostcode(String sPostcode)
		{
		this.sPostcode = sPostcode;
		}
	public Integer getiPostcode()
		{
		return iPostcode;
		}
	public void setiPostcode(Integer iPostcode)
		{
		this.iPostcode = iPostcode;
		}
	public String getsSalePrice()
		{
		return sSalePrice;
		}
	public void setsSalePrice(String sSalePrice)
		{
		this.sSalePrice = sSalePrice;
		}
	public Double getdSalesPrice()
		{
		return dSalePrice;
		}
	public void setdSalesPrice(Double dSalesPrice)
		{
		this.dSalePrice = dSalesPrice;
		}
	public String getsShippingPrice()
		{
		return sShippingPrice;
		}
	public void setsShippingPrice(String sShippingPrice)
		{
		this.sShippingPrice = sShippingPrice;
		}
	public Double getdShippingPrice()
		{
		return dShippingPrice;
		}
	public void setdShippingPrice(Double dShippingPrice)
		{
		this.dShippingPrice = dShippingPrice;
		}
	public String getSku()
		{
		return sku;
		}
	public void setSku(String sku)
		{
		this.sku = sku;
		}
	public String getsOrderDate()
		{
		return sOrderDate;
		}
	public void setsOrderDate(String sOrderDate)
		{
		this.sOrderDate = sOrderDate;
		}
	public Date getdOrderDate()
		{
		return dOrderDate;
		}
	public void setdOrderDate(Date dOrderDate)
		{
		this.dOrderDate = dOrderDate;
		}

	public String getsOrderId()
		{
			return sOrderId;
		}

	public void setsOrderId(String sOrderId)
		{
			this.sOrderId = sOrderId;
		}

	public Integer getiOrderId()
		{
			return iOrderId;
		}

	public void setiOrderId(Integer iOrderId)
		{
			this.iOrderId = iOrderId;
		}
	
	
	}
