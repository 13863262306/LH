package lanhai.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import lanhai.entity.BhUser;
import lanhai.entity.Company;
import lanhai.entity.Jdhz;
import lanhai.entity.Mblx;
import lanhai.service.BaoHuoService;
import lanhai.service.JdhzService;
import lanhai.service.JiTuanHuiZongService;
import lanhai.serviceImpl.CacheDemoClient;
import utils.StrUtil;
import utils.Timeutil;

@Controller
@RequestMapping("/groupHz")
public class JiTuanHuiZongController {

	@Resource
	private JiTuanHuiZongService jiTuanHuiZongService;
	@Autowired
	private JdhzService jdhzService;
	@Resource
	private BaoHuoService baoHuoService;
	@Resource
	private CacheDemoClient cacheDemoClient;
	
	@RequestMapping("/showGroupPool")
	public String showJiTuanHuiZong(HttpServletResponse response,Model model,String day,String mbId,String jdId,String username,String password,HttpServletRequest request) {
		//获取所有酒店汇总的数据集合
		//System.out.println("参数。。。。mbid："+mbId);
		BhUser jtUser = null;
		if(username==null) {
			//jtUser = (BhUser) request.getSession().getAttribute("jtUser");
			jtUser = cacheDemoClient.getCacheDemo(request);
			if(jtUser == null) {
				model.addAttribute("errInfo","未获取到用户信息,请重新登陆");
				return "error";
			}
			
		}else {
			Map selectUserMap = new HashMap();
			selectUserMap.put("userName",username);
			jtUser = baoHuoService.selectUserByFNumber(selectUserMap);
			if(jtUser == null) {
				model.addAttribute("errInfo","用户名有误");
				return "error";
			}
		}
		/*if(jtUser.getLongNumber()==null) {
			model.addAttribute("errInfo","用户名不正确");
		}
		//System.out.println("jtUser:..."+jtUser);
		String userLongNumber = jtUser.getLongNumber();
		String orgNumber = "";
		if(userLongNumber.contains("!")) {
			orgNumber = userLongNumber.substring(0,userLongNumber.indexOf("!"));
			//System.out.println("orgNumber:"+orgNumber);
		}
		
		//通过编码获取酒店组织
		if(!"".equals(orgNumber)) {
			Map selectOrgName = new HashMap();
			selectOrgName.put("orgNumber", orgNumber);
			BhCostCenter orgName = baoHuoService.selectOrgNameByNumber(selectOrgName);
			jtUser.setCompanyName(orgName.getCenterName());
			jtUser.setCompanyId(orgName.getId());
		}*/
		
		
		//查询出该档口对应的仓库组织
		Map selectKczzMap = new HashMap();
		selectKczzMap.put("costCenterId", jtUser.getCostCenterId());
		//System.out.println(".!!!!!costCenterId:"+jtUser.getCostCenterId());
		Company kczz = baoHuoService.selectKczzByCostCenterId(selectKczzMap);
		if(kczz != null) {
			jtUser.setCompanyName(kczz.getCompanyName());
			jtUser.setCompanyId(kczz.getId());
		}else {
			jtUser.setCompanyName("蓝海集团");
			
		}
		
		
		
		
		Map paraMap = new HashMap<String,Object>();
		if(day==null) {
			day = Timeutil.currentTime();
		}
		
		
		
		
		
		List<String> states = new ArrayList<String>();
		states.add("0");
		states.add("1");
		states.add("3");
		paraMap.put("states", states);
		
		//处理模板
				//根据登录人员信息获取对应的模板
				Map selectMbMap = new HashMap();
				//System.out.println("user:"+jtUser);
				selectMbMap.put("name", jtUser.getName());
				paraMap.put("selectDay",day);
				Set keys = paraMap.keySet();
				for(Object key:keys) {
					System.out.println("key:"+key+",value:"+paraMap.get(key));
				}
				List<String> controlMbIdsOld = jiTuanHuiZongService.selectMbIdByUser(selectMbMap);
				//获取到的对应的模板
				List<String> controlMbIds = new ArrayList<String>();
				
				if(controlMbIdsOld.size()!=0) {
					for(String controlMbId:controlMbIdsOld) {
						controlMbIds.add(controlMbId.trim());
					}
				}
				for(String sysmb:controlMbIds) {
					//System.out.println("获取到的人员对应的模板id："+sysmb);
				}
				List<Mblx> controlMbs = new ArrayList<Mblx>(); 
				if(controlMbIds.size() != 0) {
					//说明登录人有对应的模板
					Map selectMbsByMbIdMap = new HashMap();
					for(String mbid:controlMbIds) {
						//System.out.println("mbid:"+mbid);
						selectMbsByMbIdMap.put("ambId", mbid.trim());
						Mblx ambLx  = null;
						try {
							ambLx  = jiTuanHuiZongService.selectMbInfosByMbIds(selectMbsByMbIdMap);
						}catch(Exception e) {
							//System.out.println(e.getMessage());
						}
						//System.out.println("根据模板id获取到的模板类型："+ambLx);
						if(ambLx != null) {
							controlMbs.add(ambLx);
						}
					}
					Set keyws = selectMbsByMbIdMap.keySet();
					for(Object o:keyws) {
						//System.out.println(selectMbsByMbIdMap.get(o));
					}
				}else {
					String errInfo = "登录人没有对应的模板，请设置后重试！";
					model.addAttribute("errInfo",errInfo);
					return "error";
				}
				/*for(String mbI:controlMbIds) {
					//System.out.println("...mbId:"+mbI);
				}
				for(Mblx mb:controlMbs) {
					//System.out.println("mb:"+mb);
				}*/
				
				
				//查询今天有过报货的酒店ids
				Map selectJdMap = new HashMap();
				selectJdMap.put("day", day);
				
				if(controlMbIds.size() != 0) {
					selectJdMap.put("mbIds", controlMbIds);
				}
				
				/*if(jdId != null && !"0".equals(jdId)) {
					selectJdMap.put("jdId", jdId);
				}*/
				List<String> jdIds = new ArrayList<String>();
				Set selectJdKeySet = selectJdMap.keySet();
				for(Object o:selectJdKeySet) {
					//System.out.println(".......key:"+o+",value:"+selectJdMap.get(o));
				}
				List<Company> bhedJds = jiTuanHuiZongService.selectBhedJdIds(selectJdMap);
				//System.out.println("******bhedJds.size:"+bhedJds.size());
				if(bhedJds.size()!=0) {
					for(Company comp:bhedJds) {
						//System.out.println("获取到的今天报过货的酒店：comp:"+comp);
						jdIds.add(comp.getId());
					}
				}
				
				if(jdId != null && !"0".equals(jdId)) {
					jdIds.clear();
					jdIds.add(jdId);
				}
				
				if(jdIds.size()!=0) {
					paraMap.put("jdId", jdIds);
				}	
				
				
				
				
				
				
		if(mbId != null) {
			controlMbIds.clear();
			controlMbIds.add(mbId);
		}else {
			if(controlMbIds.size() != 0) {
			mbId = controlMbIds.get(0).trim();
		}
		if(controlMbIds.size()==0) {
			controlMbIds.add("abcdefgh");
		}
			
		}
			
		
		
		
		
		
		
		
		model.addAttribute("mbId",mbId);
		paraMap.put("controlMbIds", controlMbIds);
		
		
		
		//通过模板获取所属的模板类型
				Mblx thisMbLx = null;
				if(mbId != null) {
					Map selectMblxMap = new HashMap();
					selectMblxMap.put("mbId", mbId.trim());
					thisMbLx = jiTuanHuiZongService.selectMbTypeByMbId(selectMblxMap);
					if(thisMbLx == null) {
						model.addAttribute("errInfo","模板与模板类型对应关系未设置");
						return "error";
					}
					//System.out.println("mbId:"+mbId+",,thisMbLx:"+thisMbLx);
					
				}
				
				
				if(thisMbLx!=null) {
					//thisMbLx.setMbNumber("004");
					System.out.println("thisMbLx:"+thisMbLx.getMbNumber());
						model.addAttribute("pageType",thisMbLx.getMbNumber());
				}
		
		//如果是004类型，那么不按照业务日期筛选
		if("004".equals(thisMbLx.getMbNumber())) {
			paraMap.put("onlyByState", "1");
			states.clear();
			states.add("0");
			states.add("3");
			paraMap.put("states", states);
		}
		
		
		Set selectKeys = paraMap.keySet();
		for(Object o:selectKeys) {
			System.out.println("获取酒店汇总数据paraMap....key:"+o+",value:"+paraMap.get(o));
		}
		List<Jdhz> jthzs = jiTuanHuiZongService.selectJiTuanhzList(paraMap);
		for(Jdhz aaaa : jthzs) {
			System.out.println("aaaaaa:....."+aaaa);
		}
		for(Jdhz jtResult:jthzs) {
			
			/*Map selectKcslMap = new HashMap();
			selectKcslMap.put("wlId", jtResult.getWlId());
			if(jtResult.getFhzz()!=null) {
				selectKcslMap.put("kczz", jtResult.getFhzz());
			}
			if(jtResult.getCkzz()!=null) {
				selectKcslMap.put("ckzz", jtResult.getCkzz());
			}
			Set selectKe = selectKcslMap.keySet();
			for(Object o:selectKe) {
				System.out.println("获取即时库存....key:"+o+",value:"+selectKcslMap.get(o));
			}
			Integer kcsl = jiTuanHuiZongService.selectKcsl(selectKcslMap);
			System.out.println("库存组织："+jtResult.getFhzz()+",仓库："+jtResult.getCkzz()+",wlId:"+jtResult.getWlId());*/
			Integer kcsl = jiTuanHuiZongService.getKcslByKczzAndCkAndWl(jtResult.getFhzz(), jtResult.getCkzz(), jtResult.getWlId());
			if(kcsl != null) {
				jtResult.setKcsl(kcsl);
			}
			System.out.println("controller,,集团汇总页面从数据库中获取的数据："+jtResult);
		}
		model.addAttribute("mbs",controlMbs);
		//request.getSession().setAttribute("jtUser", jtUser);
		cacheDemoClient.setCacheDemo(request, response, jtUser);
		model.addAttribute("jtUser",jtUser);
		model.addAttribute("jdList",bhedJds);
		model.addAttribute("jdId",jdId);
		model.addAttribute("jthzListSize",jthzs.size());
		model.addAttribute("day", day);
		model.addAttribute("jthzList",jthzs);
		
		
		
		
		if("004".equals(thisMbLx.getMbNumber())) {
			return "groupPoolSelfVagetable";
		}
		
		
		
		return "groupPool";
	}
	
	
	
