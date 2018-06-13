/**
 * 
 */
/**
 * @author Leon
 *
 */
package cn.ctsys.slk.example.constant;

import java.util.HashMap;
import java.util.Map;

public class SlkConstant{
	//基本运输问题
	public static String TYPE_BASIC = "basic";
	//多堆场多港口
	public static String TYPE_MM = "mm";
	//考虑信息更新
	public static String TYPE_INFOUPDATE = "infoupdate";
	//考虑资源约束
	public static String TYPE_ZYYS = "zyys";
	//考虑多尺寸
	public static String TYPE_MULTSIZE = "multsize";
	public static Map<String,String> TYPE_MAP;
	static{
		TYPE_MAP = new HashMap<String,String>();
		
		TYPE_MAP.put(TYPE_BASIC,  "basic");
		TYPE_MAP.put(TYPE_MM, "mm");
		TYPE_MAP.put(TYPE_INFOUPDATE,  "infoupdate");
		TYPE_MAP.put(TYPE_ZYYS,  "zyys");
		TYPE_MAP.put(TYPE_MULTSIZE, "multsize");
		
		
		
		
	}
	
	
}