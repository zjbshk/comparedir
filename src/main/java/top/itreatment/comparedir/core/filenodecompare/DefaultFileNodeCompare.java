package top.itreatment.comparedir.core.filenodecompare;

import org.springframework.stereotype.Component;
import top.itreatment.comparedir.core.CompareType;
import top.itreatment.comparedir.entity.FileNode;


//@Component
public class DefaultFileNodeCompare extends BaseFileNodeCompare {
    @Override
    public CompareType match(FileNode one, FileNode two) {
        return one.getName().equals(two.getName()) ? CompareType.SAME : CompareType.DIFFERENT;
    }
}
