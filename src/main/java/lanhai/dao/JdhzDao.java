package lanhai.dao;




import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import lanhai.entity.BhCostCenter;
import lanhai.entity.Company;
import lanhai.entity.Jdhz;
import lanhai.entity.Mblx;

public interface JdhzDao {
	List<Jdhz> selectJdhzList(Map<String,Object> paraMap);
	List<Jdhz> selectJdbhList(Map<String,Object> paraMap);
	List<Jdhz> selectDjByWlidAndDay(Map<String,Object> paraMap);
	void deleteBhListByDjbh(@Param("djbh")String djbh);
	void deleteBhWlListByDjbh(@Param("djbh")String djbh);
	void deleteJdBhListByDjbh(Map paraMap);
	void deleteJdBhWlListByDjbh(Map paraMap);
	void deleteJdBhListById(Map paraMap);
	void deleteJdBhWlListById(Map paraMap);
	void saveBhHotel(Map paraMap);
	void saveBhHotelList(Map paraMap);
	void updateJdbhByDjbhAndWlbm(Map paraMap);
	void updateJdbhSubStateByTableId(Map paraMap);
	void updateJdbhSubStateByDjbhAndWlbm(Map paraMap);
	void updateSqslAndShslByDjbh(Map paraMap);
	List<BhCostCenter> selectCentersByCostCenterId(Map paraMap);
	void updateDkBhSubState(Map paraMap);
	List<BhCostCenter> selectCostCenterIdsBhedToday(Map paraMap);
	List<String> selectIdsByJdAndMbAndDayAndWl(Map paraMap);
	String selectJtHaveByJdIdAndMbAndDay(Map paraMap);
	Jdhz selectJtWlCountByJdIdAndMbIdAndDay(Map paraMap);
	List<Company> selectControlCompanysByUsername(Map paraMap);
	void updateJdbhSubStateByJdIdAndMbIdAndDay(Map paraMap);
	Integer selectJdBhSubStateNot1(Map paraMap);
	Integer selectJtSubStateNot0(Map paraMap);
	List<String> selectIdByJdAndDay(Map paraMap);
	List<Mblx> selectBhedMbsByJdId(Map paraMap);
	List<String> selectTableHeadIdsByJdAndUserAndDay(Map paraMap);
} 
