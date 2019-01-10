package lanhai.service;

import java.util.List;
import java.util.Map;

import lanhai.entity.BhCostCenter;
import lanhai.entity.BhUser;
import lanhai.entity.Company;
import lanhai.entity.CustomerCom;
import lanhai.entity.Jdhz;
import lanhai.entity.Mblx;
import lanhai.entity.WL_list;

public interface BaoHuoService {

	List<Mblx> selectMbs();
	List<WL_list> selectWlByMbId(Map paraMap);
	List<WL_list> selectWlOnlyByMbId(Map paraMap);
	void saveBhWl(Map paraMap);
	void saveBhList(Map paraMap);
	Integer selectCountOfDjbh(String djbh);
	List<Jdhz> selectBhByDay(Map paraMap);
	void updateSqslByDjbh(Map paraMap);
	void updateSubState(Map paraMap);
	BhUser selectUserByFNumber(Map paraMap);
	Integer selectIfDayOfBh(Map paraMap);
	List<Jdhz> selectBhByDayAndWlbm(Map paraMap);
	String selectOrgIdByCostCenterId(Map paraMap);
	CustomerCom selectCustInfoByNum(Map paraMap);
	String selectFidByFnumber(Map paraMap);
	BhCostCenter selectOrgNameByNumber(Map paraMap);
	String selectInputKeyByMb(Map paraMap);
	Company selectKczzByCostCenterId(Map paraMap);
	String selectNameByOrgId(Map paraMap);
	void updateBhTimeByOrgType(Map paraMap);
	Map selectBhTime(Map paraMap);
	String checkTimeInScope(String checkTime,String startTime,String endTime);
	void updateSubState3to1(Map paraMap);
	List<Mblx> selectBhedMb(Map paraMap);
	void updateSubStateByCostCenterIdAndDay(Map paraMap);
	Integer selectHaveState3(Map paraMap);
	Company selectCwzzByOrgId(Map paraMap);
}
