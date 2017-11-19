package com.wy.insurance.box.backend.dao;

import org.apache.ibatis.exceptions.PersistenceException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public interface GeneralDao<T, PK extends Serializable> {
    /**
     *  get obj of type T by the primary key 'id'
     * @param id
     * @return
     * @throws PersistenceException
     */
    T get(PK id) throws PersistenceException;

    /**
     * get all objects of type T
     * @return
     * @throws PersistenceException
     */
    ArrayList<T> getAll() throws PersistenceException;

    /**
     * insert an object of type T into the database
     * @param obj
     * @throws PersistenceException
     */
    void create(T obj) throws PersistenceException;

    /**
     * update an object of type T
     * @param obj
     * @throws PersistenceException
     */
    void update(T obj) throws PersistenceException;

    /**
     * delete an object of type T
     * @param id
     * @throws PersistenceException
     */
    void delete(PK id) throws PersistenceException;

    /**
     * 按条件查找
     * @param parameters
     * @return
     */
    List<T> findByWhere(Map<String, ?> parameters);

    /**
     * 获取查找的数量
     * @param parameters
     * @return
     */
    Long getCountByWhere(Map<String, ?> parameters);
}
