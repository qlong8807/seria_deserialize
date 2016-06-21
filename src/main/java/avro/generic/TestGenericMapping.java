/**
 * 
 */
package avro.generic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;

/**
 * Created with IntelliJ IDEA.
 * User: lastsweetop
 * Date: 13-8-5
 * Time: 下午7:59
 * To change this template use File | Settings | File Templates.
 */
public class TestGenericMapping {
	public static void main(String[] args) {
//		try {
//			long t1 = System.currentTimeMillis();
//			for(int i=0;i<1000000;i++){
//				new TestGenericMapping().test();
//			}
//			long t2 = System.currentTimeMillis();
//			System.out.println(t2-t1);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
    public void test(Schema schema,GenericRecord datum) throws IOException {
        //将schema从StringPair.avsc文件中加载
//        Schema.Parser parser = new Schema.Parser();
//        Schema schema = parser.parse(getClass().getResourceAsStream("/StringPair.avsc"));

        //根据schema创建一个record示例
//        GenericRecord datum = new GenericData.Record(schema);
//        datum.put("left", "L");
//        datum.put("right", "R");


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //DatumWriter可以将GenericRecord变成edncoder可以理解的类型
        DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(schema);
        //encoder可以将数据写入流中，binaryEncoder第二个参数是重用的encoder，这里不重用，所用传空
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        writer.write(datum,encoder);
        encoder.flush();
        out.close();

        DatumReader<GenericRecord> reader=new GenericDatumReader<GenericRecord>(schema);
        Decoder decoder=DecoderFactory.get().binaryDecoder(out.toByteArray(),null);
        GenericRecord result=reader.read(null,decoder);
        
//        System.out.println(result.get("left").toString());
//        System.out.println(result.get("right").toString());
    }
}