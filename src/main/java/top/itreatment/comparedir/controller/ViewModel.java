package top.itreatment.comparedir.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.itreatment.comparedir.entity.CommanResponse;
import top.itreatment.comparedir.entity.CompareResult;
import top.itreatment.comparedir.entity.FileNode;

@Controller
public class ViewModel {

    @Autowired
    private CompareResult<FileNode> compareResult;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "结果显示");
        return "/index.html";
    }


}
