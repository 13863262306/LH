package lanhai.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lanhai.entity.Mblx;
import lanhai.service.BaoHuoService;
import lanhai.service.ReportFormService;
import utils.StrUtil;
import utils.Timeutil;

@Controller
@RequestMapping("/reportForm")
public class ReportFormController {
	@Resource
	private ReportFormService reportFormService;
	@Resource
	private BaoHuoService baoHuoService;
	 
	 @GetMapping("/hotelReportForm")
	 public String hotelReportForm(Model model,String username,String mbId,String startTime,String endTime) {
		 model.addAttribute("username", username);
		 List<Mblx> mbList = baoHuoService.selectMbs();
		model.addAttribute("mbList", mbList);
		model.addAttribute("startTime", Timeutil.currentTime());
		model.addAttribute("endTime", Timeutil.currentTime());
		 model.addAttribute("mbId","0");
		 HashMap<String,Object> map=new HashMap<String,Object>();
		 if(mbId==null) {
				mbId="0";
			}
		 map.put("userId",username);
		 map.put("mbId", mbId);
			map.put("startTime", Timeutil.currentTime());
			map.put("endTime", Timeutil.currentTime());
		 model.addAttribute("stalls", reportFormService.selectselectTssdlist(map));
		 return "hotelReportForm";
	 }
	 
	 
	 @GetMapping("/hotelReportFormForZiCai")
	 public String hotelReportFormForZiCai(Model model,String username,String mbId,String startTime,String endTime) {
		 model.addAttribute("username", username);
		 List<Mblx> mbList = baoHuoService.selectMbs();
		model.addAttribute("mbList", mbList);
		model.addAttribute("startTime", Timeutil.currentTime());
		model.addAttribute("endTime", Timeutil.currentTime());
		 model.addAttribute("mbId","0");
		 HashMap<String,Object> map=new HashMap<String,Object>();
		 if(mbId==null) {
				mbId="0";
			}
		 map.put("userId",username);
		 map.put("mbId", mbId);
			map.put("startTime", Timeutil.currentTime());
			map.put("endTime", Timeutil.currentTime());
		 model.addAttribute("stalls", reportFormService.selectselectTssdlist(map));
		 return "hotelReportFormForZiCai";
	 }
	 
	 
	 
