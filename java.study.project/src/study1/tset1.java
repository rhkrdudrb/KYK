package study1;


public class tset1 {
	public String ProductNo;
	public String ProductName;
	public int  Price;
	public void setProductNo(String ProductNo) {
		this.ProductNo = ProductNo;
	}
	public String getProductNo(String productNo) {return productNo;}
	
	public void setProductName(String ProductName) {
		this.ProductName = ProductName;
	}
	public String getProductName(String ProductName) {return ProductName;}
	public void setPrice(int Price) {this.Price = Price;}
	public int getPrice(int Price) {return Price;}
	
	//오버로딩
	public void setProductInfo(String ProductNo,String ProductName) {
		this.setProductNo(ProductNo);
		this.setProductName(ProductName);
	}
	public void setProductInfo(String ProductNo,String ProductName,int Price) {
		this.ProductNo = ProductNo;
		this.ProductName = ProductName;
		this.Price = Price;
	}
	
	
}
