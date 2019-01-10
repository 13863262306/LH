package lanhai.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import lanhai.entity.BhCostCenter;
import lanhai.entity.BhUser;
import lanhai.entity.Company;
import lanhai.entity.CustomerCom;
import lanhai.entity.Jdhz;
import lanhai.entity.Mblx;
import lanhai.entity.WL_list;
import lanhai.service.BaoHuoService;
import lanhai.service.JdhzService;
import lanhai.service.JiTuanHuiZongService;
import lanhai.serviceImpl.CacheDemoClient;
import utils.StrUtil;
import utils.Timeutil;

@Controller
@RequestMapping("/jdhz")
public class JdhzController {

	@Autowired
	private JdhzService jdhzService;
	@Resource
	private BaoHuoService baoHuoService;
	@Resource
	private JiTuanHuiZongService jiTuanHuiZongService;
	@Resource
	private CacheDemoClient cacheDemoClient;
	@RequestMapping("/jdhzList")
	public String showJdhz(Model model,String day,String username,String password,HttpServletRequest request,HttpServletResponse response,String costCenterName,String mbId) {
		System.out.println("username:"+username+",password:"+password);
		CustomerCom cc = null;
		BhUser bhUser = null;
		Map paraMap = new HashMap<String,Object>();
		if(day==null) {
			day = Timeutil.currentTime();
		}
		
		if(username==null) {
			/*bhUser = (BhUser) request.getSession().getAttribute("user");
			cc = (CustomerCom) request.getSession().getAttribute("customer");*/
			bhUser = cacheDemoClient.getCacheDemo(request);
			cc = bhUser.getCc();
			if(bhUser == null && cc == null) {
				model.addAttribute("errInfo","未获取到用户信息,请重新登陆");
				return "error";
			}
		}else {
			Map selectUserMap = new HashMap();
			selectUserMap.put("userName",username);
			bhUser = baoHuoService.selectUserByFNumber(selectUserMap);
			System.out.println("bhUser:"+bhUser);
			Map selectCustMap = new HashMap();
			selectCustMap.put("userNum", username);
			cc = baoHuoService.selectCustInfoByNum(selectCustMap);
			bhUser.setCc(cc);
		}
		
		
		List<BhCostCenter> costCenters = new ArrayList<BhCostCenter>();
		List<String> costCenterNames = new ArrayList<String>();
		if("20".equals(bhUser.getUserType())) {
			
			//查询出该档口对应的仓库组织
			Map selectKczzMap = new HashMap();
			selectKczzMap.put("costCenterId", bhUser.getCostCenterId());
			Company kczz = baoHuoService.selectKczzByCostCenterId(selectKczzMap);
			if(kczz == null) {
				model.addAttribute("errInfo","未获取到对应的库存组织，请分配后重试");
				return "error";
			}else {
				bhUser.setCompanyName(kczz.getCompanyName());
				bhUser.setCompanyId(kczz.getId());
				//bhUser.setCompanyAreaName(kczz.getCompanyAreaName());
				
			}
			
			
			
			
			
			/*//通过用户名获取所管理的仓库组织
			Map selectJdMap = new HashMap();
			selectJdMap.put("userName",bhUser.getName());
			List<Company> jds = jdhzService.selectControlCompanysByUsername(selectJdMap);
			if(jds.size()==0) {
				model.addAttribute("errInfo","没有权限");
				return "error";
			}
			for(Company com:jds) {
				System.out.println("说管辖的酒店："+com.getCompanyName());
			}
			//通过传过来的参数获取当前仓库对象
			Company selectedJd = new Company();
			if(jdId == null) {
				if(jds.size() != 0) {
					jdId = jds.get(0).getId();
					selectedJd = jds.get(0);
				}
			}else {
				for(int i = 0;i<jds.size();i++) {
					if(jdId.equals(jds.get(i).getId())){
						selectedJd = jds.get(i);
					}
				}
			}
			
			model.addAttribute("jdList",jds);
			model.addAttribute("jdId",jdId);
			model.addAttribute("jdName",selectedJd.getCompanyName());*/
			//内部
			//通过长编码获取酒店编码
			/*String userLongNumber = bhUser.getLongNumber();
			String orgNumber = "";
			if(userLongNumber.contains("!")) {
				orgNumber = userLongNumber.substring(userLongNumber.indexOf("!")+1);
				orgNumber = orgNumber.substring(0, orgNumber.indexOf("!"));
			}
			
			//通过编码获取酒店组织
			if(!"".equals(orgNumber)) {
				Map selectOrgName = new HashMap();
				selectOrgName.put("orgNumber", orgNumber);
				BhCostCenter orgName = baoHuoService.selectOrgNameByNumber(selectOrgName);
				bhUser.setCompanyName(orgName.getCenterName());
				bhUser.setCompanyId(orgName.getId());
			}
			System.out.println("内部组织longNumber:"+userLongNumber+",orgNumber:"+orgNumber+",bhUser:"+bhUser);*/
			
			
			Map selectCostCentersMap = new HashMap();
			selectCostCentersMap.put("today", Timeutil.currentTime());
			selectCostCentersMap.put("jdId", bhUser.getCompanyId());
			Set abc = selectCostCentersMap.keySet();
			for(Object o:abc) {
				System.out.println("key:"+o+",value:"+selectCostCentersMap.get(o));
			}
			costCenters = jdhzService.selectCostCenterIdsBhedToday(selectCostCentersMap);
			for(BhCostCenter bcc : costCenters) {
				System.out.println("bcc:"+bcc);
				costCenterNames.add(bcc.getCenterName());
				System.out.println("处理完档口名称："+bcc.getCenterName());
			}
			
		}else if("30".equals(bhUser.getUserType())) {
			//外部
			bhUser.setName(cc.getUserNum());
			bhUser.setCostCenterName(cc.getName());
			bhUser.setCompanyName(cc.getName());
			bhUser.setCostCenterId(cc.getId());
			bhUser.setCompanyId(cc.getId());
			/*request.getSession().setAttribute("customer", cc);
			request.getSession().setAttribute("user", bhUser);*/
			cacheDemoClient.setCacheDemo(request, response, bhUser);
			BhCostCenter bcca = new BhCostCenter();
			bcca.setCenterName(cc.getName());
			bcca.setId(cc.getId());
			costCenters.add(bcca);
			costCenterNames.add(cc.getName());
			
			System.out.println("输出管理店组织id："+cc.getId());
			/*Company selectedJd = new Company();
			selectedJd.setCompanyName(cc.getName());
			selectedJd.setCompanyNumber(cc.getName());
			selectedJd.setId(cc.getId());
			List<Company> jds = new ArrayList<Company>();
			jds.add(selectedJd);
			if(jdId == null) {
				jdId = cc.getId();
			}
				
			
			
			model.addAttribute("jdList",jds);
			model.addAttribute("jdId",jdId);
			model.addAttribute("jdName",selectedJd.getCompanyName());*/
		}
		
		System.out.println("组织，获取区域后输出：+报货User："+bhUser);
		
		System.out.println("bhUser.getuserType:"+bhUser.getUserType());
		for(String str:costCenterNames) {
			System.out.println("str:"+str);
		}
		paraMap.put("selectDay", day);
		if(costCenterName != null && !"0".equals(costCenterName)) {
			costCenterNames.clear();
			costCenterNames.add(costCenterName);
		}
		if(costCenterNames.size() != 0) {
			paraMap.put("costCenterNames", costCenterNames);
		}
		
		
		Map selectMbMap = new HashMap();
		//selectMbMap.put("orgId", bhUser.getCostCenterId());
		List<Mblx> mbList = baoHuoService.selectMbs();
		List<String> mbIds = new ArrayList<String>();
		for(Mblx mvs:mbList) {
			System.out.println(mvs.getId());
			mbIds.add(mvs.getId());
		}
		if(mbId != null && !"0".equals(mbId)) {
			mbIds.clear();
			mbIds.add(mbId);
		}
		/*request.getSession().setAttribute("user", bhUser);
		request.getSession().setAttribute("costCenter", costCenters);*/
		cacheDemoClient.setCacheDemo(request, response, bhUser);
		List<String> subStates = new ArrayList<String>();
		subStates.add("1");
		//subStates.add("3");
		paraMap.put("subStates", subStates);	
		paraMap.put("mbs", mbIds);
		Set keys = paraMap.keySet();
		
		
		
		paraMap.put("jdId", bhUser.getCompanyId());
		for(Object key : keys) {
			System.out.println("***jdhz.paraMap.key:"+key+"value:"+paraMap.get(key));
		}
		List<Jdhz> jdhzs = new ArrayList<Jdhz>();
		if(costCenterNames.size() != 0) {
			jdhzs = jdhzService.selectJdhzList(paraMap);
		}
		model.addAttribute("user",bhUser);
		model.addAttribute("mbList",mbList);
		model.addAttribute("mbId",mbId);
		model.addAttribute("costCenterName",costCenterName);
		model.addAttribute("costCenterList",costCenters);
		model.addAttribute("jdhzListSize",jdhzs.size());
		model.addAttribute("day", day);
		model.addAttribute("jdhzList",jdhzs);
		model.addAttribute("customerCom",cc);
		
		//时间控制生成报货单
		String nowTime = new SimpleDateFormat("HH-mm").format(new Date());
		Map selectTimeKeyMap = new HashMap();
		selectTimeKeyMap.put("orgType", "2");
		Map selectResult = baoHuoService.selectBhTime(selectTimeKeyMap);
		String startTime = "";
		String endTime = "";
		if(selectResult != null) {
			startTime = (String)selectResult.get("START_TIME");
			endTime = (String)selectResult.get("END_TIME");
		}
		
		String checkResult = "1";
		if(!"".equals(endTime) && !"".equals(startTime)) {
			checkResult = baoHuoService.checkTimeInScope(nowTime,startTime,endTime);
		}
		model.addAttribute("newBhKey",checkResult);
		
		
		return "jdhz";
	}
	
