// 导入必要的类和包
package com.wooqs.cgms.controller;

import com.wooqs.cgms.model.Log;
import com.wooqs.cgms.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/logs")
public class LogController {

    // 自动注入LogService实例
    @Autowired
    private LogService logService;

    // 获取所有Log对象
    @GetMapping
    public List<Log> getAllLogs() {
        return logService.getAllLogs();
    }

    // 通过userID获取Log对象
    @GetMapping("/{id}")
    public List<Log> getLogByUserId(@PathVariable Long id) {
        return logService.getLogByUserId(id);
    }


    // 添加Log对象
    @PostMapping
    public void addLog(@RequestBody Log log) {
        logService.addLog(log);
    }

    // 通过ID删除Log对象
    @DeleteMapping("/{id}")
    public void deleteLogById(@PathVariable Long id) {
        logService.deleteLogById(id);
    }
}