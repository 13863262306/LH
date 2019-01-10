package lanhai.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lanhai.service.BaoHuoService;
import lanhai.service.CheckTimeService;
import utils.Timeutil;
@Controller
@RequestMapping("/makeCheckDate")
public class CheckTimeController {

	@Resource
	private CheckTimeService checkTimeService;
	@Resource 
	private BaoHuoService baoHuoService;
	/**
	 * 
	 * @param startDay(开始日期）
	 * @param week(星期几，例如星期二，则为2，星期五，则为5)
	 * 
	 */
	@RequestMapping("/byWeek")
	@ResponseBody
	public String saveCheckDayByWeek(String startDate,Integer week) {
		String info = "保存失败";
		if(week>6 || week<0) {info = "week格式不正确，week的范围应该为0~6";return info;} 
		String thisDayStr = startDate.substring(startDate.lastIndexOf("-")+1);
		Integer thisDay = Integer.valueOf(thisDayStr);
		String last="";
		try {
			last = Timeutil.getLast(startDate);
		} catch (ParseException e) {
			info = "日期格式不正确";
			e.printStackTrace();
			return info;
		}
		String parentId = "yDAAAAO4/OQSucz1";//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		System.out.println("parentIdd:"+parentId+",thisDay:"+thisDay+",last:"+last);
		Integer theLastDay = Integer.valueOf(last.substring(last.lastIndexOf("-")+1));
		String yearAndMonth = startDate.substring(0, startDate.lastIndexOf("-")+1);
		int j = 1;
		for(int i=thisDay;i<=theLastDay;i++) {
			String aDay = yearAndMonth+i;
			int theWeekOfDay = Timeutil.getDayofweek(aDay);
			if(theWeekOfDay == week) {
				Map paraMap = new HashMap();
				paraMap.put("inde", j);
				paraMap.put("parentId", parentId);
				paraMap.put("day",i);
				paraMap.put("date", aDay);
				//try {
					checkTimeService.saveCheckTime(paraMap);
					j++;
				/*}catch(Exception e) {
					System.out.println(e.getMessage());
					System.out.println(e.getStackTrace().toString());
					info="插入数据库时异常";
					return info;
				}*/
			}
		}
		info="保存成功";
		return info;
	}
	
	/**
	 * 根据时间间隔获取日期
	 * @param startDate
	 * @param dayInterval
	 * @return
	 */
	@RequestMapping("/byDayInterval")
	@ResponseBody
	public String saveCheckDayByInterval(String startDate,Integer dayInterval) {
		String info="保存失败";
		//获取当前月号
		Integer thisDay = Integer.valueOf(startDate.substring(startDate.lastIndexOf("-")+1));
		//获取当前月的最后一天
		String last="";
		try {
			last = Timeutil.getLast(startDate);
		} catch (ParseException e) {
			info = "日期格式不正确";
			e.printStackTrace();
			return info;
		}
		Integer theLastDay = Integer.valueOf(last.substring(last.lastIndexOf("-")+1));
		//循环加，判断是否大于最后一天，不大月则存入数据库
		String yearAndMonth = startDate.substring(0, startDate.lastIndexOf("-")+1);
		String parentId = "yDAAAAO4/OQSucz1";///////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		int j=1;
		for(int i=thisDay;i<=theLastDay;i+=dayInterval) {
			String saveDate = yearAndMonth+i;
			String day= saveDate.substring(saveDate.lastIndexOf("-")+1);
			System.out.println("saveDate:"+saveDate+",day:"+day);
			Map paraMap = new HashMap();
			paraMap.put("inde", j);
			paraMap.put("parentId", parentId);
			paraMap.put("day", day);
			paraMap.put("date", saveDate);
			try {
				checkTimeService.saveCheckTime(paraMap);
				j++;
			}catch(Exception e) {
				System.out.println(e.getMessage());
				info="插入数据库时异常";
				return info;
			}
		}
		info= "保存成功";
		return info;
	}
	
	
	//设置时间页面
	@RequestMapping("/showSetBhTime")
	public String showSetTime(String orgType,Model model) {
		Map selectBhTimeMap =  new HashMap();
		selectBhTimeMap.put("orgType", "1");
		Map bhTimeMap = baoHuoService.selectBhTime(selectBhTimeMap);
		if(bhTimeMap != null) {
			model.addAttribute("dkStartTime",bhTimeMap.get("START_TIME"));
			model.addAttribute("dkEndTime",bhTimeMap.get("END_TIME"));
		}
				
		
		Map selectjdTimeMap =  new HashMap();
		selectjdTimeMap.put("orgType", "2");
		Map jdTimeMap = baoHuoService.selectBhTime(selectBhTimeMap);
		if(jdTimeMap != null) {
			model.addAttribute("jdStartTime",jdTimeMap.get("START_TIME"));
			model.addAttribute("jdEndTime",jdTimeMap.get("END_TIME"));
		}
				
		
		
		return "setTime";
	}
	
	
	
