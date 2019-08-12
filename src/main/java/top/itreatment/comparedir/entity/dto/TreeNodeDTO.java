package top.itreatment.comparedir.entity.dto;

import lombok.Data;
import top.itreatment.comparedir.core.CompareType;
import top.itreatment.comparedir.entity.FileNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class TreeNodeDTO {

    private String name;

    /**
     * 存储计算得到的值，不做重复的计算
     */
    private boolean type;

    private long size;

    private long lastTime;

    private String relationPath;

    private int absoluteChild;

    private CompareType tag;

    private List<TreeNodeDTO> childTreeNodes;

    public static List<TreeNodeDTO> toDTO(List<FileNode> tempFileNodeList) {
        List<TreeNodeDTO> treeNodeDTOList = new CopyOnWriteArrayList<>();
        tempFileNodeList.parallelStream().forEach(fileNode -> treeNodeDTOList.add(toDTO(fileNode)));
        return treeNodeDTOList;
    }

    public static TreeNodeDTO toDTO(FileNode tempFileNode) {
        TreeNodeDTO treeNodeDTO = new TreeNodeDTO();
        treeNodeDTO.setName(tempFileNode.getName());
        treeNodeDTO.setType(tempFileNode.isType());
        treeNodeDTO.setTag(tempFileNode.getTag());
        treeNodeDTO.setLastTime(tempFileNode.getLastTime());
        treeNodeDTO.setSize(tempFileNode.getSize());
        treeNodeDTO.setRelationPath(tempFileNode.getRelationPath());
        return treeNodeDTO;
    }
}
