package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author Tians
 * @Date 2023/9/1 10:30
 * @Version 17
 */

/**
 * 菜单
 */
@Mapper
public interface SetmealMapper {
    /**
     * 根据分类id查询菜品数量
     *
     * @param id
     * @return
     */
    @Select("select count(*) from setmeal where category_id = #{id}")
    Integer countByCategoryId(long id);

    /**
     * 新增套餐
     *
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 分页查询套餐
     *
     * @param setmealDTO
     * @return
     */
    Page<SetmealVO> selectByPage(SetmealPageQueryDTO setmealDTO);

    /**
     * 通过id查询
     *
     * @param id
     */
    SetmealVO selectById(Long id);

    /**
     * 通过id删除
     *
     * @param id
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long id);

    /**
     * 修改套餐
     *
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 套餐起售、停售
     *
     * @param status
     * @param id
     */
    @Update("update setmeal set status = #{status} where id = #{id}")
    void enableAndDisable(Integer status, Long id);

    /**
     * 根据分类id查询套餐
     *
     * @param categoryId
     * @return
     */
    @Select("select * from setmeal where category_id = #{categoryId}")
    List<Setmeal> selectListByCategoryId(Long categoryId);

    /**
     * 根据分类id查询已售套餐
     *
     * @param categoryId
     * @return
     */
    List<Setmeal> selectListByCategoryIdWithStatus(Long categoryId);

    /**
     * 查询套餐启停售
     *
     * @param status
     * @return
     */
    @Select("select count(*) from setmeal where status = #{status}")
    Integer countSetmeal(Integer status);
}
