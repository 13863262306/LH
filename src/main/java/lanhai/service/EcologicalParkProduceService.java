package lanhai.service;

import java.util.List;
import java.util.Map;

import lanhai.entity.Jdhz;
import lanhai.entity.ProducePaper;

public interface EcologicalParkProduceService {

	void saveEcoParkProduce(Map paraMap);
	List<Jdhz> selectParkTbByDay(Map paraMap);	
	void updateSbslByDjbh(Map paraMap);
	void updateSubState(Map paraMap);
	void deleteTbByDjbh(String djbh);
	ProducePaper selectIfHaveByWlbmAndDay(Map paraMap);
}
