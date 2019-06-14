package cn.javayuan.admin.job.controller;

import cn.javayuan.admin.common.annotation.Log;
import cn.javayuan.admin.common.controller.BaseController;
import cn.javayuan.admin.common.domain.QueryRequest;
import cn.javayuan.admin.common.exception.AdminException;
import cn.javayuan.admin.job.domain.Job;
import cn.javayuan.admin.job.service.JobService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/job")
public class JobController extends BaseController {

    private String message;

    @Autowired
    private JobService jobService;

    @GetMapping
    @RequiresPermissions("job:view")
    public Map<String, Object> jobList(QueryRequest request, Job job) {
        return getDataTable(this.jobService.findJobs(request, job));
    }

    @GetMapping("cron/check")
    public boolean checkCron(String cron) {
        try {
            return CronExpression.isValidExpression(cron);
        } catch (Exception e) {
            return false;
        }
    }

    @Log("新增定时任务")
    @PostMapping
    @RequiresPermissions("job:add")
    public void addJob(@Valid @RequestBody Job job) throws AdminException {
        try {
            this.jobService.createJob(job);
        } catch (Exception e) {
            message = "新增定时任务失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @Log("删除定时任务")
    @DeleteMapping("/{jobIds}")
    @RequiresPermissions("job:delete")
    public void deleteJob(@NotBlank(message = "{required}") @PathVariable String jobIds) throws AdminException {
        try {
            String[] ids = jobIds.split(StringPool.COMMA);
            this.jobService.deleteJobs(ids);
        } catch (Exception e) {
            message = "删除定时任务失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @Log("修改定时任务")
    @PutMapping
    @RequiresPermissions("job:update")
    public void updateJob(@Valid @RequestBody Job job) throws AdminException {
        try {
            this.jobService.updateJob(job);
        } catch (Exception e) {
            message = "修改定时任务失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @Log("执行定时任务")
    @GetMapping("run/{jobId}")
    @RequiresPermissions("job:run")
    public void runJob(@NotBlank(message = "{required}") @PathVariable String jobId) throws AdminException {
        try {
            this.jobService.run(jobId);
        } catch (Exception e) {
            message = "执行定时任务失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @Log("暂停定时任务")
    @GetMapping("pause/{jobId}")
    @RequiresPermissions("job:pause")
    public void pauseJob(@NotBlank(message = "{required}") @PathVariable String jobId) throws AdminException {
        try {
            this.jobService.pause(jobId);
        } catch (Exception e) {
            message = "暂停定时任务失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @Log("恢复定时任务")
    @GetMapping("resume/{jobId}")
    @RequiresPermissions("job:resume")
    public void resumeJob(@NotBlank(message = "{required}") @PathVariable String jobId) throws AdminException {
        try {
            this.jobService.resume(jobId);
        } catch (Exception e) {
            message = "恢复定时任务失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("job:export")
    public void export(QueryRequest request, Job job, HttpServletResponse response) throws AdminException {
        try {
            List<Job> jobs = this.jobService.findJobs(request, job).getRecords();
            ExcelKit.$Export(Job.class, response).downXlsx(jobs, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new AdminException(message);
        }
    }
}
