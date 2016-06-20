/**
 * 
 */
package entity;

import java.io.Serializable;

/**
 * @author zyl
 * @date 2016年6月20日
 * 
 */
public class Product implements Serializable{

	private int id;
	private String name;
	private String address;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
