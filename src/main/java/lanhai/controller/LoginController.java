package lanhai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/log")
public class LoginController {

	@RequestMapping("/in")
	public String login() {
		System.out.println("login:...");
		return "login";
	}
	
	@RequestMapping("/toLogin")
	@ResponseBody
	public String toLogin(String userName,String password) {
		String info = "ok";
		//查看数据库中该用户名是否存在
		
		//如果存在则验证密码
		return info;
	}
	
	@RequestMapping("/out")
	public String logout() {
		return "login";
	}
}
