package one.sunny.ttoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import one.sunny.ttoj.entity.Submission;
import one.sunny.ttoj.mapper.SubmissionMapper;
import one.sunny.ttoj.pojo.params.oj.SubmissionParams;
import one.sunny.ttoj.pojo.vo.oj.SubmissionVo;
import one.sunny.ttoj.service.SubmissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission> implements SubmissionService {
    @Autowired
    private SubmissionMapper submissionMapper;

    @Override
    public Map<String, Object> getSubmissionByCondition(SubmissionParams submissionParams) {
        Integer currentPage = submissionParams.getCurrentPage();
        Integer pageSize = submissionParams.getPageSize();
        Long userId = submissionParams.getUserId();
        Long problemId = submissionParams.getProblemId();

        Page<Submission> page = new Page<>(currentPage, pageSize);
        submissionMapper.selectPage(page, new LambdaQueryWrapper<Submission>()
                .eq(userId != null, Submission::getUserId ,userId)
                .eq(problemId != null, Submission::getProblemId, problemId)
                .orderByDesc(Submission::getSubmitTime)
        );

        List<Submission> submissions = page.getRecords();
        List<SubmissionVo> submissionVos = new ArrayList<>();
        submissions.forEach(item -> {
            SubmissionVo submissionVo = new SubmissionVo();
            BeanUtils.copyProperties(item, submissionVo);
            submissionVos.add(submissionVo);
        });
        long total = page.getTotal();
        HashMap<String, Object> map = new HashMap<>();
        map.put("submissions", submissionVos);
        map.put("total", total);
        return map;
    }
}
