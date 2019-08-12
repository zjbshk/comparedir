package top.itreatment.comparedir.core.traversingimpl;

import top.itreatment.comparedir.entity.FileNode;
import top.itreatment.comparedir.filter.DefaultFileFilter;
import top.itreatment.comparedir.util.Util;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BreadthTraversing extends BaseTraversing {

    private Map<File, Integer> noAccessdirMap = new ConcurrentHashMap<>();
    private Map<File, Integer> AccessingdirMap = new ConcurrentHashMap<>();

    @Override
    protected void traversing(File root, String rootAbsolutePath, int level) {
        noAccessdirMap.put(root, level);
        while (true) {
            int size = noAccessdirMap.size();
            if (size == 0) break;
            AccessingdirMap.clear();
            AccessingdirMap.putAll(noAccessdirMap);
            noAccessdirMap.clear();
            AccessingdirMap.forEach((file, integer) -> traversingOne(file, rootAbsolutePath, integer));
        }
    }

    private void traversingOne(File root, String rootAbsolutePath, int level) {
        FileNode fileNode = Util.getFileNode(root, rootAbsolutePath, level);
        if (fileNode.isType()) {
            File[] list = root.listFiles(new DefaultFileFilter());
            if (list != null)
                for (File file : list) {
                    if (file.isDirectory()) noAccessdirMap.put(file, level + 1);
                    else fileNodeList.add(Util.getFileNode(file, rootAbsolutePath, level + 1));
                }
        }
        fileNodeList.add(fileNode);
    }
}
