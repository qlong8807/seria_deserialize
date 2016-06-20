/**
 * 
 */
package keyo;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import entity.Product;

/**
 * @author zyl
 * @date 2016年6月20日
 * 
 */
public class KryoUtil {
	private static Kryo kryo;
	private static Registration registration;
	static {
		kryo = new Kryo();
		// kryo.setReferences(true);
		// kryo.setRegistrationRequired(true);
		// kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		// 注册类
		registration = kryo.register(Product.class);
	}

	public static List<byte[]> serialize(List<Product> lp) {
		List<byte[]> lb = new ArrayList<byte[]>();
		for(Product product : lp) {
			// 序列化
			Output output = null;
			// ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			// output = new Output( outStream , 4096);
			output = new Output(1, 4096);
			kryo.writeObject(output, product);
			byte[] bb = output.toBytes();
			// System.out.println(bb.length);
			lb.add(bb);
			output.flush();
		}
		return lb;
	}

	public static List<Product> deserialize(List<byte[]> lb) {
		List<Product> lp = new ArrayList<Product>();
		for (byte[] bb : lb) {
			// 反序列化
			Input input = null;
			// input = new Input(new
			// ByteArrayInputStream(outStream.toByteArray()),4096);
			input = new Input(bb);
			Product s = (Product) kryo
					.readObject(input, registration.getType());
			lp.add(s);
			input.close();
		}
		return lp;
	}
}
