package top.itreatment.comparedir.core.traversingimpl;

import top.itreatment.comparedir.core.Traversing;
import top.itreatment.comparedir.entity.FileNode;
import top.itreatment.comparedir.entity.GlobalInfo;

import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public abstract class BaseTraversing implements Traversing<FileNode> {

    protected List<FileNode> fileNodeList = new CopyOnWriteArrayList<>();
    protected GlobalInfo<FileNode> globalInfo = new GlobalInfo<>();

    protected abstract void traversing(File root, String rootAbsolutePath, int level);

    @Override
    public GlobalInfo<FileNode> getNodeList(File root) {
        boolean exists = root.exists();
        globalInfo.setExist(exists);
        if (exists) {
            String absolutePath = root.getAbsolutePath();
            traversing(root, absolutePath, 0);
            fileNodeList.parallelStream().forEach(new Consumer<FileNode>() {
                private AtomicLong total = new AtomicLong();
                private AtomicInteger count = new AtomicInteger();

                @Override
                public void accept(FileNode fileNode) {
                    count.incrementAndGet();
                    total.addAndGet(fileNode.getSize());
                    globalInfo.setFileNum(count.intValue());
                    globalInfo.setFileSize(total.longValue());
                    fileNode.setNameLength(fileNode.getName().length());
                }
            });
            globalInfo.setRootPath(absolutePath);
            globalInfo.setFileNodeList(fileNodeList);
        }
        return globalInfo;
    }

    @Override
    public GlobalInfo<FileNode> getNodeList(String rootPath) {
        return getNodeList(new File(rootPath));
    }
}
