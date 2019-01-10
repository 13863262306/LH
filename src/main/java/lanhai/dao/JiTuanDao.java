package lanhai.dao;

import java.util.List;
import java.util.Map;

import lanhai.entity.Company;
import lanhai.entity.Jdhz;
import lanhai.entity.Mblx;

public interface JiTuanDao {

	List<Jdhz> selectJiTuanhzList(Map paraMap);
	Integer saveJtHz(Jdhz jdhz);
	Integer saveJtHzEntry(Jdhz jdhz);
	List<String> selectYieldByWlIdAndDay(Map paraMap);
	Integer saveJtResultNotNull(Jdhz jdhz);
	Integer saveJtResultEntryNotNull(Jdhz jdhz);
	List<Company> selectBhedJdIds(Map paraMap);
	void updateJtResultEntryNotNullById(Jdhz jdhz);
	void updateJtResultNotNullById(Jdhz jdhz);
	void setMbSubKeyByMb(Map paraMap);
	List<String> selectMbIdByUser(Map paraMap);
	Mblx selectMbInfosByMbIds(Map paraMap);
	Mblx selectMbTypeByMbId(Map paraMap);
	String selectJlIdByWlId(Map paraMap);
	void deleteJtTableHeadById(Map paraMap);
	void deleteJtTableEntryByParentId(Map paraMap);
	Integer selectSerialNumberByPaperAndDay(Map paraMap);
	Integer saveSerialNumber(Map paraMap);
	void updateSerialNumber(Map paraMap);
	Jdhz selectTableHeadInfo(Map paraMap);
	Integer selectKcsl(Map paraMap);
	Jdhz selectFbPaperById(Map paraMap);
	String selectAreaByJdId(Map paraMap);
	String selectCityByJdId(Map paraMap);
}
