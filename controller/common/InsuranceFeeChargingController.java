package com.wanshun.oaChargingManager.controller.common;

import java.util.List;
import com.wanshun.oaChargingManager.service.common.InsuranceFeeChargingService;
import org.springframework.beans.factory.annotation.Autowired;
import com.wanshun.oaChargingManager.ao.common.InsuranceFeeChargingAo;
import com.wanshun.oaChargingManager.vo.common.InsuranceFeeChargingVo;
import com.wanshun.common.annotion.OpRequestSaveMapping;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.OpRequestUpdateMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.web.AbstractManagerBaseController;

/**
 * @Author: zhangruiping
 * @Description: 计价系统保险配置
 * @Date: 2020/9/28 15:17
 * @Version: 1.0
 */
@WSRestController(path = "/charging/insuranceFeeCharging", module = "保险配置")
public class InsuranceFeeChargingController extends AbstractManagerBaseController {
	@Autowired
	private InsuranceFeeChargingService insuranceFeeChargingService;

	@OpRequestSelectMapping(op = "selectPage", desc = "保险配置-分页查询")
	public List<InsuranceFeeChargingVo> selectPage(PageModel<InsuranceFeeChargingAo> ao) {
		return insuranceFeeChargingService.selectPage(ao);
	}

	@OpRequestSelectMapping(op = "selectCount", desc = "保险配置-数量查询")
	public Integer selectCount(PageModel<InsuranceFeeChargingAo> ao) {
		return insuranceFeeChargingService.selectCount(ao);
	}

	@OpRequestSelectMapping(op = "selectDetils", desc = "保险配置-配置查询")
	public InsuranceFeeChargingVo selectDetils(InsuranceFeeChargingAo ao) {
		return insuranceFeeChargingService.selectDetils(ao);
	}

	@OpRequestUpdateMapping(op = "updateStatus", desc = "保险配置-启用/禁用")
	public AnwserCode updateStatus(InsuranceFeeChargingAo ao) {
		return insuranceFeeChargingService.updateStatus(ao);
	}

	@OpRequestUpdateMapping(op = "delete", desc = "保险配置-删除")
	public AnwserCode delete(InsuranceFeeChargingAo ao) {
		return insuranceFeeChargingService.delete(ao);
	}

	@OpRequestSaveMapping(op = "save", desc = "保险配置-添加")
	public AnwserCode save(InsuranceFeeChargingAo ao) {
		return insuranceFeeChargingService.save(ao);
	}

	@OpRequestUpdateMapping(op = "update", desc = "保险配置-修改")
	public AnwserCode update(InsuranceFeeChargingAo ao) {
		return insuranceFeeChargingService.update(ao);
	}
}