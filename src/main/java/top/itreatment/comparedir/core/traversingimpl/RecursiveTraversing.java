package top.itreatment.comparedir.core.traversingimpl;

import top.itreatment.comparedir.entity.FileNode;
import top.itreatment.comparedir.filter.DefaultFileFilter;
import top.itreatment.comparedir.util.Util;

import java.io.File;

public class RecursiveTraversing extends BaseTraversing {

    protected void traversing(File root, String rootAbsolutePath, int level) {
        FileNode fileNode = Util.getFileNode(root, rootAbsolutePath, level);
        if (fileNode.isType()) {
            File[] list = root.listFiles(new DefaultFileFilter());
            if (list != null)
                for (File file : list) {
                    traversing(file, rootAbsolutePath, level + 1);
                }
        }
        fileNodeList.add(fileNode);
    }
}
