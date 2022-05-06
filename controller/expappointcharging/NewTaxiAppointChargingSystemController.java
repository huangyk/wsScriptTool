package com.wanshun.oaChargingManager.controller.expappointcharging;

import com.wanshun.oaChargingManager.ao.expcharging.ExpChargingAo;
import com.wanshun.oaChargingManager.ao.expcharging.ExpChargingOpAo;
import com.wanshun.oaChargingManager.service.expcharging.ExpChargingService;
import com.wanshun.oaChargingManager.vo.expcharging.ExpChargingVo;
import com.wanshun.common.annotion.OpRequestSaveMapping;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.OpRequestUpdateMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.AttributeConst;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.utils.ChargingServiceUtil;
import com.wanshun.common.web.AbstractManagerBaseController;
import com.wanshun.constants.manager.fileExport.constants.FileExportModuleConstant;
import com.wanshun.constants.platform.chargingservice.ChargingServiceAttributeConst;
import com.wanshun.constants.platform.ordersystem.carorder.constants.OrderAttributeConst;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@WSRestController(path = "/newtaxiappoint/charging/system", module = "平台计费-新出租车计费规则")
public class NewTaxiAppointChargingSystemController extends AbstractManagerBaseController {

    @Autowired
    private ExpChargingService expChargingService;

    private static List<Integer> rideTypeList = new ArrayList<Integer>() {{
        add(AttributeConst.RIDE_TYPE_TAXI);
    }};

    @OpRequestSelectMapping(op = "selectPage", desc = "平台计费-新出租车计费规则-分页查询")
    public List<ExpChargingVo> selectPage(PageModel<ExpChargingAo> ao) {
        ao.getBody().setNewTaxi(true);
        ao.getBody().setChannelId(ChargingServiceUtil.WANSHUN_CHANNELID);
        ao.getBody().setRideTypeList(rideTypeList);
        return expChargingService.selectPage(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
    }

    @OpRequestSelectMapping(op = "selectCount", desc = "平台计费-新出租车计费规则-数量查询")
    public Integer selectCount(PageModel<ExpChargingAo> ao) {
        ao.getBody().setNewTaxi(true);
        ao.getBody().setChannelId(ChargingServiceUtil.WANSHUN_CHANNELID);
        ao.getBody().setRideTypeList(rideTypeList);
        return expChargingService.selectCount(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
    }

    @OpRequestSelectMapping(op = "selectDetils", desc = "平台计费-新出租车计费规则-详情查询")
    public ExpChargingVo selectDetils(ExpChargingAo ao) {
        ao.setNewTaxi(true);
        return expChargingService.selectDetils(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
    }

    @OpRequestUpdateMapping(op = "updateStatus", desc = "平台计费-新出租车计费规则-启用/禁用")
    public AnwserCode updateStatus(ExpChargingAo ao) {
        ao.setNewTaxi(true);
        return expChargingService.updateStatus(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
    }

    @OpRequestUpdateMapping(op = "delete", desc = "平台计费-新出租车计费规则-删除")
    public AnwserCode delete(ExpChargingAo ao) {
        ao.setNewTaxi(true);
        return expChargingService.delete(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
    }

    @OpRequestSaveMapping(op = "save", desc = "平台计费-新出租车计费规则-添加")
    public AnwserCode save(ExpChargingOpAo ao) {
        ao.setNewTaxi(true);
        ao.setChannelId(ChargingServiceUtil.WANSHUN_CHANNELID);
        ao.setRideTypeList(rideTypeList);
        return expChargingService.save(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
    }

    @OpRequestUpdateMapping(op = "update", desc = "平台计费-新出租车计费规则-修改")
    public AnwserCode update(ExpChargingOpAo ao) {
        ao.setNewTaxi(true);
        ao.setChannelId(ChargingServiceUtil.WANSHUN_CHANNELID);
        ao.setRideType(AttributeConst.RIDE_TYPE_TAXI);
        return expChargingService.update(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
    }

    @OpRequestUpdateMapping(op = "batchAdjust", desc = "平台计费-新出租车计费规则-批量调整配置")
    public AnwserCode batchAdjust(ExpChargingAo ao) {
        ao.setNewTaxi(true);
        ao.setChannelId(ChargingServiceUtil.WANSHUN_CHANNELID);
        return expChargingService.batchAdjust(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
    }

    @OpRequestUpdateMapping(op = "batchUpdateUpStatus", desc = "平台计费-新出租车计费规则-批量启用")
    public int batchUpdateUpStatus(ExpChargingAo ao) {
        ao.setNewTaxi(true);
        return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_UP, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT).getCount();
    }

    @OpRequestUpdateMapping(op = "batchUpdateDownStatus", desc = "平台计费-新出租车计费规则-批量停用")
    public int batchUpdateDownStatus(ExpChargingAo ao) {
        ao.setNewTaxi(true);
        return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DOWN, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT).getCount();
    }

    @OpRequestUpdateMapping(op = "batchUpdateDelStatus", desc = "平台计费-新出租车计费规则-批量删除")
    public int batchUpdateDelStatus(ExpChargingAo ao) {
        ao.setNewTaxi(true);
        return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DEL, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT).getCount();
    }

    @OpRequestSelectMapping(op = "export", desc = "平台计费-新出租车计费规则-导出")
    public AnwserCode export(ExpChargingAo ao) {
        ao.setNewTaxi(true);
        ao.setRideTypeList(rideTypeList);
        return expChargingService.export(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT, FileExportModuleConstant.NEW_TAXI_APPOINT_CHARGING_SYSTEM);
    }

}
