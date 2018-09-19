package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ProjectDocument;
import org.springframework.web.multipart.MultipartFile;

public class ProjectDocumentVO extends ProjectDocument {


    /**
     * 文件实体
     */
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public ProjectDocumentVO() {
    }
}
