package lanhai.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import lanhai.entity.BhCostCenter;
import lanhai.entity.BhUser;
import lanhai.entity.Jdhz;
import lanhai.entity.Mblx;
import lanhai.entity.ProducePaper;
import lanhai.entity.WL_list;
import lanhai.service.BaoHuoService;
import lanhai.service.EcologicalParkProduceService;
import lanhai.serviceImpl.CacheDemoClient;
import utils.Timeutil;

@Controller
@RequestMapping("/ecologicalProduce")
public class EcologicalParkProduceController {

	@Resource
	private BaoHuoService baoHuoService;
	@Resource
	private CacheDemoClient cacheDemoClient;
	@Resource
	private EcologicalParkProduceService ecologicalParkProduceService;
	
	@RequestMapping("/showEcoProduce")
	public String showEcologicalProduce(Model model,String mbId,String username,HttpServletRequest request,HttpServletResponse response) {
		BhUser bhUser = null;
		if(username==null) {
			//bhUser = (BhUser) request.getSession().getAttribute("user");
			bhUser = cacheDemoClient.getCacheDemo(request);
			if(bhUser == null) {
				model.addAttribute("errInfo","未获取到用户信息,请重新登陆");
				return "error";
			}
		}else {
			Map selectUserMap = new HashMap();
			selectUserMap.put("userName",username);
			bhUser = baoHuoService.selectUserByFNumber(selectUserMap);	
		}
		//通过长编码获取酒店编码
		String userLongNumber = bhUser.getLongNumber();
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
		
		
		
		
		Map selectMbMap = new HashMap();
		List<Mblx> mbList = baoHuoService.selectMbs();
		if(mbId == null) {
			if(mbList.size() != 0) {
				mbId = mbList.get(0).getId();
			}
		}
		
		//request.getSession().setAttribute("user", bhUser);
		cacheDemoClient.setCacheDemo(request, response, bhUser);
		Map paraMap = new HashMap();
		List<String> mbIds = new ArrayList<String>();
		if(mbId != null) {
			mbIds.clear();
			mbIds.add(mbId);
		}
		
		paraMap.put("mbIds", mbIds);
		
		String today = Timeutil.currentTime();
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
		paraMap.put("lastMonth", lastMonth);
		paraMap.put("sameMonthOfLastYear", sameMonthOfLastYear);
		List<WL_list> wlList = new ArrayList<WL_list>();
		if(mbIds.size() != 0) {
			wlList = baoHuoService.selectWlByMbId(paraMap);
		}
		model.addAttribute("user",bhUser);
		model.addAttribute("day", today);
		model.addAttribute("wlList", wlList);
		model.addAttribute("mbId",mbId);
		model.addAttribute("mbList", mbList);
		return "ecologicalParkProduce";
	}
	
	
	@RequestMapping("/toSaveBh")
	@ResponseBody
	public String toSaveBh(HttpServletRequest request,String day) {
		String info = "保存失败";
		//Integer djbhCount = baoHuoService.selectCountOfDjbh(djbh);
		/*if(djbhCount != 0) {
			info="该单据编号已被占用！";
			return info;
		}*/
		String dateRegex = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
		if(!day.matches(dateRegex)) {
			info="日期格式输入不正确，请重新输入";
			return info;
		}
		//BhUser bhUser = (BhUser) request.getSession().getAttribute("user");
		BhUser bhUser = cacheDemoClient.getCacheDemo(request);
		System.out.println("从session中获取的user："+bhUser);
		if(bhUser == null) {
			info = "未获取到用户信息,请重新登陆";
			return info;
		}
		List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
		Integer bhCount = 0;
		for(Map value:values) {
			Map paraMap = new HashMap<String,Object>();
			String id = bhUser.getCompanyId()+new Date().getTime();
			paraMap.put("id", id.toString());
			paraMap.put("wlbm", value.get("wlbm"));
			paraMap.put("yield", value.get("sbsl"));
			paraMap.put("day", day);
			paraMap.put("epId", bhUser.getCompanyId());
			try {
				//查看今天上报的是否已经存在
				ProducePaper pp = ecologicalParkProduceService.selectIfHaveByWlbmAndDay(paraMap);
				if(pp == null) {
					ecologicalParkProduceService.saveEcoParkProduce(paraMap);
					bhCount++;
				}else {
					Map updateMap = new HashMap<String,Object>();
					updateMap.put("djbh", pp.getId());
					BigDecimal oldCount = new BigDecimal(pp.getYield());
					BigDecimal moreCount = new BigDecimal((String)paraMap.get("yield"));
					BigDecimal newCount = oldCount.add(moreCount);
					
					updateMap.put("sqsl", newCount.toString());
					updateMap.put("epId", bhUser.getCompanyId());
						ecologicalParkProduceService.updateSbslByDjbh(updateMap);
						bhCount++;
				}
				
				
				
			}catch(Exception e) {
				System.out.println(e.getMessage());
				info="系统异常";
				return info;
			}
		}
		if(bhCount == values.size()) {
			info="保存成功";
		}
		return info;
	}
	
