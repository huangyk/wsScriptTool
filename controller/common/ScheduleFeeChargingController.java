package com.wanshun.oaChargingManager.controller.common;

import com.wanshun.oaChargingManager.ao.common.ScheduleFeeChargingAo;
import com.wanshun.oaChargingManager.service.common.ScheduleFeeChargingService;
import com.wanshun.oaChargingManager.vo.common.ScheduleFeeChargingVo;
import com.wanshun.common.annotion.OpRequestSaveMapping;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.OpRequestUpdateMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.web.AbstractManagerBaseController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@WSRestController(path = "/charging/scheduleFeeCharging", module = "远途加价配置")
public class ScheduleFeeChargingController extends AbstractManagerBaseController {

    @Autowired
    private ScheduleFeeChargingService scheduleFeeChargingService;

    @OpRequestSelectMapping(op = "selectPage", desc = "远途加价配置-分页查询")
    public List<ScheduleFeeChargingVo> selectPage(PageModel<ScheduleFeeChargingAo> ao) {
        return scheduleFeeChargingService.selectPage(ao);
    }

    @OpRequestSelectMapping(op = "selectCount", desc = "远途加价配置-数量查询")
    public Integer selectCount(PageModel<ScheduleFeeChargingAo> ao) {
        return scheduleFeeChargingService.selectCount(ao);
    }

    @OpRequestSelectMapping(op = "selectDetils", desc = "远途加价配置-详情查询")
    public ScheduleFeeChargingVo selectDetils(ScheduleFeeChargingAo ao) {
        return scheduleFeeChargingService.selectDetils(ao);
    }

    @OpRequestUpdateMapping(op = "updateStatus", desc = "远途加价配置-启用/禁用")
    public AnwserCode updateStatus(ScheduleFeeChargingAo ao) {
        return scheduleFeeChargingService.updateStatus(ao);
    }

    @OpRequestUpdateMapping(op = "delete", desc = "远途加价配置-删除")
    public AnwserCode delete(ScheduleFeeChargingAo ao) {
        return scheduleFeeChargingService.delete(ao);
    }

    @OpRequestSaveMapping(op = "save", desc = "远途加价配置-添加")
    public AnwserCode save(ScheduleFeeChargingAo ao) {
        return scheduleFeeChargingService.save(ao);
    }

    @OpRequestUpdateMapping(op = "update", desc = "远途加价配置-修改")
    public AnwserCode update(ScheduleFeeChargingAo ao) {
        return scheduleFeeChargingService.update(ao);
    }
}