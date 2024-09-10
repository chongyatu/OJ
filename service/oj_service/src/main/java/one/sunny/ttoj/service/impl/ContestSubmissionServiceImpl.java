package one.sunny.ttoj.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import one.sunny.commonutils.Constants;
import one.sunny.ttoj.entity.Contest;
import one.sunny.ttoj.entity.ContestSubmission;
import one.sunny.ttoj.mapper.ContestSubmissionMapper;
import one.sunny.ttoj.pojo.bo.LoginUserBo;
import one.sunny.ttoj.pojo.params.oj.ContestSubmissionParams;
import one.sunny.ttoj.pojo.vo.oj.ContestSubmissionVo;
import one.sunny.ttoj.service.ContestService;
import one.sunny.ttoj.service.ContestSubmissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContestSubmissionServiceImpl extends ServiceImpl<ContestSubmissionMapper, ContestSubmission> implements ContestSubmissionService {
    @Autowired
    private ContestService contestService;

    @Autowired
    private ContestSubmissionMapper contestSubmissionMapper;

    public List<ContestSubmissionVo> copyList(List<ContestSubmission> contestSubmissions){
        List<ContestSubmissionVo> contestSubmissionVos = new ArrayList<>();
        for (ContestSubmission contestSubmission : contestSubmissions){
            contestSubmissionVos.add(copy(contestSubmission));
        }
        return contestSubmissionVos;
    }
    public ContestSubmissionVo copy(ContestSubmission contestSubmission){
        ContestSubmissionVo contestSubmissionVo = new ContestSubmissionVo();
        BeanUtils.copyProperties(contestSubmission, contestSubmissionVo);
        contestSubmissionVo.setMemoryUse(contestSubmissionVo.getMemoryUse());
        return contestSubmissionVo;
    }

    @Override
    public Map<String, Object> getContestSubmission(ContestSubmissionParams contestSubmissionParams) {
        Long contestId = contestSubmissionParams.getContestId();
        Integer currentPage = contestSubmissionParams.getCurrentPage();
        Integer pageSize = contestSubmissionParams.getPageSize();
        Long userId = contestSubmissionParams.getUserId();

        Page<ContestSubmission> page = new Page<>(currentPage, pageSize);
        contestSubmissionMapper.selectPage(page, new LambdaQueryWrapper<ContestSubmission>()
                .eq(ContestSubmission::getContestId, contestId)
                .eq(ContestSubmission::getUserId, userId)
                .orderByDesc(ContestSubmission::getSubmitTime)
        );

        List<ContestSubmission> contestSubmissions = page.getRecords();
        List<ContestSubmissionVo> contestSubmissionVos = copyList(contestSubmissions);
        long total = page.getTotal();

        Map<String, Object> contestSubmissionsByConditionMap = new HashMap<>();
        contestSubmissionsByConditionMap.put("contestSubmissions", contestSubmissionVos);
        contestSubmissionsByConditionMap.put("total", total);
        return contestSubmissionsByConditionMap;
    }
}
