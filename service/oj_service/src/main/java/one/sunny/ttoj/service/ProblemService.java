package one.sunny.ttoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import one.sunny.ttoj.entity.Problem;
import one.sunny.ttoj.pojo.params.manage.ManageProblemSaveParams;
import one.sunny.ttoj.pojo.params.oj.ProblemQueryParams;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface ProblemService extends IService<Problem> {
    Map<String, Object> getProblemsByCondition(ProblemQueryParams problemQueryParams, Boolean admin);

    <K> K getProblemById(Long id, Boolean admin);

    @Transactional
    Boolean updateProblemVisibility(Long problemId, Boolean visible);

    @Transactional
    Boolean saveProblem(File testcase, ManageProblemSaveParams adminProblemSaveParams) throws IOException;

    @Transactional
    Boolean updateProblem(File testcase, ManageProblemSaveParams problemSaveParams) throws IOException;

    @Transactional
    boolean deleteProblemById(Long problemId);
}

