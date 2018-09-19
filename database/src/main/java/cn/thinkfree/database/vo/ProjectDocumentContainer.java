package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ProjectDocument;

import java.util.List;

public class ProjectDocumentContainer {
    List<ProjectDocumentVO> projectDocuments;

    public List<ProjectDocumentVO> getProjectDocuments() {
        return projectDocuments;
    }

    public void setProjectDocuments(List<ProjectDocumentVO> projectDocuments) {
        this.projectDocuments = projectDocuments;
    }
}
