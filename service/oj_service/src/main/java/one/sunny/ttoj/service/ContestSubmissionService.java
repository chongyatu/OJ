package one.sunny.ttoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import one.sunny.ttoj.entity.ContestSubmission;
import one.sunny.ttoj.pojo.params.oj.ContestSubmissionParams;

import java.util.Map;


public interface ContestSubmissionService extends IService<ContestSubmission> {
    Map<String, Object> getContestSubmission(ContestSubmissionParams contestSubmissionParams);
}
