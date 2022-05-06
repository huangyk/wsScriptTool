package com.wanshun.oaChargingManager.controller.charteredbusordercharging;

import java.util.List;

import com.wanshun.authority.control.common.TokenUtils;
import com.wanshun.chargingServiceConsoleApi.rpcApi.charteredbusordercharging.RpcCharteredBusOrderChargingConsoleService;
import com.wanshun.chargingServiceConsoleApi.rpcao.charteredbusordercharging.RpcCharteredBusOrderChargingBatchSaveConsoleAo;
import com.wanshun.chargingServiceConsoleApi.rpcao.charteredbusordercharging.RpcCharteredBusOrderChargingConsoleAo;
import com.wanshun.chargingServiceConsoleApi.rpcao.charteredbusordercharging.RpcCharteredBusOrderChargingOpConsoleAo;
import com.wanshun.chargingServiceConsoleApi.rpcvo.charteredbusordercharging.RpcCharteredBusOrderChargingConsoleVo;
import com.wanshun.common.code.AttributeConst;
import com.wanshun.common.utils.BaseRpcVoUtil;
import com.wanshun.common.vo.BaseRecordRpcVo;
import com.wanshun.common.vo.BaseRpcVo;
import com.wanshun.common.vo.RpcCountVo;
import com.wanshun.constants.manager.fileExport.constants.FileExportModuleConstant;
import com.wanshun.constants.platform.chargingservice.ChargingServiceAttributeConst;
import com.wanshun.manageraccountctrl.common.ManagerTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.wanshun.common.annotion.OpRequestSaveMapping;
import com.wanshun.common.annotion.OpRequestSelectMapping;
import com.wanshun.common.annotion.OpRequestUpdateMapping;
import com.wanshun.common.annotion.WSRestController;
import com.wanshun.common.code.anwser.AnwserCode;
import com.wanshun.common.page.PageModel;
import com.wanshun.common.utils.ChargingServiceUtil;
import com.wanshun.common.web.AbstractManagerBaseController;

@WSRestController(path = "/charteredbusorder/charging/enterprise", module = "企业用车-包车计费规则")
public class CharteredBusOrderChargingEnterPriseController extends AbstractManagerBaseController {

	@Autowired
	private RpcCharteredBusOrderChargingConsoleService rpcCharteredBusOrderChargingConsoleService;

