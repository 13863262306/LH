package lanhai.serviceImpl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lanhai.dao.ReportFormDao;
import lanhai.service.ReportFormService;
@Service
public class ReportFormServiceImpl implements ReportFormService{
	@Resource
	private ReportFormDao reportFormDao;
	public List<HashMap<String,Object>>selecthotelReportFormlist(HashMap<String,Object> map){
		return reportFormDao.selecthotelReportFormlist(map);
	 }
	public List<HashMap<String,Object>>selectselectTssdlist(HashMap<String,Object> map){
		return reportFormDao.selectselectTssdlist(map);
	 }
	public List<HashMap<String,Object>>selecthotelReportFormtlist(HashMap<String,Object> map){
		return reportFormDao.selecthotelReportFormtlist(map);
	 }
	public  List<HashMap<String,Object>>selecjrrrlist(HashMap<String,Object> map){
		return reportFormDao.selecjrrrlist(map);
	 }
	
	public List<HashMap<String,Object>>selectgroupReportFormtlist(HashMap<String,Object> map){
		return reportFormDao.selectgroupReportFormtlist(map);
	 }
	
	public List<HashMap<String,Object>>selectyieldReportFormtlist(HashMap<String,Object> map){
		return reportFormDao.selectyieldReportFormtlist(map);
	 }
	
	public List<HashMap<String,Object>>selectdeliveryReportFormtlist(HashMap<String,Object> map){
		return reportFormDao.selectdeliveryReportFormtlist(map);
	 }
}