	@RequestMapping("/toSeeTbList")
	public String toSeeTbHistory(String day,Model model,HttpServletRequest request) {
		if(day == null) {
			day=Timeutil.currentTime();
		}
		//BhUser bhUser = (BhUser) request.getSession().getAttribute("user");
		BhUser bhUser = cacheDemoClient.getCacheDemo(request);
		if(bhUser == null) {
			String errinfo = "未获取到用户信息,请重新登陆";
			model.addAttribute("errInfo",errinfo);
			return "error";
		}
		Map paraMap = new HashMap();
		paraMap.put("day", day);
		paraMap.put("epId", bhUser.getCompanyId());
		List<Jdhz> bhhzList = ecologicalParkProduceService.selectParkTbByDay(paraMap);
		model.addAttribute("user",bhUser);
		model.addAttribute("listSize", bhhzList.size());
		model.addAttribute("day",day);
		model.addAttribute("wlList", bhhzList);
		return "ParkTbList";
	}
	
	
	
	@RequestMapping("/toUpdateTbHistory")
	@ResponseBody
	public String toUpdateTbHistory(HttpServletRequest request,Model model) {
		String info = "修改失败";
		// bhUser = (BhUser) request.getSession().getAttribute("user");
		BhUser bhUser = cacheDemoClient.getCacheDemo(request);
		
		if(bhUser == null) {
			String errinfo = "未获取到用户信息,请重新登陆";
			model.addAttribute("errInfo",errinfo);
			return "error";
		}
		List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
		Integer bhCount = 0;
		for(Map value:values) {
			Map paraMap = new HashMap<String,Object>();
			paraMap.put("djbh", value.get("djbh"));
			paraMap.put("sqsl", value.get("sqsl"));
			paraMap.put("epId", bhUser.getCompanyId());
			try {
				ecologicalParkProduceService.updateSbslByDjbh(paraMap);
				bhCount++;
			}catch(Exception e) {
				System.out.println(e.getMessage());
				info="系统异常";
				return info;
			}
		}
		if(bhCount == values.size()) {
			info="修改成功";
		}
		return info;
	}
	
	
	
	
	@RequestMapping("/toSubmitTb")
	@ResponseBody
	public String toSubmitBh(HttpServletRequest request,String day,Model model) {
		String info = "提交失败";
		if(day == null) {
			day = Timeutil.currentTime();
		}
		//BhUser bhUser = (BhUser) request.getSession().getAttribute("user");
		BhUser bhUser = cacheDemoClient.getCacheDemo(request);
		if(bhUser == null) {
			String errinfo = "未获取到用户信息,请重新登陆";
			model.addAttribute("errInfo",errinfo);
			return "error";
		}
		List<Map> values = JSON.parseArray(request.getParameter("val"), Map.class);
		Integer bhCount = 0;
		for(Map value:values) {
			Map paraMap = new HashMap<String,Object>();
			paraMap.put("djbh", value.get("djbh"));
			paraMap.put("sqrq", day);
			paraMap.put("state", "1");
			paraMap.put("epId", bhUser.getCompanyId());
			try {
				ecologicalParkProduceService.updateSubState(paraMap);
				bhCount++;
			}catch(Exception e) {
				System.out.println(e.getMessage());
				info="系统异常";
				return info;
			}
		}
		if(bhCount == values.size()) {
			info="已提交";
		}
		
		
		return info;
	}
	
	
	
	
	
	@RequestMapping("/delOneRow")
	@ResponseBody
	public String delOneRow(String djbh) {
		String info="删除失败";
		if(djbh==null) {
			return info;
		}
		try {
			ecologicalParkProduceService.deleteTbByDjbh(djbh);
			info="删除成功";
			return info;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return info;
	}
	
	
	
	
	
	
	
	
	
	
}
