package lanhai.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lanhai.dao.JdhzDao;
import lanhai.entity.BhCostCenter;
import lanhai.entity.Company;
import lanhai.entity.Jdhz;
import lanhai.entity.Mblx;
import lanhai.service.JdhzService;
@Service("jdhzService")
public class JdhzServiceImpl implements JdhzService {
@Resource
private JdhzDao jdhzDao;

@Override
public List<Jdhz> selectJdhzList(Map<String, Object> paraMap) {
	return jdhzDao.selectJdhzList(paraMap);
}

@Override
public List<Jdhz> selectDjByWlidAndDay(Map<String, Object> paraMap) {
	return jdhzDao.selectDjByWlidAndDay(paraMap);
}

@Override
public void deleteBhListByDjbh(String djbh) {
	jdhzDao.deleteBhListByDjbh(djbh);
	
}

@Override
public void deleteBhWlListByDjbh(String djbh) {
	jdhzDao.deleteBhWlListByDjbh(djbh);
	
}

@Override
public void deleteJdBhListByDjbh(Map paraMap) {
	jdhzDao.deleteJdBhListByDjbh(paraMap);
	
}

@Override
public void deleteJdBhWlListByDjbh(Map paraMap) {
	jdhzDao.deleteJdBhWlListByDjbh(paraMap);
	
}

@Override
public void saveBhHotel(Map paraMap) {
	jdhzDao.saveBhHotel(paraMap);
	
}

@Override
public void saveBhHotelList(Map paraMap) {
	jdhzDao.saveBhHotelList(paraMap);
	
}

@Override
public List<Jdhz> selectJdbhList(Map<String, Object> paraMap) {
	return jdhzDao.selectJdbhList(paraMap);
}

@Override
public void updateJdbhByDjbhAndWlbm(Map paraMap) {
	jdhzDao.updateJdbhByDjbhAndWlbm(paraMap);
	
}

@Override
public void updateJdbhSubStateByDjbhAndWlbm(Map paraMap) {
	jdhzDao.updateJdbhSubStateByDjbhAndWlbm(paraMap);
}

@Override
public List<BhCostCenter> selectCentersByCostCenterId(Map paraMap) {
	return jdhzDao.selectCentersByCostCenterId(paraMap);
}

@Override
public void updateDkBhSubState(Map paraMap) {
	jdhzDao.updateDkBhSubState(paraMap);
	
}

@Override
public void updateSqslAndShslByDjbh(Map paraMap) {
	jdhzDao.updateSqslAndShslByDjbh(paraMap);
	
}

@Override
public List<BhCostCenter> selectCostCenterIdsBhedToday(Map paraMap) {
	return jdhzDao.selectCostCenterIdsBhedToday(paraMap);
}

@Override
public List<String> selectIdsByJdAndMbAndDayAndWl(Map paraMap) {
	return jdhzDao.selectIdsByJdAndMbAndDayAndWl(paraMap);
}

@Override
public void updateJdbhSubStateByTableId(Map paraMap) {
	jdhzDao.updateJdbhSubStateByTableId(paraMap);
	
}

@Override
public void deleteJdBhListById(Map paraMap) {
	jdhzDao.deleteJdBhListById(paraMap);
	
}

@Override
public void deleteJdBhWlListById(Map paraMap) {
	jdhzDao.deleteJdBhWlListById(paraMap);
	
}

@Override
public String selectJtHaveByJdIdAndMbAndDay(Map paraMap) {
	return jdhzDao.selectJtHaveByJdIdAndMbAndDay(paraMap);
}

@Override
public Jdhz selectJtWlCountByJdIdAndMbIdAndDay(Map paraMap) {
	return jdhzDao.selectJtWlCountByJdIdAndMbIdAndDay(paraMap);
}

@Override
public List<Company> selectControlCompanysByUsername(Map paraMap) {
	return jdhzDao.selectControlCompanysByUsername(paraMap);
}

@Override
public void updateJdbhSubStateByJdIdAndMbIdAndDay(Map paraMap) {
	jdhzDao.updateJdbhSubStateByJdIdAndMbIdAndDay(paraMap);
}

@Override
public Integer selectJdBhSubStateNot1(Map paraMap) {
	return jdhzDao.selectJdBhSubStateNot1(paraMap);
}

@Override
public List<String> selectIdByJdAndDay(Map paraMap) {
	return jdhzDao.selectIdByJdAndDay(paraMap);
}

@Override
public List<Mblx> selectBhedMbsByJdId(Map paraMap) {
	return jdhzDao.selectBhedMbsByJdId(paraMap);
}

@Override
public Integer selectJtSubStateNot0(Map paraMap) {
	return jdhzDao.selectJtSubStateNot0(paraMap);
}

@Override
public List<String> selectTableHeadIdsByJdAndUserAndDay(Map paraMap) {
	return jdhzDao.selectTableHeadIdsByJdAndUserAndDay(paraMap);
}
	
}
