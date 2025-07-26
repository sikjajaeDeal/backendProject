package likelion.beanBa.backendProject.log.service;

import jakarta.servlet.http.HttpServletRequest;
import likelion.beanBa.backendProject.log.entity.LoginLog;
import likelion.beanBa.backendProject.log.entity.UpdateLog;
import likelion.beanBa.backendProject.log.repository.LoginLogRepository;
import likelion.beanBa.backendProject.log.repository.UpdateLogRepository;
import likelion.beanBa.backendProject.log.util.IpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 로그 저장 메소드를 관리하는 service
 * */
@Service
@RequiredArgsConstructor
public class LogService {
    private final LoginLogRepository loginLogRepository;
    private final UpdateLogRepository updateLogRepository;

    /*
    * 로그인 로그 저장하기
    * param : request - http요청
    *         memberPk - 로그인하는 사용자 멤버pk
    * */
    public void saveLoginLog(HttpServletRequest request, Long memberPk) {
        LoginLog loginLog = new LoginLog();
        loginLog.setMemberPk(memberPk);
        loginLog.setLoginIp(IpUtil.getClientIp(request));

        loginLogRepository.save(loginLog);
    }

    /*
    * 업데이트 로그 저장하기
    * param : request - http요청
    *         tableName - 테이블명
    *         recordPk - 삭제 혹은 사용유무 변경한 데이터의 pk값
    *         updateKind - 삭제('D') 혹은 사용유무
    *         updateState - 삭제 혹은 사용유무 변경 후 데이터의 상태값
    *         updateMemberPk - 삭제 혹은 사용유무 벼경한 사용자 PK값
    * */
    public void saveUpdateLog(HttpServletRequest request, String tableName, Long recordPk
            , String updateKind, String updateState, Long updateMemberPk) {
        UpdateLog updateLog = new UpdateLog();
        updateLog.setTableName(tableName);
        updateLog.setRecordPk(recordPk);
        updateLog.setUpdateKind(updateKind);
        updateLog.setUpdateState(updateState);
        updateLog.setUpdateMemberPk(updateMemberPk);
        updateLog.setUpdateIp(IpUtil.getClientIp(request));

        updateLogRepository.save(updateLog);
    }


}
