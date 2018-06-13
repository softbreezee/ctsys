/**
 * 
 */
/**
 * @author Leon
 *
 */
package cn.ctsys.core.action;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseAction extends ActionSupport{
	protected Map<String,Object> tempMap = new HashMap<String,Object>();

	public Map<String, Object> getTempMap() {
		return tempMap;
	}

	
	
}