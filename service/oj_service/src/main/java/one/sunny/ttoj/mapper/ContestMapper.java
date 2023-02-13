package one.sunny.ttoj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import one.sunny.ttoj.entity.Contest;
import one.sunny.ttoj.pojo.params.oj.ContestQueryParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestMapper extends BaseMapper<Contest> {

}
