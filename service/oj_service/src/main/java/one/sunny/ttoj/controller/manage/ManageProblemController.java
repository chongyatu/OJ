package one.sunny.ttoj.controller.manage;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import one.sunny.commonutils.MyFileUtil;
import one.sunny.commonutils.R;
import one.sunny.ttoj.pojo.params.manage.ManageProblemSaveParams;
import one.sunny.ttoj.pojo.params.oj.ProblemQueryParams;
import one.sunny.ttoj.pojo.vo.manage.ManageProblemVo;
import one.sunny.ttoj.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

@Slf4j
@Api("manage_problem_service")
@RestController
@RequestMapping("manage/problem")
@CrossOrigin
public class ManageProblemController {
    @Autowired
    private ProblemService problemService;

    @ApiOperation(value = "带条件分页查询题目列表")
    @PostMapping("getProblemsByCondition")
    public R getProblemsByCondition(@RequestBody ProblemQueryParams problemQueryParams) {
        Map<String, Object> problemsByConditionMap = problemService.getProblemsByCondition(problemQueryParams, true);
        Long total = (Long) problemsByConditionMap.get("total");
        List<ManageProblemVo> manageProblemVos = (List<ManageProblemVo>) problemsByConditionMap.get("problems");
        return R.ok().data("problems", manageProblemVos).data("total", total);
    }

    @ApiOperation("通过题目id修改题目是否可见")
    @PostMapping("updateProblemVisibility/{problemId}/{visible}")
    public R updateProblemVisibility(@PathVariable("problemId") Long problemId,
                                     @PathVariable("visible") Boolean visible) {
        Boolean hasChange = problemService.updateProblemVisibility(problemId, visible);
        return hasChange ? R.ok().message("修改成功") : R.ok().message("修改失败");
    }

    @ApiOperation("通过id获得单个题目")
    @GetMapping("{id}")
    public R getProblemById(@PathVariable("id") Long id) {
        ManageProblemVo problem = problemService.getProblemById(id, true);
        return R.ok().data("problem", problem);
    }

    @ApiOperation("保存题目")
    @PostMapping("save")
    public R saveProblem(@RequestPart("testcase") MultipartFile testcase,
                         @RequestPart("adminProblemSaveParams") ManageProblemSaveParams problemSaveParams) throws IOException {
//        ManageProblemSaveParams problemSaveParams = JSON.parseObject(adminProblemSaveParams, ManageProblemSaveParams.class);
        File testcaseFile = MyFileUtil.MultipartFileToFile(testcase);
        Boolean save = problemService.saveProblem(testcaseFile, problemSaveParams);
        return save ? R.ok().message("保存成功") : R.error().message("保存失败");
    }

    @ApiOperation("修改题目")
    @PostMapping("update")
    public R updateProblem(@RequestPart(value = "testcase", required = false) MultipartFile testcase,
                           @RequestPart("adminProblemSaveParams") ManageProblemSaveParams problemSaveParams) throws IOException {
//        ManageProblemSaveParams problemSaveParams = JSON.parseObject(adminProblemSaveParams, ManageProblemSaveParams.class);
        File testcaseFile = MyFileUtil.MultipartFileToFile(testcase);
        Boolean update = problemService.updateProblem(testcaseFile, problemSaveParams);
        return update ? R.ok().message("修改成功") : R.ok().message("修改失败");
    }

    @ApiOperation("根据problemId删除题目")
    @PostMapping("delete/{problemId}")
    public R deleteProblem(@PathVariable("problemId") Long problemId){
        boolean remove = problemService.deleteProblemById(problemId);
        return remove ? R.ok().message("删除成功") : R.error().message("删除失败");
    }
}
