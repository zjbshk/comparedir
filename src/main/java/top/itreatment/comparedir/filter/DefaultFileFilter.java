package top.itreatment.comparedir.filter;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FilenameFilter;

//@Component
public class DefaultFileFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        return true;
    }
}
