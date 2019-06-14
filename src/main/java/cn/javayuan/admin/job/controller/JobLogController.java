package cn.javayuan.admin.job.controller;

import cn.javayuan.admin.common.controller.BaseController;
import cn.javayuan.admin.common.domain.QueryRequest;
import cn.javayuan.admin.common.exception.AdminException;
import cn.javayuan.admin.job.domain.JobLog;
import cn.javayuan.admin.job.service.JobLogService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/job/log")
public class JobLogController extends BaseController {

    private String message;

    @Autowired
    private JobLogService jobLogService;

    @GetMapping
    @RequiresPermissions("jobLog:view")
    public Map<String, Object> jobLogList(QueryRequest request, JobLog log) {
        return getDataTable(this.jobLogService.findJobLogs(request, log));
    }

    @DeleteMapping("/{jobIds}")
    @RequiresPermissions("jobLog:delete")
    public void deleteJobLog(@NotBlank(message = "{required}") @PathVariable String jobIds) throws AdminException {
        try {
            String[] ids = jobIds.split(StringPool.COMMA);
            this.jobLogService.deleteJobLogs(ids);
        } catch (Exception e) {
            message = "删除调度日志失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("jobLog:export")
    public void export(QueryRequest request, JobLog jobLog, HttpServletResponse response) throws AdminException {
        try {
            List<JobLog> jobLogs = this.jobLogService.findJobLogs(request, jobLog).getRecords();
            ExcelKit.$Export(JobLog.class, response).downXlsx(jobLogs, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }
}
