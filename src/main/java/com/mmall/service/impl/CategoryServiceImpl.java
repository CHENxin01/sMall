package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by chen
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory (String categoryName ,Integer parentId) {
        //校验数据
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        //创建category
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类可以

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        //校验数据
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        //创建category
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParenId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归查询本节点的id及孩子节点的id
     * @param CategoryId
     * @return
     */
    public  ServerResponse<List<Integer>> SelectCategoryAndChildrenById(Integer CategoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, CategoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if (CategoryId != null){
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //递归算法，算出子节点
    public Set<Category> findChildCategory(Set<Category> categorySet,Integer CategoryId) {
        Category category = categoryMapper.selectByPrimaryKey(CategoryId);
        if (category != null) {
            categorySet.add(category);
        }
        //查找子节点
        //递归算法一定要有一个退出条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParenId(CategoryId);
        for (Category categoryItem : categoryList) {
            //自己调用自己
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }










}
