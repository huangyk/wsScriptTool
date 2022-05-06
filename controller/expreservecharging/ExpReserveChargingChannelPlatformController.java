package com.wanshun.oaChargingManager.controller.expreservecharging;

import java.util.List;
import com.wanshun.oaChargingManager.ao.common.ChargingBatchSaveAo;
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

@WSRestController(path = "/expreserve/charging/channel", module = "H5渠道-预约计费规则")
public class ExpReserveChargingChannelPlatformController extends AbstractManagerBaseController {

	@Autowired
	private ExpChargingService expChargingService;

	@OpRequestSelectMapping(op = "selectPage", desc = "H5渠道-预约计费规则-分页查询")
	public List<ExpChargingVo> selectPage(PageModel<ExpChargingAo> ao) {
		return expChargingService.selectPage(ao, ChargingServiceUtil.CHANNELPLATFORM, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE);
	}

	@OpRequestSelectMapping(op = "selectCount", desc = "H5渠道-预约计费规则-数量查询")
	public Integer selectCount(PageModel<ExpChargingAo> ao) {
		return expChargingService.selectCount(ao, ChargingServiceUtil.CHANNELPLATFORM, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE);
	}

	@OpRequestSelectMapping(op = "selectDetils", desc = "H5渠道-预约计费规则-详情查询")
	public ExpChargingVo selectDetils(ExpChargingAo ao) {
		return expChargingService.selectDetils(ao, ChargingServiceUtil.CHANNELPLATFORM, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE);
	}

	@OpRequestUpdateMapping(op = "updateStatus", desc = "H5渠道-预约计费规则-启用/禁用")
	public AnwserCode updateStatus(ExpChargingAo ao) {
		return expChargingService.updateStatus(ao, ChargingServiceUtil.CHANNELPLATFORM, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE);
	}

	@OpRequestUpdateMapping(op = "delete", desc = "H5渠道-预约计费规则-删除")
	public AnwserCode delete(ExpChargingAo ao) {
		return expChargingService.delete(ao, ChargingServiceUtil.CHANNELPLATFORM, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE);
	}

	@OpRequestSaveMapping(op = "save", desc = "H5渠道-预约计费规则-添加")
	public AnwserCode save(ExpChargingOpAo ao) {
		return expChargingService.save(ao, ChargingServiceUtil.CHANNELPLATFORM, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE);
	}

	@OpRequestUpdateMapping(op = "update", desc = "H5渠道-预约计费规则-修改")
	public AnwserCode update(ExpChargingOpAo ao) {
		return expChargingService.update(ao, ChargingServiceUtil.CHANNELPLATFORM, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE);
	}

	@OpRequestUpdateMapping(op = "batchAdjust", desc = "H5渠道-预约计费规则-批量调整配置")
	public AnwserCode batchAdjust(ExpChargingAo ao) {
		return expChargingService.batchAdjust(ao, ChargingServiceUtil.CHANNELPLATFORM, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE);
	}

	@OpRequestUpdateMapping(op = "batchUpdateUpStatus", desc = "H5渠道-预约计费规则-批量启用")
	public int batchUpdateUpStatus(ExpChargingAo ao) {
		return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_UP, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE).getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateDownStatus", desc = "H5渠道-预约计费规则-批量禁用")
	public int batchUpdateDownStatus(ExpChargingAo ao) {
		return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DOWN, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE).getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateDelStatus", desc = "H5渠道-预约计费规则-批量删除")
	public int batchUpdateDelStatus(ExpChargingAo ao) {
		return expChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DEL, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE).getCount();
	}

	@OpRequestSelectMapping(op = "batchSave", desc = "H5渠道-预约计费规则-批量新增")
	public List<ExpChargingVo> batchSave(ChargingBatchSaveAo ao) {
		return expChargingService.batchSave(ao, ChargingServiceUtil.CHANNELPLATFORM, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE);
	}

	@OpRequestSelectMapping(op = "export", desc = "H5渠道-预约计费规则-导出")
	public AnwserCode export(ExpChargingAo ao) {
		return expChargingService.export(ao, ChargingServiceUtil.CHANNELPLATFORM, OrderAttributeConst.ORDER_TYPE_EXPCAR_RESERVE, FileExportModuleConstant.EXP_RESERVE_CHARGING_CHANNEL_PLATFORM);
	}

}
