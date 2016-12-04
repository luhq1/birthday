package com.hm.birthday.core.controller;

import java.util.Map;
import java.util.Set;

import org.apache.velocity.tools.generic.DateTool;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.hm.birthday.core.exception.ViewNullException;

public class AbstractDisplayController {
	
	private static final int DEFAULT_LIMIT = 10;
	private static final int FIRST_PAGE = 1;
	
	/**
	 * 分页绑定工具类
	 * @param limit
	 * @param currentNum
	 * @param setContainsTotalCount 是否返回总条数
	 * @return
	 */
	protected PageBounds buildPageBounds(Integer limit, Integer currentNum, boolean setContainsTotalCount) {
		PageBounds p = new PageBounds();
		if (limit == null) {
			limit = DEFAULT_LIMIT;
		}
		if (currentNum == null) {
			currentNum = FIRST_PAGE;
		}
		p.setLimit(limit);
		p.setPage(currentNum);
		p.setContainsTotalCount(true);
		return p;
	}
	
	/**
	 * 分页绑定工具类,默认显示总条数
	 * @param limit
	 * @param currentNum
	 * @return
	 */
	protected PageBounds buildPageBounds(Integer limit, Integer currentNum) {
		return buildPageBounds(limit, currentNum, true);
	}
	
	/**
	 * 返回视图
	 * @param view
	 * @return
	 */
	protected ModelAndView setModelView(String view) {
		return setModelView(view,null);
	}
	
	/**
	 * 带有存储共享数据得视图
	 * @param view
	 * @param modelMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected ModelAndView setModelView(String view,Map<String,Object> modelMap) {
		if (StringUtils.isEmpty(view)) {
			throw new ViewNullException("路径不能为空");
		}
		ModelAndView mv = new ModelAndView(view);
		if( !CollectionUtils.isEmpty(modelMap)) {
			Set<String> keys = modelMap.keySet();
			for(String key : keys) {
				if (modelMap.get(key) instanceof PageList) {
					modelMap.put("paginator", ((PageList)modelMap.get(key)).getPaginator());
				}
			}
			modelMap.put("dateTool", new DateTool());
			mv.addAllObjects(modelMap);
		}
		return mv;
	}
}
