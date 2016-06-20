/**
 * 
 */
package test;

import java.util.ArrayList;
import java.util.List;

import javaseria.JavaSerializeUtil;
import keyo.KryoUtil;
import protostuff.ProtoStuffSerializeUtil;
import entity.Product;

/**
 * @author zyl
 * @date 2016年6月20日
 * 
 */
public class SerializeTest {

	public static void main(String[] args) {
		List<Product> pList = new ArrayList<Product>();
		Product p = new Product();
		p.setId(1);
		p.setName("a");
		p.setAddress("asdfasdf");
		pList.add(p);
		for(int i=0;i<1000000;i++){
			Product p2 = new Product();
			p2.setId(i);
			p2.setName("a"+i);
			p2.setAddress("asdfasdf"+i);
			pList.add(p2);
		}
		long t1 = System.currentTimeMillis();
		byte[] serialize3 = ProtoStuffSerializeUtil.serializeList(pList);
		long t2 = System.currentTimeMillis();
		List<Product> pl2 = ProtoStuffSerializeUtil.deserializeList(serialize3, Product.class);
		long t3 = System.currentTimeMillis();
		System.out.println("protostuff:序列化/反序列化/总耗时："+(t2-t1) +" ---- "+ (t3-t2) +" === "+(t3-t1) +" ==="+pl2.get(0).getId());
		
		t1 = System.currentTimeMillis();
		byte[] serialize = JavaSerializeUtil.serialize(pList);
		t2 = System.currentTimeMillis();
		List<Product> deserialize = (List<Product>) JavaSerializeUtil.deserialize(serialize);
		t3 = System.currentTimeMillis();
		System.err.println("java:序列化/反序列化/总耗时："+(t2-t1) +" ---- "+ (t3-t2) +" === "+(t3-t1)+"---"+deserialize.get(0).getId());
		
		t1 = System.currentTimeMillis();
		List<byte[]> serialize2 = KryoUtil.serialize(pList);
		t2 = System.currentTimeMillis();
		List<Product> deserialize2 = KryoUtil.deserialize(serialize2);
		t3 = System.currentTimeMillis();
		System.err.println("kryo:序列化/反序列化/总耗时："+(t2-t1) +" ---- "+ (t3-t2) +" === "+(t3-t1)+"---"+deserialize2.get(0).getId());
	}

}
