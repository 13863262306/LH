	var isIE = navigator.userAgent.toLowerCase().search(/(msie\s|trident.*rv:)([\w.]+)/)!= -1;
	if (isIE) {
		document.writeln("<OBJECT classid=\"CLSID:3F367B74-92D9-4C5E-AB93-234F8A91D5E6\" height=1 id=XTXAPP  style=\"HEIGHT: 1px; LEFT: 10px; TOP: 28px; WIDTH: 1px\" width=1 VIEWASTEXT>");
		document.writeln("</OBJECT>");
		//XTXAPP.CertListFormElement = "form1.login_test_certlst";
		//XTXAPP.ServerRan = "X7nVg+dKzpQ+xMTrZtshBS0vww4jpoO2prAP7GMY8IA=";
		//XTXAPP.ServerCert = "MIID+TCCAuGgAwIBAgIKMAAAAAAAAJA3aTANBgkqhkiG9w0BAQUFADBSMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTEYMBYGA1UECwwPUHVibGljIFRydXN0IENBMRowGAYDVQQDDBFQdWJsaWMgVHJ1c3QgQ0EtMTAeFw0xMTA0MjgxNjAwMDBaFw0xNjA0MjYxNjAwMDBaMDgxCzAJBgNVBAYTAkNOMRUwEwYDVQQKDAzljJfkuqzpk7booYwxEjAQBgNVBAMMCei1teawuOecgTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAzx+IS4BD69SPA4NPFUiOmk01Hqpgv/Q5FPQydHK3zK1c2sIK1L/C2jabjFzlhlQIKaqPpLYjOKjL4WTLr13GVSsFUqspG98tFdgWarHX2tlzsfVfZ6yqxuiCvfY4xX8VKUeie9pUWF2D5+ozvXN41S9dkgDp9XvDmZiprbopJRUCAwEAAaOCAW0wggFpMB8GA1UdIwQYMBaAFKw77K8Mo1AO76+vtE9sO9vRV9KJMAwGA1UdDwQFAwMHqAAwCQYDVR0TBAIwADCBrwYDVR0fBIGnMIGkMG2ga6BppGcwZTELMAkGA1UEBhMCQ04xDTALBgNVBAoMBEJKQ0ExGDAWBgNVBAsMD1B1YmxpYyBUcnVzdCBDQTEaMBgGA1UEAwwRUHVibGljIFRydXN0IENBLTExETAPBgNVBAMTCGNhM2NybDYyMDOgMaAvhi1odHRwOi8vbGRhcC5iamNhLm9yZy5jbi9jcmwvcHRjYS9jYTNjcmw2Mi5jcmwwHQYFKlYLBwEEFFNGMzcyNTI2MTk4MjEyMDI2Njc5MB0GBSpWCwcIBBRTRjM3MjUyNjE5ODIxMjAyNjY3OTAgBghghkgBhvhEAgQUU0YzNzI1MjYxOTgyMTIwMjY2NzkwGwYIKlaGSAGBMAEEDzMwNTAwMDEwMTIyMTQxMjANBgkqhkiG9w0BAQUFAAOCAQEABtSWzYq6zscNKvV760VI5VOO6ktM4nDfl2m9Zm2wqHAXMcZmqFx1Tc8bQiBjPfJgyhSW68kpL69GET+8SPARPPuOKNsq+7OtBjxjxePVimSZQyEL89oPSADhOOW/ikPcGLcHHELj8FxyrgtCWswyFV8iQ+4a3Gmos4wKD/3fi9WiJIXqfzxLc48JB36jlZjkwXCxSENUbABmwW8f6+8m1HEX/2uPw2YrFheUAdd/D5eOlhUANlcPDvtIMpOR+ksH0fQh9B1BLFj37fR6URNBC/X1sSUiKe+SeTAR+ln86bKIN9yE/C4pOQC5NqaiuNfGbxyAJvquxYPCiczeXt7XCw==";
		//XTXAPP.ServerSignedData = "uQcGafJPE0dIQZhhCYU1R5m1IQli+TTx+RZ87S6K6JBNFPI8lCMfahBKIMnyYjSZvR/EIZ63XkatR6zcbNSzIyP/KAOzfuFNRfc5F/FZFrvakXoEUcUwbQvgtu7QpWDlkJs7VjEHpqCMBOnoIw/eUxO9tUnsdKzRLGk3mSTKCpM=";
		if(XTXAPP!=null){
		XTXAPP.SOF_GetVersion(); 
		XTXAPP.EnableSoftDevice(true, "");
		}else{
			alert("请安装BJCA证书助手");
		}
		//XTXAPP.SetUserConfig(6, "1");
	} else {
		document.writeln("<embed id=XTXAPP type=application/x-xtx-axhost clsid={3F367B74-92D9-4C5E-AB93-234F8A91D5E6} event_OnUsbkeyChange=OnUsbKeyChange width=1 height=1 />");
		//XTXAPP0.SOF_GetVersion(); 
		//XTXAPP = document.getElementById("XTXAPP0");
		if(XTXAPP!=null){
		XTXAPP.SOF_GetVersion(); 
		XTXAPP.EnableSoftDevice(true, "");
              }else{
            	  alert("请安装BJCA证书助手");
		}
		//XTXAPP.SetUserConfig(6, "1");
	}
	
/*var getBJCAkey=function(){
	var BJCAkey="";
	var strUserList = XTXAPP.SOF_GetUserList();
	while (1) {
		var i = strUserList.indexOf("&&&");
		if (i <= 0 ) {
			break;
		}
		var strOneUser = strUserList.substring(0, i);
		var strName = strOneUser.substring(0, strOneUser.indexOf("||"));
		var strUniqueID = strOneUser.substring(strOneUser.indexOf("||") + 2, strOneUser.length);
		if(strName=='测试用户(测试)'){
			var cert = XTXAPP.SOF_ExportUserCert(strUniqueID);
		    var exp = new Date(); 
		    exp.setTime(exp.getTime() +24*60*60*1000); 
			document.cookie="BJCAkey="+cert+ ";expires=" + exp.toGMTString(); 
			console.log(cert);
		}
		var len = strUserList.length;
		strUserList = strUserList.substring(i + 3,len);
	}
	return BJCAkey;
}*/