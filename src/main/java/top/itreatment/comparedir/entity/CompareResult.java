package top.itreatment.comparedir.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.itreatment.comparedir.core.CompareType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareResult<T> {

    private GlobalInfo<T> oneGlobalInfo;
    private GlobalInfo<T> twoGlobalInfo;
    private CompareType match;

}
