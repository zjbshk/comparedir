package top.itreatment.comparedir.controller;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.itreatment.comparedir.core.CompareType;
import top.itreatment.comparedir.core.globalcompare.BeseGlobalCompare;
import top.itreatment.comparedir.entity.CommanResponse;
import top.itreatment.comparedir.entity.CompareResult;
import top.itreatment.comparedir.entity.FileNode;
import top.itreatment.comparedir.entity.GlobalInfo;
import top.itreatment.comparedir.entity.dto.TreeNodeDTO;
import top.itreatment.comparedir.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
public class CompareController {


    @Autowired
    private CompareResult<FileNode> compareResult;

    @Autowired
    private BeseGlobalCompare defaultGlobalInfoCompare;

    @GetMapping("/result/detail")
    public CommanResponse resultDetail() {
        return CommanResponse.SUCCESS.setO(compareResult);
    }

    @GetMapping("/result")
    public CommanResponse result() {
        CompareResult<FileNode> temCompareResult = new CompareResult<>();
        temCompareResult.setMatch(compareResult.getMatch());

        GlobalInfo<FileNode> oneGlobalInfo = new GlobalInfo<>();
        GlobalInfo<FileNode> twoGlobalInfo = new GlobalInfo<>();
        temCompareResult.setOneGlobalInfo(oneGlobalInfo);
        temCompareResult.setTwoGlobalInfo(twoGlobalInfo);

        BeanUtils.copyProperties(compareResult.getOneGlobalInfo(), oneGlobalInfo);
        BeanUtils.copyProperties(compareResult.getTwoGlobalInfo(), twoGlobalInfo);

        oneGlobalInfo.setFileNodeList(null);
        twoGlobalInfo.setFileNodeList(null);

        return CommanResponse.SUCCESS.setO(temCompareResult);
    }

    @GetMapping("/match")
    public CommanResponse match() {
        return CommanResponse.SUCCESS.setO(compareResult.getMatch());
    }

    @GetMapping("/refreshMatch")
    public CommanResponse refreshMatch() {
        CompareType match = defaultGlobalInfoCompare.match(compareResult.getOneGlobalInfo(), compareResult.getTwoGlobalInfo());
        compareResult.setMatch(match);
        return CommanResponse.SUCCESS.setO(compareResult.getMatch());
    }

    @GetMapping("/tree/{level}")
    public CommanResponse getTree(@RequestParam(required = false) boolean isOne, @PathVariable("level") int level) {
        List<FileNode> tempFileNodeList = getFileNodeListByBoolean(isOne);
        tempFileNodeList = Util.getLevelList(tempFileNodeList, level);
        return CommanResponse.SUCCESS.setO(TreeNodeDTO.toDTO(tempFileNodeList));
    }

    @GetMapping("/tree")
    public CommanResponse getTree(@RequestParam(required = false) boolean isOne, @RequestParam String relationPath, @RequestParam int level) {
        if (relationPath == null) {
            return CommanResponse.FAIL.setO("必要参数为空");
        } else if (relationPath.endsWith("\\")) {
            relationPath += "\\";
        }
        List<FileNode> fileNodeList = getFileNodeListByBoolean(isOne);
        List<FileNode> childFileNodeList = Util.getChildFileNodeList(fileNodeList, relationPath, level);
        return CommanResponse.SUCCESS.setO(TreeNodeDTO.toDTO(childFileNodeList));
    }

    @GetMapping("/two/tree")
    public CommanResponse getTreeTwoLevel(@RequestParam(required = false) boolean isOne, @RequestParam String relationPath, @RequestParam int level) {
        CommanResponse tree = getTree(isOne, relationPath, level);
        if (tree == CommanResponse.SUCCESS) {
            List<TreeNodeDTO> list = (List<TreeNodeDTO>) tree.getO();
            List<TreeNodeDTO> childList = null;
            for (TreeNodeDTO treeNodeDTO : list) {
                CommanResponse responseTemp = getTree(isOne, treeNodeDTO.getRelationPath(), level + 1);
                if (responseTemp == CommanResponse.SUCCESS) {
                    childList = (List<TreeNodeDTO>) responseTemp.getO();
                }
                treeNodeDTO.setChildTreeNodes(childList);
            }
//            tree.setO(list);
        }
        return tree;
    }

    @RequestMapping("/search/{name}")
    public CommanResponse searchFileNodeList(@RequestParam(required = false) boolean isOne, @PathVariable("name") String name) {
        if (name == null) {
            return CommanResponse.FAIL.setO("必要参数为空");
        }
        List<FileNode> tempFileNodeList = getFileNodeListByBoolean(isOne);
        tempFileNodeList = tempFileNodeList.parallelStream().filter(fileNode -> fileNode.getName().contains(name)).collect(Collectors.toList());
        return CommanResponse.SUCCESS.setO(TreeNodeDTO.toDTO(tempFileNodeList));
    }

    @RequestMapping("/search/{start}-{end}")
    public CommanResponse searchFileNodeList(@RequestParam(required = false) boolean isOne
            , @PathVariable("start") Long start
            , @PathVariable("end") Long end) {
        if (start == null) {
            return CommanResponse.FAIL.setO("必要参数为空");
        }
        List<FileNode> tempFileNodeList = getFileNodeListByBoolean(isOne);
        tempFileNodeList = tempFileNodeList.parallelStream().filter(
                fileNode -> {
                    long lastTime = fileNode.getLastTime();
                    return lastTime > start && (end == null || lastTime < end);
                }).collect(Collectors.toList());
        return CommanResponse.SUCCESS.setO(TreeNodeDTO.toDTO(tempFileNodeList));
    }


    //    page=1&limit=10
    @RequestMapping("/list/one")
    public CommanResponse list(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        List<FileNode> fileNodeList = getFileNodeListByBoolean(true);
        List<FileNode> tempFileNodeList = PageList(page, limit, fileNodeList);
        Map<String, Object> map = new HashMap<>();
        map.put("data", tempFileNodeList);
        map.put("count", fileNodeList.size());
        return CommanResponse.SUCCESS.setO(map);
    }

    @RequestMapping("/list/two")
    public CommanResponse twolist(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        List<FileNode> fileNodeList = getFileNodeListByBoolean(false);
        List<FileNode> tempFileNodeList = PageList(page, limit, fileNodeList);
        Map<String, Object> map = new HashMap<>();
        map.put("data", tempFileNodeList);
        map.put("count", fileNodeList.size());
        return CommanResponse.SUCCESS.setO(map);
    }

    public List<FileNode> PageList(int page, int limit, List<FileNode> fileNodeList) {
        int skip = page * limit;
        List<FileNode> tempFileNode = new ArrayList<>();
        for (FileNode fileNode : fileNodeList) {
            if (skip > 0) {
                skip--;
                continue;
            }
            tempFileNode.add(fileNode);
        }
        return tempFileNode;
    }

    private List<FileNode> getFileNodeListByBoolean(boolean isOne) {
        return isOne ? compareResult.getOneGlobalInfo().getFileNodeList()
                : compareResult.getTwoGlobalInfo().getFileNodeList();
    }
}
