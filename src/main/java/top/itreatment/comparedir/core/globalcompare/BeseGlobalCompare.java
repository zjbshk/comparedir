package top.itreatment.comparedir.core.globalcompare;

import top.itreatment.comparedir.core.Compare;
import top.itreatment.comparedir.core.CompareType;
import top.itreatment.comparedir.entity.FileNode;
import top.itreatment.comparedir.entity.GlobalInfo;

public abstract class BeseGlobalCompare implements Compare<GlobalInfo<FileNode>> {
    @Override
    public abstract CompareType match(GlobalInfo<FileNode> one, GlobalInfo<FileNode> two);
}
