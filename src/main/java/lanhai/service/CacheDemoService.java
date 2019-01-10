package lanhai.service;

import java.util.Map;

import lanhai.entity.BhUser;

public interface CacheDemoService {
	BhUser findPut(String name,BhUser BhUser);
	void delete();
	void delete(String name);
	BhUser findById(String name,BhUser BhUser);
}
