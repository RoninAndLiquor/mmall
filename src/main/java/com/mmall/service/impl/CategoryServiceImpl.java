package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMsg("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int insert = categoryMapper.insert(category);
        if(insert>0){
            return ServerResponse.createBySuccess("品类添加成功");
        }
        return ServerResponse.createByErrorMsg("品类添加失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMsg("更新品类名称参数有误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if(count>0){
            return ServerResponse.createBySuccess("更新品类名称成功");
        }
        return ServerResponse.createByErrorMsg("更新品类名称失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId) {
        List<Category> categories = categoryMapper.selectCategotyChildrenByParentId(parentId);
        if(CollectionUtils.isEmpty(categories)){
            LOG.info("*** 未找到当前分类的子分类 ***");
        }
        return ServerResponse.createBySuccess(categories);
    }

    /**
     * 递归查找所有的子品类
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        Set<Category> childCategory = findChildCategory(categorySet, categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for (Category category:categorySet){
                categoryIdList.add(category.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    /**
     * 递归算法
     * @param categories
     * @param categoryId
     * @return
     */
    private Set<Category> findChildCategory(Set<Category> categories,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categories.add(category);
        }
        List<Category> categoriesList = categoryMapper.selectCategotyChildrenByParentId(categoryId);
        for(Category cate : categoriesList){
            findChildCategory(categories,cate.getId());
        }
        return categories;
    }
}
