package cn.polister.infosys.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "分页查询结果")
public class PageResult<T> {
    @Schema(description = "当前页码", example = "1")
    private long current;

    @Schema(description = "每页大小", example = "10")
    private long size;

    @Schema(description = "总记录数", example = "100")
    private long total;

    @Schema(description = "总页数", example = "10")
    private long pages;

    @Schema(description = "当前页数据")
    private List<T> records;

    public static <T> PageResult<T> from(Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(page.getRecords());
        return result;
    }
}