	 @GetMapping("/download")
	 public void  download(HttpServletResponse response,HttpServletRequest request,String username,String mbId,String startTime,String endTime) {
			if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			 List<HashMap<String,Object>> hotelReportFormlist=new ArrayList<>();
			 HashMap<String,Object> map=new HashMap<String,Object>();
			 map.put("userId",username);
				map.put("mbId", mbId);
				map.put("startTime", startTime);
				map.put("endTime", endTime);
				 hotelReportFormlist=reportFormService.selecthotelReportFormlist(map);
	        try {
	     HSSFWorkbook workbook = new HSSFWorkbook();
	     HSSFSheet sheet = workbook.createSheet("档口报货报表");
	     sheet.createFreezePane( 0, 3, 0, 3 );
         sheet.setColumnWidth(0, 20 * 256);
         sheet.setColumnWidth(1, 20 * 256);
         sheet.setColumnWidth(2, 20 * 256);
         sheet.setColumnWidth(3, 20 * 256);
         CellRangeAddress ffd = new CellRangeAddress(0, 0, 0, 6);
         sheet.addMergedRegion(ffd);
          ffd = new CellRangeAddress(1, 1, 1, 6);
         sheet.addMergedRegion(ffd);
	     HSSFRow rowL = sheet.createRow(2);
	     String[] to=new String[] {"报货模板","物料编码","物料名称","物料规格","计量单位","申请数量","审核数量"};
	     for(int i=0;i<to.length;i++) {
	    	 HSSFCell cell =rowL.createCell(i);
	    	 cell.setCellValue(to[i]);
	    	 HSSFCellStyle style = workbook.createCellStyle();
				HSSFFont font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
				style.setFont(font);
				cell.setCellStyle(style);
	     }
	     HSSFCellStyle jzcellStyle = workbook.createCellStyle(); 
	     jzcellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
	     jzcellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
         HSSFRow ff1=sheet.createRow(0);
         HSSFCell fgt=ff1.createCell(0);
         fgt.setCellValue("档口报货报表");
         fgt.setCellStyle(jzcellStyle);
         ff1.setHeightInPoints(40);
         ff1=sheet.createRow(1);
         fgt=ff1.createCell(0);
         fgt.setCellValue("日期范围");
         fgt=ff1.createCell(1);
         fgt.setCellValue(Timeutil.stringToString(startTime)+"--"+Timeutil.stringToString(endTime));
	     int i=3;
	     String pbane="kongnull";
	     int pi=0;
	     for (HashMap<String,Object> ma:hotelReportFormlist) {
	    	 HSSFRow row = sheet.createRow(i);
	    	 for(int r=0;r<to.length;r++) {
	    		 HSSFCell cell =row.createCell(r);
	    		 switch(r){
	    		 case 0:
	    			 if(!pbane.equals(ma.get("mbName"))) {
			    	 cell.setCellValue(ma.get("mbName")==null?"":ma.get("mbName").toString());
			    	 cell.setCellStyle(jzcellStyle);
			    	 pbane=ma.get("mbName")==null?"":ma.get("mbName").toString();
			    	 if(i!=3) {
			    		  CellRangeAddress pp = new CellRangeAddress(pi, i-1, 0, 0);
			    		  sheet.addMergedRegion(pp);
			    	 }
	    				 pi=i;
			    	 }
	    			 break;
	    		 case 1:
			    	 cell.setCellValue(ma.get("wlbm")==null?"":ma.get("wlbm").toString());
	    			 break;
	    		 case 2:
			    	 cell.setCellValue(ma.get("wlmc")==null?"":ma.get("wlmc").toString());
	    			 break;
	    		 case 3:
			    	 cell.setCellValue(ma.get("wlgg")==null?"":ma.get("wlgg").toString());
	    			 break;
	    		 case 4:
			    	 cell.setCellValue(ma.get("jldw")==null?"":ma.get("jldw").toString());
	    			 break;
	    		 case 5:
			    	 cell.setCellValue(ma.get("sqsl")==null?"":ma.get("sqsl").toString());
	    			 break;
	    		 case 6:
			    	 cell.setCellValue(ma.get("shsl")==null?"":ma.get("shsl").toString());
	    			 break;
	    		 }
	    	 }
	    	 i++;
	     }
	     
	     if(pi!=i) {
	    	  CellRangeAddress pp = new CellRangeAddress(pi, i-1, 0, 0);
    		  sheet.addMergedRegion(pp);
	     }
	     response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	     
	     
	     String s = request.getHeader("USER-AGENT").toLowerCase();
	     if(s.indexOf("firefox")>0){
	        response.setHeader("Content-disposition", "attachment; filename="
	              +new String("档口报货报表".getBytes("UTF-8"), "ISO8859-1") + ".xls");
	     }else {
	    	 response.setHeader("Content-Disposition", "attachment;filename=%e6%a1%a3%e5%8f%a3%e6%8a%a5%e8%b4%a7%e6%8a%a5%e8%a1%a8.xls");}
	        response.flushBuffer();
				workbook.write(response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 @GetMapping("/stallsReportForm")
	 public String stallsReportForm(Model model,String username) {
		 model.addAttribute("username", username);
		 List<Mblx> mbList = baoHuoService.selectMbs();
		model.addAttribute("mbList", mbList);
		model.addAttribute("startTime", Timeutil.currentTime());
		model.addAttribute("endTime", Timeutil.currentTime());
		 model.addAttribute("mbId","0");
		 return "stallsReportForm";
	 }
	 
	 @PostMapping("/hotelReportFormData")
	 @ResponseBody
	 public JSONObject hotelReportFormData(String username,String mbId,String startTime,String endTime,int page,int rows,String ziCai) {
		 List<HashMap<String,Object>> hotelReportFormlist=new ArrayList<>();
		 HashMap<String,Object> map=new HashMap<String,Object>();
		 map.put("userId",username);
			if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			map.put("mbId", mbId);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			List<HashMap<String,Object>> ddf=reportFormService.selectselectTssdlist(map);
			String stalls="(";
			for (HashMap<String,Object> dd:ddf) {
				stalls+="'"+dd.get("dk").toString()+"' as "+dd.get("dk").toString()+",";
			}
			stalls+="^^ls^^";
			stalls=stalls.replace(",^^ls^^", ")");
			if(stalls.equals("(^^ls^^")) {
				stalls="()";
			}
			map.put("stalls", stalls);
			if("1".equals(ziCai)) {
				map.put("subState", "04");
			}
            PageHelper.startPage(page,rows);
			hotelReportFormlist=reportFormService.selecthotelReportFormtlist(map);
			List<HashMap<String,Object>> mergeCells=new ArrayList<>();
			int i=0;
			int pp=0;
			String mbName="kongnull";
			HashMap<String,Object> mergeCell=new HashMap<String,Object>();
			for(HashMap<String,Object> hotelReportForm:hotelReportFormlist) {
				String mapmbName=hotelReportForm.get("mbName")==null?"":hotelReportForm.get("mbName").toString();
				if(!mapmbName.equals(mbName)) {
					mbName=mapmbName;
					if(i!=0) {
					 mergeCell.put("rowspan", pp);
					 mergeCells.add(mergeCell);
					 mergeCell=new HashMap<String,Object>();
					 pp=0;
					}
					mergeCell.put("index", i);
				}
				pp++;
				i++;
				
				int lsd=0;
				for (HashMap<String,Object> dd:ddf) {
					lsd+= hotelReportForm.get(dd.get("dk").toString())==null?0:Integer.parseInt(hotelReportForm.get(dd.get("dk").toString()).toString());
				}
				if(lsd!=0) {
					hotelReportForm.put("summary", lsd);
				}
			}
			if(pp!=0) {
				 mergeCell.put("rowspan", pp);
				 mergeCells.add(mergeCell);
			}
	        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(hotelReportFormlist);
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("total", pageInfo.getTotal());
	        jsonObject.put("rows", pageInfo.getList());
	        jsonObject.put("mergeCells",mergeCells);
		 return jsonObject;
	 } 
	 
	 @PostMapping("/stallsReportFormData")
	 @ResponseBody
	 public JSONObject stallsReportFormData(String username,String mbId,String startTime,String endTime,int page,int rows) {
		 List<HashMap<String,Object>> hotelReportFormlist=new ArrayList<>();
		 HashMap<String,Object> map=new HashMap<String,Object>();
		 map.put("userId",username);
			if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			map.put("mbId", mbId);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
            PageHelper.startPage(page,rows);
			hotelReportFormlist=reportFormService.selecthotelReportFormlist(map);
			List<HashMap<String,Object>> mergeCells=new ArrayList<>();
			int i=0;
			int pp=0;
			String mbName="kongnull";
			HashMap<String,Object> mergeCell=new HashMap<String,Object>();
			for(HashMap<String,Object> hotelReportForm:hotelReportFormlist) {
				String mapmbName=hotelReportForm.get("mbName")==null?"":hotelReportForm.get("mbName").toString();
				if(!mapmbName.equals(mbName)) {
					mbName=mapmbName;
					if(i!=0) {
					 mergeCell.put("rowspan", pp);
					 mergeCells.add(mergeCell);
					 mergeCell=new HashMap<String,Object>();
					 pp=0;
					}
					mergeCell.put("index", i);
				}
				pp++;
				i++;
			}
			if(pp!=0) {
				 mergeCell.put("rowspan", pp);
				 mergeCells.add(mergeCell);
			}
	        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(hotelReportFormlist);
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("total", pageInfo.getTotal());
	        jsonObject.put("rows", pageInfo.getList());
	        jsonObject.put("mergeCells",mergeCells);
		 return jsonObject;
	 } 
	 
	 @GetMapping("/hoteldownload")
	 public void  hoteldownload(HttpServletResponse response,HttpServletRequest request,String username,String mbId,String startTime,String endTime,String ziCai) {
		 if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			 List<HashMap<String,Object>> hotelReportFormlist=new ArrayList<>();
			 HashMap<String,Object> map=new HashMap<String,Object>();
			 map.put("userId",username);
				map.put("mbId", mbId);
				map.put("startTime", startTime);
				map.put("endTime", endTime);
				if("1".equals(ziCai)) {
					map.put("subState","04");
				}
				List<HashMap<String,Object>> ddf=reportFormService.selectselectTssdlist(map);
				String stalls="(";
				for (HashMap<String,Object> dd:ddf) {
					stalls+="'"+dd.get("dk").toString()+"' as "+dd.get("dk").toString()+",";
				}
				stalls+="^^ls^^";
				stalls=stalls.replace(",^^ls^^", ")");
				if(stalls.equals("(^^ls^^")) {
					stalls="()";
				}
				map.put("stalls", stalls);
				 hotelReportFormlist=reportFormService.selecthotelReportFormtlist(map);
	        try {
	     HSSFWorkbook workbook = new HSSFWorkbook();
	     HSSFSheet sheet = null;
	     if("1".equals(ziCai)) {
	    	 sheet = workbook.createSheet("酒店自采报表");
		}else {
			sheet = workbook.createSheet("酒店报货报表");
		}
	     
	     sheet.createFreezePane( 0, 3, 0, 3 );
         sheet.setColumnWidth(0, 20 * 256);
         sheet.setColumnWidth(1, 20 * 256);
         sheet.setColumnWidth(2, 20 * 256);
         sheet.setColumnWidth(3, 20 * 256);
         CellRangeAddress ffd = new CellRangeAddress(0, 0, 0, 4+ddf.size()+1);
         sheet.addMergedRegion(ffd);
          ffd = new CellRangeAddress(1, 1, 1, 4+ddf.size()+1);
         sheet.addMergedRegion(ffd);
	     HSSFRow rowL = sheet.createRow(2);
	     String[] to=new String[] {"报货模板","物料编码","物料名称","物料规格","计量单位"};
	     to= java.util.Arrays.copyOf(to,to.length+ddf.size()+1);
	     int tolength=5;
	     for (HashMap<String,Object> dd:ddf) {
	    	 to[tolength]=dd.get("dk").toString();
	    	 tolength++;
	     }
	     to[tolength]="合计";
	     for(int i=0;i<to.length;i++) {
	    	 HSSFCell cell =rowL.createCell(i);
	    	 cell.setCellValue(to[i]);
	    	 HSSFCellStyle style = workbook.createCellStyle();
				HSSFFont font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
				style.setFont(font);
				cell.setCellStyle(style);
	     }
	     HSSFCellStyle jzcellStyle = workbook.createCellStyle(); 
	     jzcellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
	     jzcellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
         HSSFRow ff1=sheet.createRow(0);
         HSSFCell fgt=ff1.createCell(0);
         
         if("1".equals(ziCai)) {
        	 fgt.setCellValue("酒店自采报表");
		}else {
			fgt.setCellValue("酒店报货报表");
		}
         
         //fgt.setCellValue("酒店报货报表");
         fgt.setCellStyle(jzcellStyle);
         ff1.setHeightInPoints(40);
         ff1=sheet.createRow(1);
         fgt=ff1.createCell(0);
         fgt.setCellValue("日期范围");
         fgt=ff1.createCell(1);
         fgt.setCellValue(Timeutil.stringToString(startTime)+"--"+Timeutil.stringToString(endTime));
	     int i=3;
	     String pbane="kongnull";
	     int pi=0;
	     for (HashMap<String,Object> ma:hotelReportFormlist) {
	    	 HSSFRow row = sheet.createRow(i);
	    	 int lsd=0;
	    	 for(int r=0;r<to.length-1;r++) {
	    		 HSSFCell cell =row.createCell(r);
	    		 switch(r){
	    		 case 0:
	    			 if(!pbane.equals(ma.get("mbName"))) {
			    	 cell.setCellValue(ma.get("mbName")==null?"":ma.get("mbName").toString());
			    	 cell.setCellStyle(jzcellStyle);
			    	 pbane=ma.get("mbName")==null?"":ma.get("mbName").toString();
			    	 if(i!=3) {
			    		  CellRangeAddress pp = new CellRangeAddress(pi, i-1, 0, 0);
			    		  sheet.addMergedRegion(pp);
			    	 }
	    				 pi=i;
			    	 }
	    			 break;
	    		 case 1:
			    	 cell.setCellValue(ma.get("wlbm")==null?"":ma.get("wlbm").toString());
	    			 break;
	    		 case 2:
			    	 cell.setCellValue(ma.get("wlmc")==null?"":ma.get("wlmc").toString());
	    			 break;
	    		 case 3:
			    	 cell.setCellValue(ma.get("wlgg")==null?"":ma.get("wlgg").toString());
	    			 break;
	    		 case 4:
			    	 cell.setCellValue(ma.get("jldw")==null?"":ma.get("jldw").toString());
	    			 break;
	    		 default:
	    			 cell.setCellValue(ma.get(to[r])==null?"":ma.get(to[r]).toString());
	    			 lsd+= ma.get(to[r])==null?0:Integer.parseInt(ma.get(to[r]).toString());
	    			break;
	    		 }
	    	 }
	    	 if(lsd!=0) {
	    	 HSSFCell cell =row.createCell(to.length-1);
	    	 cell.setCellValue(lsd);}
	    	 i++;
	     }
	     
	     if(pi!=i) {
	    	  CellRangeAddress pp = new CellRangeAddress(pi, i-1, 0, 0);
    		  sheet.addMergedRegion(pp);
	     }
	     response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	     
	     
	     String s = request.getHeader("USER-AGENT").toLowerCase();
	     if(s.indexOf("firefox")>0){
	    	 
	    	 if("1".equals(ziCai)) {
	        	 response.setHeader("Content-disposition", "attachment; filename="
	   	              +new String("酒店自采报表".getBytes("UTF-8"), "ISO8859-1") + ".xls");
			}else {
				response.setHeader("Content-disposition", "attachment; filename="
			              +new String("酒店报货报表".getBytes("UTF-8"), "ISO8859-1") + ".xls");
			}
	    	 
	        //response.setHeader("Content-disposition", "attachment; filename="
	        //      +new String("酒店报货报表".getBytes("UTF-8"), "ISO8859-1") + ".xls");
	     }else {
	    	 
	    	 if("1".equals(ziCai)) {
	    		 response.setHeader("Content-Disposition", "attachment;filename=%E9%85%92%E5%BA%97%E8%87%AA%E9%87%87%E6%8A%A5%E8%A1%A8.xls");
			}else {
				response.setHeader("Content-Disposition", "attachment;filename=%e9%85%92%e5%ba%97%e6%8a%a5%e8%b4%a7%e6%8a%a5%e8%a1%a8.xls");
			}
	    	 
	    	 //response.setHeader("Content-Disposition", "attachment;filename=%e9%85%92%e5%ba%97%e6%8a%a5%e8%b4%a7%e6%8a%a5%e8%a1%a8.xls");
	     	}
	        response.flushBuffer();
				workbook.write(response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 @GetMapping("/groupReportForm")
	 public String groupReportForm(Model model,String username,String mbId,String startTime,String endTime) {
		 model.addAttribute("username", username);
		 List<Mblx> mbList = baoHuoService.selectMbs();
		model.addAttribute("mbList", mbList);
		model.addAttribute("startTime", Timeutil.currentTime());
		model.addAttribute("endTime", Timeutil.currentTime());
		 model.addAttribute("mbId","0");
		 HashMap<String,Object> map=new HashMap<String,Object>();
		 map.put("userId",username);
			if(mbId==null) {
				mbId="0";
			}
			map.put("mbId", mbId);
			map.put("startTime", Timeutil.currentTime());
			map.put("endTime", Timeutil.currentTime());
		 List<HashMap<String,Object>> stalls=reportFormService.selecjrrrlist(map);
		 model.addAttribute("stalls", stalls);
		 List<HashMap<String,Object>>dff=new ArrayList<>();
		 for(HashMap<String,Object>stall:stalls) {
			 HashMap<String,Object> p=new HashMap<String,Object>();
			 p.put("name", "申请数量");
			 p.put("value", StrUtil.getMD5(stall.get("jd").toString())+"_sq");
			 dff.add(p);
			 p=new HashMap<String,Object>();
			 p.put("name", "自产量");
			 p.put("value", StrUtil.getMD5(stall.get("jd").toString())+"_zc");
			 dff.add(p);
			 p=new HashMap<String,Object>();
			 p.put("name", "库存量");
			 p.put("value", StrUtil.getMD5(stall.get("jd").toString())+"_kc");
			 dff.add(p);
		 }
		 model.addAttribute("stallsp", dff);
		 return "groupReportForm";
	 }
	 
	 @PostMapping("/groupReportFormData")
	 @ResponseBody
	 public JSONObject groupReportFormData(String username,String mbId,String startTime,String endTime,int page,int rows) {
		 List<HashMap<String,Object>> hotelReportFormlist=new ArrayList<>();
		 HashMap<String,Object> map=new HashMap<String,Object>();
		 map.put("userId",username);
			if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			map.put("mbId", mbId);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			List<HashMap<String,Object>> ddf=reportFormService.selecjrrrlist(map);
			String stalls="(";
			for (HashMap<String,Object> dd:ddf) {
				stalls+="'"+dd.get("jd").toString()+"' as \""+StrUtil.getMD5(dd.get("jd").toString())+"\",";
			}
			stalls+="^^ls^^";
			stalls=stalls.replace(",^^ls^^", ")");
			if(stalls.equals("(^^ls^^")) {
				stalls="()";
			}
			map.put("stalls", stalls);
            PageHelper.startPage(page,rows);
			hotelReportFormlist=reportFormService.selectgroupReportFormtlist(map);
			List<HashMap<String,Object>> mergeCells=new ArrayList<>();
			int i=0;
			int pp=0;
			String mbName="kongnull";
			HashMap<String,Object> mergeCell=new HashMap<String,Object>();
			for(HashMap<String,Object> hotelReportForm:hotelReportFormlist) {
				String mapmbName=hotelReportForm.get("mbName")==null?"":hotelReportForm.get("mbName").toString();
				if(!mapmbName.equals(mbName)) {
					mbName=mapmbName;
					if(i!=0) {
					 mergeCell.put("rowspan", pp);
					 mergeCells.add(mergeCell);
					 mergeCell=new HashMap<String,Object>();
					 pp=0;
					}
					mergeCell.put("index", i);
				}
				pp++;
				i++;
			}
			if(pp!=0) {
				 mergeCell.put("rowspan", pp);
				 mergeCells.add(mergeCell);
			}
	        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(hotelReportFormlist);
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("total", pageInfo.getTotal());
	        jsonObject.put("rows", pageInfo.getList());
	        jsonObject.put("mergeCells",mergeCells);
		 return jsonObject;
	 } 
	 
	 
	 @GetMapping("/groupdownload")
	 public void  groupdownload(HttpServletResponse response,HttpServletRequest request,String username,String mbId,String startTime,String endTime) {
			if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			 List<HashMap<String,Object>> hotelReportFormlist=new ArrayList<>();
			 HashMap<String,Object> map=new HashMap<String,Object>();
			 map.put("userId",username);
				map.put("mbId", mbId);
				map.put("startTime", startTime);
				map.put("endTime", endTime);
				List<HashMap<String,Object>> ddf=reportFormService.selecjrrrlist(map);
				String stalls="(";
				for (HashMap<String,Object> dd:ddf) {
					stalls+="'"+dd.get("jd").toString()+"' as \""+StrUtil.getMD5(dd.get("jd").toString())+"\",";
				}
				stalls+="^^ls^^";
				stalls=stalls.replace(",^^ls^^", ")");
				if(stalls.equals("(^^ls^^")) {
					stalls="()";
				}
				map.put("stalls", stalls);
				 hotelReportFormlist=reportFormService.selectgroupReportFormtlist(map);
	        try {
	     HSSFWorkbook workbook = new HSSFWorkbook();
	     HSSFSheet sheet = workbook.createSheet("集团报货报表");
	     sheet.createFreezePane( 0, 4, 0, 4 );
         sheet.setColumnWidth(0, 20 * 256);
         sheet.setColumnWidth(1, 20 * 256);
         sheet.setColumnWidth(2, 20 * 256);
         sheet.setColumnWidth(3, 20 * 256);
         CellRangeAddress ffd = new CellRangeAddress(0, 0, 0, 4+ddf.size()*3);
         sheet.addMergedRegion(ffd);
          ffd = new CellRangeAddress(1, 1, 1, 4+ddf.size()*3);
         sheet.addMergedRegion(ffd);
	     HSSFRow rowL = sheet.createRow(2);
	     String[] to=new String[] {"报货模板","物料编码","物料名称","物料规格","计量单位"};
	     for(int i=0;i<to.length;i++) {
	    	 HSSFCell cell =rowL.createCell(i);
	    	 cell.setCellValue(to[i]);
	    	 HSSFCellStyle style = workbook.createCellStyle();
				HSSFFont font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
				style.setFont(font);
				style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
				style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(style);
			    ffd = new CellRangeAddress(2, 3, i, i);
			    sheet.addMergedRegion(ffd);
	     }
	     int is=5;
		    HSSFRow rowLt=sheet.createRow(3);
	     for (HashMap<String,Object> dd:ddf) {
	    	 HSSFCell cell =rowL.createCell(is);
	    	 cell.setCellValue(dd.get("jd").toString());
	    	 HSSFCellStyle style = workbook.createCellStyle();
				HSSFFont font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
				style.setFont(font);
				cell.setCellStyle(style);
			    ffd = new CellRangeAddress(2, 2, is, is+2);
			    sheet.addMergedRegion(ffd); 
			for(int iifi=0;iifi<3;iifi++) {
				 cell =rowLt.createCell(is+iifi);
					 switch(iifi) {
					 case 0:
		    	      cell.setCellValue("申请数量");
		    	     break;
					 case 1:
			    	      cell.setCellValue("自产量");
			    	     break;
					 case 2:
			    	      cell.setCellValue("库存量");
			    	     break;
					 }
		    	  style = workbook.createCellStyle();
					 font = workbook.createFont();
					font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
					style.setFont(font);
					cell.setCellStyle(style);}
			    is++;is++;is++;
	     }
	     HSSFCellStyle jzcellStyle = workbook.createCellStyle(); 
	     jzcellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
	     jzcellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
         HSSFRow ff1=sheet.createRow(0);
         HSSFCell fgt=ff1.createCell(0);
         fgt.setCellValue("集团报货报表");
         fgt.setCellStyle(jzcellStyle);
         ff1.setHeightInPoints(40);
         ff1=sheet.createRow(1);
         fgt=ff1.createCell(0);
         fgt.setCellValue("日期范围");
         fgt=ff1.createCell(1);
         fgt.setCellValue(Timeutil.stringToString(startTime)+"--"+Timeutil.stringToString(endTime));
	     int i=4;
	     String pbane="kongnull";
	     int pi=0;
	     for (HashMap<String,Object> ma:hotelReportFormlist) {
	    	 HSSFRow row = sheet.createRow(i);
	    	 for(int r=0;r<to.length;r++) {
	    		 HSSFCell cell =row.createCell(r);
	    		 switch(r){
	    		 case 0:
	    			 if(!pbane.equals(ma.get("mbName"))) {
			    	 cell.setCellValue(ma.get("mbName")==null?"":ma.get("mbName").toString());
			    	 cell.setCellStyle(jzcellStyle);
			    	 pbane=ma.get("mbName")==null?"":ma.get("mbName").toString();
			    	 if(i!=4) {
			    		  CellRangeAddress pp = new CellRangeAddress(pi, i-1, 0, 0);
			    		  sheet.addMergedRegion(pp);
			    	 }
	    				 pi=i;
			    	 }
	    			 break;
	    		 case 1:
			    	 cell.setCellValue(ma.get("wlbm")==null?"":ma.get("wlbm").toString());
	    			 break;
	    		 case 2:
			    	 cell.setCellValue(ma.get("wlmc")==null?"":ma.get("wlmc").toString());
	    			 break;
	    		 case 3:
			    	 cell.setCellValue(ma.get("wlgg")==null?"":ma.get("wlgg").toString());
	    			 break;
	    		 case 4:
			    	 cell.setCellValue(ma.get("jldw")==null?"":ma.get("jldw").toString());
	    			 break;
	    		 default:
	    			// cell.setCellValue(ma.get(to[r])==null?"":ma.get(to[r]).toString());
	    			break;
	    		 }
	    	 }
	    	 int ldf=5;
			 for (HashMap<String,Object> dd:ddf) {
				String com= StrUtil.getMD5(dd.get("jd").toString());
				 HSSFCell cell =row.createCell(ldf);
				 cell.setCellValue(ma.get(com+"_sq")==null?"":ma.get(com+"_sq").toString());
				 cell =row.createCell(ldf+1);
				 cell.setCellValue(ma.get(com+"_zc")==null?"":ma.get(com+"_zc").toString());
				 cell =row.createCell(ldf+2);
				 cell.setCellValue(ma.get(com+"_kc")==null?"":ma.get(com+"_kc").toString());
				 ldf++;ldf++;ldf++;
			 }
	    	 i++;
	     }
	     
	     if(pi!=i) {
	    	  CellRangeAddress pp = new CellRangeAddress(pi, i-1, 0, 0);
    		  sheet.addMergedRegion(pp);
	     }
	     response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	     
	     
	     String s = request.getHeader("USER-AGENT").toLowerCase();
	     if(s.indexOf("firefox")>0){
	        response.setHeader("Content-disposition", "attachment; filename="
	              +new String("集团报货报表".getBytes("UTF-8"), "ISO8859-1") + ".xls");
	     }else {
	    	 response.setHeader("Content-Disposition", "attachment;filename=%e9%9b%86%e5%9b%a2%e6%8a%a5%e8%b4%a7%e6%8a%a5%e8%a1%a8.xls");}
	        response.flushBuffer();
				workbook.write(response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 @GetMapping("/yieldReportForm")
	 public String yieldReportForm(Model model) {
		 List<Mblx> mbList = baoHuoService.selectMbs();
		model.addAttribute("mbList", mbList);
		model.addAttribute("startTime", Timeutil.currentTime());
		model.addAttribute("endTime", Timeutil.currentTime());
		 model.addAttribute("mbId","0");
		 return "yieldReportForm";
	 }
	 
	 @PostMapping("/yieldReportFormData")
	 @ResponseBody
	 public JSONObject yieldReportFormData(String mbId, String startTime,String endTime,int page,int rows) {
		 List<HashMap<String,Object>> hotelReportFormlist=new ArrayList<>();
		 HashMap<String,Object> map=new HashMap<String,Object>();
			if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			map.put("mbId", mbId);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
            PageHelper.startPage(page,rows);
			hotelReportFormlist=reportFormService.selectyieldReportFormtlist(map);
			List<HashMap<String,Object>> mergeCells=new ArrayList<>();
			int i=0;
			int pp=0;
			String mbName="kongnull";
			HashMap<String,Object> mergeCell=new HashMap<String,Object>();
			for(HashMap<String,Object> hotelReportForm:hotelReportFormlist) {
				String mapmbName=hotelReportForm.get("mbName")==null?"":hotelReportForm.get("mbName").toString();
				if(!mapmbName.equals(mbName)) {
					mbName=mapmbName;
					if(i!=0) {
					 mergeCell.put("rowspan", pp);
					 mergeCells.add(mergeCell);
					 mergeCell=new HashMap<String,Object>();
					 pp=0;
					}
					mergeCell.put("index", i);
				}
				pp++;
				i++;
			}
			if(pp!=0) {
				 mergeCell.put("rowspan", pp);
				 mergeCells.add(mergeCell);
			}
	        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(hotelReportFormlist);
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("total", pageInfo.getTotal());
	        jsonObject.put("rows", pageInfo.getList());
	        jsonObject.put("mergeCells",mergeCells);
		 return jsonObject;
	 } 
	 
	 
	 @GetMapping("/yielddownload")
	 public void  yielddownload(HttpServletResponse response,HttpServletRequest request,String mbId,String startTime,String endTime) {
			if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			 List<HashMap<String,Object>> hotelReportFormlist=new ArrayList<>();
			 HashMap<String,Object> map=new HashMap<String,Object>();
				map.put("mbId", mbId);
				map.put("startTime", startTime);
				map.put("endTime", endTime);
				 hotelReportFormlist=reportFormService.selectyieldReportFormtlist(map);
	        try {
	     HSSFWorkbook workbook = new HSSFWorkbook();
	     HSSFSheet sheet = workbook.createSheet("生态园生产量报表");
	     sheet.createFreezePane( 0, 3, 0, 3 );
         sheet.setColumnWidth(0, 20 * 256);
         sheet.setColumnWidth(1, 20 * 256);
         sheet.setColumnWidth(2, 20 * 256);
         sheet.setColumnWidth(3, 20 * 256);
         CellRangeAddress ffd = new CellRangeAddress(0, 0, 0, 4);
         sheet.addMergedRegion(ffd);
          ffd = new CellRangeAddress(1, 1, 1, 4);
         sheet.addMergedRegion(ffd);
	     HSSFRow rowL = sheet.createRow(2);
	     String[] to=new String[] {"物料编码","物料名称","物料规格","计量单位","数量"};
	     for(int i=0;i<to.length;i++) {
	    	 HSSFCell cell =rowL.createCell(i);
	    	 cell.setCellValue(to[i]);
	    	 HSSFCellStyle style = workbook.createCellStyle();
				HSSFFont font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
				style.setFont(font);
				cell.setCellStyle(style);
	     }
	     HSSFCellStyle jzcellStyle = workbook.createCellStyle(); 
	     jzcellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
	     jzcellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
         HSSFRow ff1=sheet.createRow(0);
         HSSFCell fgt=ff1.createCell(0);
         fgt.setCellValue("生态园生产量报表");
         fgt.setCellStyle(jzcellStyle);
         ff1.setHeightInPoints(40);
         ff1=sheet.createRow(1);
         fgt=ff1.createCell(0);
         fgt.setCellValue("日期范围");
         fgt=ff1.createCell(1);
         fgt.setCellValue(Timeutil.stringToString(startTime)+"--"+Timeutil.stringToString(endTime));
	     int i=3;
	     String pbane="kongnull";
	     int pi=0;
	     for (HashMap<String,Object> ma:hotelReportFormlist) {
	    	 HSSFRow row = sheet.createRow(i);
	    	 for(int r=0;r<to.length;r++) {
	    		 HSSFCell cell =row.createCell(r);
	    		 switch(r){
	    		 case 1000:
	    			 if(!pbane.equals(ma.get("mbName"))) {
			    	 cell.setCellValue(ma.get("mbName")==null?"":ma.get("mbName").toString());
			    	 cell.setCellStyle(jzcellStyle);
			    	 pbane=ma.get("mbName")==null?"":ma.get("mbName").toString();
			    	 if(i!=3) {
			    		  CellRangeAddress pp = new CellRangeAddress(pi, i-1, 0, 0);
			    		  sheet.addMergedRegion(pp);
			    	 }
	    				 pi=i;
			    	 }
	    			 break;
	    		 case 0:
			    	 cell.setCellValue(ma.get("wlbm")==null?"":ma.get("wlbm").toString());
	    			 break;
	    		 case 1:
			    	 cell.setCellValue(ma.get("wlmc")==null?"":ma.get("wlmc").toString());
	    			 break;
	    		 case 2:
			    	 cell.setCellValue(ma.get("wlgg")==null?"":ma.get("wlgg").toString());
	    			 break;
	    		 case 3:
			    	 cell.setCellValue(ma.get("jldw")==null?"":ma.get("jldw").toString());
	    			 break;
	    		 case 4:
			    	 cell.setCellValue(ma.get("sqsl")==null?"":ma.get("sqsl").toString());
	    			 break;
	    		 }
	    	 }
	    	 i++;
	     }
	     
//	     if(pi!=i) {
//	    	  CellRangeAddress pp = new CellRangeAddress(pi, i-1, 0, 0);
//    		  sheet.addMergedRegion(pp);
//	     }
	     response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	     
	     
	     String s = request.getHeader("USER-AGENT").toLowerCase();
	     if(s.indexOf("firefox")>0){
	        response.setHeader("Content-disposition", "attachment; filename="
	              +new String("生态园生产量报表".getBytes("UTF-8"), "ISO8859-1") + ".xls");
	     }else {
	    	 response.setHeader("Content-Disposition", "attachment;filename=%e7%94%9f%e6%80%81%e5%9b%ad%e7%94%9f%e4%ba%a7%e9%87%8f%e6%8a%a5%e8%a1%a8.xls");}
	        response.flushBuffer();
				workbook.write(response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 @PostMapping("/hotelReportFormthead")
	 @ResponseBody
	 public JSONObject hotelReportFormthead(String username,String mbId,String startTime,String endTime,String ziCai) {
		  JSONObject jsonObject = new JSONObject();
		  HashMap<String,Object> map=new HashMap<String,Object>();
			 map.put("userId",username);
				if(mbId==null) {
					mbId="0";
				}
				if(startTime==null) {
					startTime="";
				}
				if(endTime==null) {
					endTime="";
				}
				map.put("mbId", mbId);
				map.put("startTime", startTime);
				map.put("endTime", endTime);
				if("1".equals(ziCai)) {
					map.put("subState", "04");
				}
				 List<HashMap<String,Object>> fh=new ArrayList<>();
				List<HashMap<String,Object>> ddf=reportFormService.selectselectTssdlist(map);
				for(HashMap<String,Object> dd:ddf) {
					HashMap<String,Object> lf=new HashMap<String,Object>();
					lf.put("field", dd.get("dk").toString());
					lf.put("title", dd.get("dk").toString());
					lf.put("frozen", true);
					fh.add(lf);
				}
				HashMap<String,Object> lf=new HashMap<String,Object>();
				lf.put("field", "summary");
				lf.put("title", "合计");
				lf.put("frozen", true);
				fh.add(lf);
				jsonObject.put("data", fh);
		 return jsonObject;
	 }
	 
	 @PostMapping("/groupReportFormthead")
	 @ResponseBody
	 public JSONObject groupReportFormthead(String username,String mbId,String startTime,String endTime) {
		 HashMap<String,Object> map=new HashMap<String,Object>();
		 map.put("userId",username);
			if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			map.put("mbId", mbId);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
		 List<HashMap<String,Object>> stalls=reportFormService.selecjrrrlist(map);
		 List<HashMap<String,Object>>dff=new ArrayList<>();
		 List<HashMap<String,Object>>dff2=new ArrayList<>();
		 for(HashMap<String,Object>stall:stalls) {
			 HashMap<String,Object> p=new HashMap<String,Object>();
			 p.put("field", stall.get("jd").toString());
				p.put("title", stall.get("jd").toString());
				p.put("frozen", true);
				p.put("colspan", 3);
				p.put("width", "5%");
				dff.add(p);
		 }
		 for(HashMap<String,Object>stall:stalls) {
			 HashMap<String,Object> p=new HashMap<String,Object>();
			 p.put("title", "申请数量");
			 p.put("field", StrUtil.getMD5(stall.get("jd").toString())+"_sq");
			 p.put("frozen", true);
				p.put("width", "5%");
			 dff2.add(p);
			 p=new HashMap<String,Object>();
			 p.put("title", "自产量");
			 p.put("field", StrUtil.getMD5(stall.get("jd").toString())+"_zc");
			 p.put("frozen", true);
				p.put("width", "5%");
			 dff2.add(p);
			 p=new HashMap<String,Object>();
			 p.put("title", "库存量");
			 p.put("field", StrUtil.getMD5(stall.get("jd").toString())+"_kc");
			 p.put("frozen", true);
				p.put("width", "5%");
			 dff2.add(p);
		 }
		 
		 JSONObject jsonObject = new JSONObject();
		 jsonObject.put("data", dff);
		 jsonObject.put("data2", dff2);
		 return jsonObject;
	 }
	 
	 
	 @GetMapping("/deliveryReportForm")
	 public String deliveryReportForm(Model model,String username,String mbId,String startTime,String endTime) {
		 model.addAttribute("username", username);
		 List<Mblx> mbList = baoHuoService.selectMbs();
		model.addAttribute("mbList", mbList);
		model.addAttribute("startTime", Timeutil.currentTime());
		model.addAttribute("endTime", Timeutil.currentTime());
		 model.addAttribute("mbId","0");
		 HashMap<String,Object> map=new HashMap<String,Object>();
		 map.put("userId",username);
			if(mbId==null) {
				mbId="0";
			}
			map.put("mbId", mbId);
			map.put("startTime", Timeutil.currentTime());
			map.put("endTime", Timeutil.currentTime());
		 List<HashMap<String,Object>> stalls=reportFormService.selecjrrrlist(map);
		 model.addAttribute("stalls", stalls);
		 List<HashMap<String,Object>>dff=new ArrayList<>();
		 for(HashMap<String,Object>stall:stalls) {
			 HashMap<String,Object> p=new HashMap<String,Object>();
			 p.put("name", "申请数量");
			 p.put("value", StrUtil.getMD5(stall.get("jd").toString())+"_sq");
			 dff.add(p);
			 p=new HashMap<String,Object>();
			 p.put("name", "发货数量");
			 p.put("value", StrUtil.getMD5(stall.get("jd").toString())+"_fh");
			 dff.add(p);
			 p=new HashMap<String,Object>();
			 p.put("name", "无法配送");
			 p.put("value", StrUtil.getMD5(stall.get("jd").toString())+"_wf");
			 dff.add(p);
		 }
		 model.addAttribute("stallsp", dff);
		 return "deliveryReportForm";
	 }
	 
	 @PostMapping("/deliveryReportFormData")
	 @ResponseBody
	 public JSONObject deliveryportFormData(String username,String mbId,String startTime,String endTime,int page,int rows) {
		 List<HashMap<String,Object>> hotelReportFormlist=new ArrayList<>();
		 HashMap<String,Object> map=new HashMap<String,Object>();
		 map.put("userId",username);
			if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			map.put("mbId", mbId);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			List<HashMap<String,Object>> ddf=reportFormService.selecjrrrlist(map);
			String stalls="(";
			for (HashMap<String,Object> dd:ddf) {
				stalls+="'"+dd.get("jd").toString()+"' as \""+StrUtil.getMD5(dd.get("jd").toString())+"\",";
			}
			stalls+="^^ls^^";
			stalls=stalls.replace(",^^ls^^", ")");
			if(stalls.equals("(^^ls^^")) {
				stalls="()";
			}
			map.put("stalls", stalls);
            PageHelper.startPage(page,rows);
			hotelReportFormlist=reportFormService.selectdeliveryReportFormtlist(map);
			List<HashMap<String,Object>> mergeCells=new ArrayList<>();
			int i=0;
			int pp=0;
			String mbName="kongnull";
			HashMap<String,Object> mergeCell=new HashMap<String,Object>();
			for(HashMap<String,Object> hotelReportForm:hotelReportFormlist) {
				String mapmbName=hotelReportForm.get("mbName")==null?"":hotelReportForm.get("mbName").toString();
				if(!mapmbName.equals(mbName)) {
					mbName=mapmbName;
					if(i!=0) {
					 mergeCell.put("rowspan", pp);
					 mergeCells.add(mergeCell);
					 mergeCell=new HashMap<String,Object>();
					 pp=0;
					}
					mergeCell.put("index", i);
				}
				pp++;
				i++;
			}
			if(pp!=0) {
				 mergeCell.put("rowspan", pp);
				 mergeCells.add(mergeCell);
			}
	        PageInfo<HashMap<String, Object>> pageInfo = new PageInfo<>(hotelReportFormlist);
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("total", pageInfo.getTotal());
	        jsonObject.put("rows", pageInfo.getList());
	        jsonObject.put("mergeCells",mergeCells);
		 return jsonObject;
	 } 
	 
	 
	 @GetMapping("/deliverydownload")
	 public void  deliverydownload(HttpServletResponse response,HttpServletRequest request,String username,String mbId,String startTime,String endTime) {
			if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			 List<HashMap<String,Object>> hotelReportFormlist=new ArrayList<>();
			 HashMap<String,Object> map=new HashMap<String,Object>();
			 map.put("userId",username);
				map.put("mbId", mbId);
				map.put("startTime", startTime);
				map.put("endTime", endTime);
				List<HashMap<String,Object>> ddf=reportFormService.selecjrrrlist(map);
				String stalls="(";
				for (HashMap<String,Object> dd:ddf) {
					stalls+="'"+dd.get("jd").toString()+"' as \""+StrUtil.getMD5(dd.get("jd").toString())+"\",";
				}
				stalls+="^^ls^^";
				stalls=stalls.replace(",^^ls^^", ")");
				if(stalls.equals("(^^ls^^")) {
					stalls="()";
				}
				map.put("stalls", stalls);
				 hotelReportFormlist=reportFormService.selectdeliveryReportFormtlist(map);
	        try {
	     HSSFWorkbook workbook = new HSSFWorkbook();
	     HSSFSheet sheet = workbook.createSheet("仓库发货表");
	     sheet.createFreezePane( 0, 4, 0, 4 );
         sheet.setColumnWidth(0, 20 * 256);
         sheet.setColumnWidth(1, 20 * 256);
         sheet.setColumnWidth(2, 20 * 256);
         sheet.setColumnWidth(3, 20 * 256);
         CellRangeAddress ffd = new CellRangeAddress(0, 0, 0, 4+ddf.size()*3);
         sheet.addMergedRegion(ffd);
          ffd = new CellRangeAddress(1, 1, 1, 4+ddf.size()*3);
         sheet.addMergedRegion(ffd);
	     HSSFRow rowL = sheet.createRow(2);
	     String[] to=new String[] {"报货模板","物料编码","物料名称","物料规格","计量单位"};
	     for(int i=0;i<to.length;i++) {
	    	 HSSFCell cell =rowL.createCell(i);
	    	 cell.setCellValue(to[i]);
	    	 HSSFCellStyle style = workbook.createCellStyle();
				HSSFFont font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
				style.setFont(font);
				style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
				style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
				cell.setCellStyle(style);
			    ffd = new CellRangeAddress(2, 3, i, i);
			    sheet.addMergedRegion(ffd);
	     }
	     int is=5;
		    HSSFRow rowLt=sheet.createRow(3);
	     for (HashMap<String,Object> dd:ddf) {
	    	 HSSFCell cell =rowL.createCell(is);
	    	 cell.setCellValue(dd.get("jd").toString());
	    	 HSSFCellStyle style = workbook.createCellStyle();
				HSSFFont font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
				style.setFont(font);
				cell.setCellStyle(style);
			    ffd = new CellRangeAddress(2, 2, is, is+2);
			    sheet.addMergedRegion(ffd); 
			for(int iifi=0;iifi<3;iifi++) {
				 cell =rowLt.createCell(is+iifi);
					 switch(iifi) {
					 case 0:
		    	      cell.setCellValue("申请数量");
		    	     break;
					 case 1:
			    	      cell.setCellValue("发货数量");
			    	     break;
					 case 2:
			    	      cell.setCellValue("无法配送");
			    	     break;
					 }
		    	  style = workbook.createCellStyle();
					 font = workbook.createFont();
					font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
					style.setFont(font);
					cell.setCellStyle(style);}
			    is++;is++;is++;
	     }
	     HSSFCellStyle jzcellStyle = workbook.createCellStyle(); 
	     jzcellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
	     jzcellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
         HSSFRow ff1=sheet.createRow(0);
         HSSFCell fgt=ff1.createCell(0);
         fgt.setCellValue("仓库发货表");
         fgt.setCellStyle(jzcellStyle);
         ff1.setHeightInPoints(40);
         ff1=sheet.createRow(1);
         fgt=ff1.createCell(0);
         fgt.setCellValue("日期范围");
         fgt=ff1.createCell(1);
         fgt.setCellValue(Timeutil.stringToString(startTime)+"--"+Timeutil.stringToString(endTime));
	     int i=4;
	     String pbane="kongnull";
	     int pi=0;
	     for (HashMap<String,Object> ma:hotelReportFormlist) {
	    	 HSSFRow row = sheet.createRow(i);
	    	 for(int r=0;r<to.length;r++) {
	    		 HSSFCell cell =row.createCell(r);
	    		 switch(r){
	    		 case 0:
	    			 if(!pbane.equals(ma.get("mbName"))) {
			    	 cell.setCellValue(ma.get("mbName")==null?"":ma.get("mbName").toString());
			    	 cell.setCellStyle(jzcellStyle);
			    	 pbane=ma.get("mbName")==null?"":ma.get("mbName").toString();
			    	 if(i!=4) {
			    		  CellRangeAddress pp = new CellRangeAddress(pi, i-1, 0, 0);
			    		  sheet.addMergedRegion(pp);
			    	 }
	    				 pi=i;
			    	 }
	    			 break;
	    		 case 1:
			    	 cell.setCellValue(ma.get("wlbm")==null?"":ma.get("wlbm").toString());
	    			 break;
	    		 case 2:
			    	 cell.setCellValue(ma.get("wlmc")==null?"":ma.get("wlmc").toString());
	    			 break;
	    		 case 3:
			    	 cell.setCellValue(ma.get("wlgg")==null?"":ma.get("wlgg").toString());
	    			 break;
	    		 case 4:
			    	 cell.setCellValue(ma.get("jldw")==null?"":ma.get("jldw").toString());
	    			 break;
	    		 default:
	    			// cell.setCellValue(ma.get(to[r])==null?"":ma.get(to[r]).toString());
	    			break;
	    		 }
	    	 }
	    	 int ldf=5;
			 for (HashMap<String,Object> dd:ddf) {
				String com= StrUtil.getMD5(dd.get("jd").toString());
				 HSSFCell cell =row.createCell(ldf);
				 cell.setCellValue(ma.get(com+"_sq")==null?"":ma.get(com+"_sq").toString());
				 cell =row.createCell(ldf+1);
				 cell.setCellValue(ma.get(com+"_fh")==null?"":ma.get(com+"_fh").toString());
				 cell =row.createCell(ldf+2);
				 cell.setCellValue(ma.get(com+"_wf")==null?"":ma.get(com+"_wf").toString());
				 ldf++;ldf++;ldf++;
			 }
	    	 i++;
	     }
	     
	     if(pi!=i) {
	    	  CellRangeAddress pp = new CellRangeAddress(pi, i-1, 0, 0);
    		  sheet.addMergedRegion(pp);
	     }
	     response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	     
	     
	     String s = request.getHeader("USER-AGENT").toLowerCase();
	     if(s.indexOf("firefox")>0){
	        response.setHeader("Content-disposition", "attachment; filename="
	              +new String("仓库发货表".getBytes("UTF-8"), "ISO8859-1") + ".xls");
	     }else {
	    	 response.setHeader("Content-Disposition", "attachment;filename=%e4%bb%93%e5%ba%93%e5%8f%91%e8%b4%a7%e8%a1%a8.xls");}
	        response.flushBuffer();
				workbook.write(response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
	 
	 @PostMapping("/deliveryReportFormthead")
	 @ResponseBody
	 public JSONObject deliveryReportFormthead(String username,String mbId,String startTime,String endTime) {
		 HashMap<String,Object> map=new HashMap<String,Object>();
		 map.put("userId",username);
			if(mbId==null) {
				mbId="0";
			}
			if(startTime==null) {
				startTime="";
			}
			if(endTime==null) {
				endTime="";
			}
			map.put("mbId", mbId);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
		 List<HashMap<String,Object>> stalls=reportFormService.selecjrrrlist(map);
		 List<HashMap<String,Object>>dff=new ArrayList<>();
		 List<HashMap<String,Object>>dff2=new ArrayList<>();
		 for(HashMap<String,Object>stall:stalls) {
			 HashMap<String,Object> p=new HashMap<String,Object>();
			 p.put("field", stall.get("jd").toString());
				p.put("title", stall.get("jd").toString());
				p.put("frozen", true);
				p.put("colspan", 3);
				p.put("width", "5%");
				dff.add(p);
		 }
		 for(HashMap<String,Object>stall:stalls) {
			 HashMap<String,Object> p=new HashMap<String,Object>();
			 p.put("title", "申请数量");
			 p.put("field", StrUtil.getMD5(stall.get("jd").toString())+"_sq");
			 p.put("frozen", true);
				p.put("width", "5%");
			 dff2.add(p);
			 p=new HashMap<String,Object>();
			 p.put("title", "发货数量");
			 p.put("field", StrUtil.getMD5(stall.get("jd").toString())+"_fh");
			 p.put("frozen", true);
				p.put("width", "5%");
			 dff2.add(p);
			 p=new HashMap<String,Object>();
			 p.put("title", "无法配送");
			 p.put("field", StrUtil.getMD5(stall.get("jd").toString())+"_wf");
			 p.put("frozen", true);
				p.put("width", "5%");
			 dff2.add(p);
		 }
		 
		 JSONObject jsonObject = new JSONObject();
		 jsonObject.put("data", dff);
		 jsonObject.put("data2", dff2);
		 return jsonObject;
	 }
}
