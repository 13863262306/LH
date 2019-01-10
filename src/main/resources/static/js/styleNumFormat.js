//千分
		function toThousands(num) {
		    var num = (num || 0).toString(), result = '';
		    while (num.length > 3) {
		        result = ',' + num.slice(-3) + result;
		        num = num.slice(0, num.length - 3);
		    }
		    if (num) { result = num + result; }
		    return result;
		}
		
		//去逗号
		function clearComma(str) { 
			str = str.replace(/^0+/,"");
			str = str.replace(/,/g, "");//取消字符串中出现的所有逗号 
			return str; 
			}
		//强制保留两位小数
		function toDecimal2(x) { 
     	   var f = parseFloat(x); 
     	   if (isNaN(f)) { 
     	    return false; 
     	   } 
     	   var f = Math.round(x*100)/100; 
     	   var s = f.toString(); 
     	   var rs = s.indexOf('.'); 
     	   if (rs < 0) { 
     	    rs = s.length; 
     	    s += '.'; 
     	   } 
     	   while (s.length <= rs + 2) { 
     	    s += '0'; 
     	   } 
     	   return s; 
     	  } 