	@RequestMapping("/saveJthzOnly")
	@ResponseBody
	public String saveJthzResultOnly(HttpServletRequest request,String day,Model model) {
		String info="保存失败";
		if(day == null) {
			day=Timeutil.currentTime();
		}
		//BhUser jtUser = (BhUser) request.getSession().getAttribute("jtUser");
		BhUser jtUser = cacheDemoClient.getCacheDemo(request);
		if(jtUser == null) {
			return "未获取到用户信息,请重新登陆";
		}
		
		/*Map checkDayMap = new HashMap();
		checkDayMap.put("costCenterId",jtUser.getCostCenterId());
		checkDayMap.put("checkDate", day);
		
		int checkDayResult = baoHuoService.selectIfDayOfBh(checkDayMap);
		if(!(checkDayResult > 0)) {
			info="当前日期不允许报货";
			return info;
		}*/
		
		
		String costCenterName = jtUser.getCostCenterName();
		List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
		for(Map a:values) {
			Set keys = a.keySet();
			for(Object o:keys) {
				//System.out.println("key:"+o+","+a.get(o));
			}
		}
		Integer bhCount = 0;
		String today = Timeutil.currentTime();
		Map<String,Jdhz> tableHead = new HashMap<String,Jdhz>();
		for(Map value:values) {
			/*//报货酒店
			String thisCostCenter = (String)value.get("jdId");
			//模板ID
			String thisMbId = (String)value.get("mbId");
			String resultType="0";
			//物料编码
			String wlbm = (String)value.get("wlbm");
			//System.out.println("wlbm:"+wlbm);
			//申请数量
			String sqsl = (String)value.get("sqsl");
			//审核数量
			String shsl = (String)value.get("shsl");*/
			//表体ID
			String tableEntryId = (String)value.get("tableEntryId");
			//酒店：
			String jdmc = (String)value.get("jdmc");
			//库存数量
			String kcsl = (String)value.get("kcsl");
			//采购数量
			String cgsl = (String)value.get("cgsl");
			//分拨数量
			String fbsl = (String)value.get("fbsl");
			//自产数量
			String zcsl = (String)value.get("zcsl");
			//发货组织
			String fhzz = (String)value.get("fhzz");
			//仓库：
			String ckzz = (String)value.get("ckzz");
			//仓库发货数量
			String ckfhsl = (String)value.get("ckfhsl");
			//采购组织
			String cgzz = (String)value.get("cgzz");
			//发货数量
			String fhsl = "";
			
			
			Jdhz updateHz = new Jdhz();
			updateHz.setId(tableEntryId);
			if(StrUtil.strIsNum(kcsl)) {
				updateHz.setKcsl(Integer.valueOf(kcsl));
			}
			if(StrUtil.strIsNum(cgsl)) {
				updateHz.setCgsl(Integer.valueOf(cgsl));
			}
			if(StrUtil.strIsNum(fbsl)) {
				updateHz.setFbsl(Integer.valueOf(fbsl));
			}
			if(StrUtil.strIsNum(zcsl)) {
				updateHz.setZcsl(Integer.valueOf(zcsl));
			}
				
			if(StrUtil.strIsNum(ckfhsl)) {
				updateHz.setCkfhsl(Integer.valueOf(ckfhsl));
			}
			
				updateHz.setCgzz(cgzz);
				updateHz.setFhzz(fhzz);
			//System.out.println("需要保存的数据："+updateHz);
				try {
					jiTuanHuiZongService.updateJtResultEntryNotNullById(updateHz);
					bhCount++;
				}catch(Exception e) {
					//System.out.println(e.getMessage());
					info = "系统出错，请稍后再试";
					return info;
				}
			
			//查看是否有这个酒店和模板信息，
			/*String key = thisCostCenter+thisMbId;
			//System.out.println("key:"+key+",时候有这个酒店和模板："+tableHead.containsKey(key));
			if(tableHead.containsKey(key)) {
				Jdhz tableEntry = new Jdhz();
				tableEntry.setWlbm(wlbm);
				tableEntry.setId(tableHead.get(thisCostCenter+thisMbId).getId());
				if(sqsl != null && StrUtil.strIsNum(sqsl)) {
					tableEntry.setSqsl(Integer.valueOf(sqsl));
				}
				if(shsl != null && StrUtil.strIsNum(shsl)) {
					tableEntry.setShsl(Integer.valueOf(shsl));
				}
				//设置库存
				if(kcsl != null && StrUtil.strIsNum(kcsl)) {
					tableEntry.setKcsl(Integer.valueOf(kcsl));
				}
				//设置采购
				if(cgsl != null && StrUtil.strIsNum(cgsl)) {
					tableEntry.setCgsl(Integer.valueOf(cgsl));
				}
				//设置分拨
				if(fbsl != null && StrUtil.strIsNum(fbsl)) {
					tableEntry.setFbsl(Integer.valueOf(fbsl));
				}
				//设置自产
				if(zcsl != null && StrUtil.strIsNum(zcsl)) {
					tableEntry.setZcsl(Integer.valueOf(zcsl));
				}
				//设置发货组织
				tableEntry.setFhzz(fhzz);
				//设置采购组织
				tableEntry.setCgzz(cgzz);
				//设置仓库发货数量
				if(ckfhsl != null && StrUtil.strLikeDouble(ckfhsl)) {
					tableEntry.setZcsl(Integer.valueOf(ckfhsl));
				}
				//设置发货数量
				if(fhsl != null && StrUtil.strIsNum(fhsl)) {
					tableEntry.setFhsl(Integer.valueOf(fhsl));
				}
				
				try {
					Integer ifSaved2 = jiTuanHuiZongService.saveJtResultEntryNotNull(tableEntry);
					ffid++;
					bhCount++;
					
					
					
					
				}catch(Exception e) {
					//System.out.println(e.getMessage());
					info="系统异常";
					return info;
				}
				
			}else {
			
			Jdhz jdhz = new Jdhz();
			jdhz.setJdId(thisCostCenter);
			jdhz.setMbId(thisMbId);
			jdhz.setSubUser(jtUser.getName());
			jdhz.setSaveTime(today);
			jdhz.setWlbm(wlbm);
			if(sqsl != null && StrUtil.strLikeDouble(sqsl)) {
				jdhz.setSqsl(Integer.valueOf(sqsl));
			}
			if(shsl != null && StrUtil.strLikeDouble(shsl)) {
				jdhz.setShsl(Integer.valueOf(shsl));
			}
			//设置库存
			if(kcsl != null && StrUtil.strLikeDouble(kcsl)) {
				jdhz.setKcsl(Integer.valueOf(kcsl));
			}
			//设置采购
			if(cgsl != null && StrUtil.strLikeDouble(cgsl)) {
				jdhz.setCgsl(Integer.valueOf(cgsl));
			}
			//设置分拨
			if(fbsl != null && StrUtil.strLikeDouble(fbsl)) {
				jdhz.setFbsl(Integer.valueOf(fbsl));
			}
			//设置自产
			if(zcsl != null && StrUtil.strLikeDouble(zcsl)) {
				jdhz.setZcsl(Integer.valueOf(zcsl));
			}
			//设置仓库发货数量
			if(ckfhsl != null && StrUtil.strLikeDouble(ckfhsl)) {
				jdhz.setCkfhsl(Integer.valueOf(ckfhsl));
			}
			//设置发货组织
			jdhz.setFhzz(fhzz);
			//设置采购组织
			jdhz.setCgzz(cgzz);
			//设置发货数量
			if(fhsl != null && StrUtil.strLikeDouble(fhsl)) {
				jdhz.setFhsl(Integer.valueOf(fhsl));
			}
			
			
			//存入数据库：
			Integer saveResultHead = jiTuanHuiZongService.saveJtResultNotNull(jdhz);
			Integer saveResultEntry = jiTuanHuiZongService.saveJtResultEntryNotNull(jdhz);
			tableHead.put(thisCostCenter+thisMbId, jdhz);
			bhCount++;
		}*/
		}
		
		if( bhCount == values.size()) {
			info="保存成功";
		}
		return info;
	}
	
	
	
	
	//提交
		@RequestMapping("/saveJthz")
		@ResponseBody
		public String newJdBh(HttpServletRequest request,String day,Model model) {
			String info="保存失败";
			if(day == null) {
				day=Timeutil.currentTime();
			}
			//BhUser jtUser = (BhUser) request.getSession().getAttribute("jtUser");
			BhUser jtUser = cacheDemoClient.getCacheDemo(request);
			if(jtUser == null) {
				return "未获取到用户信息,请重新登陆";
			}
			List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
			
			
			
			
			//根据登录人员信息获取对应的模板
			Map selectMbMap = new HashMap();
			//System.out.println("user:"+jtUser);
			selectMbMap.put("name", jtUser.getName());
			List<String> controlMbIdsOld = jiTuanHuiZongService.selectMbIdByUser(selectMbMap);
			//获取到的对应的模板
			List<String> controlMbIds = new ArrayList<String>();
			
			if(controlMbIdsOld.size()!=0) {
				for(String controlMbId:controlMbIdsOld) {
					controlMbIds.add(controlMbId.trim());
				}
			}
			/*List<String> states = new ArrayList<String>();
			states.add("0");
			Map selectJtResultMap  = new HashMap();
			selectJtResultMap.put("controlMbIds", controlMbIds);
			selectJtResultMap.put("states", states);
			selectJtResultMap.put("selectDay", Timeutil.currentTime());
			List<Jdhz> jtResultList = jiTuanHuiZongService.selectJiTuanhzList(selectJtResultMap);*/
			
			
			/*for(Jdhz jtResult:jtResultList) {
				//System.out.println("集团提交时从数据库获取到的数据："+jtResult);
			}*/
			
			
			/*for(Map a:values) {
				Set keys = a.keySet();
				for(Object o:keys) {
					//System.out.println("key:"+o+","+a.get(o));
				}
			}*/
			Integer bhCount = 0;
			String today = Timeutil.currentTime();
			Map<String,Jdhz> tableHead = new HashMap<String,Jdhz>();
			
				//判断逻辑
				Map<String,Jdhz> tableHeadEas = new HashMap<String,Jdhz>();
				
				
				System.out.println("values.size:"+values.size());
				
				
				
				for(Map value:values) {
					//报货酒店
					String thisCostCenter = (String)value.get("jdId");
					
					
					
					//报货酒店
					String jdmc = (String)value.get("jdmc");
					//模板ID
					String thisMbId = (String)value.get("mbId");
					//String resultType="0";
					//物料编码
					String wlbm = (String)value.get("wlbm");
					String oldWlbm = wlbm;
					Map selectChatWlbm = new HashMap();
					selectChatWlbm.put("number", wlbm);
					wlbm = baoHuoService.selectFidByFnumber(selectChatWlbm);
					//申请数量
					String sqsl = (String)value.get("sqsl");
					//审核数量
					String shsl = (String)value.get("shsl");
					//库存数量
					String kcsl = (String)value.get("kcsl");
					System.out.println("从页面获取的库存数量："+kcsl);
					//采购数量
					String cgsl = (String)value.get("cgsl");
					//分拨数量
					String fbsl = (String)value.get("fbsl");
					//自产数量
					String zcsl = (String)value.get("zcsl");
					//发货组织
					String fhzz = (String)value.get("fhzz");
					//仓库
					String ckzz = (String)value.get("ckzz");
					//是否自采
					String isZiCai = (String)value.get("isZiCai");
					//System.out.println("前台接收到的发货组织："+fhzz);
					//采购组织
					String cgzz = (String)value.get("cgzz");
					Map selectCwzzMap = new HashMap();
					selectCwzzMap.put("orgId", thisCostCenter);
					Company cwzzCompany = baoHuoService.selectCwzzByOrgId(selectCwzzMap);
					String cwzz = "";
					if(cwzzCompany!=null) {
						cwzz=cwzzCompany.getId();
					}
					//时候输出店
					String isCustomer = (String)value.get("isCustomer");
					String area = "";
					if("0".equals(isCustomer)) {
						//不是输出管理店
						Map selectAreaMap = new HashMap();
						selectAreaMap.put("jdId", thisCostCenter);
						System.out.println("内部组织查询区域的jdid:"+thisCostCenter);
						area = jiTuanHuiZongService.selectAreaByJdId(selectAreaMap);
					}else if("1".equals(isCustomer)) {
						Map selectCityMap = new HashMap();
						System.out.println("输出管理店查询区域的jdid:"+thisCostCenter);
						selectCityMap.put("jdId", thisCostCenter);
						area = jiTuanHuiZongService.selectCityByJdId(selectCityMap);
					}
					System.out.println("获取到的酒店区域为："+area);
					String tableEntryId = (String)value.get("tableEntryId");
					String tableHeadId = (String)value.get("tableHeadId");
					String ckfhsl = (String)value.get("ckfhsl");
					String subTime = (String)value.get("subTime");
					//1.采购数量不为空
					//是否继续分拨，0代表不继续分拨，1代表继续分拨
					String goOnfb = (String)value.get("goOnFb");
					
					
					
					
					//如果goOnfb为1，新形成一条单据
					if("1".equals(goOnfb)) {
						System.out.println("goOnfb为1，开始拆单：");
						
						//根据当前表头id生成需要新生成的表头id("---X"),并判断是否存在该表头id
						//调用方法获取新的表头id：
						String newTableHeadId = StrUtil.getNewTableHeadId(tableHeadId);
						String newTableEntryId = StrUtil.getNewTableHeadId(tableEntryId);
						//如果存在该表头id，则根据原表体id，生成新的表体id，计算新的申请数量，生成数据
						
						//查看数据库中是否存在该表头id：
						Map seleteFbPaperMap = new HashMap();
						seleteFbPaperMap.put("id", tableHeadId);
						Jdhz oldJdhz = jiTuanHuiZongService.selectFbPaperById(seleteFbPaperMap);
						oldJdhz.setSubTime(Timeutil.currentTime2());
						oldJdhz.setId(newTableHeadId);
						oldJdhz.setTableEntryId(newTableEntryId);
						oldJdhz.setWlbm(oldWlbm);
						oldJdhz.setSubState("3");
						Integer fbslInt = 0;
						Integer cgslInt = 0;
						if(StrUtil.strLikeDouble(fbsl)) {
							fbslInt = Integer.valueOf(fbsl);
						}
						if(StrUtil.strLikeDouble(cgsl)) {
							cgslInt = Integer.valueOf(cgsl);
						}
						Integer newSqsl = Integer.valueOf(sqsl)-fbslInt-cgslInt;
							if(newSqsl > 0 ) {
							oldJdhz.setSqsl(newSqsl);
							oldJdhz.setShsl(newSqsl);
							System.out.println("新的申请数量:"+newSqsl+",新生成的单据数据："+oldJdhz);
							
							
								try {
									Map seleteNewPaperMap = new HashMap();
									seleteNewPaperMap.put("id", newTableHeadId);
									Jdhz newJdhz = jiTuanHuiZongService.selectFbPaperById(seleteNewPaperMap);
									if(newJdhz == null) {
										Integer saveResultHead = jiTuanHuiZongService.saveJtResultNotNull(oldJdhz);
										Integer saveResultEntry = jiTuanHuiZongService.saveJtResultEntryNotNull(oldJdhz);
									}else {
										oldJdhz.setId(newTableHeadId);
										Integer saveResultEntry = jiTuanHuiZongService.saveJtResultEntryNotNull(oldJdhz);
									}
									
									
									
								}catch(Exception e) {
									System.out.println("拆分新单据保存数据时出错：，，，"+e.getMessage());
								}
							
						   }
					}
					
					
					
					//先保存
					Jdhz updateHz = new Jdhz();
					updateHz.setId(tableEntryId);
					//System.out.println("kcsl:"+kcsl+",cgsl:"+cgsl+",fbsl:"+fbsl+",zcsl:"+zcsl+",ckfhsl:"+ckfhsl);
					if(StrUtil.strLikeDouble(kcsl)) {
						updateHz.setKcsl(Integer.valueOf(kcsl));
					}
					if(StrUtil.strLikeDouble(cgsl)) {
						updateHz.setCgsl(Integer.valueOf(cgsl));
					}
					if(StrUtil.strLikeDouble(fbsl)) {
						updateHz.setFbsl(Integer.valueOf(fbsl));
					}
					if(StrUtil.strLikeDouble(zcsl)) {
						updateHz.setZcsl(Integer.valueOf(zcsl));
					}
						
					if(StrUtil.strLikeDouble(ckfhsl)) {
						updateHz.setCkfhsl(Integer.valueOf(ckfhsl));
					}
					
					
						updateHz.setCgzz(cgzz);
						updateHz.setFhzz(fhzz);
						//获取报货酒店对应的财务组织
						
						updateHz.setCwzz(cwzz);
						updateHz.setCkzz(ckzz);
					System.out.println("需要保存的数据："+updateHz);
						try {
							jiTuanHuiZongService.updateJtResultEntryNotNullById(updateHz);
						}catch(Exception e) {
							//System.out.println(e.getMessage());
							info = "系统出错，请稍后再试";
							return info;
						}
					
					//System.out.println("保存后调试：报货酒店:"+thisCostCenter+",模板Id:"+thisMbId+",物料编码："+wlbm+",申请数量："+sqsl+",审核数量："+shsl+",库存数量："+kcsl+",采购数量："+cgsl+",仓库发货数量："+ckfhsl+",分拨数量："+fbsl+",自产数量："+zcsl+",采购组织："+cgzz+",发货组织："+fhzz);
					
					if(cgsl != null && StrUtil.strLikeDouble(cgsl) && !"0".equals(cgsl)) {
						System.out.println("采购数量不为空，area为："+area);
						if(area!=null && "东营".equals(area)) {
						System.out.println("采购数量不为空：。。。。。jd:"+thisCostCenter+",thisMb:"+thisMbId+",cgzz?????????:"+cgzz+",采购数量不为空。。。。");
						Jdhz cghz = new Jdhz();
						String key = "caigou"+thisCostCenter+thisMbId+cgzz+today+jtUser.getPmId();
						
						/*Map selectTableHeadMap = new HashMap();
						selectTableHeadMap.put("day", today);
						selectTableHeadMap.put("jdId", thisCostCenter);
						selectTableHeadMap.put("mbId", thisMbId);
						selectTableHeadMap.put("cgzz", cgzz);
						selectTableHeadMap.put("subUser", jtUser.getPmId());
						selectTableHeadMap.put("paperType", "P");
						selectTableHeadMap.put("subTime", Timeutil.currentTime2());
						Jdhz tableHeadJdhz = jiTuanHuiZongService.selectTableHeadInfo(selectTableHeadMap);*/
						
						
						
						
						
						if(tableHeadEas.containsKey(key)) {
							/*//根据ID判断表体中是否有该物料
							Map selectIfHaveThisWl = new HashMap();
							selectIfHaveThisWl.put("id", tableHeadJdhz.getId());
							selectIfHaveThisWl.put("wlbm", wlbm);*/
							
							System.out.println("采购数量不为空，包含表头tableHeadEas.containsKey....key:");
							Jdhz theHeadOfTableEas = tableHeadEas.get(key);
							Jdhz tableEntry = new Jdhz();
							tableEntry.setId(theHeadOfTableEas.getId());
							tableEntry.setWlbm(wlbm);
							Map selectJldwMap = new HashMap();
							selectJldwMap.put("wlbm", wlbm);
							String jldwId = jiTuanHuiZongService.selectJlIdByWlId(selectJldwMap);
							////System.out.println("wlbm:"+wlbm+",jldwId:"+jldwId);
							if(jldwId != null) {
								tableEntry.setJldw(jldwId);
							}
							tableEntry.setSqsl(Integer.valueOf(sqsl));
							if(shsl != null && StrUtil.strIsNum(shsl)) {
								tableEntry.setShsl(Integer.valueOf(shsl));
							}
							//设置库存数
							/*if(kcsl != null && StrUtil.strLikeDouble(kcsl)) {
								tableEntry.setKcsl(Integer.valueOf(kcsl));
							}*/
							//设置自产数
							/*if(zcsl != null && StrUtil.strLikeDouble(zcsl)) {
								tableEntry.setZcsl(Integer.valueOf(zcsl));
							}*/
							
							//设置发货数量
							//tableEntry.setCkfhsl(Integer.valueOf(ckfhsl));
							tableEntry.setMbId(thisMbId);
							//设置采购数量
							tableEntry.setCgsl(Integer.valueOf(cgsl));
							jiTuanHuiZongService.saveJtHzEntry(tableEntry);
							
							
						}else {
							//System.out.println("采购数量不为空，不包含表头jtUser:"+jtUser);
							Jdhz jdhz = new Jdhz();
							jdhz.setMbId(thisMbId);
							//jdhz.setCostCenterSubUser((String)value.get("costCenterSubUser"));//档口申请人
							jdhz.setSubCostCenter(thisCostCenter);//档口
							//System.out.println("!!!!!thisCostCenter:"+thisCostCenter);
							jdhz.setSqsl(Integer.valueOf(sqsl));
							if(shsl != null && StrUtil.strIsNum(shsl)) {
								jdhz.setShsl(Integer.valueOf(shsl));
							}
							jdhz.setWlbm(wlbm);
							Map selectJldwMap = new HashMap();
							selectJldwMap.put("wlbm", wlbm);
							String jldwId = jiTuanHuiZongService.selectJlIdByWlId(selectJldwMap);
							////System.out.println("wlbm:"+wlbm+",jldwId:"+jldwId);
							if(jldwId != null) {
								jdhz.setJldw(jldwId);
							}
							jdhz.setSubTime((String)value.get("subTime"));
							
							Map selectNumMap = new HashMap();
							selectNumMap.put("day", today);
							selectNumMap.put("paperType", "P");
							selectNumMap.put("orgId", cgzz);
							Integer serialNumber = jiTuanHuiZongService.getSerialNumber(selectNumMap);
							String str = String.format("%05d", serialNumber);
							String djbh = "P"+Timeutil.getStringOfDateNotLine()+str;
							
							System.out.println("采购数量单据编号："+djbh);
							jdhz.setTableHeadId(djbh);
							jdhz.setCgzz(cgzz);
							jdhz.setFhzz(fhzz);
							if(ckzz!=null) {
								jdhz.setCkzz(ckzz);
							}
							//获取财务组织
							jdhz.setCwzz(cwzz);
							
							jdhz.setIsCustomer(isCustomer);
							jdhz.setJtSubUserId(jtUser.getPmId());
							if("1".equals(isCustomer)) {
								jdhz.setCustomerId(thisCostCenter);
							}
							//设置库存数
							/*if(kcsl != null && StrUtil.strLikeDouble(kcsl)) {
								jdhz.setKcsl(Integer.valueOf(kcsl));
							}*/
							//设置自产数
							/*if(zcsl != null && StrUtil.strLikeDouble(zcsl)) {
								jdhz.setZcsl(Integer.valueOf(zcsl));
							}*/
							//ffid++;
							//设置发货数量
							//jdhz.setCkfhsl(Integer.valueOf(ckfhsl));
							//设置采购数量
							jdhz.setCgsl(Integer.valueOf(cgsl));
							/*try {*/
								jdhz.setPaperType("P");
								System.out.println("11111saveJtHz:..jdhz:"+jdhz);
								Integer ifSaved1 = jiTuanHuiZongService.saveJtHz(jdhz);
								Integer ifSaved2 = jiTuanHuiZongService.saveJtHzEntry(jdhz);
								//System.out.println(ifSaved1+ifSaved2);
								tableHeadEas.put("caigou"+thisCostCenter+thisMbId+cgzz+today+jtUser.getPmId(), jdhz);
							/*}catch(Exception e) {
								//System.out.println(e.getMessage());
								info="系统异常";
								return info;
							}*/
						}
					
					}
					}
					
					
					if(fbsl != null && StrUtil.strLikeDouble(fbsl) && !"0".equals(fbsl)) {
						//System.out.println("分拨数量不为空。。。。。。。jd:"+thisCostCenter+",thisMb:"+thisMbId+",fhzz:"+fhzz);
						Jdhz cghz = new Jdhz();
						String key = "fenbo"+thisCostCenter+thisMbId+"yDAAAAAADK/M567U"+today+jtUser.getPmId();
						
						
						/*Map selectTableHeadMap = new HashMap();
						selectTableHeadMap.put("day", today);
						selectTableHeadMap.put("jdId", thisCostCenter);
						selectTableHeadMap.put("mbId", thisMbId);
						selectTableHeadMap.put("fhzz", "yDAAAAAADK/M567U");
						//selectTableHeadMap.put("cgzz", cgzz);
						selectTableHeadMap.put("subUser", jtUser.getPmId());
						selectTableHeadMap.put("paperType", "SP");
						Jdhz tableHeadJdhz = jiTuanHuiZongService.selectTableHeadInfo(selectTableHeadMap);*/
						
						
						
						if(tableHeadEas.containsKey(key)) {
							//System.out.println("分拨数量不为空，，，，，tableHeadEas.containsKey....key:"+key);
							Jdhz theHeadOfTableEas = tableHeadEas.get(key);
							Jdhz tableEntry = new Jdhz();
							tableEntry.setId(theHeadOfTableEas.getId());
							tableEntry.setWlbm(wlbm);
							Map selectJldwMap = new HashMap();
							selectJldwMap.put("wlbm", wlbm);
							String jldwId = jiTuanHuiZongService.selectJlIdByWlId(selectJldwMap);
							////System.out.println("wlbm:"+wlbm+",jldwId:"+jldwId);
							if(jldwId != null) {
								tableEntry.setJldw(jldwId);
							}
							tableEntry.setSqsl(Integer.valueOf(sqsl));
							if(shsl != null && StrUtil.strLikeDouble(shsl)) {
								tableEntry.setShsl(Integer.valueOf(shsl));
							}
							/*//设置库存数
							if(kcsl != null && StrUtil.strLikeDouble(kcsl)) {
								tableEntry.setKcsl(Integer.valueOf(kcsl));
							}*/
							//设置自产数
							if(zcsl != null && StrUtil.strLikeDouble(zcsl)) {
								tableEntry.setZcsl(Integer.valueOf(zcsl));
							}
							
							//设置发货数量
							//tableEntry.setCkfhsl(Integer.valueOf(ckfhsl));
							tableEntry.setMbId(thisMbId);
							//设置采购数量
							tableEntry.setFbsl(Integer.valueOf(fbsl));
							tableEntry.setFhzz("yDAAAAAADK/M567U");
							jiTuanHuiZongService.saveJtHzEntry(tableEntry);
							
							
						}else {
							//System.out.println("!!!!!thisCostCenter:"+thisCostCenter);
							Jdhz jdhz = new Jdhz();
							jdhz.setMbId(thisMbId);
							jdhz.setCostCenterSubUser((String)value.get("costCenterSubUser"));//档口申请人
							jdhz.setSubCostCenter(thisCostCenter);//档口
							jdhz.setSqsl(Integer.valueOf(sqsl));
							jdhz.setIsCustomer(isCustomer);
							jdhz.setJtSubUserId(jtUser.getPmId());
							
							if("1".equals(isCustomer)) {
								jdhz.setCustomerId(thisCostCenter);
							}
							/*if(shsl != null && StrUtil.strLikeDouble(shsl)) {
								jdhz.setSqsl(Integer.valueOf(shsl));
							}*/
							jdhz.setWlbm(wlbm);
							Map selectJldwMap = new HashMap();
							selectJldwMap.put("wlbm", wlbm);
							String jldwId = jiTuanHuiZongService.selectJlIdByWlId(selectJldwMap);
							////System.out.println("wlbm:"+wlbm+",jldwId:"+jldwId);
							if(jldwId != null) {
								jdhz.setJldw(jldwId);
							}
							jdhz.setSubTime((String)value.get("subTime"));
							
							Map selectNumMap = new HashMap();
							selectNumMap.put("day", today);
							selectNumMap.put("paperType", "SP");
							selectNumMap.put("orgId", "yDAAAAAADK/M567U");
							Integer serialNumber = jiTuanHuiZongService.getSerialNumber(selectNumMap);
							
							String str = String.format("%05d", serialNumber);
							String djbh = "SP"+Timeutil.getStringOfDateNotLine()+str;
							System.out.println("分拨数量单据编号："+djbh);
							jdhz.setTableHeadId(djbh);
							jdhz.setFhzz("yDAAAAAADK/M567U");
							if(ckzz!=null) {
								jdhz.setCkzz(ckzz);
							}
							jdhz.setCwzz(cwzz);
							//System.out.println("fhzz:"+jdhz.getFhzz());
							/*//设置库存数
							if(kcsl != null && StrUtil.strLikeDouble(kcsl)) {
								jdhz.setKcsl(Integer.valueOf(kcsl));
							}*/
							//设置自产数
							if(zcsl != null && StrUtil.strLikeDouble(zcsl)) {
								jdhz.setZcsl(Integer.valueOf(zcsl));
							}
							//ffid++;
							//设置发货数量
							//jdhz.setCkfhsl(Integer.valueOf(ckfhsl));
							//设置分拨数量
							jdhz.setFbsl(Integer.valueOf(fbsl));
							//System.out.println("有分拨数量的时候的jdhz:"+jdhz);
							/*try {*/
							jdhz.setPaperType("SP");
							System.out.println("22222saveJtHz:..jdhz:"+jdhz);
								Integer ifSaved1 = jiTuanHuiZongService.saveJtHz(jdhz);
								Integer ifSaved2 = jiTuanHuiZongService.saveJtHzEntry(jdhz);
								//System.out.println(ifSaved1+ifSaved2);
								tableHeadEas.put("fenbo"+thisCostCenter+thisMbId+"yDAAAAAADK/M567U"+today+jtUser.getPmId(), jdhz);
							/*}catch(Exception e) {
								//System.out.println(e.getMessage());
								info="系统异常";
								return info;
							}*/
						}
					}
					
					//System.out.println("ckfhsl:"+ckfhsl);
					if("0".equals(ckfhsl)  || "".equals(ckfhsl) ) {
						//更改数据库的subState
						Jdhz updateResultState = new Jdhz();
						updateResultState.setId(tableHeadId);
						updateResultState.setSubState("1");
						updateResultState.setSubTime(Timeutil.currentTime2());
						try {
							jiTuanHuiZongService.updateJtResultNotNullById(updateResultState);
							//jdhzService.updateJdbhSubStateByJdIdAndMbIdAndDay(updateBhHotelSubState);
						}catch(Exception e) {
							//System.out.println(e.getMessage());
							info = "系统异常，请稍后再试";
							return info;
						}
						bhCount++;
						continue;
					}
					
					
					//发货数量=申请数量-采购数量-分拨数量
					
					//System.out.println("通常情况：。。。。jd:"+thisCostCenter+",thisMb:"+thisMbId+",fhzz:"+fhzz);
					String key = "yiban"+thisCostCenter+thisMbId+fhzz+today+jtUser.getPmId();
					
					
					/*Map selectTableHeadMap = new HashMap();
					selectTableHeadMap.put("day", today);
					selectTableHeadMap.put("jdId", thisCostCenter);
					selectTableHeadMap.put("mbId", thisMbId);
					//selectTableHeadMap.put("fhzz", "yDAAAAAADK/M567U");
					//selectTableHeadMap.put("cgzz", cgzz);
					selectTableHeadMap.put("subUser", jtUser.getPmId());
					selectTableHeadMap.put("paperType", "WQ");
					Jdhz tableHeadJdhz = jiTuanHuiZongService.selectTableHeadInfo(selectTableHeadMap);*/
					
					
					
					if(tableHeadEas.containsKey(key)) {
						Jdhz theHeadOfTableEas = tableHeadEas.get(key);
						Jdhz tableEntry = new Jdhz();
						tableEntry.setId(theHeadOfTableEas.getId());
						tableEntry.setWlbm(wlbm);
						Map selectJldwMap = new HashMap();
						selectJldwMap.put("wlbm", wlbm);
						String jldwId = jiTuanHuiZongService.selectJlIdByWlId(selectJldwMap);
						////System.out.println("wlbm:"+wlbm+",jldwId:"+jldwId);
						if(jldwId != null) {
							tableEntry.setJldw(jldwId);
						}
						tableEntry.setSqsl(Integer.valueOf(sqsl));
						
						if(shsl != null && StrUtil.strLikeDouble(shsl)) {
							tableEntry.setShsl(Integer.valueOf(shsl));
						}
						Integer sqslInt = Integer.valueOf(sqsl);
						//设置库存数
						if(kcsl != null && StrUtil.strLikeDouble(kcsl)) {
							tableEntry.setKcsl(Integer.valueOf(kcsl));
						}
						//设置自产数
						if(zcsl != null && StrUtil.strLikeDouble(zcsl)) {
							tableEntry.setZcsl(Integer.valueOf(zcsl));
						}
						
						//计算发货数量：
						
						//采购数量
						Integer cgslInt = 0;
						if(cgsl != null && StrUtil.strLikeDouble(cgsl)) {
							cgslInt = Integer.valueOf(cgsl);
						}
						
						//分拨数量：
						Integer fbslInt = 0;
						if(fbsl != null && StrUtil.strLikeDouble(fbsl)) {
							fbslInt = Integer.valueOf(fbsl);
						}
						
						//Integer fhslInt = sqslInt-cgslInt-fbslInt;
						//设置发货数量
						if(ckfhsl != null && StrUtil.strLikeDouble(ckfhsl))
						tableEntry.setCkfhsl(Integer.valueOf(ckfhsl));
						//设置采购数量
						//tableEntry.setCgsl(cgslInt);
						//tableEntry.setFbsl(fbslInt);
						tableEntry.setMbId(thisMbId);
						try {
							System.out.println("一般单据，只更新表体："+tableEntry);
							jiTuanHuiZongService.saveJtHzEntry(tableEntry);
						}catch(Exception e) {
							//System.out.println(e.getMessage());
						}
						
						
					}else {
						//System.out.println("!!!!!thisCostCenter:"+thisCostCenter);
						Jdhz jdhz = new Jdhz();
						jdhz.setMbId(thisMbId);
						jdhz.setCostCenterSubUser((String)value.get("costCenterSubUser"));//档口申请人
						jdhz.setSubCostCenter(thisCostCenter);//档口
						jdhz.setSqsl(Integer.valueOf(sqsl));
						jdhz.setIsCustomer(isCustomer);
						jdhz.setJtSubUserId(jtUser.getPmId());
						if("1".equals(isCustomer)) {
							jdhz.setCustomerId(thisCostCenter);
						}
						if(shsl != null && StrUtil.strLikeDouble(shsl)) {
							jdhz.setShsl(Integer.valueOf(shsl));
						}
						jdhz.setWlbm(wlbm);
						Map selectJldwMap = new HashMap();
						selectJldwMap.put("wlbm", wlbm);
						String jldwId = jiTuanHuiZongService.selectJlIdByWlId(selectJldwMap);
						////System.out.println("wlbm:"+wlbm+",jldwId:"+jldwId);
						if(jldwId != null) {
							jdhz.setJldw(jldwId);
						}
						jdhz.setSubTime((String)value.get("subTime"));
						
						Map selectNumMap = new HashMap();
						selectNumMap.put("day", today);
						selectNumMap.put("paperType", "WQ");
						selectNumMap.put("orgId", fhzz);
						Integer serialNumber = jiTuanHuiZongService.getSerialNumber(selectNumMap);
						
						
						String str = String.format("%05d",serialNumber);
						String djbh = "WQ"+Timeutil.getStringOfDateNotLine()+str;
						
						System.out.println("一般数量单据编号："+djbh);
						jdhz.setTableHeadId(djbh);
						jdhz.setFhzz(fhzz);
						if(ckzz!=null) {
							jdhz.setCkzz(ckzz);
						}
						jdhz.setCwzz(cwzz);
						//设置库存数
						if(kcsl != null && StrUtil.strLikeDouble(kcsl)) {
							System.out.println("一般数量单据编号：库存数量："+kcsl);
							jdhz.setKcsl(Integer.valueOf(kcsl));
						}
						//设置自产数
						if(zcsl != null && StrUtil.strLikeDouble(zcsl)) {
							jdhz.setZcsl(Integer.valueOf(zcsl));
						}
						//ffid++;
						Integer sqslInt = Integer.valueOf(sqsl);
						
						
						//计算发货数量：
						
						//采购数量
						Integer cgslInt = 0;
						if(cgsl != null && StrUtil.strLikeDouble(cgsl)) {
							cgslInt = Integer.valueOf(cgsl);
						}
						
						//分拨数量：
						Integer fbslInt = 0;
						if(fbsl != null && StrUtil.strLikeDouble(fbsl)) {
							fbslInt = Integer.valueOf(fbsl);
						}
						
						//Integer fhslInt = sqslInt-cgslInt-fbslInt;
						//设置发货数量
						if(ckfhsl != null && StrUtil.strLikeDouble(ckfhsl)) {
							jdhz.setCkfhsl(Integer.valueOf(ckfhsl));
						}
						
						//设置采购数量
						//jdhz.setCgsl(cgslInt);
						//jdhz.setFbsl(fbslInt);
						jdhz.setPaperType("WQ");
						try {
							System.out.println("3333saveJtHz:..jdhz:"+jdhz);
							Integer ifSaved1 = jiTuanHuiZongService.saveJtHz(jdhz);
							Integer ifSaved2 = jiTuanHuiZongService.saveJtHzEntry(jdhz);
							//System.out.println(ifSaved1+ifSaved2);
							tableHeadEas.put("yiban"+thisCostCenter+thisMbId+fhzz+today+jtUser.getPmId(), jdhz);
						}catch(Exception e) {
							//System.out.println(e.getMessage());
							info="系统异常";
							return info;
						}
					}
					
					//更改数据库的subState
					Jdhz updateResultState = new Jdhz();
					updateResultState.setId(tableHeadId);
					updateResultState.setSubState("1");
					updateResultState.setSubTime(Timeutil.currentTime2());
					//System.out.println("tableHeadId:"+tableHeadId);
					/*Map updateBhHotelSubState = new HashMap();
					updateBhHotelSubState.put("jdId", )*/
					
					try {
						jiTuanHuiZongService.updateJtResultNotNullById(updateResultState);
						//jdhzService.updateJdbhSubStateByJdIdAndMbIdAndDay(updateBhHotelSubState);
					}catch(Exception e) {
						//System.out.println(e.getMessage());
						info = "系统异常，请稍后再试";
						return info;
					}
					
				bhCount++;	
			}
				
			if(bhCount == values.size()) {
				
				System.out.println("提交成功");
				
				info = "提交成功";
				return info;
			}
			return info;
		}
		
		
		
		
		@RequestMapping("/setCostCenterInput")
		@ResponseBody
		public String stopCostCenterInput(String mbIds,String inputKey,String jdIds) {
			String info = "设置失败，请稍后再试";
			
			
			/*List<String> jdIdsList = new ArrayList<String>();
			if(jdIds != null && jdIds.contains(";")) {
				String[] jdIdsStrs = jdIds.split(";");
				if(jdIdsStrs.length != 0) {
					for(String jdId:jdIdsStrs) {
						if("0".equals(jdId)) {
							continue;
						}
						jdIdsList.add(jdId);
					}
				}
			}
			
			if(jdIdsList.size() == 0) {
				info = "没有数据，设置失败";
				return info;
			}*/
			
			
			
			List<String> mbIdsList = new ArrayList<String>();
			if(mbIds != null && mbIds.contains(";")) {
				String[] mbIdsStrs = mbIds.split(";");
				if(mbIdsStrs.length != 0) {
					for(String mbId:mbIdsStrs) {
						if("".equals(mbId)) {
							continue;
						}
						mbIdsList.add(mbId);
					}
				}
				
				
			}
			
			if(mbIdsList.size() == 0) {
				info = "没有数据，设置失败";
				return info;
			}
			
			
			Map updateBhHotelSubState = new HashMap();
			//updateBhHotelSubState.put("jdIds", jdIdsList);
			updateBhHotelSubState.put("mbIds", mbIdsList);
			updateBhHotelSubState.put("subState", "3");
			updateBhHotelSubState.put("day", Timeutil.currentTime());
			updateBhHotelSubState.put("oldSubState", "1");
			
			Map setMap = new HashMap();
			setMap.put("mbIds", mbIdsList);
			setMap.put("inputKey", inputKey);
			Set setMapSet = setMap.keySet();
			for(Object o:setMapSet) {
				//System.out.println("key:"+o+",value:"+setMap.get(o));
			}
			try {
				jiTuanHuiZongService.setMbSubKeyByMb(setMap);
				jdhzService.updateJdbhSubStateByJdIdAndMbIdAndDay(updateBhHotelSubState);
				info="设置成功";
			}catch(Exception e) {
				//System.out.println(e.getMessage());
				info="设置档口报货输入报错";
				return info;
			}
			
			
			
			return info;
		}
		
		
		
		
		
		
		
}
