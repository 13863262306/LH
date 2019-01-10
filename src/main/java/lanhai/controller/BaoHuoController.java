package lanhai.controller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import lanhai.entity.BhUser;
import lanhai.entity.Company;
import lanhai.entity.CustomerCom;
import lanhai.entity.Jdhz;
import lanhai.entity.Mblx;
import lanhai.entity.WL_list;
import lanhai.service.BaoHuoService;
import lanhai.serviceImpl.CacheDemoClient;
import utils.StrUtil;
import utils.Timeutil;

/**
 * 
 * @author liuyang
 *
 */
@Controller
@RequestMapping("/bh")
public class BaoHuoController {

	@Resource
	private BaoHuoService baoHuoService;
	@Resource
	private CacheDemoClient cacheDemoClient;
	@RequestMapping("/showBh")
	public String showBh(Model model,String mbId,String username,String password,HttpServletRequest request,HttpServletResponse response) {
		CustomerCom cc = null;
		String bhkey="0";
		//根据申请人id查询申请人信息
		System.out.println("userName:"+username+",password:"+password);
		BhUser bhUser = null;
		if(username==null) {
			
			/*bhUser = (BhUser)request.getSession().getAttribute("user");
			cc=(CustomerCom)request.getSession().getAttribute("customer");*/
			bhUser=cacheDemoClient.getCacheDemo(request);
			cc = bhUser.getCc();
			System.out.println("从缓存中获取到的bhuser："+bhUser+",,cc:"+cc);
			if(bhUser == null && cc == null) {
				model.addAttribute("errInfo","未获取到用户信息,请重新登陆");
				return "error";
			}
		}else {
			Map selectUserMap = new HashMap();
			selectUserMap.put("userName",username);
			bhUser = baoHuoService.selectUserByFNumber(selectUserMap);
			System.out.println("%%%%%%bhUser:"+bhUser);
			if(bhUser == null) {
				String errInfo = "用户不存在！";
				model.addAttribute("errInfo",errInfo);
				return "error";
			}
			Map selectCustMap = new HashMap();
			selectCustMap.put("userNum", username);
			cc = baoHuoService.selectCustInfoByNum(selectCustMap);
			bhUser.setCc(cc);
			System.out.println("从数据库中获取的cc:"+cc);
			System.out.println("bhUser:"+bhUser);
			System.out.println("cc:"+cc);
			
		}
		
		//判断类型
		//如果是内部组织,
		String type = bhUser.getUserType();
		if("20".equals(type)) {
			
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
				
			}
			
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
			}*/
		}else if("30".equals(type)) {
			//外部
			Map selectCustMap = new HashMap();
			selectCustMap.put("userNum", cc.getUserNum());
			cc = baoHuoService.selectCustInfoByNum(selectCustMap);
			bhUser.setCc(cc);
			System.out.println("cc:"+cc);
			bhUser.setName(cc.getUserNum());
			bhUser.setCostCenterName(cc.getName());
			bhUser.setCompanyName(cc.getName());
			bhUser.setCostCenterId(cc.getId());
			bhUser.setCompanyId(cc.getId());
			//cacheDemoClient.setCacheDemo(request,response, bhUser);
			/*request.getSession().setAttribute("customer", cc);
			request.getSession().setAttribute("user", bhUser);*/
		}else {
			model.addAttribute("errInfo","登录人类型不正确");
			return "error";
		}
		System.out.println("bhUser:......."+bhUser);
		String today = Timeutil.currentDateTime();
		String year = today.substring(0, 4);
		String month = today.substring(5, 7);
		String lastMonth = "";
		String sameMonthOfLastYear = (Integer.valueOf(year)-1)+"-"+month;
		if("01".equals(month) || "1-".equals(month)) {
			Integer lastYear =  Integer.valueOf(year)-1;
			lastMonth = lastYear+"-12";
		}else {
			lastMonth = year +"-"+ (Integer.valueOf(month)-1);
		}
		
		
		
		Map selectMbMap = new HashMap();
		//selectMbMap.put("orgId", bhUser.getCostCenterId());
		List<Mblx> mbList = baoHuoService.selectMbs();
		
		 /*Collections.sort(mbList, new Comparator<Mblx>() {
	            public int compare(Mblx arg0, Mblx arg1) {
	                int hits0 = Integer.valueOf(arg0.getMbNumber());
	                int hits1 = Integer.valueOf(arg1.getMbNumber());
	                if (hits1 > hits0) {
	                    return 1;
	                } else if (hits1 == hits0) {
	                    return 0;
	                } else {
	                    return -1;
	                }
	            }
	        });*/
	        
		//根据档口ID查询所属酒店的id
		/*String jdId = "";
		if("20".equals(bhUser.getUserType())) {
			Map selectJdMap = new HashMap();
			selectJdMap.put("costCenterId", bhUser.getCostCenterId());
			jdId = baoHuoService.selectOrgIdByCostCenterId(selectJdMap);
			System.out.println("所属酒店ID："+jdId);
		}else if("30".equals(bhUser.getUserType())) {
			jdId = cc.getId();
			System.out.println("所属酒店ID："+jdId);
		}*/
		
		
		List<String> mbIds = new ArrayList<String>();
		if(mbId == null || "0".equals(mbId)) {
			if(mbList.size() != 0) {
				mbId = mbList.get(0).getId();
				mbIds.add(mbId);
			}
		}else {
			mbIds.add(mbId);
		}
		
		//获取年月
		//String yearAndMonth = today.substring(0,today.lastIndexOf("-"));
		
		
		//根据模板id，酒店id，当前月份查询当月可以报货的日期
		Map checkDayMap = new HashMap();
		if("20".equals(bhUser.getUserType())) {
			checkDayMap.put("jdId",bhUser.getCompanyId());
			System.out.println("...20...jdId:"+bhUser.getCompanyId());
		}else if("30".equals(bhUser.getUserType())) {
			checkDayMap.put("customId",cc.getId());
			System.out.println("...30...customId:"+cc.getId());
		}
		
		
		checkDayMap.put("day", Timeutil.getTomorrow(1));
		//checkDayMap.put("day", Timeutil.currentTime());
		checkDayMap.put("mbId", mbId);
		Set mapas = checkDayMap.keySet();
		for(Object o:mapas) {
			System.out.println(checkDayMap.get(o));
		}
		Set checkDaySet = checkDayMap.keySet();
		for(Object o:checkDaySet) {
			System.out.println("?????key:"+o+",,value:"+checkDayMap.get(o));
		}
		Integer bhDays = 0;
		if(mbId!=null) {
			bhDays = baoHuoService.selectIfDayOfBh(checkDayMap);
		}
		//System.out.println("bhDays.size:"+bhDays.size());
		/*for(String theDay:bhDays) {
			System.out.println("theDay:"+theDay);
		}
		String dayOfToday =  today.substring(8, 10);
		
		String withOutZeroDay = dayOfToday;*/
		if(bhDays>0) {
			bhkey="1";
		}else {
			bhkey="0";
		}
		/*if(bhDays.contains(dayOfToday) || bhDays.contains(withOutZeroDay)) {
			bhkey="1";
		}*/
		
		List<WL_list> wlList = new ArrayList<WL_list>();
		if(mbIds.size() != 0) {
			Map paraMap = new HashMap();
			paraMap.put("mbIds", mbIds);
			paraMap.put("lastMonth", lastMonth);
			paraMap.put("sameMonthOfLastYear", sameMonthOfLastYear);
			wlList = baoHuoService.selectWlByMbId(paraMap);
		}
		model.addAttribute("day", Timeutil.currentTime());
		model.addAttribute("wlList", wlList);
		model.addAttribute("mbId",mbId);
		model.addAttribute("mbList", mbList);
		model.addAttribute("BHKEY",bhkey);
		System.out.println("存入缓存之前的user："+bhUser);
		cacheDemoClient.setCacheDemo(request,response, bhUser);
		//request.getSession().setAttribute("user", bhUser);
		String inputKey = "1";
		if(mbId != null) {
			//获取inputKey
			Map selectInputKeyMap = new HashMap();
			selectInputKeyMap.put("mbId", mbId);
			inputKey = baoHuoService.selectInputKeyByMb(selectInputKeyMap);
			
				model.addAttribute("inputKey",inputKey);
		}
		
		
		//验证当前时间是否在报货时间范围内
		String nowTime = new SimpleDateFormat("HH-mm").format(new Date());
		System.out.println("当前时分："+nowTime);
		String startTime = "";
		String endTime = "";
		Map selectBhTimeMap = new HashMap();
		selectBhTimeMap.put("orgType", "1");
		Map bhTime = baoHuoService.selectBhTime(selectBhTimeMap);
		if(bhTime != null && startTime!=null && endTime!=null) {
			startTime = (String)bhTime.get("START_TIME");
			endTime = (String)bhTime.get("END_TIME");
		}
		String checkResult = "1";
		if(startTime!=null && endTime!=null && !"".equals(endTime) && !"".equals(startTime)) {
			checkResult = baoHuoService.checkTimeInScope(nowTime,startTime,endTime);
			endTime = endTime.replace("-", ":");
		}
		
		String allEndTime = Timeutil.currentTime()+" "+endTime+":00";
		System.out.println("allEndTime:"+allEndTime);
		model.addAttribute("endTime",allEndTime);
		model.addAttribute("user",bhUser);
		model.addAttribute("allInputKey",checkResult);
		System.out.println("bhKey:"+bhkey+",inputKey:"+inputKey+",allInputKey:"+checkResult);
		return "baohuo";
	}

	
	@RequestMapping("/toSubmitBh")
	@ResponseBody
	public String toSubmitBh(HttpServletRequest request,String day) {
		String info = "提交失败";
		System.out.println("day:"+day);
		if(day == null) {
			day = Timeutil.currentTime();
		}
		//BhUser bhUser = (BhUser) request.getSession().getAttribute("user");
		BhUser bhUser=cacheDemoClient.getCacheDemo(request);
		if(bhUser == null) {
			info = "未获取到用户信息,请重新登陆";
			return "info";
		}
		
		//String jdId = bhUser.getCompanyId();
		//List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
		//Integer bhCount = 0;
		//String subTime = Timeutil.currentTime2();
		/*for(Map value:values) {
			Map paraMap = new HashMap<String,Object>();
			paraMap.put("djbh", value.get("djbh"));
			paraMap.put("wlbm", value.get("wlbm"));
			paraMap.put("sqsl", value.get("sqsl"));
			paraMap.put("sqrq", day);
			paraMap.put("state", "1");
			paraMap.put("subTime", subTime);
			try {
				baoHuoService.updateSubState(paraMap);
				bhCount++;
			}catch(Exception e) {
				info="系统异常";
				return info;
			}
		}*/
		/*if(bhCount == values.size()) {
			info="已提交";
		}*/
		
		String today = Timeutil.currentTime();
		Map updateSubStateMap = new HashMap();
		updateSubStateMap.put("sqrq", today);
		updateSubStateMap.put("costCenterId", bhUser.getCostCenterId());
		updateSubStateMap.put("oldSubState", "0");
		updateSubStateMap.put("newSubState", "1");
		updateSubStateMap.put("subState", "0");
		updateSubStateMap.put("subTime", Timeutil.currentTime2());
		try {
			Integer saved = baoHuoService.selectHaveState3(updateSubStateMap);
			if(saved == 0) {
				info = "没有数据需要提交";
				return info;
			}
			baoHuoService.updateSubStateByCostCenterIdAndDay(updateSubStateMap);
			//删除该申请日期的，该档口的，substate为1的，sqsl为0的条目
			
			
			info="提交成功";
		}catch(Exception e) {
			System.out.println("档口提交时出错："+e.getMessage());
			return "系统异常，请稍后再试";
		}
		return info;
	}
	
	
	
	@RequestMapping("/toSaveBh")
	@ResponseBody
	public String toSaveBh(HttpServletRequest request,String day,Integer sub) {
		String info = "保存失败";
		
		//获取session中的user值
		//BhUser bhUser = (BhUser) request.getSession().getAttribute("user");
		BhUser bhUser=cacheDemoClient.getCacheDemo(request);
		
		if(bhUser == null) {
			info = "未获取到用户信息,请重新登陆";
			return "info";
		}
		List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
		Integer bhCount = 0;
		for(Map value:values) {
			Map paraMap = new HashMap<String,Object>();
			String djbh = StrUtil.getUUID();
			paraMap.put("djbh", djbh);
			paraMap.put("wlbm", value.get("wlbm"));
			paraMap.put("sqsl", value.get("sqsl"));
			paraMap.put("day", day);
			paraMap.put("user", bhUser.getName());
			paraMap.put("subCostCenter", bhUser.getCostCenterName());
			paraMap.put("company", bhUser.getCompanyName());
			paraMap.put("subState", 0);
			paraMap.put("mbId", value.get("mbId"));
			paraMap.put("companyId", bhUser.getCompanyId());
			paraMap.put("subCostCenterId", bhUser.getCostCenterId());
			//查看暂存中是否有该物料，有则添加数量，没有则添加条目
			
			List<Jdhz> bhInfos = baoHuoService.selectBhByDayAndWlbm(paraMap);
			System.out.println("当天该操作人保存的该物料的信息个数："+bhInfos.size());
			for(Jdhz aaa:bhInfos) {
				System.out.println("当天该操作人保存的该物料的信息"+aaa);
			}
			if(bhInfos.size() == 0) {
				try {
					baoHuoService.saveBhWl(paraMap);
					baoHuoService.saveBhList(paraMap);
					bhCount++;
				}catch(Exception e) {
					info="系统异常";
					return info;
				}
			}else {
				//原数量：
				Integer oldCount = bhInfos.get(0).getSqsl();
				String theDjbh = bhInfos.get(0).getDjbh();
				Integer newCount = oldCount+Integer.valueOf((String)value.get("sqsl"));
				System.out.println("原数量："+oldCount+",djbh:"+theDjbh+",newCount:"+newCount);
				Map paMap = new HashMap();
				paMap.put("djbh", theDjbh);
				paMap.put("sqsl", newCount);
				try {
					baoHuoService.updateSqslByDjbh(paMap);
					bhCount++;
				}catch(Exception e) {
					System.out.println(e.getMessage());
					info="系统异常";
					return info;
				}
			}
		}
		if(bhCount == values.size()) {
			info="保存成功";
			
		}
		return info;
	}
	
	
	@RequestMapping("/toSaveBhOfHistory")
	@ResponseBody
	public String toSaveBhOfHistory(HttpServletRequest request,String day) {
		String info = "修改失败";
		List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
		Integer bhCount = 0;
		for(Map value:values) {
			Map paraMap = new HashMap<String,Object>();
			paraMap.put("djbh", value.get("djbh"));
			paraMap.put("sqsl", value.get("sqsl"));
			paraMap.put("day", day);
			try {
				baoHuoService.updateSqslByDjbh(paraMap);
				bhCount++;
			}catch(Exception e) {
				info="系统异常";
				return info;
			}
		}
		if(bhCount == values.size()) {
			info="修改成功";
		}
		return info;
	}
	
	
	
	//查看暂存
	@RequestMapping("/toSeeHistoryOfSave")
	public String toSeeHistoryOfSave(String day,Model model,HttpServletRequest request,String mbId) {
		System.out.println("day:"+day+",mbId:"+mbId);
		if(day == null) {
			day=Timeutil.currentTime();
		}
		
		
		//BhUser user = (BhUser)request.getSession().getAttribute("user");
		BhUser user=cacheDemoClient.getCacheDemo(request);
		if(user == null) {
			String errInfo = "登陆过期，请重新登陆";
			return "error";
		}
		Map paraMap = new HashMap();
		//paraMap.put("day", day);
		paraMap.put("user", user.getName());
		List<String> subStates = new ArrayList<String>();
		subStates.add("0");
		paraMap.put("subStates", subStates);
		Set keys = paraMap.keySet();
		for(Object key:keys) {
			System.out.println(paraMap.get(key));
		}
		
		Map selectBhedMbMap = new HashMap();
		selectBhedMbMap.put("costCenterId", user.getCostCenterId());
		//selectBhedMbMap.put("sqrq", day);
		selectBhedMbMap.put("subState","0");
		List<Mblx> mbList = baoHuoService.selectBhedMb(selectBhedMbMap);
		
		if(mbList.size() != 0) {
			if(mbId == null) {
				mbId = mbList.get(0).getId();
			}
		}
		paraMap.put("mbId", mbId);
		
		List<Jdhz> bhhzList = baoHuoService.selectBhByDay(paraMap);
		model.addAttribute("mbId",mbId);
		model.addAttribute("mbList",mbList);
		model.addAttribute("listSize", bhhzList.size());
		model.addAttribute("day",day);
		model.addAttribute("wlList", bhhzList);
		model.addAttribute("user",user);
		return "historyBh";
	}
	
	//查看暂存
		@RequestMapping("/toSeeHistoryOfSub")
		public String toSeeHistoryOfSub(String day,Model model,HttpServletRequest request) {
			if(day == null) {
				day=Timeutil.currentTime();
			}
			//BhUser user = (BhUser)request.getSession().getAttribute("user");
			BhUser user=cacheDemoClient.getCacheDemo(request);
			if(user == null) {
				String errInfo = "登陆过期，请重新登陆";
				return "error";
			}
			Map paraMap = new HashMap();
			paraMap.put("day", day);
			paraMap.put("user", user.getName());
			List<String> subStates = new ArrayList<String>();
			subStates.add("1");
			subStates.add("3");
			paraMap.put("subStates", subStates);
			List<Jdhz> bhhzList = baoHuoService.selectBhByDay(paraMap);
			model.addAttribute("listSize", bhhzList.size());
			model.addAttribute("day",day);
			model.addAttribute("wlList", bhhzList);
			model.addAttribute("user",user);
			return "historyBhOfSub";
		}
		
		
		//全部撤回
		@RequestMapping("/toBackBh")
		@ResponseBody
		public String toBackBh(HttpServletRequest request) {
			String info = "撤回失败";
			BhUser user=cacheDemoClient.getCacheDemo(request);
			if(user == null) {
				String errInfo = "登陆过期，请重新登陆";
				return "error";
			}
			//List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
			/*Integer bhCount = 0;
			for(Map value:values) {
				String djbh = (String)value.get("djbh");
				Map paraMap = new HashMap<String,Object>();
				paraMap.put("djbh", djbh);
				paraMap.put("state", "0");
				try {
					baoHuoService.updateSubState(paraMap);
					bhCount++;
				}catch(Exception e) {
					info="系统异常";
					return info;
				}
			}*/
			/*if(bhCount == values.size()) {
				info="撤回成功";
			}*/
			//验证时候有sub_state为3的
			
			//如果没有为3的，则把所有的
			Map backMap = new HashMap();
			backMap.put("costCenterId", user.getCostCenterId());
			backMap.put("sqrq", Timeutil.currentTime());
			backMap.put("oldSubState", "1");
			backMap.put("newSubState", "0");
			backMap.put("subState", "3");
			Integer state3Count = 1;
			Integer subedCount = 1;
			try {
				 state3Count = baoHuoService.selectHaveState3(backMap);
			}catch(Exception e) {
				System.out.println("获取档口报货表中是否有状态为3的，，，报错："+e.getMessage());
				info = "系统出错，请稍后重试";
				return info;
			}
			Map selectSubedMap = new HashMap();
			selectSubedMap.put("costCenterId", user.getCostCenterId());
			selectSubedMap.put("sqrq", Timeutil.currentTime());
			selectSubedMap.put("subState", "1");
			try {
				subedCount = baoHuoService.selectHaveState3(selectSubedMap);
			}catch(Exception e) {
				System.out.println("获取是否有提交过的数据。。。报错："+e.getMessage());
				info = "系统出错，请稍后重试";
				return info;
			}
			if(state3Count!=0) {
				info="数据已被处理，无法撤回";
				return info;
			}else if(subedCount == 0) {
				info="没有数据需要撤回";
				return info;
			}else {
				baoHuoService.updateSubStateByCostCenterIdAndDay(backMap);
				
				Map deleteJtMap = new HashMap();
				deleteJtMap.put("jdId", user.getCompanyId());
				deleteJtMap.put("userName", user.getName());
				deleteJtMap.put("sqrq", Timeutil.currentTime());
				deleteJtMap.put("subState", "0");
				
				info="撤回成功";
			}
			return info;
		}
		
		
		@RequestMapping("/checkInputKey")
		@ResponseBody
		public String checkInputKey(String mbId) {
			String info="1";
			Map selectInputKeyMap = new HashMap();
			if(mbId != null) {
				selectInputKeyMap.put("mbId", mbId);
				try {
					info = baoHuoService.selectInputKeyByMb(selectInputKeyMap);
				}catch(Exception e) {
					System.out.println("checkInputKey..."+e.getMessage());
					
				}
			}
			return info;
			
		}
	
	
		@RequestMapping("/csCacheDemo")
		@ResponseBody
		public String csCacheDemo(String mbId,HttpServletRequest request,HttpServletResponse response) {
			BhUser BhUser=new BhUser();
			BhUser.setName(mbId);
			cacheDemoClient.setCacheDemo(request,response, BhUser);
			return "";
			
		}
		
		@RequestMapping("/csCacheDemoh")
		@ResponseBody
		public String csCacheDemoh(HttpServletRequest request) {
			BhUser BhUser=cacheDemoClient.getCacheDemo(request);
			return BhUser.getName();
			
		}
}
