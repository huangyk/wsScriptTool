package com.wanshun.oaChargingManager.controller.fencecharging;

import com.wanshun.oaChargingManager.ao.expcharging.ExpChargingAo;
import com.wanshun.oaChargingManager.ao.expcharging.ExpChargingOpAo;
import com.wanshun.oaChargingManager.service.fencecharging.FenceChargingService;
import com.wanshun.oaChargingManager.vo.expcharging.ExpChargingVo;
import com.wanshun.common.annotion.OpRequestSaveMapping;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.OpRequestUpdateMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.web.AbstractManagerBaseController;
import com.wanshun.constants.manager.fileExport.constants.FileExportModuleConstant;
import com.wanshun.constants.platform.chargingservice.ChargingServiceAttributeConst;

import javax.annotation.Resource;
import java.util.List;

@WSRestController(path = "/fence/charging", module = "围栏计价")
public class FenceChargingController extends AbstractManagerBaseController {

    @Resource
    private FenceChargingService fenceChargingService;

    @OpRequestSelectMapping(op = "selectPage", desc = "围栏计价-分页查询")
    public List<ExpChargingVo> selectPage(PageModel<ExpChargingAo> ao) {
        return fenceChargingService.selectPage(ao);
    }

    @OpRequestSelectMapping(op = "selectCount", desc = "围栏计价-数量查询")
    public Integer selectCount(PageModel<ExpChargingAo> ao) {
        return fenceChargingService.selectCount(ao);
    }

    @OpRequestSelectMapping(op = "selectDetils", desc = "围栏计价-详情查询")
    public ExpChargingVo selectDetils(ExpChargingAo ao) {
        return fenceChargingService.selectDetils(ao);
    }

    @OpRequestUpdateMapping(op = "updateStatus", desc = "围栏计价-启用/禁用")
    public AnwserCode updateStatus(ExpChargingAo ao) {
        return fenceChargingService.updateStatus(ao);
    }

    @OpRequestUpdateMapping(op = "delete", desc = "围栏计价-删除")
    public AnwserCode delete(ExpChargingAo ao) {
        return fenceChargingService.delete(ao);
    }

    @OpRequestSaveMapping(op = "save", desc = "围栏计价-添加")
    public AnwserCode save(ExpChargingOpAo ao) {
        return fenceChargingService.save(ao);
    }

    @OpRequestUpdateMapping(op = "update", desc = "围栏计价-修改")
    public AnwserCode update(ExpChargingOpAo ao) {
        return fenceChargingService.update(ao);
    }

    @OpRequestUpdateMapping(op = "batchAdjust", desc = "围栏计价-批量调整配置")
    public AnwserCode batchAdjust(ExpChargingAo ao) {
        return fenceChargingService.batchAdjust(ao);
    }

    @OpRequestUpdateMapping(op = "batchUpdateUpStatus", desc = "围栏计价-批量启用")
    public int batchUpdateUpStatus(ExpChargingAo ao) {
        return fenceChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_UP).getCount();
    }

    @OpRequestUpdateMapping(op = "batchUpdateDownStatus", desc = "围栏计价-批量禁用")
    public int batchUpdateDownStatus(ExpChargingAo ao) {
        return fenceChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DOWN).getCount();
    }

    @OpRequestUpdateMapping(op = "batchUpdateDelStatus", desc = "围栏计价-批量删除")
    public int batchUpdateDelStatus(ExpChargingAo ao) {
        return fenceChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DEL).getCount();
    }

    @OpRequestSelectMapping(op = "export", desc = "围栏计价-导出")
    public AnwserCode export(ExpChargingAo ao) {
        return fenceChargingService.export(ao, FileExportModuleConstant.FENCE_CHARGING);
    }
}
