package lanhai.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lanhai.dao.JiTuanDao;
import lanhai.entity.Company;
import lanhai.entity.Jdhz;
import lanhai.entity.Mblx;
import lanhai.service.JiTuanHuiZongService;
@Service("jiTuanHuiZongService")
public class JiTuanHuiZongServiceImpl implements JiTuanHuiZongService{

	@Resource
	private JiTuanDao jiTuanDao;
	public List<Jdhz> selectJiTuanhzList(Map paraMap){
		return jiTuanDao.selectJiTuanhzList(paraMap);
	}
	@Override
	public Integer saveJtHz(Jdhz jdhz) {
		return jiTuanDao.saveJtHz(jdhz);
	}
	@Override
	public Integer saveJtHzEntry(Jdhz jdhz) {
		return jiTuanDao.saveJtHzEntry(jdhz);
	}
	@Override
	public List<String> selectYieldByWlIdAndDay(Map paraMap) {
		return jiTuanDao.selectYieldByWlIdAndDay(paraMap);
	}
	@Override
	public Integer saveJtResultNotNull(Jdhz jdhz) {
		return jiTuanDao.saveJtResultNotNull(jdhz);
	}
	@Override
	public Integer saveJtResultEntryNotNull(Jdhz jdhz) {
		return jiTuanDao.saveJtResultEntryNotNull(jdhz);
	}
	@Override
	public List<Company> selectBhedJdIds(Map paraMap) {
		return jiTuanDao.selectBhedJdIds(paraMap);
	}
	@Override
	public void updateJtResultEntryNotNullById(Jdhz jdhz) {
		jiTuanDao.updateJtResultEntryNotNullById(jdhz);		
	}
	@Override
	public void updateJtResultNotNullById(Jdhz jdhz) {
		jiTuanDao.updateJtResultNotNullById(jdhz);
	}
	@Override
	public List<String> selectMbIdByUser(Map paraMap) {
		return jiTuanDao.selectMbIdByUser(paraMap);
	}
	@Override
	public Mblx selectMbInfosByMbIds(Map paraMap) {
		return jiTuanDao.selectMbInfosByMbIds(paraMap);
	}
	@Override
	public Mblx selectMbTypeByMbId(Map paraMap) {
		return jiTuanDao.selectMbTypeByMbId(paraMap);
	}
	@Override
	public void setMbSubKeyByMb(Map paraMap) {
		jiTuanDao.setMbSubKeyByMb(paraMap);
	}
	@Override
	public String selectJlIdByWlId(Map paraMap) {
		String jldwId = null;
		try {
			jldwId = jiTuanDao.selectJlIdByWlId(paraMap);
		}catch(Exception e) {
			System.out.println("通过物料编码获取其计量单位出现错误："+e.getMessage());
		}
		return jldwId;
	}
	@Override
	public void deleteJtTableHeadById(Map paraMap) {
		jiTuanDao.deleteJtTableHeadById(paraMap);
		
	}
	@Override
	public void deleteJtTableEntryByParentId(Map paraMap) {
		jiTuanDao.deleteJtTableEntryByParentId(paraMap);
		
	}
	@Override
	public Integer selectSerialNumberByPaperAndDay(Map paraMap) {
		return jiTuanDao.selectSerialNumberByPaperAndDay(paraMap);
	}
	@Override
	public Integer saveSerialNumber(Map paraMap) {
		return jiTuanDao.saveSerialNumber(paraMap);
	}
	@Override
	public void updateSerialNumber(Map paraMap) {
		jiTuanDao.updateSerialNumber(paraMap);
		
	}
	@Override
	public Integer getSerialNumber(Map paraMap) {
		Integer serialNumber = this.selectSerialNumberByPaperAndDay(paraMap);
		//如果是null说明是今天第一次报本单据类型，新增数据
		if(serialNumber == null) {
			paraMap.put("beginNumber", 1);
			try {
				this.saveSerialNumber(paraMap);
			}catch(Exception e) {
				System.out.println("新增单据"+paraMap.get("paperType")+"流水号出错：,"+e.getMessage());
			}
			serialNumber=0;
		}else {
			try {
				this.updateSerialNumber(paraMap);
			}catch(Exception e) {
				System.out.println("更改单据"+paraMap.get("paperType")+"流水号出错：,"+e.getMessage());
			}
		}
		return ++serialNumber;
	}
	@Override
	public Jdhz selectTableHeadInfo(Map paraMap) {
		return jiTuanDao.selectTableHeadInfo(paraMap);
	}
	@Override
	public Integer selectKcsl(Map paraMap) {
		return jiTuanDao.selectKcsl(paraMap);
	}
	@Override
	public Jdhz selectFbPaperById(Map paraMap) {
		return jiTuanDao.selectFbPaperById(paraMap);
	}
	@Override
	public String selectAreaByJdId(Map paraMap) {
		return jiTuanDao.selectAreaByJdId(paraMap);
	}
	@Override
	public String selectCityByJdId(Map paraMap) {
		return jiTuanDao.selectCityByJdId(paraMap);
	}
	@Override
	public Integer getKcslByKczzAndCkAndWl(String kczz,String ck,String wlId) {
		
		Map selectKcslMap = new HashMap();
		selectKcslMap.put("wlId", wlId);
		if(kczz!=null) {
			selectKcslMap.put("kczz", kczz);
		}
		if(ck!=null) {
			selectKcslMap.put("ckzz", ck);
		}
			
		Integer kcsl = this.selectKcsl(selectKcslMap);
		System.out.println("获取即时库存....库存组织:"+kczz+",仓库:"+ck+",wlId:"+wlId);
		
		
		return kcsl;
	}
	
	
	
}