	@OpRequestSelectMapping(op = "selectPage", desc = "企业用车-包车计费规则-分页查询")
	public List<RpcCharteredBusOrderChargingConsoleVo> selectPage(PageModel<RpcCharteredBusOrderChargingConsoleAo> ao) {
		ao.getBody().setChannelType(ChargingServiceUtil.ENTERPRISE);
		BaseRecordRpcVo<List<RpcCharteredBusOrderChargingConsoleVo>> rpcVo = rpcCharteredBusOrderChargingConsoleService.selectPage(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getData();
	}

	@OpRequestSelectMapping(op = "selectCount", desc = "企业用车-包车计费规则-数量查询")
	public Integer selectCount(PageModel<RpcCharteredBusOrderChargingConsoleAo> ao) {
		ao.getBody().setChannelType(ChargingServiceUtil.ENTERPRISE);
		RpcCountVo rpcVo = rpcCharteredBusOrderChargingConsoleService.selectCount(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getCount();
	}

	@OpRequestSelectMapping(op = "selectDetils", desc = "企业用车-包车计费规则-详情查询")
	public RpcCharteredBusOrderChargingConsoleVo selectDetils(RpcCharteredBusOrderChargingConsoleAo ao) {
		ao.setChannelType(ChargingServiceUtil.ENTERPRISE);
		RpcCharteredBusOrderChargingConsoleVo rpcVo = rpcCharteredBusOrderChargingConsoleService.selectDetils(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo;
	}

	@OpRequestUpdateMapping(op = "updateStatus", desc = "企业用车-包车计费规则-启用/禁用")
	public AnwserCode updateStatus(RpcCharteredBusOrderChargingConsoleAo ao) {
		ao.setChannelType(ChargingServiceUtil.ENTERPRISE);
		ao.setModule(AttributeConst.CHARGING_MANAGER_MODULE);
		ao.setUserId(TokenUtils.getCurrentUser().getId());
		BaseRpcVo rpcVo = rpcCharteredBusOrderChargingConsoleService.updateStatus(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getAnwserCode();
	}

	@OpRequestUpdateMapping(op = "delete", desc = "企业用车-包车计费规则-删除")
	public AnwserCode delete(RpcCharteredBusOrderChargingConsoleAo ao) {
		ao.setChannelType(ChargingServiceUtil.ENTERPRISE);
		ao.setModule(AttributeConst.CHARGING_MANAGER_MODULE);
		ao.setUserId(TokenUtils.getCurrentUser().getId());
		ao.setStatus(ChargingServiceAttributeConst.CHARGING_STATUS_DEL);
		BaseRpcVo rpcVo = rpcCharteredBusOrderChargingConsoleService.updateStatus(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getAnwserCode();
	}

	@OpRequestSaveMapping(op = "save", desc = "企业用车-包车计费规则-添加")
	public AnwserCode save(RpcCharteredBusOrderChargingOpConsoleAo ao) {
		ao.setChannelType(ChargingServiceUtil.ENTERPRISE);
		ao.setUserId(TokenUtils.getCurrentUser().getId());
		ao.setModule(AttributeConst.CHARGING_MANAGER_MODULE);
		ao.setId(null);
		BaseRpcVo rpcVo = rpcCharteredBusOrderChargingConsoleService.save(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getAnwserCode();
	}

	@OpRequestUpdateMapping(op = "update", desc = "企业用车-包车计费规则-修改")
	public AnwserCode update(RpcCharteredBusOrderChargingOpConsoleAo ao) {
		ao.setChannelType(ChargingServiceUtil.ENTERPRISE);
		ao.setUserId(TokenUtils.getCurrentUser().getId());
		ao.setModule(AttributeConst.CHARGING_MANAGER_MODULE);
		BaseRpcVo rpcVo = rpcCharteredBusOrderChargingConsoleService.update(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getAnwserCode();
	}

	@OpRequestUpdateMapping(op = "batchAdjust", desc = "企业用车-包车计费规则-批量调整配置")
	public AnwserCode batchAdjust(RpcCharteredBusOrderChargingConsoleAo ao) {
		ao.setChannelType(ChargingServiceUtil.ENTERPRISE);
		ao.setUserId(TokenUtils.getCurrentUser().getId());
		ao.setModule(AttributeConst.CHARGING_MANAGER_MODULE);
		BaseRpcVo rpcVo = rpcCharteredBusOrderChargingConsoleService.batchAdjust(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getAnwserCode();
	}

	@OpRequestUpdateMapping(op = "batchUpdateUpStatus", desc = "企业用车-包车计费规则-批量启用")
	public int batchUpdateUpStatus(RpcCharteredBusOrderChargingConsoleAo ao) {
		ao.setChannelType(ChargingServiceUtil.ENTERPRISE);
		ao.setUserId(TokenUtils.getCurrentUser().getId());
		ao.setModule(AttributeConst.CHARGING_MANAGER_MODULE);
		ao.setStatus(ChargingServiceAttributeConst.CHARGING_STATUS_UP);
		RpcCountVo rpcVo = rpcCharteredBusOrderChargingConsoleService.batchUpdateStatus(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateDownStatus", desc = "企业用车-包车计费规则-批量停用")
	public int batchUpdateDownStatus(RpcCharteredBusOrderChargingConsoleAo ao) {
		ao.setChannelType(ChargingServiceUtil.ENTERPRISE);
		ao.setUserId(TokenUtils.getCurrentUser().getId());
		ao.setModule(AttributeConst.CHARGING_MANAGER_MODULE);
		ao.setStatus(ChargingServiceAttributeConst.CHARGING_STATUS_DOWN);
		RpcCountVo rpcVo = rpcCharteredBusOrderChargingConsoleService.batchUpdateStatus(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getCount();
	}

	@OpRequestUpdateMapping(op = "batchUpdateDelStatus", desc = "企业用车-包车计费规则-批量删除")
	public int batchUpdateDelStatus(RpcCharteredBusOrderChargingConsoleAo ao) {
		ao.setChannelType(ChargingServiceUtil.ENTERPRISE);
		ao.setUserId(TokenUtils.getCurrentUser().getId());
		ao.setModule(AttributeConst.CHARGING_MANAGER_MODULE);
		ao.setStatus(ChargingServiceAttributeConst.CHARGING_STATUS_DEL);
		RpcCountVo rpcVo = rpcCharteredBusOrderChargingConsoleService.batchUpdateStatus(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getCount();
	}

	@OpRequestSelectMapping(op = "batchSave", desc = "企业用车-包车计费规则-批量新增")
	public List<RpcCharteredBusOrderChargingConsoleVo> batchSave(RpcCharteredBusOrderChargingBatchSaveConsoleAo ao) {
		ao.setChannelType(ChargingServiceUtil.ENTERPRISE);
		ao.setUserId(TokenUtils.getCurrentUser().getId());
		ao.setModule(AttributeConst.CHARGING_MANAGER_MODULE);
		BaseRecordRpcVo<List<RpcCharteredBusOrderChargingConsoleVo>> rpcVo = rpcCharteredBusOrderChargingConsoleService.batchSave(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getData();
	}

	@OpRequestSelectMapping(op = "export", desc = "企业用车-包车计费规则-导出")
	public AnwserCode export(RpcCharteredBusOrderChargingConsoleAo ao){
		ao.setChannelType(ChargingServiceUtil.ENTERPRISE);
		ao.setUserId(TokenUtils.getCurrentUser().getId());
		ao.setModule(AttributeConst.CHARGING_MANAGER_MODULE);
		ao.setFileType(FileExportModuleConstant.CHARTERED_BUS_ORDER_CHARGING_ENTERPRISE);
		ao.setAccountType(FileExportModuleConstant.FILE_EXPORT_ACCOUNT_TYPE_CHARGING);
		BaseRpcVo rpcVo = rpcCharteredBusOrderChargingConsoleService.export(ao);
		BaseRpcVoUtil.checkFail(rpcVo);
		return rpcVo.getAnwserCode();
	}
}
