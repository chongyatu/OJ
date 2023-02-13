package one.sunny.ttoj.pojo.bo;

import lombok.Data;

@Data
public class SelectMenuKeyAndNameBo {
    private Long id;
    private String key;
    private String name;
    private Long parentId;
}
