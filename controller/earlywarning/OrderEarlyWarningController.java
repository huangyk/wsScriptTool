package com.wanshun.oaChargingManager.controller.earlywarning;

import com.wanshun.authority.control.common.TokenUtils;
import com.wanshun.oaChargingManager.ao.earlywarning.OrderEarlyWarningAo;
import com.wanshun.oaChargingManager.vo.earlywarning.OrderEarlyWarningVo;
import com.wanshun.chargingServiceConsoleApi.rpcApi.earlywarning.RpcOrderEarlyWarningConsoleService;
import com.wanshun.chargingServiceConsoleApi.rpcao.earlywarning.RpcOrderEarlyWarningConsoleAo;
import com.wanshun.chargingServiceConsoleApi.rpcvo.earlywarning.RpcOrderEarlyWarningConsoleVo;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.exception.BusinessException;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.utils.BaseRpcVoUtil;
import com.wanshun.common.utils.JsonUtil;
import com.wanshun.common.utils.StringUtil;
import com.wanshun.common.utils.WsBeanUtil;
import com.wanshun.common.vo.BaseRecordRpcVo;
import com.wanshun.common.vo.BaseRpcVo;
import com.wanshun.common.vo.RpcCountVo;
import com.wanshun.common.web.AbstractManagerBaseController;
import com.wanshun.constants.manager.fileExport.constants.FileExportModuleConstant;
import com.wanshun.fileexport.rpcAo.RpcCreateFileExportTaskAo;
import com.wanshun.fileexport.rpcapi.RpcFileExportService;
import com.wanshun.manageraccountctrl.common.ManagerTokenUtil;
import com.wanshun.vpo.chargingservice.earlywarning.OrderEarlyWarningDetailPo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@WSRestController(path = "/charging/orderEarlyWarning", module = "预警订单")
public class OrderEarlyWarningController extends AbstractManagerBaseController {

    @Autowired
    private RpcOrderEarlyWarningConsoleService rpcOrderEarlyWarningConsoleService;

    @Autowired
    private RpcFileExportService rpcFileExportService;

    @OpRequestSelectMapping(op = "selectPage", desc = "预警订单-分页查询")
    public List<OrderEarlyWarningVo> selectPage(PageModel<OrderEarlyWarningAo> ao) {
        List<OrderEarlyWarningVo> result = new ArrayList<>();

        RpcOrderEarlyWarningConsoleAo rpcAo = new RpcOrderEarlyWarningConsoleAo();
        WsBeanUtil.copyProperties(ao.getBody(), rpcAo);
        rpcAo.setNext(ao.getNext());
        rpcAo.setPageId(ao.getPageId());
        rpcAo.setPageCreateTime(ao.getPageCreateTime());
        rpcAo.setPageSize(ao.getPageSize());

        BaseRecordRpcVo<List<RpcOrderEarlyWarningConsoleVo>> rpcVo = rpcOrderEarlyWarningConsoleService.selectPage(rpcAo);
        BaseRpcVoUtil.checkFail(rpcVo);

        rpcVo.getData().forEach(item -> {
            OrderEarlyWarningVo vo = new OrderEarlyWarningVo();
            WsBeanUtil.copyProperties(item, vo);
            result.add(vo);
        });

        return result;
    }

    @OpRequestSelectMapping(op = "selectCount", desc = "预警订单-数量查询")
    public Integer selectCount(PageModel<OrderEarlyWarningAo> ao) {
        RpcOrderEarlyWarningConsoleAo rpcAo = new RpcOrderEarlyWarningConsoleAo();
        WsBeanUtil.copyProperties(ao.getBody(), rpcAo);
        RpcCountVo rpcVo = rpcOrderEarlyWarningConsoleService.selectCount(rpcAo);
        BaseRpcVoUtil.checkFail(rpcVo);
        return rpcVo.getCount();
    }

    @OpRequestSelectMapping(op = "selectDetils", desc = "预警订单-详情查询")
    public OrderEarlyWarningVo selectDetils(OrderEarlyWarningAo ao) {
        OrderEarlyWarningVo result = new OrderEarlyWarningVo();

        if (StringUtil.isNullOrBlank(ao.getOrderId())) {
            throw new BusinessException(AnwserCode.PARAMETER_ERROR);
        }

        RpcOrderEarlyWarningConsoleAo rpcAo = new RpcOrderEarlyWarningConsoleAo();
        rpcAo.setOrderId(ao.getOrderId());
        RpcOrderEarlyWarningConsoleVo rpcVo = rpcOrderEarlyWarningConsoleService.selectDetils(rpcAo);
        BaseRpcVoUtil.checkFail(rpcVo);

        WsBeanUtil.copyProperties(rpcVo, result);
        OrderEarlyWarningDetailPo earlyWarningDetailPo = JsonUtil.fromJson(rpcVo.getEarlyWarningDetail(), OrderEarlyWarningDetailPo.class);
        result.setEarlyWarningFee(earlyWarningDetailPo.getEarlyWarningFee());
        result.setEarlyWarningList(earlyWarningDetailPo.getEarlyWarningList());
        return result;
    }

    @OpRequestSelectMapping(op = "export", desc = "预警订单-导出")
    public AnwserCode export(OrderEarlyWarningAo ao) {
        RpcOrderEarlyWarningConsoleAo rpcAo = new RpcOrderEarlyWarningConsoleAo();
        WsBeanUtil.copyProperties(ao, rpcAo);
        RpcCountVo rpcVo = rpcOrderEarlyWarningConsoleService.selectCount(rpcAo);
        BaseRpcVoUtil.checkFail(rpcVo);

        if (rpcVo.getCount() <= 0) {
            throw new BusinessException("数据不存在");
        }
        if (rpcVo.getCount() > 100000) {
            throw new BusinessException("总数据量不能超过10万条，请选择检索条件！");
        }

        // 创建导出任务
        RpcCreateFileExportTaskAo taskAo = new RpcCreateFileExportTaskAo();
        taskAo.setCreaterId(Long.valueOf(TokenUtils.getCurrentUser().getId()));
        taskAo.setArgument(JsonUtil.toJsonExcludeNull(rpcAo));
        taskAo.setAccountType(FileExportModuleConstant.FILE_EXPORT_ACCOUNT_TYPE_CHARGING);
        taskAo.setFileType(FileExportModuleConstant.FILE_TYPE_ORDER_EARLY_WARNING);
        BaseRpcVo baseRpcVo = rpcFileExportService.createFileExportTask(taskAo);
        return baseRpcVo.getAnwserCode();
    }

}