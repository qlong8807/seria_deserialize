/**
 * 
 */
package avro.specific;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import avro.StringPair;

public class TestSprecificMapping {
	public static void main(String[] args) {
//		try {
//			long t1 = System.currentTimeMillis();
//			for(int i=0;i<1000000;i++){
//				new TestSprecificMapping().test();
//			}
//			long t2 = System.currentTimeMillis();
//			System.out.println(t2-t1);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
    public void test(StringPair datum) throws IOException {
        //因为已经生成StringPair的源代码，所以不再使用schema了，直接调用setter和getter即可
//        StringPair datum=new StringPair();
//        datum.setLeft("L");
//        datum.setRight("R");
//        StringPair build = StringPair.newBuilder().setLeft("L").setRight("RRR").build();

        ByteArrayOutputStream out=new ByteArrayOutputStream();
        //不再需要传schema了，直接用StringPair作为范型和参数，
        DatumWriter<StringPair> writer=new SpecificDatumWriter<StringPair>(StringPair.class);
        Encoder encoder= EncoderFactory.get().binaryEncoder(out,null);
        writer.write(datum, encoder);
        encoder.flush();
        out.close();

        DatumReader<StringPair> reader=new SpecificDatumReader<StringPair>(StringPair.class);
        Decoder decoder= DecoderFactory.get().binaryDecoder(out.toByteArray(),null);
        StringPair result=reader.read(null,decoder);
        
//        System.out.println(result.get("left").toString());
//        System.out.println(result.get("right").toString());
    }
}