	@RequestMapping("/oneDetail")
	public String editOneWl(Model model,String day,String wlbm,HttpServletRequest request) {
		List<BhCostCenter> costCenters =  (List<BhCostCenter>) request.getSession().getAttribute("costCenter");
		//BhUser bhUser = (BhUser) request.getSession().getAttribute("user");
		BhUser bhUser = cacheDemoClient.getCacheDemo(request);
		
		if(bhUser == null) {
			model.addAttribute("errInfo","未获取到用户信息,请重新登陆");
			return "error";
		}
		List<String> costCenterNames = new ArrayList<String>();
		if(costCenters.size() != 0) {
			for(BhCostCenter bcc : costCenters) {
				costCenterNames.add(bcc.getCenterName());
			}
		}else {
			costCenterNames.add(bhUser.getCostCenterName());
		}
		
		Map paraMap = new HashMap<String,Object>();
		paraMap.put("costCenters", costCenterNames);
		paraMap.put("wlbm", wlbm);
		paraMap.put("day", day);
		List<Jdhz> jdhzs = jdhzService.selectDjByWlidAndDay(paraMap);
		if(jdhzs.size() != 0) {
			Jdhz jdhz = jdhzs.get(0);
			String wlbmI = jdhz.getWlbm();
			String wlmc = jdhz.getWlmc();
			String wlgg = jdhz.getWlgg();
			String jldw = jdhz.getJldw();
			String sqrq = jdhz.getSqrq();
			model.addAttribute("user", bhUser);
			model.addAttribute("day", day);
			model.addAttribute("wlbm",wlbmI);
			model.addAttribute("wlmc",wlmc);
			model.addAttribute("wlgg",wlgg);
			model.addAttribute("jldw",jldw);
			model.addAttribute("sqrq",sqrq);
		}
		model.addAttribute("jdhzList",jdhzs);
		return "jdhzOneDetail";
	}
	
