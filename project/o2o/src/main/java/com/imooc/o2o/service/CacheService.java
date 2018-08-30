package com.imooc.o2o.service;

public interface CacheService {
	/**
	 * 依据key前缀删除匹配该模式下的所有key-value
	 * 如传入：shopCategory,如传入shopCategory,则shopcategory_allfirtstlevel
	 * 等的以shopcategory打头的key_value都会被清空
	 * @param keyPrefix
	 */
	void removeFromCache(String keyPrefix);
}
