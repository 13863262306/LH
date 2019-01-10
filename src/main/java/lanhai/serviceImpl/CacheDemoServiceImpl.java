package lanhai.serviceImpl;

import java.util.Map;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lanhai.entity.BhUser;
import lanhai.service.CacheDemoService;

@Service
public class CacheDemoServiceImpl implements CacheDemoService {
 
	 public static final String THING_ALL_KEY   = "demo";
	 @CachePut(value = THING_ALL_KEY,key="#name")
	public BhUser findPut(String name,BhUser BhUser){
		return BhUser;
	}
	@CacheEvict(value=THING_ALL_KEY, allEntries=true)
	public void delete() {
		
	}
	
	@CacheEvict(value = THING_ALL_KEY,key="#name")
	public  void delete(String name){
	}
	
	@Cacheable(value = THING_ALL_KEY,key="#name")
	public BhUser findById(String name,BhUser BhUser){
		return BhUser;
	}
}
