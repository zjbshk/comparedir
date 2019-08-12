package top.itreatment.comparedir.core.traversingimpl;

import top.itreatment.comparedir.entity.FileNode;
import top.itreatment.comparedir.filter.DefaultFileFilter;
import top.itreatment.comparedir.util.Util;

import java.io.File;
import java.util.List;
import java.util.Vector;


public class ParallelBreadthTraversing extends BaseTraversing {

//    @Autowired
//    FilenameFilter filenameFilter;


    private static class KeyValue {
        File root;
        int level;

        public KeyValue(File root, int level) {
            this.root = root;
            this.level = level;
        }
    }

    private List<KeyValue> noAccessdirList = new Vector<>();
    private List<KeyValue> AccessingdirList = new Vector<>();

    @Override
    protected void traversing(File root, String rootAbsolutePath, int level) {
        noAccessdirList.add(new KeyValue(root, level));
        while (true) {
            int size = noAccessdirList.size();
            if (size == 0) break;
            AccessingdirList.clear();
            AccessingdirList.addAll(noAccessdirList);
            noAccessdirList.clear();
            AccessingdirList.parallelStream().forEach(keyValue -> traversingOne(keyValue.root, rootAbsolutePath, keyValue.level));
        }
    }

    private void traversingOne(File root, String rootAbsolutePath, int level) {
        FileNode fileNode = Util.getFileNode(root, rootAbsolutePath, level);
        if (fileNode.isType()) {
            File[] list = root.listFiles(new DefaultFileFilter());
            if (list != null)
                for (File file : list) {
                    if (file.isDirectory()) noAccessdirList.add(new KeyValue(file, level + 1));
                    else fileNodeList.add(Util.getFileNode(file, rootAbsolutePath, level + 1));
                }
        }
        fileNodeList.add(fileNode);
    }
}
