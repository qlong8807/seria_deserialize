/**
 * 
 */
package thrift;

import org.apache.thrift.TException;

import thrift.pojo.HelloWorldService.Iface;

public class HelloWorldImpl implements Iface {

	public HelloWorldImpl() {
	}

	@Override
	public String sayHello(String username) throws TException {
		System.out.println("server receive:"+username);
		return "Hi," + username + " welcome to my server";
	}

}