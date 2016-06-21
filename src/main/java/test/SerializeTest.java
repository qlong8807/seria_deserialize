/**
 * 
 */
package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javaseria.JavaSerializeUtil;
import keyo.KryoUtil;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import protostuff.ProtoStuffSerializeUtil;
import avro.StringPair;
import avro.generic.TestGenericMapping;
import avro.specific.TestSprecificMapping;
import entity.Product;

/**
 * @author zyl
 * @date 2016年6月20日
 * 
 */
public class SerializeTest {

	public static void main(String[] args) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
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
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TestSprecificMapping tsm = new TestSprecificMapping();
					StringPair datum=new StringPair();
			        datum.setLeft("L");
			        datum.setRight("R");
					long t1 = System.currentTimeMillis();
					for(int i=0;i<1000000;i++){
						tsm.test(datum);
					}
					long t2 = System.currentTimeMillis();
					System.out.println("avro生成实体："+(t2-t1));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TestGenericMapping tgm = new TestGenericMapping();
					Schema.Parser parser = new Schema.Parser();
			        Schema schema = parser.parse(getClass().getResourceAsStream("/StringPair.avsc"));
			        GenericRecord datum = new GenericData.Record(schema);
			        datum.put("left", "L");
			        datum.put("right", "R");
					long t1 = System.currentTimeMillis();
					for(int i=0;i<1000000;i++){
						tgm.test(schema,datum);
					}
					long t2 = System.currentTimeMillis();
					System.out.println("avro不生成实体："+(t2-t1));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}

}
