package lanhai.dao;

import java.util.HashMap;
import java.util.List;

public interface ReportFormDao {
	 List<HashMap<String,Object>>selecthotelReportFormlist(HashMap<String,Object> map);
	 List<HashMap<String,Object>>selectselectTssdlist(HashMap<String,Object> map);
	 List<HashMap<String,Object>>selecthotelReportFormtlist(HashMap<String,Object> map);
	 List<HashMap<String,Object>>selecjrrrlist(HashMap<String,Object> map);
	 List<HashMap<String,Object>>selectgroupReportFormtlist(HashMap<String,Object> map);
	 List<HashMap<String,Object>>selectyieldReportFormtlist(HashMap<String,Object> map);
	 List<HashMap<String,Object>>selectdeliveryReportFormtlist(HashMap<String,Object> map);
}
