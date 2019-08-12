package top.itreatment.comparedir.core.filenodecompare;

import top.itreatment.comparedir.core.Compare;
import top.itreatment.comparedir.core.CompareType;
import top.itreatment.comparedir.entity.FileNode;

public abstract class BaseFileNodeCompare implements Compare<FileNode> {
    @Override
    public abstract CompareType match(FileNode one, FileNode two);
}
