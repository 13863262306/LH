package lanhai.serviceImpl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lanhai.dao.CheckTimeDao;
import lanhai.service.CheckTimeService;

@Service("CheckTimeService")
public class CheckTimeServiceImpl implements CheckTimeService {

	@Resource
	private CheckTimeDao checkTimeDao;
	@Override
	public void saveCheckTime(Map paraMap) {
		checkTimeDao.saveCheckTime(paraMap);

	}

}