	/**
	 * 
	 * @param request
	 * @param startTime
	 * @param endTime
	 * @param orgType 1为档口input，2位酒店生成报货单
	 * @return
	 */
	@RequestMapping("/setBhTime")
	@ResponseBody
	public String setBhTime(HttpServletRequest request,String startTime,String endTime,String orgType) {
		String info = "设置失败，请稍后重试";
		
		System.out.println("startTime:"+startTime+",endTime:"+endTime+",orgType:"+orgType);
		
		if(startTime == null || endTime == null || orgType == null || "".equals(startTime) || "".equals(endTime)) {
			info = "参数不全，无法设置！";
			return info;
		}
		Map updateBhTimeMap = new HashMap();
			updateBhTimeMap.put("startTime", startTime);
			updateBhTimeMap.put("endTime", endTime);
			updateBhTimeMap.put("orgType", orgType);
		try {
			baoHuoService.updateBhTimeByOrgType(updateBhTimeMap);
			info = "设置成功";
		}catch(Exception e) {
			System.out.println("设置报货时间时报错："+e.getMessage());
			info = "系统出错，请稍后重试！";
			return info;
		}
		
		
		return info;
	}
	
	
	@RequestMapping("/checkAllInputKey")
	@ResponseBody
	public String checkAllInputKey() {
		String info="1";
		String nowTime = new SimpleDateFormat("HH-mm").format(new Date());
		System.out.println("当前时分："+nowTime);
		String startTime = "";
		String endTime = "";
		Map selectBhTimeMap = new HashMap();
		selectBhTimeMap.put("orgType", "1");
		Map bhTimeList = baoHuoService.selectBhTime(selectBhTimeMap);
		if(bhTimeList != null) {
			startTime = (String)bhTimeList.get("START_TIME");
			endTime = (String)bhTimeList.get("END_TIME");
		}
				
		if(!"".equals(endTime) && !"".equals(startTime)) {
			info = baoHuoService.checkTimeInScope(nowTime,startTime,endTime);
		}
		
		if("0".equals(info)) {
			info = "0";
		}
		return info;
	}
	
	
	@RequestMapping("/checkJdKey")
	@ResponseBody
	public String checkJdKey() {
		String info = "1";
		
		String nowTime = new SimpleDateFormat("HH-mm").format(new Date());
		String startTime = "";
		String endTime = "";
		Map selectBhTimeMap = new HashMap();
		selectBhTimeMap.put("orgType", "2");
		Map bhTimeList = baoHuoService.selectBhTime(selectBhTimeMap);
		if(bhTimeList != null) {
			startTime = (String)bhTimeList.get("START_TIME");
			endTime = (String)bhTimeList.get("END_TIME");
		}
				
		if(!"".equals(endTime) && !"".equals(startTime)) {
			info = baoHuoService.checkTimeInScope(nowTime,startTime,endTime);
		}
		
		if("0".equals(info)) {
			info = "0";
		}
		return info;
	}
	
	
	
	
	
	
}
