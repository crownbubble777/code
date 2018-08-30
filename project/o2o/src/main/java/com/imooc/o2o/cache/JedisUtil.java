package com.imooc.o2o.cache;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

public class JedisUtil {
	
	/**操作Key方法*/
	public Keys KEYS;
	/**对存储结构为String类型的操作*/
	public Strings STRINGS;
	/**Redis连接池对象*/
	private JedisPool jedisPool;
	/**
	 * 获取redis连接池
	 * @return
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	/**
	 * 设置redis连接池
	 * @param jedisPoolWriper
	 */
	public void setJedisPool(JedisPoolWriper jedisPoolWriper) {
		this.jedisPool=jedisPoolWriper.getJedisPool();
	}
	/**
	 * 从jedis连接池中获取jedis对象
	 * @return
	 */
	public Jedis getJedis() {
		return jedisPool.getResource();
	}
	
//	=======================================keys===============================================
	//定义一个Keys成员内部内
	public class Keys{
		/**
		 * 判断key是否存在
		 * @param key
		 * @return
		 */
		public boolean exists(String key) {
			Jedis sJedisUtil=getJedis();
			boolean exis=sJedisUtil.exists(key);
			sJedisUtil.close();
			return exis;
		}
		/**
		 * 查找所有匹配给定模式的键
		 * @param pattern
		 * @return
		 * key的表达式，*表示多个,?表示一个
		 */
		public Set<String> keys(String pattern) {
			Jedis jedis=getJedis();
			Set<String> set=jedis.keys(pattern);
			jedis.close();
			return set;
		}
		public long del(String...keys) {
			Jedis jedis=getJedis();
			long count=jedis.del(keys);
			jedis.close();
			return count;
		}
		/**
		 * 删除keys对应的记录，可以是多个key
		 * @param key
		 * @return
		 */
		public long del(byte[]... keys) {
			Jedis jedis=getJedis();
			long count=jedis.del(keys);
			jedis.close();
			return count;
			
		}
	}
//	==========================================strings==================================================
	//定义Strings成员内部类
	public class Strings{
		/**
		 * 根据key获取记录
		 * @param key
		 * @return
		 */
		public String get(String key) {
			Jedis sjedis=getJedis();
			String value=sjedis.get(key);
			sjedis.close();
			return value;
		}
		/**
		 * 根据key获取记录
		 * @param key
		 * @return
		 */
		public byte[] get(byte[] key) {
			Jedis sjedis=getJedis();
			byte[] value=sjedis.get(key);
			sjedis.close();
			return value;
		}
		/**
		 * 添加记录，如果记录已存在将覆盖原有的value
		 * @param key
		 * @param value
		 * @return
		 */
		public String set(String key,String value) {
			return set(SafeEncoder.encode(key),SafeEncoder.encode(value));
		}
		/**
		 * 添加记录，如果记录已经存在将覆盖原有的value
		 * @param key
		 * @param value
		 * @return
		 */
		public String set(String key,byte[] value) {
			return set(SafeEncoder.encode(key), value);
		}
		public String set(byte[] key,byte[] value) {
			Jedis jedis=getJedis();
			String status=jedis.set(key, value);
			jedis.close();
			return status;
		}
	}
}
