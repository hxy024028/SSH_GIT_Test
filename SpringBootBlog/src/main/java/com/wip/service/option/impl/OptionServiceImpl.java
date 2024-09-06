/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * DateTime: 2018/7/27 21:55
 **/
package com.wip.service.option.impl;

import com.wip.constant.ErrorConstant;
import com.wip.dao.OptionDao;
import com.wip.exception.BusinessException;
import com.wip.model.OptionsDomain;
import com.wip.service.option.OptionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    private OptionDao optionDao;

//    @Cacheable 注解：
//    这是 Spring 的缓存注解，用于标记一个方法的结果应该被缓存。
//    value 属性指定缓存的名称。这里的 "optionsCache" 是缓存的名称，用于标识缓存存储的位置。
//    key 属性指定缓存的键。这里的键是固定的字符串 'options_'。所有调用 getOptions() 方法的结果都将缓存到这个键下。
    @Override
    @Cacheable(value = "optionsCache", key = "'options_'")
    public List<OptionsDomain> getOptions() {
        return optionDao.getOptions();
    }

    //批量处理
    @Override
    @Transactional
    @CacheEvict(value = {"optionsCache", "optionCache"}, allEntries = true, beforeInvocation = true)
    public void saveOptions(Map<String, String> options) {
        if (null != options && !options.isEmpty()) {
            options.forEach(this::updateOptionByName);
        }
    }

    //单个处理更新
    @Override
    @Transactional
    @CacheEvict(value = {"optionsCache", "optionCache"}, allEntries = true, beforeInvocation = true)
    public void updateOptionByName(String name, String value) {
        if (StringUtils.isBlank(name))
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        OptionsDomain option = new OptionsDomain();
        option.setName(name);
        option.setValue(value);
        optionDao.updateOptionByName(option);
    }

    @Override
    @Cacheable(value = "optionCache", key = "'optionByname+' + #p0")
    public OptionsDomain getOptionByName(String name) {
        if (StringUtils.isBlank(name))
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return optionDao.getOptionByName(name);
    }
}
