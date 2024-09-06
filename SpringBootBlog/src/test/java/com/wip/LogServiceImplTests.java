package com.wip;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.PageInfo;
import com.wip.model.LogDomain;
import com.wip.service.log.impl.LogServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogServiceImplTests {
	@Autowired
	private LogServiceImpl logService;
	
	/**
	 * 测试新增日志信息
	 */
	@Test
	public void addLog(){
		String action = "User Login";
        String data = "User logged in successfully";
        String ip = "192.168.1.1";
        Integer authorId = 1;
        logService.addLog(action, data, ip, authorId);
	}
	
	/**
	 * 测试通过分页的方式获取日志信息
	 */
	@Test
	public void testGetLogs() {
		int pageNum = 1;
        int pageSize = 2;
		PageInfo<LogDomain> log = logService.getLogs(pageNum, pageSize);
		if (log != null) {
            System.out.println("testGetAtts passed: PageInfo is not null");
            System.out.println("Total: " + log.getTotal());
            System.out.println("List size: " + log.getList().size());
        } else {
            System.out.println("testGetAtts failed: log is null");
        }
	}
}
