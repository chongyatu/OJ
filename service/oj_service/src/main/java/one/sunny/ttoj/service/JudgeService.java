package one.sunny.ttoj.service;


import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.params.oj.ContestSubmitParams;
import one.sunny.ttoj.pojo.params.oj.JudgeParams;
import one.sunny.ttoj.pojo.params.oj.ArchiveJudgeParams;
import one.sunny.ttoj.pojo.params.oj.TestJudgeParams;
import one.sunny.ttoj.pojo.vo.oj.JudgeRes;
import org.springframework.transaction.annotation.Transactional;

public interface JudgeService {
    @Transactional
    JudgeRes judge(JudgeParams judgeParams);

    @Transactional
    JudgeRes problemArchiveJudge(ArchiveJudgeParams archiveJudgeParams);

    @Transactional
    JudgeRes contestSubmit(ContestSubmitParams contestSubmitParams);

    @Transactional
    JudgeRes testJudge(TestJudgeParams testJudgeParams);
}
