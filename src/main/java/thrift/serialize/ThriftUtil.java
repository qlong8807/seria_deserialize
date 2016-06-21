/**
 * 
 */
package thrift.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TIOStreamTransport;

import thrift.pojo.TestMap;

/**
 * @author zyl
 * @date 2016年6月21日
 * 
 */
public class ThriftUtil {

	public static <T extends TBase<?,?>> byte[] serialize(T t) throws IOException, TException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		t.write(new TBinaryProtocol(new TIOStreamTransport(out)));
		out.close();
		return out.toByteArray();
	}
	
	public static <T extends TBase<?,?>> T deserialize(byte[] bytes,T t) throws IOException,TException{
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		t.read(new TBinaryProtocol(new TIOStreamTransport(in)));
		in.close();
		return t;
	}
	
	public static void main(String[] args) {
		TestMap map = new TestMap();
		TestMap tm2 = new TestMap();
		long t1 = System.currentTimeMillis();
		for(int i=0;i<1000000;i++){
			map.setKey("aaa"+i).setValue("value"+i);
			try {
				byte[] b = ThriftUtil.serialize(map);
				tm2 = ThriftUtil.deserialize(b,tm2);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TException e) {
				e.printStackTrace();
			}
			if(i%100000 == 0){
				System.out.println(i+"---"+System.currentTimeMillis());
			}
		}
		long t2 = System.currentTimeMillis();
		System.out.println("time:"+(t2-t1)+"  "+tm2.getKey());
	}
}
