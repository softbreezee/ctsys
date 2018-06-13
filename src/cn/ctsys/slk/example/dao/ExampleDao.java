package cn.ctsys.slk.example.dao;

import java.io.Serializable;
import java.util.List;

import cn.ctsys.core.dao.BaseDao;
import cn.ctsys.slk.example.entity.Example;
import cn.ctsys.slk.example.entity.Task;

public interface ExampleDao extends BaseDao<Example> {
	
	/**
	 * 保存算例
	 * @param example
	 */
//	void save(Example example);

	/**
	 * 得到所有算例
	 * @return
	 */
//	List<Example> getAll();
	/**
	 * 根据算例id查找对应的tasks集合
	 * @param eid
	 * @return
	 */
	List<Task> getTasksByEid(int eid);
	
	/**
	 * 根据id查找IE算例
	 * 根据id查找OE算例
	 * 根据id查找IF算例
	 * 根据id查找OF算例
	 * @param id
	 * @return
	 */
	Example findById(int id);

	/**
	 * 修改算例
	 * @param example
	 */
//	void update(Example example);
	
	/**
	 * 删除算例
	 * @param example
	 */
	void delete(Example example);
	
}
