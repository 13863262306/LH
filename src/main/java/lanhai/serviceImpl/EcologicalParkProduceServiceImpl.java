package lanhai.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lanhai.dao.EcologicalParkProduceDao;
import lanhai.entity.Jdhz;
import lanhai.entity.ProducePaper;
import lanhai.service.EcologicalParkProduceService;

@Service("ecologicalParkProduceService")
public class EcologicalParkProduceServiceImpl implements EcologicalParkProduceService {

	@Resource
	private EcologicalParkProduceDao ecologicalParkProduceDao;
	@Override
	public void saveEcoParkProduce(Map paraMap) {
		ecologicalParkProduceDao.saveEcoParkProduce(paraMap);
	}
	@Override
	public List<Jdhz> selectParkTbByDay(Map paraMap) {
		return ecologicalParkProduceDao.selectParkTbByDay(paraMap);
	}
	@Override
	public void updateSbslByDjbh(Map paraMap) {
		ecologicalParkProduceDao.updateSbslByDjbh(paraMap);
		
	}
	@Override
	public void updateSubState(Map paraMap) {
		ecologicalParkProduceDao.updateSubState(paraMap);
		
	}
	@Override
	public void deleteTbByDjbh(String djbh) {
		ecologicalParkProduceDao.deleteTbByDjbh(djbh);
		
	}
	@Override
	public ProducePaper selectIfHaveByWlbmAndDay(Map paraMap) {
		return ecologicalParkProduceDao.selectIfHaveByWlbmAndDay(paraMap);
	}

}
