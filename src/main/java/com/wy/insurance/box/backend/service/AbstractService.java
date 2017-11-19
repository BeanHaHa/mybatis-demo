package com.wy.insurance.box.backend.service;

import com.wy.insurance.box.backend.dao.GeneralDao;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public abstract class AbstractService<T, PK extends Serializable, D extends GeneralDao<T, PK>> {

	protected abstract D getDao();

	@Transactional(readOnly = true)
	public T get(PK id) {
		return getDao().get(id);
	}

	@Transactional(readOnly = true)
	public ArrayList<T> getAll() {
		return getDao().getAll();
	}

	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	public void create(T obj) {
		getDao().create(obj);
	}

	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	public void update(T obj) {
		getDao().update(obj);
	}

	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	public void delete(PK id) {
		getDao().delete(id);
	}

	
	@Transactional(readOnly = true)
	public List<T> findByWhere(Map<String,?> parameters){
		return getDao().findByWhere(parameters);
	}
	
	@Transactional(readOnly = true)
	public Long getCountByWhere(Map<String,?> parameters){
		return getDao().getCountByWhere(parameters);
	}

}
