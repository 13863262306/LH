package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class StrUtil {

	private static final String SYMBOLS = "0123456789"; // 数字
	private static final Random RANDOM = new SecureRandom();
	
	public static boolean strIsNum(String str) {
		boolean Checked = true;
		try {
			Long strInt = Long.valueOf(str);
		}catch(Exception e){
			Checked = false;
			return Checked;
		}
		return Checked;
	}
	
	
	public static boolean strLikeDouble(String str) {
		boolean checked = true;
		try {
			Double dou = Double.valueOf(str);
		}catch(Exception e) {
			checked = false;
			return checked;
		}
		return checked;
	}
	
	
	public static  List <String> getNewList(List<String> li){
        List<String> list = new ArrayList<String>();//创建新的list
        for(int i=0; i<li.size(); i++){
            String str = li.get(i);  //获取传入集合对象的每一个元素
            if(!list.contains(str)){   //查看新集合中是否有指定的元素，如果没有则加入
                list.add(str);
            }
        }
        return list;  //返回集合
    }
	
	
	//生成随机字符串
	public static String getRandomChar(int length) {            
        char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
        buffer.append(chr[random.nextInt(36)]);
        }
        return buffer.toString();
        }
	
	
	   public static String getUUID(){
	        String uuid = UUID.randomUUID().toString();
	        return uuid.replace("-", "");
	    }
	   public static String getMD5(String str) {
		   String result = "";
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            md.update(str.getBytes());
	            byte b[] = md.digest();
	            int i;
	            StringBuffer buf = new StringBuffer("");
	            for (int offset = 0; offset < b.length; offset++) {
	                i = b[offset];
	                if (i < 0)
	                    i += 256;
	                if (i < 16)
	                    buf.append("0");
	                buf.append(Integer.toHexString(i));
	            }
	            result = buf.toString();
//	            System.out.println("MD5(" + str + ",32) = " + result);
//	            System.out.println("MD5(" + str + ",16) = " + buf.toString().substring(8, 24));
	            result=buf.toString().substring(8, 24);
	        } catch (NoSuchAlgorithmException e) {
	            System.out.println(e);
	        }
	        return result;
	   }
	   
	   
	   
	   
	   public static String getNonce_str(int count) {
			
			// 如果需要4位，那 new char[4] 即可，其他位数同理可得
			char[] nonceChars = new char[count];
			
			for (int index = 0; index < nonceChars.length; ++index) {
				nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
			}
			
			return new String(nonceChars);
		}
	   
	   
		public static String getNewTableHeadId(String oldId) {
			boolean isCopy = oldId.contains("----");
			System.out.println(oldId);
			if(isCopy) {
				int strLeng = oldId.length();
				String lastNum = oldId.substring(oldId.lastIndexOf("-")+1);
				Integer num = Integer.valueOf(lastNum);
				String a = oldId.substring(0, strLeng-lastNum.length());
				return a+(++num);
			}else {
				return oldId+"----1";
			}
		};
		
	   
	   
	   
}