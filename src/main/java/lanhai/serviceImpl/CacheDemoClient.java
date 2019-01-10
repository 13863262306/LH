package lanhai.serviceImpl;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import lanhai.entity.BhUser;
import lanhai.service.CacheDemoService;
import utils.StrUtil;

@Component
public class CacheDemoClient {
	@Resource
	private CacheDemoService cacheDemoService;
	
	public void setCacheDemo(HttpServletRequest request,HttpServletResponse response,BhUser BhUser) {
//		Cookie[] cookies = request.getCookies();
//		String my=null;
//		String eas=null;
		String uuid=StrUtil.getUUID();
//		if(cookies!=null) {
//		for(Cookie c :cookies ){
//			if(c.getName().equals("EasPortalUserId")) {
//				eas=c.getValue();
//			}
//		}}else {
//			request.getSession().setAttribute("uuid", uuid);
//			my=uuid;
//		}
//		if(eas!=null) {
//			cacheDemoService.findPut(eas, BhUser);
//		}else {
//			cacheDemoService.findPut(my, BhUser);
//		}
		String id=null;
		Cookie[] cookies = request.getCookies();
		if(cookies!=null) {
		for(Cookie c :cookies ){
		if(c.getName().equals("BHSessionId")) {
			id=c.getValue();
		}
	}}
		if(id==null) {
		Cookie cookie = new Cookie("BHSessionId",uuid);
		response.addCookie(cookie);
		id=uuid;
		}
		cacheDemoService.findPut(id, BhUser);
	}
	
	public BhUser getCacheDemo(HttpServletRequest request) {
//		Cookie[] cookies = request.getCookies();
//		String my=null;
//		String eas=null;
//		BhUser BhUser=new BhUser();
//		for(Cookie c :cookies ){
//			if(c.getName().equals("EasPortalUserId")) {
//				eas=c.getValue();
//			}
//		}
//		if(eas!=null) {
//			BhUser=cacheDemoService.findById(eas, null);
//		}else {
//			my=(String)request.getSession().getAttribute("uuid");
//			BhUser=cacheDemoService.findById(my, null);
//		}
		String id=null;
		Cookie[] cookies = request.getCookies();
		if(cookies!=null) {
		for(Cookie c :cookies ){
		if(c.getName().equals("BHSessionId")) {
			id=c.getValue();
		   }
	    }}
		if(id==null) {
			return new BhUser();
		}
		BhUser BhUser=cacheDemoService.findById(id, null);
		return BhUser;
	}
}
