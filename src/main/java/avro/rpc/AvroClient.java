/**
 * 
 */
package avro.rpc;

import java.io.File;
import java.net.URL;

import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;

public class AvroClient {
	private Protocol protocol;

	private GenericRequestor requestor = null;

	public void setUp() throws Exception {
		String filepath = AvroHttpServer.class.getResource("/").getPath() 
				+ java.io.File.separator + "mail.avsc";
		protocol = Protocol.parse(new File(filepath));
		Transceiver t = new HttpTransceiver(new URL("http://localhost:8088")); // 这里如果要在两台机器上运行记得把localhost改成服务端的ip
		requestor = new GenericRequestor(protocol, t);
	}

	public void testSendMessage() throws Exception {
		GenericRecord requestData = new GenericData.Record(
				protocol.getType("nameMessage"));
		// initiate the request data
		requestData.put("name", "123");

		System.out.println("发送前："+requestData);
		Object result = requestor.request("sayHello", requestData);
		if (result instanceof GenericData.Record) {
			GenericData.Record record = (GenericData.Record) result;
			System.out.println("收到的name是："+record.get("name"));
		}
		System.out.println("收到："+result);
	}

	public static void main(String[] args) {
		AvroClient client = new AvroClient();
		try {
			client.setUp();
			client.testSendMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}