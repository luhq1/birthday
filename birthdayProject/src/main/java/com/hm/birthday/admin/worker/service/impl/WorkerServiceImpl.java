package com.hm.birthday.admin.worker.service.impl;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.hm.birthday.admin.worker.dao.WorkerMapper;
import com.hm.birthday.admin.worker.service.IWorkerService;
import com.hm.birthday.entity.WorkerInfo;
import com.hm.birthday.utils.DateUtils;


@Service("workerService")
public class WorkerServiceImpl implements IWorkerService {
	
	private final Logger logger = LoggerFactory.getLogger(WorkerServiceImpl.class);
	
	@Autowired
	private WorkerMapper workerMapper;
	
	@Override
	public PageList<WorkerInfo> queryWithPage(WorkerInfo w,PageBounds pageBounds) throws Exception {
		
		try {
			return workerMapper.queryWithPage(w, pageBounds);
		} catch (Exception e) {
			logger.error("插叙员工信息失败", e);
		};
		return null;
	}

	@Override
	public Map<String,Object> login(String phoneNum,String password) throws Exception {
		Map<String, Object> map = null;
		try {
			map = workerMapper.selectByPhoneNumAndPass(phoneNum, password);
			if (!CollectionUtils.isEmpty(map)) {
				// 查询用户生日的月份
				final String crrMonthBir = DateUtils.dateFormat(6, map.containsKey("birthday")? (Date) map.get("birthday"): null);
				final String nowMonth = DateUtils.dateFormat(6, new Date());
				boolean isLucky = false; // 是否已抽奖
				boolean isBirthday = false; // 是否本月生日
				if (nowMonth.equals(crrMonthBir)) {
					isBirthday = true;
				}
				map.put("birthday", DateUtils.dateFormat(3, (Date)map.get("birthday")));
				map.put("isLucky", isLucky); // 是否已抽奖
				map.put("isBirthday", isBirthday);// 是否生日
			}
		} catch (Exception e) {
			logger.error("用户登录查询失败", e);
			throw e;
		}
		
		return map;
	}

	@Override
	public int setFirstLogin(Integer id) throws Exception  {
		WorkerInfo workerInfo = new WorkerInfo();
		workerInfo.setId(id);
		workerInfo.setIsfirstLogin("02");
		workerInfo.setUpdateTime(new Date());
		int update = 0;
		try {
			update = workerMapper.updateByPrimaryKeySelective(workerInfo);
		} catch (Exception e) {
			logger.error("设置用户首次登陆的标志失败", e);
			throw e;
		}
		return update;
	}

	@Override
	public int AddWorker(WorkerInfo workerInfo) throws Exception {
		int count = 0;
		try {
			count = workerMapper.insertSelective(workerInfo);
		} catch (Exception e) {
			logger.error("用户信息插入失败", e);
			throw e;
		}
		return count;
	}

	@Override
	public int deleteWorker(Integer id) throws Exception  {
		try {
			return  workerMapper.deleteByPrimaryKey(id);
		} catch (Exception e) {
			logger.error("删除用户信息失败", e);
			throw e;
		}
	}
	
}