	@RequestMapping("/delOneRow")
	@ResponseBody
	public String delOneRow(String djbh,String saveTime) {
		String info="删除失败";
		if(djbh==null) {
			return info;
		}
		
		//验证subState，如果不等于3则删除，否则不允许删除！！！！！！！！！！！！！！！！！！！！！！！
		try {
			jdhzService.deleteBhListByDjbh(djbh);
			jdhzService.deleteBhWlListByDjbh(djbh);
			info="删除成功";
			return info;
		}catch(Exception e) {
			
		}
		return info;
	}
	
	
	@RequestMapping("/delOneRowOfDetail")
	@ResponseBody
	public String delOneRowOfDetail(String djbh) {
		String info="删除失败";
		if(djbh==null) {
			return info;
		}
		try {
			jdhzService.deleteBhListByDjbh(djbh);
			jdhzService.deleteBhWlListByDjbh(djbh);
			info="删除成功";
			return info;
		}catch(Exception e) {
			
		}
		return info;
	}
	
	
	@RequestMapping("/toSaveBhOfDetail")
	@ResponseBody
	public String toSaveBhOfDetail(String djbh,String sqsl,String shsl) {
		String info="保存失败";
		HashMap paraMap = new HashMap();
		paraMap.put("djbh", djbh);
		paraMap.put("sqsl",sqsl);
		if(shsl != null && !"".equals(shsl)) {
			paraMap.put("shsl", shsl);
		}
		try {
			baoHuoService.updateSqslByDjbh(paraMap);
			info = "保存成功";
			return info;
		}catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
		return info;
		
	}
	
	
	
	
	//生成酒店报货表
	@RequestMapping("/newJdBh")
	@ResponseBody
	public String newJdBh(HttpServletRequest request,String day,Model model) {
		System.out.println("进入newJdbh()...................");
		String info="保存失败";
		if(day == null) {
			day=Timeutil.currentTime();
		}
		CustomerCom cc = null;
		/*BhUser bhUser = (BhUser) request.getSession().getAttribute("user");
		CustomerCom cc = (CustomerCom)request.getSession().getAttribute("customer");*/
		BhUser bhUser = cacheDemoClient.getCacheDemo(request);
		cc = bhUser.getCc();
		if(bhUser == null && cc == null) {
			return "未获取到用户信息,请重新登陆";
		}
		
		
		//通过sql查询档口报货表中，org_id为本库存组织的，今天的，已经提交并且酒店未处理的，改为sub_state为3
		
		Map selectDkBhedMap = new HashMap();
		List<String> jdIds = new ArrayList<String>();
		List<String> subStates = new ArrayList<String>();
		jdIds.add(bhUser.getCompanyId());
		subStates.add("1");
		selectDkBhedMap.put("jdIds", jdIds);
		//selectDkBhedMap.put("selectDay", Timeutil.currentTime());
		selectDkBhedMap.put("subStates", subStates);
		//查询出档口的，org_id未本酒店的，substate为1(档口提交，酒店未处理)的数据，循环并保存到酒店表里面
		List<Jdhz> dkBhedDatas = jdhzService.selectJdhzList(selectDkBhedMap);
		
		//String djbh = bhUser.getCompanyId()+new Date().getTime()+StrUtil.getRandomChar(4);
		String theDj = StrUtil.getRandomChar(30);
		Integer bhCount = 0;
		for(Jdhz aData:dkBhedDatas) {
			System.out.println("插入酒店表数据循环。。。。查询出来的档口报货数据是审核数量："+aData.getShsl()+",申请数量："+aData.getSqsl());
			if(aData.getSqsl()==0){
				System.out.println("插入酒店表数据循环。。。。，申请数量为0");
				bhCount++;
				continue;
			}
			String djbh = StrUtil.getUUID();
			System.out.println("aaa:"+aData);
			Map paraMap = new HashMap<String,Object>();
			paraMap.put("id", djbh);
			System.out.println("id:"+djbh+",jdbh:"+theDj+"查询出的rog_id为本组织的，substate为1的数据："+aData);
			paraMap.put("subUser", aData.getSubUser());
			if("20".equals(bhUser.getUserType())) {
				/*String jdId = (String)value.get("jdId");
				Map selectNameMap = new HashMap();
				selectNameMap.put("id", jdId);
				String orgName = baoHuoService.selectNameByOrgId(selectNameMap);*/
				paraMap.put("jdmc",bhUser.getCompanyName());
				paraMap.put("jdId", bhUser.getCompanyId());
				paraMap.put("jdSubUserId", bhUser.getUserNumber());
				paraMap.put("isCustomer","0");
				if(bhUser.getCompanyAreaName()!=null) {
					paraMap.put("jdAreaName", bhUser.getCompanyAreaName());
				}
				
			}else if("30".equals(bhUser.getUserType())) {
				paraMap.put("jdmc", cc.getName());
				paraMap.put("jdId", cc.getId());
				paraMap.put("jdSubUserId", cc.getUserNum());
				paraMap.put("isCustomer","1");
			}
			paraMap.put("subCostCenter", aData.getSubCostCenter());
			paraMap.put("djbh", theDj);
			paraMap.put("wlbm", aData.getWlbm());
			
			paraMap.put("sqsl", aData.getSqsl());
			if(aData.getShsl() == null) {
				paraMap.put("shsl", aData.getSqsl());
			}else {
				paraMap.put("shsl", aData.getShsl());
			}
			paraMap.put("sqrq", day);
			paraMap.put("saveTime",Timeutil.currentTime2());
			paraMap.put("costSubTime", aData.getCostCenterSubTime());
			paraMap.put("mbId", aData.getMbId());
			paraMap.put("dkbhDjbh", aData.getDkBhDjbh());
			paraMap.put("jdSubUser", bhUser.getName());
			//0代表新生成
			String isZiCai = aData.getIsZiCai();
			System.out.println("往酒店表添加数据是获取到的isZiCai:"+isZiCai);
			if("1".equals(isZiCai)) {
				paraMap.put("jdSubState", "04");
			}else {
				paraMap.put("jdSubState", "0");
			}
			try {
				jdhzService.saveBhHotel(paraMap);
				jdhzService.saveBhHotelList(paraMap);
				Map setStateMap = new HashMap();
				setStateMap.put("djbh", aData.getDkBhDjbh());
				setStateMap.put("state", 3);
				jdhzService.updateDkBhSubState(setStateMap);
				setStateMap.put("sqsl", aData.getSqsl());
				if(aData.getShsl() != null) {
					paraMap.put("shsl", aData.getShsl());
				}
				baoHuoService.updateSqslByDjbh(setStateMap);
				bhCount++;
			}catch(Exception e) {
				System.out.println(e.getMessage());
				info="系统异常";
				return info;
			}
		}
		
		if(bhCount == dkBhedDatas.size()) {
			info="保存成功";
			
			
		//保存好以后，查询出酒店表中的substate为0的总结果并提交：	
			
			Map selectJdhzResultMap = new HashMap();
			selectJdhzResultMap.put("jdId", bhUser.getCompanyId());
			selectJdhzResultMap.put("selectDay", Timeutil.currentTime());
			selectJdhzResultMap.put("subState","0");
			List<Jdhz> JdNewDatas = jdhzService.selectJdbhList(selectJdhzResultMap);
			
			for(Jdhz resultData:JdNewDatas) {
				System.out.println("jdid:"+bhUser.getCompanyId()+",selectDay:"+selectJdhzResultMap.get("selectDay")+",substate:0"+"酒店生成报货单后的结果：查询出酒店表中的substate为0的总结果"+resultData);
			}
			
			
			
			
			Integer bhSubCount = 0;
			for(Jdhz resultData:JdNewDatas) {
				Map paraMap = new HashMap<String,Object>();
				paraMap.put("wlbm", resultData.getWlbm());
				paraMap.put("day", day);
				paraMap.put("mbId", resultData.getMbId());
				paraMap.put("jdId", bhUser.getCompanyId());
				paraMap.put("oldSubState", "0");
				try {
				List<String> tableIds = jdhzService.selectIdsByJdAndMbAndDayAndWl(paraMap);
				for(String str:tableIds) {
					System.out.println("更新酒店表状态：.....tableIds....str:"+str);
				}
				Map updateStateMap = new HashMap();
				updateStateMap.put("id", tableIds);
				updateStateMap.put("subState", "1");
					jdhzService.updateJdbhSubStateByTableId(updateStateMap);
					bhSubCount++;
				}catch(Exception e) {
					System.out.println("更改jdbhSubState的状态错误："+e.getMessage());
					info="系统异常";
					return info;
				}
			}
			if(bhSubCount == JdNewDatas.size()) {
				
				//提交成功后写入集团数据表
				
				
				
				Integer saveJtResult = 0;
				//查看该酒店该模板该物料时候有申请数，如果有数则添加，如果没有数则创建新数据：
				String subTime = Timeutil.currentTime2();
				
				
				for(Jdhz resultData:JdNewDatas) {
				
				System.out.println("写入集团表循环中。。。。resultData:"+resultData);
				String isZiCai = resultData.getIsZiCai();
				//如果是自采，跳过
				if(isZiCai!=null && "1".equals(isZiCai)) {
					saveJtResult++;
					continue;
				}
				
				if(resultData.getShsl()!=null && resultData.getShsl()==0) {
					System.out.println("写入集团表循环中。。。。审核数量为0");
					saveJtResult++;
					continue;
				}
				
				Map selectIfHavMap = new HashMap();
				selectIfHavMap.put("jdId", bhUser.getCompanyId());
				selectIfHavMap.put("mbId",resultData.getMbId());
				selectIfHavMap.put("day", day);
				//selectIfHavMap.put("subTime", subTime);
				selectIfHavMap.put("subState", "0");
				String tableHeadId = jdhzService.selectJtHaveByJdIdAndMbAndDay(selectIfHavMap);
				System.out.println("根据酒店id和模板，搜索到的tableHeadID："+tableHeadId);
				if(tableHeadId == null) {
					//说明数据库中没有值，则直接创建表头和表体：
					Jdhz jdhzJtResult = new Jdhz();
					jdhzJtResult.setJdId(bhUser.getCompanyId());
					//jdhzJtResult.setJdAreaName(resultData.getJdAreaName());
					jdhzJtResult.setMbId(resultData.getMbId());
					jdhzJtResult.setSaveTime(Timeutil.currentTime());
					jdhzJtResult.setSubTime(Timeutil.currentTime2());
					jdhzJtResult.setSubUserId(bhUser.getPmId());
					if(resultData.getSqsl()!=null) {
						jdhzJtResult.setSqsl(resultData.getSqsl());
					}
					if(resultData.getShsl()!=null) {
						jdhzJtResult.setShsl(resultData.getShsl());
						if(resultData.getShsl().equals("0")) {
							saveJtResult++;
							continue;
						}
					}
					if("20".equals(bhUser.getUserType())) {
						jdhzJtResult.setIsCustomer("0");
					}else if("30".equals(bhUser.getUserType())) {
						jdhzJtResult.setIsCustomer("1");
					}
					//jdhzJtResult.setSubState("0");
					/*Map selectNameMap = new HashMap();
					selectNameMap.put("id", bhUser.getCompanyId());
					String orgName = baoHuoService.selectNameByOrgId(selectNameMap);*/
					jdhzJtResult.setSubCostCenter(bhUser.getCompanyName());
					jdhzJtResult.setSqrq(Timeutil.currentTime());
					jdhzJtResult.setWlbm(resultData.getWlbm());	
					jdhzJtResult.setSubState("0");
					jdhzJtResult.setId(StrUtil.getUUID());
					jdhzJtResult.setTableEntryId(StrUtil.getUUID());
					System.out.println("数据库中没有值的情况：jdhzJtResult:"+jdhzJtResult);
					Integer saveResultHead = jiTuanHuiZongService.saveJtResultNotNull(jdhzJtResult);
					Integer saveResultEntry = jiTuanHuiZongService.saveJtResultEntryNotNull(jdhzJtResult);
					saveJtResult++;
				}else {
					//说明该酒店该模板有数据，查出id，然后根据物料ID查询是否有该物料的数据
					Map selectJtHzInfoMap = new HashMap();
					selectJtHzInfoMap.put("wlbm", resultData.getWlbm());
					selectJtHzInfoMap.put("id", tableHeadId);
					Set keys = selectJtHzInfoMap.keySet();
					for(Object o:keys) {
						System.out.println("..."+selectJtHzInfoMap.get(o));
					}
					Jdhz jtHz = jdhzService.selectJtWlCountByJdIdAndMbIdAndDay(selectJtHzInfoMap);
					System.out.println("该集团有模板？》jtHz:"+jtHz);
					if(jtHz == null) {
						//说明有表头，没有表体，新插入表体
						Jdhz resultEntry = new Jdhz();
						resultEntry.setId(tableHeadId);
						resultEntry.setTableEntryId(StrUtil.getUUID());
						resultEntry.setWlbm(resultData.getWlbm());
						resultEntry.setSqsl(resultData.getSqsl());
						if(resultData.getShsl()!=null) {
							resultEntry.setShsl(resultData.getShsl());
						}
						
						Integer saveResultEntry = jiTuanHuiZongService.saveJtResultEntryNotNull(resultEntry);
						saveJtResult++;
						
					}else {
						//说明有该物料，直接加数
						BigDecimal oldCount = new BigDecimal(jtHz.getSqsl());
						BigDecimal more = new BigDecimal(resultData.getSqsl());
						BigDecimal newSqsl = oldCount.add(more);
						BigDecimal newShsl = new BigDecimal("0");
						if(jtHz.getShsl()!= null) {
							BigDecimal oldShsl = new BigDecimal(jtHz.getShsl());
							BigDecimal moreShsl = new BigDecimal(resultData.getShsl());
							newShsl = oldShsl.add(moreShsl);
							
						}
						
						Jdhz updateJtHz = new Jdhz();
						updateJtHz.setId(jtHz.getId());
						updateJtHz.setSqsl(newSqsl.intValue());
						if(newShsl.ZERO.compareTo(newShsl) != 0) {
							updateJtHz.setShsl(newShsl.intValue());
						}
						jiTuanHuiZongService.updateJtResultEntryNotNullById(updateJtHz);
						saveJtResult++;	
						
					}
				}
				
				}
				
				if(saveJtResult == JdNewDatas.size()) {
					info="汇总并提交成功";
					return info;
				}
			}else {
				System.out.println("更改jdbhSubState的状态数量不对");
			}
		}else {
			info = "保存失败，请稍后再试！";
			System.out.println("保存的时候，bhCount!=dkBhedDatas.size()");
			return info;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
		Integer bhCount = 0;
		
		
		List<String> costNameList = new ArrayList<String>();
		List<String> mbIds = new ArrayList<String>();
		String djbh = bhUser.getCompanyId()+new Date().getTime();
		for(Map value:values) {
			
			Map paraMap = new HashMap<String,Object>();
			paraMap.put("id", new Date().getTime());
			paraMap.put("subUser", value.get("subUser"));
			if("20".equals(bhUser.getUserType())) {
				String jdId = (String)value.get("jdId");
				Map selectNameMap = new HashMap();
				selectNameMap.put("id", jdId);
				String orgName = baoHuoService.selectNameByOrgId(selectNameMap);
				paraMap.put("jdmc",bhUser.getCompanyName());
				paraMap.put("jdId", bhUser.getCompanyId());
				paraMap.put("jdSubUserId", bhUser.getUserNumber());
				paraMap.put("isCustomer","0");
			}else if("30".equals(bhUser.getUserType())) {
				paraMap.put("jdmc", cc.getName());
				paraMap.put("jdId", cc.getId());
				paraMap.put("jdSubUserId", cc.getUserNum());
				paraMap.put("isCustomer","1");
			}
			paraMap.put("subCostCenter", value.get("subCostCenter"));
			paraMap.put("djbh", djbh.toString());
			paraMap.put("wlbm", value.get("wlbm"));
			paraMap.put("sqsl", value.get("sqsl"));
			paraMap.put("shsl", value.get("shsl"));
			paraMap.put("sqrq", day);
			paraMap.put("saveTime",Timeutil.currentTime2());
			paraMap.put("costSubTime", value.get("dkSubTime"));
			paraMap.put("mbId", value.get("mbId"));
			paraMap.put("dkbhDjbh", value.get("dkBhDjbh"));
			paraMap.put("jdSubUser", bhUser.getName());
			paraMap.put("jdSubState", "0");//0代表新生成
			try {
				jdhzService.saveBhHotel(paraMap);
				jdhzService.saveBhHotelList(paraMap);
				Map setStateMap = new HashMap();
				setStateMap.put("djbh", value.get("dkBhDjbh"));
				setStateMap.put("state", 3);
				jdhzService.updateDkBhSubState(setStateMap);
				setStateMap.put("sqsl", value.get("sqsl"));
				setStateMap.put("shsl", value.get("shsl"));
				baoHuoService.updateSqslByDjbh(setStateMap);
				bhCount++;
			}catch(Exception e) {
				System.out.println(e.getMessage());
				info="系统异常";
				return info;
			}
		}
		if(bhCount == values.size()) {
			info="保存成功";
		}*/
		return info;
	}
	
	
	
	//查看并编辑酒店报货单
	@RequestMapping("/showJdBh")
	public String showJdBh(String day,Model model,HttpServletRequest request,String costCenterName,String mbId) {
		/*BhUser bhUser = (BhUser) request.getSession().getAttribute("user");
		CustomerCom cc = (CustomerCom)request.getSession().getAttribute("customer");*/
		CustomerCom cc = null;
		BhUser bhUser = cacheDemoClient.getCacheDemo(request);
		cc = bhUser.getCc();
		Map paraMap = new HashMap<String,Object>();
		if(day==null) {
			day = Timeutil.currentTime();
		}
		if(bhUser!= null) {
			if("20".equals(bhUser.getUserType())) {
				paraMap.put("jdId", bhUser.getCompanyId());
			}else if("30".equals(bhUser.getUserType())){
				paraMap.put("jdId", cc.getId());
			}
		}
		if(bhUser == null && cc == null){
			String errorInfo = "获取登陆信息异常，请重新登陆";
			model.addAttribute("errInfo",errorInfo);
			return "error";
		}
		
		
		
		/*if("20".equals(bhUser.getUserType())) {
			//通过用户名获取所管理的仓库组织
			Map selectJdMap = new HashMap();
			selectJdMap.put("userName",bhUser.getName());
			List<Company> jds = jdhzService.selectControlCompanysByUsername(selectJdMap);
			if(jds.size()==0) {
				model.addAttribute("errInfo","没有权限");
				return "error";
			}
			for(Company com:jds) {
				System.out.println("说管辖的酒店："+com.getCompanyName());
			}
			//通过传过来的参数获取当前仓库对象
			Company selectedJd = new Company();
			if(jdId == null) {
				if(jds.size() != 0) {
					jdId = jds.get(0).getId();
					selectedJd = jds.get(0);
				}
			}else {
				for(int i = 0;i<jds.size();i++) {
					if(jdId.equals(jds.get(i).getId())){
						selectedJd = jds.get(i);
					}
				}
			}
			
			model.addAttribute("jdList",jds);
			model.addAttribute("jdId",jdId);
			model.addAttribute("jdName",selectedJd.getCompanyName());
		}*/
		
		
		
		
		
		
		
		List<BhCostCenter> costCenters = new ArrayList<BhCostCenter>();
		List<String> costCenterNames = new ArrayList<String>();
		Map selectCostCentersMap = new HashMap();
		selectCostCentersMap.put("today",day);
		selectCostCentersMap.put("jdId", bhUser.getCompanyId());
		Set abc = selectCostCentersMap.keySet();
		for(Object o:abc) {
			System.out.println("key:"+o+",value:"+selectCostCentersMap.get(o));
		}
		if("20".equals(bhUser.getUserType())) {
			costCenters = jdhzService.selectCostCenterIdsBhedToday(selectCostCentersMap);
		}else if("30".equals(bhUser.getUserType())) {
			BhCostCenter bac = new BhCostCenter();
			bac.setCenterName(cc.getName());
			bac.setId(cc.getId());
			costCenters.add(bac);
		}
		for(BhCostCenter bcc : costCenters) {
			System.out.println("bcc:"+bcc);
			costCenterNames.add(bcc.getCenterName());
			System.out.println("处理完档口名称："+bcc.getCenterName());
		}
		
		
		if(costCenterName != null && !"0".equals(costCenterName)) {
			costCenterNames.clear();
			costCenterNames.add(costCenterName);
		}
		paraMap.put("selectDay", day);
		//paraMap.put("costCenterNames", costCenterNames);
		if(mbId != null && !"0".equals(mbId)) {
			//根据模板id获取物料id
			List<String> wlbms = new ArrayList<String>();
			Map param = new HashMap();
			param.put("mbId", mbId);
			List<WL_list> wlList = baoHuoService.selectWlOnlyByMbId(param);
			System.out.println("wlList.size:"+wlList.size());
			if(wlList.size() != 0) {
				for(WL_list wl:wlList) {
					wlbms.add(wl.getWlbm());
				}
				paraMap.put("wlbms", wlbms);
			}else {
				wlbms.add("zhegemobanxiamianmeiyouwuliao");
				paraMap.put("wlbms", wlbms);
			}
			
		}
		
		String today = Timeutil.currentTime();
		Map selectMbMap = new HashMap();
		//List<Mblx> mbList = baoHuoService.selectMbs();
		selectMbMap.put("jdId", bhUser.getCompanyId());
		selectMbMap.put("sqrq", today);
		selectMbMap.put("subState", "1");
		List<Mblx> mbList = jdhzService.selectBhedMbsByJdId(selectMbMap);
		
		
		paraMap.put("selectDay", day);
		//paraMap.put("jdId", jdId);
		if(mbId != null) {
			paraMap.put("mbId", mbId);
		}
		//查询出今天报过货的模板：
		
		Set keys = paraMap.keySet();
		for(Object key:keys) {
			System.out.println("#####jdbhList.....key:"+key+"value:"+paraMap.get(key));
		}
		List<Jdhz> jdBhs = jdhzService.selectJdbhList(paraMap);
		//model.addAttribute("mbList",mbList);
		model.addAttribute("user", bhUser);
		model.addAttribute("jdBhListSize",jdBhs.size());
		model.addAttribute("day", day);
		model.addAttribute("jdBhList",jdBhs);
		model.addAttribute("username",bhUser.getName());
		return "jdBhList";
	}
	
	
	//删除酒店报货的某一条
	@RequestMapping("/delJdBhOfOne")
	@ResponseBody     
	public String delJdBhOfOne(String mbId,String day,String wlbm,HttpServletRequest request,String jdId) {
		String info="删除失败";
		
		//BhUser bhUser = (BhUser)request.getSession().getAttribute("user");
		BhUser bhUser = cacheDemoClient.getCacheDemo(request);
		if(bhUser == null) {
			info = "登录过期，请重新登录";
			return info;
		}
		
		Map selectIdMap = new HashMap<String,Object>();
		selectIdMap.put("wlbm", wlbm);
		selectIdMap.put("day", day);
		selectIdMap.put("mbId", mbId);
		selectIdMap.put("jdId", jdId);
		try {
		List<String> tableIds = jdhzService.selectIdsByJdAndMbAndDayAndWl(selectIdMap);
			Map deletejdBhMap = new HashMap();
			deletejdBhMap.put("id", tableIds);
			jdhzService.deleteJdBhListById(deletejdBhMap);
			jdhzService.deleteJdBhWlListById(deletejdBhMap);
			info = "已删除";
			return info;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return info;
	}
	
	
	@RequestMapping("/toSubJdBh")
	@ResponseBody
	public String toSubJdBh(HttpServletRequest request,String day) {
		String info="提交失败";
		if(day == null) {
			day = Timeutil.currentTime();
		}
		List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
		//BhUser bhUser = (BhUser)request.getSession().getAttribute("user");
		BhUser bhUser = cacheDemoClient.getCacheDemo(request);
		if(bhUser == null) {
			info = "登录超时，请重新登录";
			return info;
		}
		Integer bhCount = 0;
		for(Map value:values) {
			Map paraMap = new HashMap<String,Object>();
			paraMap.put("wlbm", value.get("wlbm"));
			paraMap.put("day", day);
			paraMap.put("mbId", value.get("mbId"));
			paraMap.put("jdId", bhUser.getCompanyId());
			try {
			List<String> tableIds = jdhzService.selectIdsByJdAndMbAndDayAndWl(paraMap);
			for(String str:tableIds) {
				System.out.println("updatJdbhSubStateByTableId.....tableIds....str:"+str);
			}
			Map updateStateMap = new HashMap();
			updateStateMap.put("id", tableIds);
			updateStateMap.put("subState", "1");
				jdhzService.updateJdbhSubStateByTableId(updateStateMap);
				bhCount++;
			}catch(Exception e) {
				System.out.println("jdhzService.updateJdbhSubStateByTableId"+e.getMessage());
				info="系统异常";
				return info;
			}
		}
		if(bhCount == values.size()) {
			
			//提交成功后写入集团数据表
			
			
			
			Integer saveJtResult = 0;
			//查看该酒店该模板该物料时候有申请数，如果有数则添加，如果没有数则创建新数据：
			String subTime = Timeutil.currentTime2();
			for(Map value:values) {
				
			
			Map selectIfHavMap = new HashMap();
			selectIfHavMap.put("jdId", bhUser.getCompanyId());
			selectIfHavMap.put("mbId",value.get("mbId"));
			selectIfHavMap.put("day", day);
			selectIfHavMap.put("subTime", subTime);
			String tableHeadId = jdhzService.selectJtHaveByJdIdAndMbAndDay(selectIfHavMap);
			System.out.println("根据酒店id和模板，日期搜索到的tableHeadID："+tableHeadId);
			if(tableHeadId == null) {
				//说明数据库中没有值，则直接创建表头和表体：
				Jdhz jdhzJtResult = new Jdhz();
				jdhzJtResult.setJdId(bhUser.getCompanyId());
				jdhzJtResult.setMbId((String)value.get("mbId"));
				jdhzJtResult.setSaveTime(Timeutil.currentTime());
				jdhzJtResult.setSubTime(Timeutil.currentTime2());
				jdhzJtResult.setSubUserId(bhUser.getPmId());
				if(value.get("shsl")!=null) {
					jdhzJtResult.setShsl(Integer.valueOf((String)value.get("shsl")));
				}
				if(value.get("sqsl")!=null) {
					jdhzJtResult.setSqsl(Integer.valueOf((String)value.get("sqsl")));
				}
				if("20".equals(bhUser.getUserType())) {
					jdhzJtResult.setIsCustomer("0");
				}else if("30".equals(bhUser.getUserType())) {
					jdhzJtResult.setIsCustomer("1");
				}
				jdhzJtResult.setSubState("0");
				/*Map selectNameMap = new HashMap();
				selectNameMap.put("id", bhUser.getCompanyId());
				String orgName = baoHuoService.selectNameByOrgId(selectNameMap);*/
				jdhzJtResult.setSubCostCenter(bhUser.getCompanyName());
				jdhzJtResult.setSqrq((String)value.get("sqrq"));
				jdhzJtResult.setWlbm((String)value.get("wlbm"));	
				jdhzJtResult.setSubState("0");
				System.out.println("数据库中没有值：jdhzJtResult:"+jdhzJtResult);
				Integer saveResultHead = jiTuanHuiZongService.saveJtResultNotNull(jdhzJtResult);
				Integer saveResultEntry = jiTuanHuiZongService.saveJtResultEntryNotNull(jdhzJtResult);
				saveJtResult++;
			}else {
				//说明该酒店该模板有数据，查出id，然后根据物料ID查询是否有该物料的数据
				Map selectJtHzInfoMap = new HashMap();
				selectJtHzInfoMap.put("wlbm", value.get("wlbm"));
				selectJtHzInfoMap.put("id", tableHeadId);
				Set keys = selectJtHzInfoMap.keySet();
				for(Object o:keys) {
					System.out.println("..."+selectJtHzInfoMap.get(o));
				}
				Jdhz jtHz = jdhzService.selectJtWlCountByJdIdAndMbIdAndDay(selectJtHzInfoMap);
				System.out.println("该集团有模板？》jtHz:"+jtHz);
				if(jtHz == null) {
					//说明有表头，没有表体，新插入表体
					Jdhz resultEntry = new Jdhz();
					resultEntry.setId(tableHeadId);
					resultEntry.setWlbm((String)value.get("wlbm"));
					resultEntry.setSqsl(Integer.valueOf((String)value.get("sqsl")));
					if(value.get("shsl")!=null) {
						resultEntry.setShsl(Integer.valueOf((String)value.get("shsl")));
					}
					
					Integer saveResultEntry = jiTuanHuiZongService.saveJtResultEntryNotNull(resultEntry);
					saveJtResult++;
					
				}else {
					//说明有该物料，直接加数
					BigDecimal oldCount = new BigDecimal(jtHz.getSqsl());
					BigDecimal more = new BigDecimal((String)value.get("sqsl"));
					BigDecimal newSqsl = oldCount.add(more);
					BigDecimal newShsl = new BigDecimal("0");
					if(jtHz.getShsl()!= null) {
						BigDecimal oldShsl = new BigDecimal(jtHz.getShsl());
						BigDecimal moreShsl = new BigDecimal((String)value.get("shsl"));
						newShsl = oldShsl.add(moreShsl);
						
					}
					
					Jdhz updateJtHz = new Jdhz();
					updateJtHz.setId(jtHz.getId());
					updateJtHz.setSqsl(newSqsl.intValue());
					if(newShsl.ZERO.compareTo(newShsl) != 0) {
						updateJtHz.setShsl(newShsl.intValue());
					}
					jiTuanHuiZongService.updateJtResultEntryNotNullById(updateJtHz);
					saveJtResult++;	
					
				}
			}
			
			}
			
			if(saveJtResult == values.size()) {
				info="提交成功";
				return info;
			}
			
			
			
			
			
			
			
			
			
			
		}
		return info;
	}
	
	
	@RequestMapping("/updateSqslAndShslByDjbh")
	@ResponseBody
	public String updateSqslAndShslByDjbh(HttpServletRequest request) {
		String info="修改失败";
		List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
		Integer bhCount = 0;
		for(Map value:values) {
			Map paraMap = new HashMap<String,Object>();
			System.out.println("sqsl:"+value.get("sqsl")+"shsl:"+value.get("shsl")+",djbh:"+value.get("dkBhDjbh"));
			paraMap.put("djbh", value.get("dkBhDjbh"));
			paraMap.put("sqsl", value.get("sqsl"));
			paraMap.put("shsl", value.get("shsl"));
			try {
				baoHuoService.updateSqslByDjbh(paraMap);
				bhCount++;
			}catch(Exception e) {
				System.out.println("baoHuoService.updateSqslByDjbh"+e.getMessage());
				info="系统异常";
				return info;
			}
		}
		if(bhCount == values.size()) {
			info="修改成功";
		}
		return info;
	}
	
	
	//
	@RequestMapping("/toBackSub")
	@ResponseBody
	public String toBackSub(String day,HttpServletRequest request) {
		String info = "撤回失败";
		BhUser bhUser = cacheDemoClient.getCacheDemo(request);
		//BhUser bhUser = (BhUser)request.getSession().getAttribute("user");
		if(bhUser == null) {
			info = "登录超时，请重新登录";
			return info;
		}
		System.out.println("参数day:"+day);
		String today = Timeutil.currentTime();
		if(!day.equals(today)) {
			info = "只能对今天的数据进行撤回！";
			return info;
		}
		//查看今天的数据是否有被上级处理的（state不为1的）
		Map selectJdBhStateMap = new HashMap();
		selectJdBhStateMap.put("day", today);
		selectJdBhStateMap.put("jdId", bhUser.getCompanyId());
		selectJdBhStateMap.put("subState", "1");
		Integer subStateNot1Count = jdhzService.selectJdBhSubStateNot1(selectJdBhStateMap);
		
		
		//查询集团表的关于本酒店本操作人今天提交的数据的sub_state是不是0，不是0不允许撤回
		selectJdBhStateMap.put("subUserId", bhUser.getPmId());
		selectJdBhStateMap.put("jtSubState","0");
		Integer haveNot0 = jdhzService.selectJtSubStateNot0(selectJdBhStateMap);
		
		if(subStateNot1Count != 0 || haveNot0 != 0) {
			info = "数据已被处理，无法撤回！";
			return info;
		}
		
		//删除上级表数据：
		List<String> tableHeadIds = jdhzService.selectTableHeadIdsByJdAndUserAndDay(selectJdBhStateMap);
		if(tableHeadIds.size()!=0) {
			//删除表头及表体
			Map deleteMap = new HashMap();
			deleteMap.put("ids", tableHeadIds);
			try {
				jiTuanHuiZongService.deleteJtTableHeadById(deleteMap);
				jiTuanHuiZongService.deleteJtTableEntryByParentId(deleteMap);
			}catch(Exception e) {
				System.out.println("删除集团数据时出错："+e.getMessage());
				info = "系统异常，请稍后重试";
				return info;
			}
			
		}
		
		//List<String> tableHeadIds = 
		
		
		
		
		
		//如果没有被处理，那么查询出所有的ID，删除两张表中的数据
		Map selectIdsMap = new HashMap();
		selectIdsMap.put("jdId", bhUser.getCompanyId());
		selectIdsMap.put("day", today);
		selectIdsMap.put("subState", "1");
		List<String> tableIds = jdhzService.selectIdByJdAndDay(selectIdsMap);
		
		//删除：
		if(tableIds.size() != 0) {
			Map deleteBhHotel = new HashMap();
			deleteBhHotel.put("id", tableIds);
			try {
				jdhzService.deleteJdBhListById(deleteBhHotel);
				jdhzService.deleteJdBhWlListById(deleteBhHotel);
			}catch(Exception e) {
				//System.out.println("删除酒店汇总表错误："+e.getMessage());
				info = "系统异常，请稍后重试！";
				return info;
				
			}
			
		}
		
		//更改档口表的sub_state 的 3 为 1
		Map updateDkBhMap = new HashMap();
		updateDkBhMap.put("jdId", bhUser.getCompanyId());
		updateDkBhMap.put("oldSubState", "3");
		updateDkBhMap.put("newSubState", "1");
		//updateDkBhMap.put("day", today);
		try {
			baoHuoService.updateSubState3to1(updateDkBhMap);
			info="撤回成功";
		}catch(Exception e) {
			System.out.println("酒店全部撤回时修改档口表状态出错："+e.getMessage());
		}
		
		
		return info;
	}
	
	
	
	
	@RequestMapping("/toSaveJdbh")
	@ResponseBody
	public String toSaveJdbh(HttpServletRequest request,String day) {
		String info="保存失败";
		List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
		Integer bhCount = 0;
		for(Map value:values) {
			Map paraMap = new HashMap<String,Object>();
			paraMap.put("djbh", value.get("djbh"));
			paraMap.put("wlbm", value.get("wlbm"));
			paraMap.put("sqsl", value.get("sqsl"));
			paraMap.put("shsl", value.get("shsl"));
			paraMap.put("day", day);
			paraMap.put("saveTime", value.get("saveTime"));
			try {
				jdhzService.updateJdbhByDjbhAndWlbm(paraMap);
				bhCount++;
			}catch(Exception e) {
				System.out.println("jdhzService.updateJdbhByDjbhAndWlbm"+e.getMessage());
				info="系统异常";
				return info;
			}
		}
		if(bhCount == values.size()) {
			info="保存成功";
		}
		return info;
	}
	
	
}
