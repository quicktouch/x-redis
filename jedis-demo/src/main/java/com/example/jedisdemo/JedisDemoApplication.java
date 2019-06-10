package com.example.jedisdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.*;

import java.util.Set;

@SpringBootApplication
@Slf4j
public class JedisDemoApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(JedisDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		useJedis();
//		userPool();
//		transactions();
//		transactionsReturnValue();
	}

	private void transactionsReturnValue(){

		Jedis jedis = new Jedis("localhost");
		Transaction t = jedis.multi();
		t.set("foolz", "bar");
		Response<String> result1 = t.get("fool");

		t.zadd("foo", 1, "barowitch");
		t.zadd("foo", 0, "barinsky");
		t.zadd("foo", 123, "barikoviev");
		// get the entire sortedset
		Response<Set<String>> sose = t.zrange("foo", 0, -1);
		// dont forget it
		t.exec();
		// use Response.get() to retrieve things from a Response
		String foolbar = result1.get();
		// on sose.get() you can directly call Set methods!
		int soseSize = sose.get().size();
		log.error("soseSize：{}",soseSize);
		log.error("foolbar：{}",foolbar);

		// you could still get all results at once, as before
		// List<Object> allResults = t.exec();
	}

	private void transactions(){
		Jedis jedis = new Jedis("localhost");
		//jedis.watch()
		Transaction t = jedis.multi();
		t.set("foo","bar");
		t.exec();
	}

	private void useJedis(){
		Jedis jedis = new Jedis("localhost");
		jedis.set("foo","bar");
		String value = jedis.get("foo");
		log.info(value);
	}

	private void userPool(){
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
		/// Jedis implements Closeable. Hence, the jedis instance will be auto-closed after the last statement.
		try (Jedis jedis = pool.getResource()) {
			/// ... do stuff here ... for example
			jedis.set("foo", "bar");
			String foobar = jedis.get("foo");
			jedis.zadd("sose", 0, "car");
			jedis.zadd("sose", 0, "bike");
			Set<String> sose = jedis.zrange("sose", 0, -1);
			log.info(sose.toString());
		}
		/// ... when closing your application:
		pool.close();
	}

}
