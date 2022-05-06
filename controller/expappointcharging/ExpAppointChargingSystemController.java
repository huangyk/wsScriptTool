package com.wanshun.oaChargingManager.controller.expappointcharging;

import java.util.ArrayList;
import java.util.List;

import com.wanshun.common.code.AttributeConst;
import com.wanshun.constants.manager.fileExport.constants.FileExportModuleConstant;
import com.wanshun.constants.platform.chargingservice.ChargingServiceAttributeConst;
import org.springframework.beans.factory.annotation.Autowired;
import com.wanshun.oaChargingManager.ao.expcharging.ExpChargingAo;
import com.wanshun.oaChargingManager.ao.expcharging.ExpChargingOpAo;
import com.wanshun.oaChargingManager.ao.expcharging.ExpFixedPriceChargingOpAo;
import com.wanshun.oaChargingManager.service.expcharging.ExpChargingService;
import com.wanshun.oaChargingManager.service.expcharging.ExpFixedPriceChargingService;
import com.wanshun.oaChargingManager.vo.expcharging.ExpChargingVo;
import com.wanshun.oaChargingManager.vo.expcharging.ExpFixedPriceChargingVo;
import com.wanshun.common.annotion.OpRequestSaveMapping;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.OpRequestUpdateMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.utils.ChargingServiceUtil;
import com.wanshun.common.web.AbstractManagerBaseController;
import com.wanshun.constants.platform.ordersystem.carorder.constants.OrderAttributeConst;

@WSRestController(path = "/expappoint/charging/system", module = "平台计费-扫码计费规则")
public class ExpAppointChargingSystemController extends AbstractManagerBaseController {

	@Autowired
	private ExpChargingService expChargingService;

	@Autowired
	private ExpFixedPriceChargingService expFixedPriceChargingService;

	private static List<Integer> rideTypeList = new ArrayList<Integer>() {{
		add(AttributeConst.RIDE_TYPE_EXP);
		add(AttributeConst.RIDE_TYPE_SPECIAL);
		add(AttributeConst.RIDE_TYPE_LUXURY);
		add(AttributeConst.RIDE_TYPE_PREMIUM);
	}};

