package cn.thinkfree.service.approvalflow;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 10:22
 */
public interface AfInstanceService {

    void start();

    void approval(String instanceNo, String userId, Integer option);
}
