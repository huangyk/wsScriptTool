package com.wanshun.oaChargingManager.controller.citylinecharging;

import com.wanshun.oaChargingManager.ao.expcharging.ExpChargingAo;
import com.wanshun.oaChargingManager.ao.expcharging.ExpChargingOpAo;
import com.wanshun.oaChargingManager.service.citylinecharging.CityLineChargingService;
import com.wanshun.oaChargingManager.vo.expcharging.ExpChargingVo;
import com.wanshun.common.annotion.OpRequestSaveMapping;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.OpRequestUpdateMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.utils.ChargingServiceUtil;
import com.wanshun.common.web.AbstractManagerBaseController;
import com.wanshun.constants.manager.fileExport.constants.FileExportModuleConstant;
import com.wanshun.constants.platform.chargingservice.ChargingServiceAttributeConst;
import com.wanshun.constants.platform.ordersystem.carorder.constants.OrderAttributeConst;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@WSRestController(path = "/cityline/charging/system", module = "平台计费-城际专线计费规则")
public class CityLineChargingSystemController extends AbstractManagerBaseController {

	@Autowired
	private CityLineChargingService cityLineChargingService;

	@OpRequestSelectMapping(op = "selectPage", desc = "平台计费-城际专线计费规则-分页查询")
	public List<ExpChargingVo> selectPage(PageModel<ExpChargingAo> ao) {
		return cityLineChargingService.selectPage(ao);
	}

	@OpRequestSelectMapping(op = "selectCount", desc = "平台计费-城际专线计费规则-数量查询")
	public Integer selectCount(PageModel<ExpChargingAo> ao) {
		return cityLineChargingService.selectCount(ao);
	}

	@OpRequestSelectMapping(op = "selectDetils", desc = "平台计费-城际专线计费规则-查看详情")
	public ExpChargingVo selectDetils(ExpChargingAo ao) {
		return cityLineChargingService.selectDetails(ao);
	}

	@OpRequestSaveMapping(op = "save", desc = "平台计费-城际专线计费规则-添加")
	public AnwserCode save(ExpChargingOpAo ao) {
		return cityLineChargingService.save(ao);
	}

	@OpRequestSaveMapping(op = "update", desc = "平台计费-城际专线计费规则-修改")
	public AnwserCode update(ExpChargingOpAo ao) {
		return cityLineChargingService.update(ao);
	}

	@OpRequestSaveMapping(op = "updateStatus", desc = "平台计费-城际专线计费规则-停用启用")
	public AnwserCode updateStatus(ExpChargingAo ao) {
		return cityLineChargingService.updateStatus(ao);
	}

	@OpRequestSaveMapping(op = "delete", desc = "平台计费-城际专线计费规则-删除")
	public AnwserCode delete(ExpChargingAo ao) {
		return cityLineChargingService.delete(ao);
	}

	@OpRequestUpdateMapping(op = "batchUpdateUpStatus", desc = "平台计费-城际专线计费规则-批量启用")
	public int batchUpdateUpStatus(ExpChargingAo ao) {
		return cityLineChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_UP).getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateDownStatus", desc = "平台计费-城际专线计费规则-批量禁用")
	public int batchUpdateDownStatus(ExpChargingAo ao) {
		return cityLineChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DOWN).getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateDelStatus", desc = "平台计费-城际专线计费规则-批量删除")
	public int batchUpdateDelStatus(ExpChargingAo ao) {
		return cityLineChargingService.batchUpdateStatus(ao, ChargingServiceAttributeConst.CHARGING_STATUS_DEL).getCount();
	}

	@OpRequestUpdateMapping(op = "batchAdjust", desc = "平台计费-实时计费规则-批量调整计价")
	public AnwserCode batchAdjust(ExpChargingAo ao) {
		return cityLineChargingService.batchAdjust(ao);
	}
}
