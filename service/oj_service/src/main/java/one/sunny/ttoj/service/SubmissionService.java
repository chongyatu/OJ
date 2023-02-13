package one.sunny.ttoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import one.sunny.ttoj.entity.Submission;
import one.sunny.ttoj.pojo.params.oj.SubmissionParams;

import java.util.Map;

public interface SubmissionService extends IService<Submission> {

    Map<String, Object> getSubmissionByCondition(SubmissionParams submissionParams);
}
