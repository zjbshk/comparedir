package top.itreatment.comparedir.core.globalcompare;

import top.itreatment.comparedir.core.CompareType;
import top.itreatment.comparedir.core.filenodecompare.BaseFileNodeCompare;
import top.itreatment.comparedir.core.filenodecompare.DefaultFileNodeCompare;
import top.itreatment.comparedir.entity.FileNode;
import top.itreatment.comparedir.entity.GlobalInfo;

import java.util.List;

public class DefaultGlobalInfoCompare extends BeseGlobalCompare {

    @Override
    public CompareType match(GlobalInfo<FileNode> one, GlobalInfo<FileNode> two) {
        if (!one.isExist() || !two.isExist()) {
            return !one.isExist() && !two.isExist() ? CompareType.SAME : CompareType.DIFFERENT;
        }

        List<FileNode> oneFileNodeList = one.getFileNodeList();
        List<FileNode> twoFileNodeList = two.getFileNodeList();

        oneFileNodeList.sort(FileNode::compareTo);
        twoFileNodeList.sort(FileNode::compareTo);

//            AtomicInteger matchNum = new AtomicInteger();
//            oneFileNodeList.parallelStream().forEach(fileNode -> {
//                int i = Collections.binarySearch(twoFileNodeList, fileNode);
//                if (i != -1) {
//                    matchNum.incrementAndGet();
//                }
//            });
        //            开始对所有文件的名称进行匹配
        int matchNum = 0;
        int oneSize = oneFileNodeList.size();
        int twoSize = twoFileNodeList.size();
        int k = 0, j = 0;

        BaseFileNodeCompare baseFileNodeCompare = new DefaultFileNodeCompare();

        while (k < oneSize && j < twoSize) {
            FileNode tempOne = oneFileNodeList.get(k);
            FileNode tempTwo = twoFileNodeList.get(j);
            if (CompareType.SAME == baseFileNodeCompare.match(tempOne, tempTwo)) {
                tempOne.setTag(CompareType.SAME);
                tempTwo.setTag(CompareType.SAME);
                matchNum++;
                k++;
                j++;
            } else if (tempOne.compareTo(tempTwo) > 0) {
                j++;
                tempTwo.setTag(CompareType.DIFFERENT);
            } else {
                tempOne.setTag(CompareType.DIFFERENT);
                k++;
            }
        }

        for (; k < oneSize; k++) {
            FileNode tempOne = oneFileNodeList.get(k);
            tempOne.setTag(CompareType.DIFFERENT);
        }

        for (; j < twoSize; j++) {
            FileNode tempTwo = twoFileNodeList.get(j);
            tempTwo.setTag(CompareType.DIFFERENT);
        }

        float matchRate;
        if (one.getFileNum() >= two.getFileNum()) {
            matchRate = matchNum * 100.0f / one.getFileNum();
        } else {
            matchRate = matchNum * 100.0f / two.getFileNum();
        }

        one.setMatchRate(matchRate);
        two.setMatchRate(matchRate);

        if (matchNum == one.getFileNum() &&
                one.getFileSize() == two.getFileSize()
                && one.getFileNum() == two.getFileNum()) {
            return CompareType.SAME;
        }

        return CompareType.DIFFERENT;
    }
}
