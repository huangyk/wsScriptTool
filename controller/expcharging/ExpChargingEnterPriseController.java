package com.wanshun.oaChargingManager.controller.expcharging;

import java.util.ArrayList;
import java.util.List;

import com.wanshun.oaChargingManager.ao.common.ChargingBatchSaveAo;
import com.wanshun.common.code.AttributeConst;
import com.wanshun.constants.manager.fileExport.constants.FileExportModuleConstant;
import com.wanshun.constants.platform.chargingservice.ChargingServiceAttributeConst;
import com.wanshun.constants.platform.ordersystem.carorder.constants.OrderAttributeConst;
import org.springframework.beans.factory.annotation.Autowired;

import com.wanshun.oaChargingManager.ao.expcharging.ExpChargingAo;
import com.wanshun.oaChargingManager.ao.expcharging.ExpChargingOpAo;
import com.wanshun.oaChargingManager.service.expcharging.ExpChargingService;
import com.wanshun.oaChargingManager.vo.expcharging.ExpChargingVo;
import com.wanshun.common.annotion.OpRequestSaveMapping;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.OpRequestUpdateMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.utils.ChargingServiceUtil;
import com.wanshun.common.web.AbstractManagerBaseController;

@WSRestController(path = "/exp/charging/enterprise", module = "企业用车-实时计费规则")
public class ExpChargingEnterPriseController extends AbstractManagerBaseController {

	private static List<Integer> rideTypeList = new ArrayList<Integer>() {{
		add(AttributeConst.RIDE_TYPE_EXP);
		add(AttributeConst.RIDE_TYPE_SPECIAL);
		add(AttributeConst.RIDE_TYPE_LUXURY);
		add(AttributeConst.RIDE_TYPE_PREMIUM);
	}};

	@Autowired
	private ExpChargingService expChargingService;

	@OpRequestSelectMapping(op = "selectPage", desc = "企业用车-实时计费规则-分页查询")
	public List<ExpChargingVo> selectPage(PageModel<ExpChargingAo> ao) {
		ao.getBody().setRideTypeList(rideTypeList);
		return expChargingService.selectPage(ao, ChargingServiceUtil.ENTERPRISE, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT);
	}

	@OpRequestSelectMapping(op = "selectCount", desc = "企业用车-实时计费规则-数量查询")
	public Integer selectCount(PageModel<ExpChargingAo> ao) {
		ao.getBody().setRideTypeList(rideTypeList);
		return expChargingService.selectCount(ao, ChargingServiceUtil.ENTERPRISE, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT);
	}

	@OpRequestSelectMapping(op = "selectDetils", desc = "企业用车-实时计费规则-详情查询")
	public ExpChargingVo selectDetils(ExpChargingAo ao) {
		return expChargingService.selectDetils(ao, ChargingServiceUtil.ENTERPRISE, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT);
	}

	@OpRequestUpdateMapping(op = "updateStatus", desc = "企业用车-实时计费规则-启用/禁用")
	public AnwserCode updateStatus(ExpChargingAo ao) {
		return expChargingService.updateStatus(ao, ChargingServiceUtil.ENTERPRISE, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT);
	}

	@OpRequestUpdateMapping(op = "delete", desc = "企业用车-实时计费规则-删除")
	public AnwserCode delete(ExpChargingAo ao) {
		return expChargingService.delete(ao, ChargingServiceUtil.ENTERPRISE, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT);
	}

	@OpRequestSaveMapping(op = "save", desc = "企业用车-实时计费规则-添加")
	public AnwserCode save(ExpChargingOpAo ao) {
		return expChargingService.save(ao, ChargingServiceUtil.ENTERPRISE, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT);
	}

	@OpRequestUpdateMapping(op = "update", desc = "企业用车-实时计费规则-修改")
	public AnwserCode update(ExpChargingOpAo ao) {
		return expChargingService.update(ao, ChargingServiceUtil.ENTERPRISE, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT);
	}

	@OpRequestUpdateMapping(op = "batchAdjust", desc = "企业用车-实时计费规则-批量调整配置")
	public AnwserCode batchAdjust(ExpChargingAo ao) {
		return expChargingService.batchAdjust(ao, ChargingServiceUtil.ENTERPRISE, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT);
	}

	@OpRequestUpdateMapping(op = "batchUpdateUpStatus", desc = "企业用车-实时计费规则-批量启用")
	public int batchUpdateUpStatus(ExpChargingAo ao) {
		return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_UP, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT).getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateDownStatus", desc = "企业用车-实时计费规则-批量禁用")
	public int batchUpdateDownStatus(ExpChargingAo ao) {
		return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DOWN, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT).getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateDelStatus", desc = "企业用车-实时计费规则-批量删除")
	public int batchUpdateDelStatus(ExpChargingAo ao) {
		return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DEL, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT).getCount();
	}

	@OpRequestSelectMapping(op = "batchSave", desc = "企业用车-实时计费规则-批量新增")
	public List<ExpChargingVo> batchSave(ChargingBatchSaveAo ao) {
		return expChargingService.batchSave(ao, ChargingServiceUtil.ENTERPRISE, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT);
	}

	@OpRequestSelectMapping(op = "export", desc = "企业用车-实时计费规则-导出")
	public AnwserCode export(ExpChargingAo ao) {
		ao.setRideTypeList(rideTypeList);
		return expChargingService.export(ao, ChargingServiceUtil.ENTERPRISE, OrderAttributeConst.ORDER_TYPE_EXPCAR_INSTANT, FileExportModuleConstant.EXP_CHARGING_ENTERPRISE);
	}

}
