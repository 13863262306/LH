package lanhai.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lanhai.dao.BaoHuoDao;
import lanhai.entity.BhCostCenter;
import lanhai.entity.BhUser;
import lanhai.entity.Company;
import lanhai.entity.CustomerCom;
import lanhai.entity.Jdhz;
import lanhai.entity.Mblx;
import lanhai.entity.WL_list;
import lanhai.service.BaoHuoService;

@Service("baoHuoService")
public class BaoHuoServiceImpl implements BaoHuoService{

	@Resource
	private BaoHuoDao baoHuoDao;
	@Override
	public List<Mblx> selectMbs() {
		return baoHuoDao.selectMbs();
	}
	@Override
	public List<WL_list> selectWlByMbId(Map paraMap) {
		return baoHuoDao.selectWlByMbId(paraMap);
	}
	@Override
	public void saveBhWl(Map paraMap) {
		baoHuoDao.saveBhWl(paraMap);
		
	}
	@Override
	public void saveBhList(Map paraMap) {
		baoHuoDao.saveBhList(paraMap);
		
	}
	@Override
	public Integer selectCountOfDjbh(String djbh) {
		return baoHuoDao.selectCountOfDjbh(djbh);
	}
	@Override
	public List<Jdhz> selectBhByDay(Map paraMap) {
		return baoHuoDao.selectBhByDay(paraMap);
	}
	@Override
	public void updateSqslByDjbh(Map paraMap) {
		baoHuoDao.updateSqslByDjbh(paraMap);
		
	}
	@Override
	public void updateSubState(Map paraMap) {
		baoHuoDao.updateSubState(paraMap);
	}
	@Override
	public BhUser selectUserByFNumber(Map paraMap) {
		return baoHuoDao.selectUserByFNumber(paraMap);
	}
	@Override
	public Integer selectIfDayOfBh(Map paraMap) {
		return baoHuoDao.selectIfDayOfBh(paraMap);
	}
	@Override
	public List<Jdhz> selectBhByDayAndWlbm(Map paraMap) {
		return baoHuoDao.selectBhByDayAndWlbm(paraMap);
	}
	@Override
	public List<WL_list> selectWlOnlyByMbId(Map paraMap) {
		return baoHuoDao.selectWlOnlyByMbId(paraMap);
	}
	@Override
	public String selectOrgIdByCostCenterId(Map paraMap) {
		return baoHuoDao.selectOrgIdByCostCenterId(paraMap);
	}
	@Override
	public CustomerCom selectCustInfoByNum(Map paraMap) {
		return baoHuoDao.selectCustInfoByNum(paraMap);
	}
	@Override
	public String selectFidByFnumber(Map paraMap) {
		return baoHuoDao.selectFidByFnumber(paraMap);
	}
	@Override
	public BhCostCenter selectOrgNameByNumber(Map paraMap) {
		return baoHuoDao.selectOrgNameByNumber(paraMap);
	}
	@Override
	public String selectInputKeyByMb(Map paraMap) {
		return baoHuoDao.selectInputKeyByMb(paraMap);
	}
	@Override
	public Company selectKczzByCostCenterId(Map paraMap) {
		return baoHuoDao.selectKczzByCostCenterId(paraMap);
	}
	@Override
	public String selectNameByOrgId(Map paraMap) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void updateBhTimeByOrgType(Map paraMap) {
		baoHuoDao.updateBhTimeByOrgType(paraMap);
	}
	@Override
	public Map selectBhTime(Map paraMap) {
		return baoHuoDao.selectBhTime(paraMap);
	}
	
	/**
	 * 
	 * @param checkTime
	 * @param startTime
	 * @param endTime
	 * @return 1代表在范围内    0代表不在范围内   3代表参数格式不正确    4代表起始时间大于结束时间
	 */
	public String checkTimeInScope(String checkTime,String startTime,String endTime) {
		String result = "0";
		if(!checkTime.contains("-") || !startTime.contains("-") || !endTime.contains("-")) {
			result = "3";
			return result;
		}
		
		checkTime = checkTime.replace("-", "");
		startTime = startTime.replace("-", "");		
		endTime = endTime.replace("-", "");		
		
		Integer checkTimeInt = Integer.valueOf(checkTime);
		Integer startTimeInt = Integer.valueOf(startTime);
		Integer endTimeInt = Integer.valueOf(endTime);
		
		if(endTimeInt < startTimeInt) {
			return "4";
		}
		
		if(checkTimeInt > startTimeInt && checkTimeInt < endTimeInt) {
			return "1";
		}
		System.out.println("checkTimeInt:"+checkTimeInt+",startTimeInt:"+startTimeInt+",endTimeInt:"+endTimeInt);
		return result;
	}
	@Override
	public void updateSubState3to1(Map paraMap) {
		baoHuoDao.updateSubState3to1(paraMap);
	}
	@Override
	public List<Mblx> selectBhedMb(Map paraMap) {
		return baoHuoDao.selectBhedMb(paraMap);
	}
	@Override
	public void updateSubStateByCostCenterIdAndDay(Map paraMap) {
		baoHuoDao.updateSubStateByCostCenterIdAndDay(paraMap);
	}
	@Override
	public Integer selectHaveState3(Map paraMap) {
		return baoHuoDao.selectHaveState3(paraMap);
	}
	@Override
	public Company selectCwzzByOrgId(Map paraMap) {
		return baoHuoDao.selectCwzzByOrgId(paraMap);
	}
	
	
	

}
