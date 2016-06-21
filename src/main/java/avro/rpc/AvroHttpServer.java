/**
 * 
 */
package avro.rpc;

import java.io.File;

import org.apache.avro.Protocol;
import org.apache.avro.Protocol.Message;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.generic.GenericResponder;

public class AvroHttpServer extends GenericResponder {

	public AvroHttpServer(Protocol protocol) {
		super(protocol);
	}

	public Object respond(Message message, Object request) throws Exception {
		GenericRecord req = (GenericRecord) request;
		GenericRecord reMessage = null;
		if (message.getName().equals("sayHello")) {
			Object name = req.get("name");
			// do something...
			// 取得返回值的类型
			reMessage = new GenericData.Record(super.getLocal().getType(
					"nameMessage"));
			// 直接构造回复
			reMessage.put("name", "Hello, " + name.toString());
			
			System.out.println(reMessage);
		}
		return reMessage;
	}

	public static void main(String[] args) throws Exception {
		int port = 8088;
		try {
			String filepath = AvroHttpServer.class.getResource("/").getPath() 
					+ java.io.File.separator + "mail.avsc";
			System.out.println(filepath);
			Server server = new HttpServer(new AvroHttpServer(Protocol.parse(
					new File(filepath))), port);
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}