	@OpRequestSelectMapping(op = "selectPage", desc = "平台计费-扫码计费规则-分页查询")
	public List<ExpChargingVo> selectPage(PageModel<ExpChargingAo> ao) {
		ao.getBody().setChannelId(ChargingServiceUtil.WANSHUN_CHANNELID);
		ao.getBody().setRideTypeList(rideTypeList);
		return expChargingService.selectPage(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestSelectMapping(op = "selectCount", desc = "平台计费-扫码计费规则-数量查询")
	public Integer selectCount(PageModel<ExpChargingAo> ao) {
		ao.getBody().setChannelId(ChargingServiceUtil.WANSHUN_CHANNELID);
		ao.getBody().setRideTypeList(rideTypeList);
		return expChargingService.selectCount(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestSelectMapping(op = "selectDetils", desc = "平台计费-扫码计费规则-详情查询")
	public ExpChargingVo selectDetils(ExpChargingAo ao) {
		return expChargingService.selectDetils(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestUpdateMapping(op = "updateStatus", desc = "平台计费-扫码计费规则-启用/禁用")
	public AnwserCode updateStatus(ExpChargingAo ao) {
		return expChargingService.updateStatus(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestUpdateMapping(op = "delete", desc = "平台计费-扫码计费规则-删除")
	public AnwserCode delete(ExpChargingAo ao) {
		return expChargingService.delete(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestSaveMapping(op = "save", desc = "平台计费-扫码计费规则-添加")
	public AnwserCode save(ExpChargingOpAo ao) {
		ao.setChannelId(ChargingServiceUtil.WANSHUN_CHANNELID);
		return expChargingService.save(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestUpdateMapping(op = "update", desc = "平台计费-扫码计费规则-修改")
	public AnwserCode update(ExpChargingOpAo ao) {
		ao.setChannelId(ChargingServiceUtil.WANSHUN_CHANNELID);
		return expChargingService.update(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestUpdateMapping(op = "batchAdjust", desc = "平台计费-扫码计费规则-批量调整配置")
	public AnwserCode batchAdjust(ExpChargingAo ao) {
		ao.setChannelId(ChargingServiceUtil.WANSHUN_CHANNELID);
		return expChargingService.batchAdjust(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestUpdateMapping(op = "batchUpdateUpStatus", desc = "平台计费-扫码计费规则-批量启用")
	public int batchUpdateUpStatus(ExpChargingAo ao) {
		return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_UP, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT).getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateDownStatus", desc = "平台计费-扫码计费规则-批量禁用")
	public int batchUpdateDownStatus(ExpChargingAo ao) {
		return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DOWN, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT).getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateDelStatus", desc = "平台计费-扫码计费规则-批量删除")
	public int batchUpdateDelStatus(ExpChargingAo ao) {
		return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DEL, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT).getCount();
	}

	@OpRequestSelectMapping(op = "export", desc = "平台计费-扫码计费规则-导出")
	public AnwserCode export(ExpChargingAo ao) {
		ao.setRideTypeList(rideTypeList);
		return expChargingService.export(ao, ChargingServiceUtil.WANSHUNSYSTEM, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT, FileExportModuleConstant.EXP_APPOINT_CHARGING_SYSTEM);
	}

	@OpRequestSelectMapping(op = "selectFixedPricePage", desc = "平台计费-扫码一口价计费规则-分页查询")
	public List<ExpFixedPriceChargingVo> selectFixedPricePage(PageModel<ExpChargingAo> ao) {
		return expFixedPriceChargingService.selectPage(ao, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestSelectMapping(op = "selectFixedPriceCount", desc = "平台计费-扫码一口价计费规则-数量查询")
	public Integer selectFixedPriceCount(PageModel<ExpChargingAo> ao) {
		return expFixedPriceChargingService.selectCount(ao, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestSelectMapping(op = "selectFixedPriceDetils", desc = "平台计费-扫码一口价计费规则-详情查询")
	public ExpFixedPriceChargingVo selectFixedPriceDetils(ExpChargingAo ao) {
		return expFixedPriceChargingService.selectDetils(ao, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestUpdateMapping(op = "updateFixedPriceStatus", desc = "平台计费-扫码一口价计费规则-启用/禁用")
	public AnwserCode updateFixedPriceStatus(ExpChargingAo ao) {
		return expFixedPriceChargingService.updateStatus(ao, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestUpdateMapping(op = "deleteFixedPrice", desc = "平台计费-扫码一口价计费规则-删除")
	public AnwserCode deleteFixedPrice(ExpChargingAo ao) {
		return expFixedPriceChargingService.delete(ao, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestSaveMapping(op = "saveFixedPrice", desc = "平台计费-扫码一口价计费规则-添加")
	public AnwserCode saveFixedPrice(ExpFixedPriceChargingOpAo ao) {
		return expFixedPriceChargingService.save(ao, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestUpdateMapping(op = "updateFixedPrice", desc = "平台计费-扫码一口价计费规则-修改")
	public AnwserCode updateFixedPrice(ExpFixedPriceChargingOpAo ao) {
		return expFixedPriceChargingService.update(ao, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestUpdateMapping(op = "batchFixedPriceAdjust", desc = "平台计费-扫码一口价计费规则-批量调整配置")
	public AnwserCode batchFixedPriceAdjust(ExpChargingAo ao) {
		return expFixedPriceChargingService.batchAdjust(ao, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT);
	}

	@OpRequestUpdateMapping(op = "batchUpdateFixedPriceUpStatus", desc = "平台计费-扫码一口价计费规则-批量启用")
	public int batchUpdateFixedPriceUpStatus(ExpChargingAo ao) {
		return expFixedPriceChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_UP, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT).getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateFixedPriceDownStatus", desc = "平台计费-扫码一口价计费规则-批量停用")
	public int batchUpdateFixedPriceDownStatus(ExpChargingAo ao) {
		return expFixedPriceChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DOWN, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT).getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateFixedPriceDelStatus", desc = "平台计费-扫码一口价计费规则-批量删除")
	public int batchUpdateFixedPriceDelStatus(ExpChargingAo ao) {
		return expFixedPriceChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DEL, OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT).getCount();
	}

	@OpRequestSelectMapping(op = "fixedPriceExport", desc = "平台计费-扫码一口价计费规则-导出")
	public AnwserCode fixedPriceExport(ExpChargingAo ao) {
		return expFixedPriceChargingService.export(ao,  OrderAttributeConst.ORDER_TYPE_EXPCAR_APPOINT, FileExportModuleConstant.EXP_APPOINT_CHARGING_FIXED_PRICE_SYSTEM);
	}
	
}
