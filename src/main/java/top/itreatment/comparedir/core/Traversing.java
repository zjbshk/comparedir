package top.itreatment.comparedir.core;

import top.itreatment.comparedir.entity.GlobalInfo;

import java.io.File;

public interface Traversing<T> {

    GlobalInfo<T> getNodeList(File root);

    GlobalInfo<T> getNodeList(String rootPath);
}
