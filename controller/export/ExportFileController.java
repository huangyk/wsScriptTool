package com.wanshun.oaChargingManager.controller.export;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wanshun.authority.control.common.TokenUtils;
import com.wanshun.oaChargingManager.ao.export.DownloadAccountAo;
import com.wanshun.oaChargingManager.ao.export.ExportFileAo;
import com.wanshun.oaChargingManager.service.account.AccountService;
import com.wanshun.oaChargingManager.vo.export.DownloadAccountVo;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.exception.ArgsException;
import com.wanshun.common.exception.BusinessException;
import com.wanshun.common.filesystem.WsOssFileSystem;
import com.wanshun.common.filesystem.meta.WsOssFileDown;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.utils.StringUtil;
import com.wanshun.common.utils.WsBeanUtil;
import com.wanshun.common.utils.ZipFileUtil;
import com.wanshun.common.vo.BaseRecordRpcVo;
import com.wanshun.common.web.AbstractManagerBaseController;
import com.wanshun.constants.manager.fileExport.constants.FileExportModuleConstant;
import com.wanshun.constants.manager.financesystemsmanager.FinanceSystemsManagerCode;
import com.wanshun.constants.platform.FileConstant;
import com.wanshun.constants.platform.financesystemsconsole.FinanceSystemConsoleCode;
import com.wanshun.fileexport.constants.ExportFileQueryConditionConversion;
import com.wanshun.fileexport.rpcAo.RpcAddFileDownloadRecordAo;
import com.wanshun.fileexport.rpcAo.RpcDownloadRecordAo;
import com.wanshun.fileexport.rpcAo.RpcFileInfoListAo;
import com.wanshun.fileexport.rpcVo.*;
import com.wanshun.fileexport.rpcapi.RpcFileExportService;
import com.wanshun.manageraccountctrl.common.ManagerTokenUtil;
import com.wanshun.manageraccountctrl.ctrlvo.sys.CtrlSysUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WSRestController(path = "/myWorkSpace/export", module = "文件下载")
public class ExportFileController extends AbstractManagerBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExportFileController.class);

    @Autowired
    private RpcFileExportService rpcFileExportService;

    @Autowired
    private WsOssFileSystem wsOssFileSystem;

    @Autowired
    private AccountService accountService;

    // 计价系统系统
    private static List<String> onlyQueryFileTypes = CollUtil.newArrayList();

    static {
        onlyQueryFileTypes.add(FileExportModuleConstant.CHARTERED_BUS_ORDER_CHARGING_CHANNEL_PLATFORM);
        onlyQueryFileTypes.add(FileExportModuleConstant.CHARTERED_BUS_ORDER_CHARGING_ENTERPRISE);
        onlyQueryFileTypes.add(FileExportModuleConstant.CHARTERED_BUS_ORDER_CHARGING_OPEN_PLATFORM);
        onlyQueryFileTypes.add(FileExportModuleConstant.CHARTERED_BUS_ORDER_CHARGING_SYSTEM);

        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_APPOINT_CHARGING_CHANNEL_PLATFORM);
        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_APPOINT_CHARGING_ENTERPRISE);
        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_APPOINT_CHARGING_OPEN_PLATFORM);
        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_APPOINT_CHARGING_SYSTEM);
        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_APPOINT_CHARGING_FIXED_PRICE_SYSTEM);

        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_CHARGING_CHANNEL_PLATFORM);
        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_CHARGING_ENTERPRISE);
        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_CHARGING_OPEN_PLATFORM);
        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_CHARGING_SYSTEM);
        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_CHARGING_FIXED_PRICE_SYSTEM);
        onlyQueryFileTypes.add(FileExportModuleConstant.NEW_TAXI_CHARGING_SYSTEM);
        onlyQueryFileTypes.add(FileExportModuleConstant.NEW_TAXI_CHARGING_ENTERPRISE);
        onlyQueryFileTypes.add(FileExportModuleConstant.NEW_TAXI_APPOINT_CHARGING_SYSTEM);

        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_RESERVE_CHARGING_CHANNEL_PLATFORM);
        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_RESERVE_CHARGING_ENTERPRISE);
        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_RESERVE_CHARGING_OPEN_PLATFORM);
        onlyQueryFileTypes.add(FileExportModuleConstant.EXP_RESERVE_CHARGING_SYSTEM);
        onlyQueryFileTypes.add(FileExportModuleConstant.FILE_TYPE_ORDER_EARLY_WARNING);

        onlyQueryFileTypes.add(FileExportModuleConstant.FENCE_CHARGING);
    }

    /**
     * 文件下载列表查询
     */
    @OpRequestSelectMapping(op = "selectAndPage", desc = "文件分页查询")
    public PageModel<List<RpcExportFileVo>> findExportFileList(PageModel<ExportFileAo> pageModel) {

        PageModel<List<RpcExportFileVo>> pageInfo = new PageModel<List<RpcExportFileVo>>();
        ExportFileAo ao = pageModel.getBody();
        if (null == ao) {
            throw new ArgsException();
        }

        if (StringUtil.isNullOrBlank(ao.getType())) {
            throw new BusinessException(FinanceSystemConsoleCode.FILE_TYPE_NOT_NULL);
        }

        if (!onlyQueryFileTypes.contains(ao.getType())) {
            throw new ArgsException();
        }

        // 创建查询条件
        RpcFileInfoListAo rpcAo = ExportFileQueryConditionConversion.createFiletypeListCondition(ao.getType());
        //计价系统账号
        final List<Integer> accountTypes = ExportFileQueryConditionConversion.createAccountTypeListCondition(FileExportModuleConstant.FILE_EXPORT_ACCOUNT_TYPE_CHARGING);
        rpcAo.setAccountTypeList(accountTypes);

        rpcAo.setStatusList(allFileStatus());

        // 设置用户
//        SysUserVo sessionUser = SessionUtil.getUser(SysUserVo.class);
        // 根据用户名称查询用户ID
        if (StrUtil.isNotEmpty(ao.getCreateAdmin())) {
            Long queryUserId = getUserIdByUsername(ao.getCreateAdmin());
            if (null != queryUserId) {
                rpcAo.setCreaterId(queryUserId);
            } else {
                // 查询时间
                rpcAo.setCreateTimeStart(ao.getStartTime());
                rpcAo.setCreateTimeEnd(ao.getEndTime());
                RpcFileInfoListVo exportFileListByCondition = rpcFileExportService.findExportFileListByConditionAll(rpcAo);
                List<String> exportFileVos = CollUtil.newArrayList();
                List<RpcFileInfoVo> rpcFileInfoListVos = exportFileListByCondition.getRpcFileInfoListVos();
                logger.info("rpcFileInfoListVos.size()===" + rpcFileInfoListVos.size());
                if (rpcFileInfoListVos != null) {
                    for (RpcFileInfoVo rpcFileInfoListVo : rpcFileInfoListVos) {
                        //如果有重复的创建人就保留一个
                        String username = getUsername(rpcFileInfoListVo.getCreaterId());
                        if (exportFileVos.contains(username)) {
                            continue;
                        }
                        exportFileVos.add(username);
                    }
                } else {
                    return pageInfo;
                }
                //将查询出来的创建人带""的替换为系统生成
                Collections.replaceAll(exportFileVos, "", FileExportModuleConstant.FILE_EXPORT_APPLY_GENERATE_TYPE_SYSTEM_AUTO_NAME);
                logger.info("exportFileVos===" + exportFileVos.toString());
                if (queryUserId == null && exportFileVos.contains(FileExportModuleConstant.FILE_EXPORT_APPLY_GENERATE_TYPE_SYSTEM_AUTO_NAME)) {

                } else {
                    return pageInfo;
                }
            }
        }
        // 查询时间
        rpcAo.setCreateTimeStart(ao.getStartTime());
        rpcAo.setCreateTimeEnd(ao.getEndTime());

        // 分页对象
        rpcAo.setPageNo(pageModel.getCurrentPage());
        rpcAo.setPageSize(pageModel.getPageSize());

        // 查询结果
        RpcFileInfoListVo rpcResult = rpcFileExportService.findExportFileListByCondition(rpcAo);

        List<RpcExportFileVo> exportFileVoList = CollUtil.newArrayList();
        if (rpcResult.isSuccess() && CollUtil.isNotEmpty(rpcResult.getRpcFileInfoListVos())) {
            List<RpcFileInfoVo> infoListVos = rpcResult.getRpcFileInfoListVos();
            if (CollUtil.isNotEmpty(infoListVos)) {
                infoListVos.forEach(fileInfo -> {
                    RpcExportFileVo rpcExportFileVo = new RpcExportFileVo();
                    WsBeanUtil.copyProperties(fileInfo, rpcExportFileVo);
                    rpcExportFileVo.setFilename(fileInfo.getFileName());
                    rpcExportFileVo.setCreateAdmin(getUsername(fileInfo.getCreaterId()));
                    rpcExportFileVo.setCount(fileInfo.getDownloadCount());
                    rpcExportFileVo.setGenre(fileInfo.getGenerateType());
                    ExportFileQueryConditionConversion.setFileTypeAndCreateAdmin(fileInfo.getFileType(), rpcExportFileVo);
                    exportFileVoList.add(rpcExportFileVo);
                });
            }
        }

        // 设置返回值
        pageInfo.setBody(exportFileVoList);
        pageInfo.setCurrentPage(pageModel.getPageNo());
        pageInfo.setPageSize(pageModel.getPageSize());
        return pageInfo;
    }

    private List<Integer> allFileStatus() {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(FileExportModuleConstant.FILE_EXPORT_APPLY_STATUS_UNDO);
        statusList.add(FileExportModuleConstant.FILE_EXPORT_APPLY_STATUS_PROCESSING);
        statusList.add(FileExportModuleConstant.FILE_EXPORT_APPLY_STATUS_FAIL);
        statusList.add(FileExportModuleConstant.FILE_EXPORT_APPLY_STATUS_SUCCESS);
        return statusList;
    }

    /**
     * 获取用户名
     */
    private String getUsername(Long userId) {
        if (userId == null) {
            return StrUtil.EMPTY;
        }
        return accountService.getUserNameById(userId.intValue());
    }

    /**
     * 根据用户名查询用户ID
     */
    private Long getUserIdByUsername(String username) {
        CtrlSysUserVo httpSysUser = accountService.getUserByUserName(username);
        if (httpSysUser == null) {
            return null;
        }
        return httpSysUser.getId().longValue();
    }

    /**
     * 文件下载列表总条数
     */
    @OpRequestSelectMapping(op = "selectByCount", desc = "文件分页条数")
    public int selectByCount(PageModel<ExportFileAo> pageModel) {
        ExportFileAo ao = pageModel.getBody();
        if (null == ao) {
            throw new ArgsException();
        }

        if (StringUtil.isNullOrBlank(ao.getType())) {
            throw new BusinessException(FinanceSystemConsoleCode.FILE_TYPE_NOT_NULL);
        }

        if (!onlyQueryFileTypes.contains(ao.getType())) {
            throw new ArgsException();
        }

        // 创建查询条件
        RpcFileInfoListAo rpcAo = ExportFileQueryConditionConversion.createFiletypeListCondition(ao.getType());
        final List<Integer> accountTypes = ExportFileQueryConditionConversion.createAccountTypeListCondition(FileExportModuleConstant.FILE_EXPORT_ACCOUNT_TYPE_CHARGING);
        rpcAo.setAccountTypeList(accountTypes);

        rpcAo.setStatusList(allFileStatus());

        // 从session中获取用户对象
        // 设置用户
//        SysUserVo sessionUser = SessionUtil.getUser(SysUserVo.class);
        // 根据用户名称查询用户ID
        if (StrUtil.isNotEmpty(ao.getCreateAdmin())) {
            Long queryUserId = getUserIdByUsername(ao.getCreateAdmin());
            if (null != queryUserId) {
                rpcAo.setCreaterId(queryUserId);
            } else {
                // 查询时间
                rpcAo.setCreateTimeStart(ao.getStartTime());
                rpcAo.setCreateTimeEnd(ao.getEndTime());
                RpcFileInfoListVo exportFileListByCondition = rpcFileExportService.findExportFileListByConditionAll(rpcAo);
                List<String> exportFileVos = CollUtil.newArrayList();
                List<RpcFileInfoVo> rpcFileInfoListVos = exportFileListByCondition.getRpcFileInfoListVos();
                logger.info("rpcFileInfoListVos.size()===" + rpcFileInfoListVos.size());
                if (rpcFileInfoListVos != null) {
                    for (RpcFileInfoVo rpcFileInfoListVo : rpcFileInfoListVos) {
                        //如果有重复的创建人就保留一个
                        String username = getUsername(rpcFileInfoListVo.getCreaterId());
                        if (exportFileVos.contains(username)) {
                            continue;
                        }
                        exportFileVos.add(username);
                    }
                } else {
                    return 0;
                }
                //将查询出来的创建人带""的替换为系统生成
                Collections.replaceAll(exportFileVos, "", FileExportModuleConstant.FILE_EXPORT_APPLY_GENERATE_TYPE_SYSTEM_AUTO_NAME);
                logger.info("exportFileVos===" + exportFileVos.toString());
                if (queryUserId == null && exportFileVos.contains(FileExportModuleConstant.FILE_EXPORT_APPLY_GENERATE_TYPE_SYSTEM_AUTO_NAME)) {

                } else {
                    return 0;
                }
            }
            rpcAo.setCreaterId(queryUserId);
        }

        // 设置时间
        rpcAo.setCreateTimeStart(ao.getStartTime());
        rpcAo.setCreateTimeEnd(ao.getEndTime());

        // 查询
        BaseRecordRpcVo<Integer> result = rpcFileExportService.getExportFileCountByCndition(rpcAo);
        return result.isSuccess() ? result.getData() : 0;
    }

    /**
     * 文件下载
     */
    @OpRequestSelectMapping(op = "download", desc = "文件下载")
    public AnwserCode export(ExportFileAo exportFileAo, HttpServletRequest request, HttpServletResponse response) {

        if (exportFileAo.getId() == null) {
            logger.info("文件下载请求参数信息记录ID为空,args={}", JSONUtil.toJsonStr(exportFileAo));
            return FinanceSystemsManagerCode.FINANCE_DOWNLOAD_ID_ISNULL;
        }

        // 根据记录ID查询目标文件的fileId
        BaseRecordRpcVo<String> result = rpcFileExportService.findExportFileIdById(exportFileAo.getId());
        String fileId = result.isSuccess() ? result.getData() : StrUtil.EMPTY;
        if (StrUtil.isEmpty(fileId)) {
            logger.info("fileId 为空 {}", fileId);
            return FinanceSystemsManagerCode.FINANCE_IMPORT_FILE_IS_NULL;
        }

        try {
            // 获取当前登录用户信息
            // 调度rpc接口添加下载记录
            RpcAddFileDownloadRecordAo downloadRecordAo = new RpcAddFileDownloadRecordAo();
            downloadRecordAo.setAccountType(FileExportModuleConstant.FILE_EXPORT_ACCOUNT_TYPE_CHARGING);
            downloadRecordAo.setDownloadUserId(Long.parseLong(String.valueOf(TokenUtils.getCurrentUser().getId())));
            downloadRecordAo.setFileId(fileId);
            rpcFileExportService.createFileDownloadRecordWithSqlTrans(downloadRecordAo);
            // 根据fileId去fastDFS上面获取文件

            byte[] downloadFile = null;

            WsOssFileDown wsOssFileDown = wsOssFileSystem.downCosFileBytes(fileId);
            downloadFile = wsOssFileDown.getFileByte();

            if (ZipFileUtil.checkZipType(downloadFile)) {
                logger.info("导出zip文件");
                ZipFileUtil.exportZip(exportFileAo.getFilename() + FileConstant.FileType.ZIP, downloadFile, response);
            } else {
                logger.info("导出csv文件");
                ZipFileUtil.exportCsv(exportFileAo.getFilename() + FileConstant.FileType.CSV, downloadFile, response);
            }
            return AnwserCode.SUCCESS;
        } catch (IOException e) {
            logger.error(">>> 下载" + exportFileAo.getFilename() + "ExportFileController/download <<<", e);
            return AnwserCode.ERROR;
        }
    }

    /**
     * 记录分页查询
     */
    @OpRequestSelectMapping(op = "selectAccountAndPage", desc = "记录分页查询")
    public PageModel<List<DownloadAccountVo>> selectAccount(PageModel<DownloadAccountAo> pageModel) {

        PageModel<List<DownloadAccountVo>> pageInfo = new PageModel<List<DownloadAccountVo>>();
        DownloadAccountAo ao = pageModel.getBody();
        if (ao == null || StrUtil.isEmpty(ao.getFiletype()) || !onlyQueryFileTypes.contains(ao.getFiletype()) || ao.getId() == null) {
            return pageInfo;
        }

        RpcDownloadRecordAo rpcAo = new RpcDownloadRecordAo();
        rpcAo.setPageNo(pageModel.getPageNo());
        rpcAo.setPageSize(pageModel.getPageSize());
        rpcAo.setId(ao.getId());
        RpcFileDownloadRecordListVo rpcResult = rpcFileExportService.findFileDownloadRecordListByFileId(rpcAo);
        if (rpcResult.isSuccess() && CollUtil.isNotEmpty(rpcResult.getRpcFileDownloadRecordVos())) {
            List<DownloadAccountVo> returnVos = CollUtil.newArrayList();
            List<RpcFileDownloadRecordVo> rpcVos = rpcResult.getRpcFileDownloadRecordVos();
            rpcVos.forEach(rpcVo -> {
                DownloadAccountVo returnVo = new DownloadAccountVo();
                returnVo.setDownloadTime(rpcVo.getCreateTime());
                returnVo.setId(rpcVo.getId());
                returnVo.setName(getUsername(rpcVo.getUserId()));
                returnVos.add(returnVo);
            });
            pageInfo.setBody(returnVos);
        }
        pageInfo.setCurrentPage(pageModel.getCurrentPage());
        pageInfo.setPageSize(pageModel.getPageSize());
        return pageInfo;
    }

    /**
     * 记录分页条数
     */
    @OpRequestSelectMapping(op = "selectAccountByCount", desc = "记录分页条数")
    public int selectAccountByCount(PageModel<DownloadAccountAo> pageModel) {
        DownloadAccountAo ao = pageModel.getBody();
        if (ao == null || StrUtil.isEmpty(ao.getFiletype()) || !onlyQueryFileTypes.contains(ao.getFiletype()) || ao.getId() == null) {
            return 0;
        }
        BaseRecordRpcVo<Integer> result = rpcFileExportService.getFileDownloadRecordCountByFileId(ao.getId());
        return result.isSuccess() ? result.getData() : 0;
    }
}
