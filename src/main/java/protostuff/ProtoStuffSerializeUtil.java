/**
 * 
 */
package protostuff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

public class ProtoStuffSerializeUtil {
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

	private static <T> Schema<T> getSchema(Class<T> clazz) {
		@SuppressWarnings("unchecked")
		Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
		if (schema == null) {
			schema = RuntimeSchema.getSchema(clazz);
			if (schema != null) {
				cachedSchema.put(clazz, schema);
			}
		}
		return schema;
	}

	public static <T> byte[] serialize(T obj) {
		if (obj == null) {
			throw new RuntimeException("序列化对象(" + obj + ")!");
		}
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) obj.getClass();
		Schema<T> schema = getSchema(clazz);
//		LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);//这里尽量不要自己指定大小，否则速度会慢的无法想象
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		byte[] protostuff = null;
		try {
			protostuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		} catch (Exception e) {
			throw new RuntimeException("序列化(" + obj.getClass() + ")对象(" + obj
					+ ")发生异常!", e);
		} finally {
			buffer.clear();
		}
		return protostuff;
	}

	public static <T> T deserialize(byte[] paramArrayOfByte,
			Class<T> targetClass) {
		if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
			throw new RuntimeException("反序列化对象发生异常,byte序列为空!");
		}
		T instance = null;
		try {
			instance = targetClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("反序列化过程中依据类型创建对象失败!", e);
		}
		Schema<T> schema = getSchema(targetClass);
		ProtostuffIOUtil.mergeFrom(paramArrayOfByte, instance, schema);
		return instance;
	}

	public static <T> byte[] serializeList(List<T> objList) {
		if (objList == null || objList.isEmpty()) {
			throw new RuntimeException("序列化对象列表(" + objList + ")参数异常!");
		}
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) objList.get(0).getClass();
		Schema<T> schema = getSchema(clazz);
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		byte[] protostuff = null;
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			ProtostuffIOUtil.writeListTo(bos, objList, schema, buffer);
			protostuff = bos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("序列化对象列表(" + objList + ")发生异常!", e);
		} finally {
			buffer.clear();
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return protostuff;
	}

	public static <T> List<T> deserializeList(byte[] paramArrayOfByte,
			Class<T> targetClass) {
		if (paramArrayOfByte == null || paramArrayOfByte.length == 0) {
			throw new RuntimeException("反序列化对象发生异常,byte序列为空!");
		}

		Schema<T> schema = getSchema(targetClass);
		List<T> result = null;
		try {
			result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(
					paramArrayOfByte), schema);
		} catch (IOException e) {
			throw new RuntimeException("反序列化对象列表发生异常!", e);
		}
		return result;
	}

	public static void main(String[] args) {
		PTestMap map = new PTestMap();
		PTestMap tm2 = new PTestMap();
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			map.setKey("aaa" + i);
			map.setValue("value" + i);
			byte[] b = ProtoStuffSerializeUtil.serialize(map);
			tm2 = ProtoStuffSerializeUtil.deserialize(b, PTestMap.class);
			if(i%100000 == 0){
				System.out.println(i+"---"+System.currentTimeMillis());
			}
		}
		long t2 = System.currentTimeMillis();
		System.out.println("proto time:" + (t2 - t1) + "  " + tm2.getKey());
	}
}
